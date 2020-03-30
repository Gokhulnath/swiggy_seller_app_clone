package golhar.cocomo.zinger.seller.adapter;

import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import golhar.cocomo.zinger.seller.R;
import golhar.cocomo.zinger.seller.model.ShopConfigurationModel;
import golhar.cocomo.zinger.seller.utils.Constants;
import golhar.cocomo.zinger.seller.utils.SharedPref;

import static android.graphics.Color.parseColor;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ShopNameHolder> {
    String Time = new SimpleDateFormat("HH:mm:ss").format(new Date());
    Date currentTime = new SimpleDateFormat("HH:mm:ss").parse(Time);
    ArrayList<ShopConfigurationModel> shopConfigurationModelArrayList;
    Context context;

    public ShopListAdapter(ArrayList<ShopConfigurationModel> shopConfigurationModelArrayList, Context context) throws ParseException {
        this.shopConfigurationModelArrayList = shopConfigurationModelArrayList;
        this.context = context;
    }

    public ArrayList<ShopConfigurationModel> getShopConfigurationModelArrayList() {
        return shopConfigurationModelArrayList;
    }

    public void setShopConfigurationModelArrayList(ArrayList<ShopConfigurationModel> shopConfigurationModelArrayList) {
        this.shopConfigurationModelArrayList = shopConfigurationModelArrayList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ShopListAdapter.ShopNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item_row, parent, false);
        ShopNameHolder shopNameHolder = new ShopNameHolder(v);
        return shopNameHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopListAdapter.ShopNameHolder holder, int position) {
        final ShopConfigurationModel shopConfigurationModel = shopConfigurationModelArrayList.get(position);
        Glide.with(context)
                .load(shopConfigurationModel.getShopModel().getPhotoUrl())
                .placeholder(new ColorDrawable(Color.parseColor("#000000")))
                .into(holder.shopProfileImageCIV);
        holder.collegeNameTV.setText(SharedPref.getString(context, Constants.collegeName));
        holder.shopNameTV.setText(shopConfigurationModel.getShopModel().getName());
        if(SharedPref.getInt(context,Constants.shopId)==shopConfigurationModel.getShopModel().getId()){
            holder.shopLL.setBackgroundColor(Color.parseColor("#EFEFEF"));
            holder.switchBT.setVisibility(View.INVISIBLE);
        }
        else{
            holder.switchBT.setVisibility(View.VISIBLE);
            holder.shopLL.setBackgroundColor(Color.parseColor("#000000"));
        }
        holder.switchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.putInt(context,Constants.shopId,shopConfigurationModel.getShopModel().getId());
                SharedPref.putString(context,Constants.shopName,shopConfigurationModel.getShopModel().getName());
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return shopConfigurationModelArrayList.size();
    }

    public class ShopNameHolder extends RecyclerView.ViewHolder {

        CircleImageView shopProfileImageCIV;
        TextView shopNameTV;
        TextView collegeNameTV;
        Button switchBT;
        LinearLayout shopLL;

        public ShopNameHolder(@NonNull View itemView) {
            super(itemView);
            shopProfileImageCIV = itemView.findViewById(R.id.shopProfileImageCIV);
            shopNameTV = itemView.findViewById(R.id.shopNameTV);
            collegeNameTV = itemView.findViewById(R.id.collegeNameTV);
            shopLL = itemView.findViewById(R.id.shopLL);
            switchBT=itemView.findViewById(R.id.switchBT);
        }
    }
}
