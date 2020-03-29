package golhar.cocomo.zinger.seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import golhar.cocomo.zinger.seller.enums.UserRole;
import golhar.cocomo.zinger.seller.utils.Constants;
import golhar.cocomo.zinger.seller.utils.SharedPref;

public class LoginActivity extends AppCompatActivity {

    Button verifyBT;
    TextInputEditText phoneNumberTIET;
    Spinner roleSP;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        roleSP = findViewById(R.id.roleSP);
        List<String> list = new ArrayList<String>();
        list.add("none");
        list.add(UserRole.SHOP_OWNER.name());
        list.add(UserRole.SELLER.name());
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, list);
        listAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        roleSP.setAdapter(listAdapter);
        phoneNumberTIET = findViewById(R.id.phoneNumberTIET);
        verifyBT = findViewById(R.id.verifyBT);

        roleSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                text=null;
            }
        });
        verifyBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phNumber = phoneNumberTIET.getText().toString();
                if (phNumber.length() == 10 && phNumber != null) {
                    if(!text.equals("none"))
                    {
                        SharedPref.putString(getApplicationContext(),Constants.role,text);
                        SharedPref.putString(getApplicationContext(), Constants.phoneNumber, phNumber);
                        Intent otp = new Intent(LoginActivity.this, OtpVerificationActivity.class);
                        startActivity(otp);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Please selected your role", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    phoneNumberTIET.setError("Please enter valid phone number");
                }
            }
        });
    }

}
