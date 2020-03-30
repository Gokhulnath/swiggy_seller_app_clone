package golhar.cocomo.zinger.seller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import golhar.cocomo.zinger.seller.OrderHistoryItemDetailActivity;
import golhar.cocomo.zinger.seller.R;
import golhar.cocomo.zinger.seller.model.OrderItemListModel;
import golhar.cocomo.zinger.seller.model.OrderItemModel;
import golhar.cocomo.zinger.seller.utils.Constants;


public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHolder> {

    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy    hh:mm:ss a");
    List<OrderItemListModel> itemList;
    Context context, activityContext;

    public OrderHistoryAdapter(List<OrderItemListModel> itemList, Context context, Context activityContext) {
        this.itemList = itemList;
        this.context = context;
        this.activityContext = activityContext;
    }

    public void setItemList(List<OrderItemListModel> itemList) {
        this.itemList = itemList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_row, parent, false);
        OrderHolder orderHolder = new OrderHolder(v);
        return orderHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        final OrderItemListModel orderItemListModel = itemList.get(position);
        holder.hotelNameTV.setText(orderItemListModel.getOrderModel().getShopModel().getName());
        holder.hotelPriceTV.setText("₹" + String.valueOf(orderItemListModel.getOrderModel().getPrice()));
        holder.orderDateTV.setText(dateFormat.format(orderItemListModel.getOrderModel().getDate()));
        String status = String.valueOf(orderItemListModel.getOrderModel().getOrderStatus());
        holder.hotelStatusTV.setText(status);
        switch (status) {
            case "PENDING":
                holder.hotelStatusTV.setTextColor(Color.parseColor("#006400"));
                break;
            case "TXN_FAILURE":
                holder.hotelStatusTV.setTextColor(Color.parseColor("#FF0000"));
                break;
            case "PLACED":
                holder.hotelStatusTV.setTextColor(Color.parseColor("#00FF00"));
                break;
            case "CANCELLED_BY_USER":
                holder.hotelStatusTV.setTextColor(Color.parseColor("#FF0000"));
                break;
            case "ACCEPTED":
                holder.hotelStatusTV.setTextColor(Color.parseColor("#e25822"));
                break;
            case "CANCELLED_BY_SELLER":
                holder.hotelStatusTV.setTextColor(Color.parseColor("#FF0000"));
                break;
            case "READY":
                holder.hotelStatusTV.setTextColor(Color.parseColor("#e25822"));
                break;
            case "OUT_FOR_DELIVERY":
                holder.hotelStatusTV.setTextColor(Color.parseColor("#e25822"));
                break;
            case "COMPLETED":
                holder.hotelStatusTV.setTextColor(Color.parseColor("#00FF00"));
                break;
            case "DELIVERED":
                holder.hotelStatusTV.setTextColor(Color.parseColor("#00FF00"));
                break;
        }
        String itemName, item, Quantity, tempItem;
        item = "";
        List<OrderItemModel> orderItemList = orderItemListModel.getOrderItemsList();
        for (int i = 0; i < orderItemList.size(); i++) {
            if(i<3){
            Quantity = orderItemList.get(i).getQuantity().toString();
            itemName = orderItemList.get(i).getItemModel().getName();
            tempItem = itemName + " x " + Quantity + "   ";
            item = item.concat(tempItem);}
            else{
                item = item.concat("..... See More");
                break;
            }
        }
        holder.orderItemTV.setText(item);
        holder.fullOrderRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent history = new Intent(activityContext, OrderHistoryItemDetailActivity.class);
                history.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                history.putExtra("FullOrderDetails", orderItemListModel);
                context.startActivity(history);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder {

        TextView hotelNameTV;
        TextView hotelPriceTV;
        TextView hotelStatusTV;
        TextView orderItemTV;
        TextView orderDateTV;
        RelativeLayout fullOrderRL;

        //todo change rv to rl in xml
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            hotelNameTV = itemView.findViewById(R.id.hotelNameTV);
            hotelPriceTV = itemView.findViewById(R.id.hotelPriceTV);
            hotelStatusTV = itemView.findViewById(R.id.hotelStatusTV);
            orderItemTV = itemView.findViewById(R.id.orderItemsTV);
            orderDateTV = itemView.findViewById(R.id.orderDateTV);
            fullOrderRL = itemView.findViewById(R.id.fullOrderRL);
        }
    }
    void LoadData(ArrayList<OrderItemModel> orderItemModelArrayList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.sharedPreferencesCart, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(orderItemModelArrayList);
        editor.putString(Constants.cart, json);
        editor.apply();
    }
}