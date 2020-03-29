package golhar.cocomo.zinger.seller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import golhar.cocomo.zinger.seller.adapter.ShopMenuItemAdapter;
import golhar.cocomo.zinger.seller.enums.UserRole;
import golhar.cocomo.zinger.seller.model.ItemModel;
import golhar.cocomo.zinger.seller.model.ShopModel;
import golhar.cocomo.zinger.seller.service.MainRepository;
import golhar.cocomo.zinger.seller.utils.Constants;
import golhar.cocomo.zinger.seller.utils.ErrorLog;
import golhar.cocomo.zinger.seller.utils.Response;
import golhar.cocomo.zinger.seller.utils.SharedPref;
import retrofit2.Call;
import retrofit2.Callback;

public class EditMenuActivity extends AppCompatActivity {
    RecyclerView shopMenuItemListRV;
    ShopMenuItemAdapter shopMenuItemAdapter;
    String role;
    UserRole userRole;
    Button backBT;
    Button addBT;
    SwipeRefreshLayout pullToRefresh;
    int isVeg, isAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        backBT = findViewById(R.id.backBT);
        addBT = findViewById(R.id.addBT);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        role = SharedPref.getString(getApplicationContext(),Constants.role);
        if(UserRole.SELLER.toString().equals(role)){
            userRole = UserRole.SELLER;
        }else if(UserRole.SHOP_OWNER.toString().equals(role)){
            userRole=UserRole.SHOP_OWNER;
        }
        shopMenuItemAdapter = new ShopMenuItemAdapter(new ArrayList<>(), this);
        shopMenuItemListRV = findViewById(R.id.shopMenuItemListRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        shopMenuItemListRV.setLayoutManager(linearLayoutManager);
        shopMenuItemListRV.setAdapter(shopMenuItemAdapter);
        pullToRefresh.setRefreshing(true);
        getMenu();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                getMenu();
            }
        });
        
        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditMenuActivity.this);
                View vi = getLayoutInflater().inflate(R.layout.activity_update_add_item,null);
                TextInputEditText nameTIET = vi.findViewById(R.id.nameTIET);
                TextInputEditText categoryTIET = vi.findViewById(R.id.categoryTIET);
                TextInputEditText priceTIET = vi.findViewById(R.id.priceTIET);
                SwitchCompat isVegSC = vi.findViewById(R.id.isVegSC);
                ImageView isVegIV = vi.findViewById(R.id.isVegIV);
                SwitchCompat isAvailableSC = vi.findViewById(R.id.isAvailableSC);
                ImageView isAvailableIV = vi.findViewById(R.id.isAvailableIV);
                Button updateBT = vi.findViewById(R.id.updateBT);
                Button cancelBT = vi.findViewById(R.id.cancelBT);
                updateBT.setText("Add");
                dialogBuilder.setView(vi);
                AlertDialog dialog = dialogBuilder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                isVegSC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            isVeg = 1;
                            isVegIV.setImageResource(R.drawable.ic_vegetarian_mark);
                        } else {
                            isVegIV.setImageResource(R.drawable.ic_non_vegetarian_mark);
                            isVeg = 0;
                        }
                    }
                });

                isAvailableSC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            isAvailableIV.setImageResource(R.drawable.ic_check_circle);
                            isAvailable = 1;
                        } else {
                            isAvailable = 0;
                            isAvailableIV.setImageResource(R.drawable.ic_no_delivery);
                        }
                    }
                });
                updateBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (nameTIET.getEditableText().length()==0) {
                            nameTIET.setError("Please enter an item name");
                        } else if (categoryTIET.getEditableText().length()==0) {
                            categoryTIET.setError("Please enter the category");
                        } else if (priceTIET.getEditableText().length()==0) {
                            priceTIET.setError("Please enter the price of the item");
                        } else {
                            ItemModel itemModel1 = new ItemModel();
                            itemModel1.setName(nameTIET.getText().toString());
                            itemModel1.setCategory(categoryTIET.getText().toString());
                            itemModel1.setPrice(Double.parseDouble(priceTIET.getText().toString()));
                            itemModel1.setIsVeg(isVeg);
                            itemModel1.setPhotoUrl("https://www.foodiesfeed.com/wp-content/uploads/2019/04/mae-mu-oranges-ice-349x436.jpg");
                            itemModel1.setIsAvailable(isAvailable);
                            ShopModel shopModel = new ShopModel();
                            shopModel.setId(SharedPref.getInt(getApplicationContext(),Constants.shopId));
                            itemModel1.setShopModel(shopModel);
                            String phoneNumber = SharedPref.getString(getApplicationContext(), Constants.phoneNumber);
                            String authid = SharedPref.getString(getApplicationContext(), Constants.authId);
                            String role = SharedPref.getString(getApplicationContext(), Constants.role);
                            if (UserRole.SELLER.toString().equals(role)) {
                                userRole = UserRole.SELLER;
                            } else if (UserRole.SHOP_OWNER.toString().equals(role)) {
                                userRole = UserRole.SHOP_OWNER;
                            }
                            MainRepository.getItemService().insertItem(itemModel1, authid, phoneNumber, userRole.toString()).enqueue(new Callback<Response<String>>() {
                                @Override
                                public void onResponse(Call<Response<String>> call, retrofit2.Response<Response<String>> response) {
                                    Response<String> responseFromServer = response.body();
                                    if (responseFromServer.getCode().equals(ErrorLog.CodeSuccess) && responseFromServer.getMessage().equals(ErrorLog.Success)){
                                        Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        pullToRefresh.setRefreshing(true);
                                        getMenu();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response<String>> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "failure"+t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

                cancelBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void getMenu(){
        String phoneNumber = SharedPref.getString(getApplicationContext(), Constants.phoneNumber);
        String authid = SharedPref.getString(getApplicationContext(), Constants.authId);
        int shopId = SharedPref.getInt(getApplicationContext(),Constants.shopId);
        MainRepository.getItemService().getItemsByShopId(shopId, authid, phoneNumber, userRole.toString()).enqueue(new Callback<Response<List<ItemModel>>>() {
            @Override
            public void onResponse(Call<Response<List<ItemModel>>> call, retrofit2.Response<Response<List<ItemModel>>> response) {
                Response<List<ItemModel>> responseFromServer = response.body();
                if (responseFromServer.getCode().equals(ErrorLog.CodeSuccess) && responseFromServer.getMessage().equals(ErrorLog.Success)) {
                    shopMenuItemAdapter.setItemModelArrayList((ArrayList<ItemModel>) responseFromServer.getData());
                    shopMenuItemAdapter.notifyDataSetChanged();
                    ViewCompat.setNestedScrollingEnabled(shopMenuItemListRV, false);
                    pullToRefresh.setRefreshing(false);
                } else {
                    Toast.makeText(EditMenuActivity.this, responseFromServer.getMessage(), Toast.LENGTH_SHORT).show();
                    pullToRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<Response<List<ItemModel>>> call, Throwable t) {
                Toast.makeText(EditMenuActivity.this, "Unable to reach the server" + t.getMessage(), Toast.LENGTH_SHORT).show();
                pullToRefresh.setRefreshing(false);
            }
        });
    }
}

