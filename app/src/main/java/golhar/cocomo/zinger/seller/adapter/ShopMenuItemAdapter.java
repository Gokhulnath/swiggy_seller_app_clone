package golhar.cocomo.zinger.seller.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import golhar.cocomo.zinger.seller.R;
import golhar.cocomo.zinger.seller.enums.UserRole;
import golhar.cocomo.zinger.seller.model.ItemModel;
import golhar.cocomo.zinger.seller.model.OrderItemModel;
import golhar.cocomo.zinger.seller.service.MainRepository;
import golhar.cocomo.zinger.seller.utils.Constants;
import golhar.cocomo.zinger.seller.utils.ErrorLog;
import golhar.cocomo.zinger.seller.utils.Response;
import golhar.cocomo.zinger.seller.utils.SharedPref;
import retrofit2.Call;
import retrofit2.Callback;


public class ShopMenuItemAdapter extends RecyclerView.Adapter<ShopMenuItemAdapter.ItemNameHolder> {
    ArrayList<ItemModel> itemModelArrayList;
    Context context;
    ArrayList<OrderItemModel> orderItemModelArrayList = new ArrayList<>();
    int isVeg, isAvailable;
    UserRole userRole;


    public ShopMenuItemAdapter(ArrayList<ItemModel> itemModelArrayList, Context context) {
        this.itemModelArrayList = itemModelArrayList;
        this.context = context;
    }

    public ArrayList<ItemModel> getItemModelArrayList() {
        return itemModelArrayList;
    }

    public void setItemModelArrayList(ArrayList<ItemModel> itemModelArrayList) {
        this.itemModelArrayList = itemModelArrayList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ItemNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_menu_item, parent, false);
        ItemNameHolder itemNameHolder = new ItemNameHolder(v);
        return itemNameHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemNameHolder holder, int position) {
        final ItemModel itemModel = itemModelArrayList.get(position);


        Glide.with(context)
                .load(itemModel.getPhotoUrl())
                .placeholder(new ColorDrawable(Color.parseColor("#000000")))
                .into(holder.itemIconIV);
        holder.categoryTV.setText(itemModel.getCategory());
        holder.itemNameTV.setText(" " + itemModel.getName());
        if (itemModel.getIsVeg() == 0) {
            Drawable img = context.getResources().getDrawable(R.drawable.ic_non_vegetarian_mark);
            holder.itemNameTV.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }
        else{
            Drawable img = context.getResources().getDrawable(R.drawable.ic_vegetarian_mark);
            holder.itemNameTV.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }
        holder.priceTv.setText("₹" + itemModel.getPrice().toString());
        if (itemModel.getIsAvailable() == 0) {
            holder.itemLL.setBackgroundColor(Color.parseColor("#EFEFEF"));
            holder.priceTv.setText("₹" + itemModel.getPrice().toString()+"\nNot Available");
        }
        else{
            holder.itemLL.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.priceTv.setText("₹" + itemModel.getPrice().toString());
        }

        holder.deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                View vi = LayoutInflater.from(context).inflate(R.layout.activity_item_delete_prompt, null);
                TextView msgTV = vi.findViewById(R.id.msgTV);
                Button noBT = vi.findViewById(R.id.noBT);
                Button yesBT = vi.findViewById(R.id.yesBT);
                dialogBuilder.setView(vi);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                yesBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phoneNumber = SharedPref.getString(context, Constants.phoneNumber);
                        String authid = SharedPref.getString(context, Constants.authId);
                        String role = SharedPref.getString(context, Constants.role);
                        if (UserRole.SELLER.toString().equals(role)) {
                            userRole = UserRole.SELLER;
                        } else if (UserRole.SHOP_OWNER.toString().equals(role)) {
                            userRole = UserRole.SHOP_OWNER;
                        }
                        ItemModel itemModel1 = itemModel;
                        itemModel1.setIsDelete(null);
                        itemModel1.setIsAvailable(null);
                        itemModel1.setName(null);
                        itemModel1.setPhotoUrl(null);
                        itemModel1.setCategory(null);
                        itemModel1.setPrice(null);
                        MainRepository.getItemService().deleteItemById(itemModel1,authid,phoneNumber,userRole.toString()).enqueue(new Callback<Response<String>>() {
                            @Override
                            public void onResponse(Call<Response<String>> call, retrofit2.Response<Response<String>> response) {
                                Response<String> responseFromServer = response.body();
                                if (responseFromServer.getCode().equals(ErrorLog.CodeSuccess) && responseFromServer.getMessage().equals(ErrorLog.Success)){
                                    Toast.makeText(context, "deleted successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<Response<String>> call, Throwable t) {
                                Toast.makeText(context, "failure"+t.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                });
                noBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });


        holder.editItemBT.setOnClickListener((View.OnClickListener) view -> {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            View v = LayoutInflater.from(context).inflate(R.layout.activity_update_add_item, null);
            TextInputEditText nameTIET = v.findViewById(R.id.nameTIET);
            TextInputEditText categoryTIET = v.findViewById(R.id.categoryTIET);
            TextInputEditText priceTIET = v.findViewById(R.id.priceTIET);
            SwitchCompat isVegSC = v.findViewById(R.id.isVegSC);
            ImageView isVegIV = v.findViewById(R.id.isVegIV);
            SwitchCompat isAvailableSC = v.findViewById(R.id.isAvailableSC);
            ImageView isAvailableIV = v.findViewById(R.id.isAvailableIV);
            Button updateBT = v.findViewById(R.id.updateBT);
            Button cancelBT = v.findViewById(R.id.cancelBT);
            dialogBuilder.setView(v);
            AlertDialog dialog = dialogBuilder.create();
            nameTIET.setText(itemModel.getName());
            categoryTIET.setText(itemModel.getCategory());
            priceTIET.setText(itemModel.getPrice().toString());
            if (itemModel.getIsVeg() == 1) {
                isVegSC.setChecked(true);
                isVeg = 1;
                isVegIV.setImageResource(R.drawable.ic_vegetarian_mark);
            } else {
                isVegSC.setChecked(false);
                isVegIV.setImageResource(R.drawable.ic_non_vegetarian_mark);
                isVeg = 0;
            }
            if (itemModel.getIsAvailable() == 1) {
                isAvailableSC.setChecked(true);
                isAvailableIV.setImageResource(R.drawable.ic_check_circle);
                isAvailable = 1;
            } else {
                isAvailableSC.setChecked(false);
                isAvailableIV.setImageResource(R.drawable.ic_no_delivery);
                isAvailable = 0;
            }
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
                        ItemModel itemModel1 = itemModel;
                        itemModel1.setName(nameTIET.getText().toString());
                        itemModel1.setCategory(categoryTIET.getText().toString());
                        itemModel1.setPrice(Double.parseDouble(priceTIET.getText().toString()));
                        itemModel1.setIsVeg(isVeg);
                        itemModel1.setIsAvailable(isAvailable);
                        String phoneNumber = SharedPref.getString(context, Constants.phoneNumber);
                        String authid = SharedPref.getString(context, Constants.authId);
                        String role = SharedPref.getString(context, Constants.role);
                        if (UserRole.SELLER.toString().equals(role)) {
                            userRole = UserRole.SELLER;
                        } else if (UserRole.SHOP_OWNER.toString().equals(role)) {
                            userRole = UserRole.SHOP_OWNER;
                        }
                        MainRepository.getItemService().updateItemById(itemModel1, authid, phoneNumber, userRole.toString()).enqueue(new Callback<Response<String>>() {
                            @Override
                            public void onResponse(Call<Response<String>> call, retrofit2.Response<Response<String>> response) {
                                Response<String> responseFromServer = response.body();
                                if (responseFromServer.getCode().equals(ErrorLog.CodeSuccess) && responseFromServer.getMessage().equals(ErrorLog.Success)){
                                    Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Response<String>> call, Throwable t) {
                                Toast.makeText(context, "failure"+t.getMessage(), Toast.LENGTH_SHORT).show();
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

        });


    }

    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }

    public class ItemNameHolder extends RecyclerView.ViewHolder {

        ImageView itemIconIV;
        TextView categoryTV;
        TextView itemNameTV;
        TextView priceTv;
        Button editItemBT;
        Button deleteBT;
        LinearLayout itemLL;

        public ItemNameHolder(@NonNull View itemView) {
            super(itemView);
            itemIconIV = itemView.findViewById(R.id.itemIconIV);
            categoryTV = itemView.findViewById(R.id.categoryTV);
            itemNameTV = itemView.findViewById(R.id.itemNameTV);
            priceTv = itemView.findViewById(R.id.priceTV);
            editItemBT = itemView.findViewById(R.id.editItemBT);
            deleteBT = itemView.findViewById(R.id.deleteBT);
            itemLL = itemView.findViewById(R.id.itemLL);
        }
    }

}
