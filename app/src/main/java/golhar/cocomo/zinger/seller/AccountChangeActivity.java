package golhar.cocomo.zinger.seller;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import golhar.cocomo.zinger.seller.adapter.ShopListAdapter;
import golhar.cocomo.zinger.seller.enums.UserRole;
import golhar.cocomo.zinger.seller.model.ConfigurationModel;
import golhar.cocomo.zinger.seller.model.ShopConfigurationModel;
import golhar.cocomo.zinger.seller.model.ShopModel;
import golhar.cocomo.zinger.seller.model.UserModel;
import golhar.cocomo.zinger.seller.model.UserShopListModel;
import golhar.cocomo.zinger.seller.service.MainRepository;
import golhar.cocomo.zinger.seller.utils.Constants;
import golhar.cocomo.zinger.seller.utils.ErrorLog;
import golhar.cocomo.zinger.seller.utils.Response;
import golhar.cocomo.zinger.seller.utils.SharedPref;
import retrofit2.Call;
import retrofit2.Callback;

public class AccountChangeActivity extends AppCompatActivity {
    ShopListAdapter shopListAdapter;
    ShopConfigurationModel shopDetails;
    EditText priceTV;
    ToggleButton editBT;
    ToggleButton delTakenBT;
    ToggleButton orderTakenBT;
    EditText numberTV;
    ToggleButton mobileBT;
    EditText emailTV;
    ToggleButton emailBT;
    TextView openTV;
    Button openBT;
    TextView closeTV;
    Button closeBT;
    String amPm;
    Button accSwitchBT;
    Button updateAccountBT;
    Double price;
    int del, order;
    String mobile, email;
    String close, open;
    String role;
    UserRole userRole;
    Date date;
    String closing, opening;
    Button updateShopBT;
    String shopName;
    TextView shopNameTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_change);
        role = SharedPref.getString(getApplicationContext(), Constants.role);
        if (UserRole.SELLER.toString().equals(role)) {
            userRole = UserRole.SELLER;
        } else if (UserRole.SHOP_OWNER.toString().equals(role)) {
            userRole = UserRole.SHOP_OWNER;
        }
        shopNameTV = findViewById(R.id.shopNameTV);
        priceTV = findViewById(R.id.priceTV);
        editBT = findViewById(R.id.editBT);
        delTakenBT = findViewById(R.id.delTakenBT);
        orderTakenBT = findViewById(R.id.orderTakenBT);
        numberTV = findViewById(R.id.numberTV);
        mobileBT = findViewById(R.id.mobileBT);
        emailTV = findViewById(R.id.emailTV);
        emailBT = findViewById(R.id.emailBT);
        openTV = findViewById(R.id.openTV);
        openBT = findViewById(R.id.openBT);
        closeTV = findViewById(R.id.closeTV);
        closeBT = findViewById(R.id.closeBT);
        priceTV.setEnabled(false);
        accSwitchBT = findViewById(R.id.accSwitchBT);
        updateAccountBT = findViewById(R.id.updateAccountBT);
        updateShopBT = findViewById(R.id.updateShopBT);

        shopDetails = null;
        ArrayList<ShopConfigurationModel> shopConfigurationModels = (ArrayList<ShopConfigurationModel>) RetrieveData().getShopModelList();
        for (ShopConfigurationModel scm : shopConfigurationModels) {
            if (SharedPref.getInt(getApplicationContext(), Constants.shopId) == scm.getShopModel().getId()) {
                shopDetails = scm;
            }
        }
        if (shopDetails != null) {
            price = shopDetails.getConfigurationModel().getDeliveryPrice();
            del = shopDetails.getConfigurationModel().getIsDeliveryAvailable();
            order = shopDetails.getConfigurationModel().getIsOrderTaken();
            mobile = shopDetails.getShopModel().getMobile();
            email = SharedPref.getString(getApplicationContext(), Constants.userEmail);
            Date close1 = shopDetails.getShopModel().getClosingTime();
            Date open1 = shopDetails.getShopModel().getOpeningTime();
            SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
            close = format.format(shopDetails.getShopModel().getClosingTime());
            open = format.format(shopDetails.getShopModel().getOpeningTime());
            SimpleDateFormat hhformat = new SimpleDateFormat("hh");
            SimpleDateFormat mmformat = new SimpleDateFormat("mm");
            int hour = Integer.parseInt(hhformat.format(open1));
            int minutes = Integer.parseInt(mmformat.format(open1));
            if (hour >= 12) {
                amPm = "PM";
                hour -= 12;
            } else {
                amPm = "AM";
            }
            opening = String.format("%02d:%02d", hour, minutes) + amPm;
            hour = Integer.parseInt(hhformat.format(close1));
            minutes = Integer.parseInt(mmformat.format(close1));
            if (hour >= 12) {
                amPm = "PM";
                hour -= 12;
            } else {
                amPm = "AM";
            }
            closing = String.format("%02d:%02d", hour, minutes) + amPm;
            shopName=shopDetails.getShopModel().getName();
            shopNameTV.setText(shopName);
        }
        accSwitchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AccountChangeActivity.this);
                View vi = getLayoutInflater().inflate(R.layout.activity_account_change_prompt, null);
                RecyclerView accountsRV = vi.findViewById(R.id.accountsRV);
                UserShopListModel userShopListModel = RetrieveData();
                try {
                    shopListAdapter = new ShopListAdapter(new ArrayList<>(), AccountChangeActivity.this);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AccountChangeActivity.this);
                accountsRV.setLayoutManager(linearLayoutManager);
                accountsRV.setAdapter(shopListAdapter);
                shopListAdapter.setShopConfigurationModelArrayList((ArrayList<ShopConfigurationModel>) userShopListModel.getShopModelList());
                shopListAdapter.notifyDataSetChanged();
                dialogBuilder.setView(vi);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });

        priceTV.setText(price.toString());
        editBT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    priceTV.setEnabled(true);
                    editBT.setBackground(getDrawable(R.drawable.ic_check));
                    priceTV.setText(price.toString());
                } else {
                    // The toggle is disabled
                    priceTV.setEnabled(false);
                    editBT.setBackground(getDrawable(R.drawable.ic_pencil));
                    price = Double.parseDouble(priceTV.getText().toString());
                }
            }
        });

        if (del == 1) {
            delTakenBT.setChecked(true);
            delTakenBT.setBackgroundColor(Color.GREEN);
        } else {
            delTakenBT.setChecked(false);
            delTakenBT.setBackgroundColor(Color.RED);
        }
        delTakenBT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    del = 1;
                    delTakenBT.setBackgroundColor(Color.GREEN);
                } else {
                    del = 0;
                    delTakenBT.setBackgroundColor(Color.RED);
                }
            }
        });
        if (order == 1) {
            orderTakenBT.setChecked(true);
            orderTakenBT.setBackgroundColor(Color.GREEN);
        } else {
            orderTakenBT.setChecked(false);
            orderTakenBT.setBackgroundColor(Color.RED);
        }
        orderTakenBT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    order = 1;
                    orderTakenBT.setBackgroundColor(Color.GREEN);
                } else {
                    order = 0;
                    orderTakenBT.setBackgroundColor(Color.RED);
                }
            }
        });
        numberTV.setEnabled(false);
        numberTV.setText(mobile);
        mobileBT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    numberTV.setEnabled(true);
                    mobileBT.setBackground(getDrawable(R.drawable.ic_check));
                } else {
                    // The toggle is disabled
                    numberTV.setEnabled(false);
                    mobileBT.setBackground(getDrawable(R.drawable.ic_pencil));
                    mobile = numberTV.getText().toString();
                }
            }
        });

        emailTV.setEnabled(false);
        emailTV.setText(email);
        emailBT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    emailTV.setEnabled(true);
                    emailBT.setBackground(getDrawable(R.drawable.ic_check));
                } else {
                    // The toggle is disabled
                    emailTV.setEnabled(false);
                    emailBT.setBackground(getDrawable(R.drawable.ic_pencil));
                    email = emailTV.getText().toString();
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        closeTV.setText(closing);
        openTV.setText(opening);

        closeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(AccountChangeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        closeTV.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                        //close = String.format("%02d:%02d:00", hourOfDay, minutes);
                        int time=(minutes * 60 + hourOfDay * 60 * 60) * 1000;
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                        close = format.format(time);

                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();

            }
        });
        openBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AccountChangeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        openTV.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
//                        open = String.format("%02d:%02d:00", hourOfDay, minutes);
                        int time=(minutes * 60 + hourOfDay * 60 * 60) * 1000;
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                        open = format.format(time);
                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();

            }
        });

        updateAccountBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userModel = new UserModel();
                userModel.setName(SharedPref.getString(getApplicationContext(), Constants.userName));
                userModel.setMobile(mobile);
                userModel.setEmail(email);
                String phoneNo = SharedPref.getString(getApplicationContext(), Constants.phoneNumber);
                String authId = SharedPref.getString(getApplicationContext(), Constants.authId);
                MainRepository.getUserService().updateUser(userModel, authId, phoneNo, userRole.name()).enqueue(new Callback<Response<String>>() {
                    @Override
                    public void onResponse(Call<Response<String>> call, retrofit2.Response<Response<String>> response) {
                        Response<String> responseFromServer = response.body();
                        if (responseFromServer.getCode().equals(ErrorLog.CodeSuccess) && responseFromServer.getMessage().equals(ErrorLog.Success)) {
                            Toast.makeText(AccountChangeActivity.this, "Updated user profile", Toast.LENGTH_SHORT).show();
                            SharedPref.putString(getApplicationContext(), Constants.phoneNumber, mobile);
                            SharedPref.putString(getApplicationContext(), Constants.userEmail, email);

                        } else {
                            Toast.makeText(AccountChangeActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<String>> call, Throwable t) {
                        Toast.makeText(AccountChangeActivity.this, "Failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        updateShopBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigurationModel configurationModel1 = new ConfigurationModel();
                configurationModel1.setDeliveryPrice(price);
                configurationModel1.setIsDeliveryAvailable(del);
                configurationModel1.setIsOrderTaken(order);
                ShopModel shopModel = new ShopModel();
                SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                Date dateC = null;
                try {
                    dateC = new SimpleDateFormat("hh:mm:ss").parse(close);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date datep=null;
                try {
                    datep = new SimpleDateFormat("hh:mm:ss").parse(open);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                shopModel.setClosingTime(datep);
                shopModel.setOpeningTime(dateC);
                shopModel.setName(shopName);
                shopModel.setMobile(mobile);
                shopModel.setId(SharedPref.getInt(getApplicationContext(), Constants.shopId));
                configurationModel1.setShopModel(shopModel);
                String phoneNo = SharedPref.getString(getApplicationContext(), Constants.phoneNumber);
                String authId = SharedPref.getString(getApplicationContext(), Constants.authId);
                MainRepository.getShopService().updateShopConfiguration(configurationModel1, authId, phoneNo, userRole.toString()).enqueue(new Callback<Response<String>>() {
                    @Override
                    public void onResponse(Call<Response<String>> call, retrofit2.Response<Response<String>> response) {
                        Response<String> responseFromServer = response.body();
                        if (responseFromServer.getCode().equals(ErrorLog.CodeSuccess) && responseFromServer.getMessage().equals(ErrorLog.Success)) {
                            Toast.makeText(AccountChangeActivity.this, "Updated user profile", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AccountChangeActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<String>> call, Throwable t) {
                        Toast.makeText(AccountChangeActivity.this, "Failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    UserShopListModel RetrieveData() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.sharedPreferencesShopList, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Constants.shopList, null);
        Type type = new TypeToken<UserShopListModel>() {
        }.getType();
        UserShopListModel orderItemModelArrayList = gson.fromJson(json, type);
        return orderItemModelArrayList;
    }
}

