package golhar.cocomo.zinger.seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import golhar.cocomo.zinger.seller.enums.UserRole;
import golhar.cocomo.zinger.seller.utils.Constants;
import golhar.cocomo.zinger.seller.utils.SharedPref;

public class DashBoardActivity extends AppCompatActivity {
    Button logoutBT;
    Button editMenuBT;
    Button addBT;
    Button accountBT;
    String role;
    UserRole userRole;
    Button placedBT;
    TextView textOne;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        role = SharedPref.getString(getApplicationContext(), Constants.role);
        addBT = findViewById(R.id.addBT);
        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sellerAdd = new Intent(DashBoardActivity.this,AddSellerActivity.class);
                startActivity(sellerAdd);
            }
        });
        accountBT = findViewById(R.id.accountBT);
        textOne = findViewById(R.id.textOne);

        int orderNo=SharedPref.getInt(getApplicationContext(),Constants.orderNumber);
        if(orderNo==0){
            textOne.setVisibility(View.INVISIBLE);
        }else{
            textOne.setVisibility(View.VISIBLE);
            textOne.setText(String.valueOf(orderNo));
        }

        if (UserRole.SELLER.toString().equals(role)) {
            userRole = UserRole.SELLER;
        } else if (UserRole.SHOP_OWNER.toString().equals(role)) {
            userRole = UserRole.SHOP_OWNER;
        }
        placedBT = findViewById(R.id.placedBT);
        placedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newOrder = new Intent(DashBoardActivity.this,NewOrderActivity.class);
                startActivity(newOrder);
            }
        });
        editMenuBT = findViewById(R.id.editMenuBT);
        editMenuBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editMenu = new Intent(DashBoardActivity.this, EditMenuActivity.class);
                startActivity(editMenu);
            }
        });


        accountBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent account = new Intent(DashBoardActivity.this, AccountChangeActivity.class);
                startActivity(account);
            }
        });


        /*
        userEditBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(OrderHistoryActivity.this, UpdateUserProfileActivity.class);
                startActivity(edit);
            }
        });
        */
        logoutBT = findViewById(R.id.logoutBT);
        logoutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.removeAll(getApplicationContext());
                FirebaseAuth.getInstance().signOut();
                Intent MainActivity = new Intent(DashBoardActivity.this, OnBoardingActivity.class);
                finishAffinity();
                startActivity(MainActivity);
            }
        });


    }
}
