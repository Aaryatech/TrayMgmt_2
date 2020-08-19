package com.ats.traymanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.traymanagement.R;
import com.ats.traymanagement.activity.FrTrayReportEightDaysActivity;
import com.ats.traymanagement.model.AllFrBalanceTrayReport;

import java.util.ArrayList;

public class AllFrBalTrayAdapter extends RecyclerView.Adapter<AllFrBalTrayAdapter.MyViewHolder> {

    private ArrayList<AllFrBalanceTrayReport> frList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFrName, tvBalSmall, tvBalBig, tvBalLid;
        public CardView card;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvFrName = view.findViewById(R.id.tvFrName);
            tvBalSmall = view.findViewById(R.id.tvBalSmall);
            tvBalBig = view.findViewById(R.id.tvBalBig);
            tvBalLid = view.findViewById(R.id.tvBalLid);
            card = view.findViewById(R.id.card);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }


    public AllFrBalTrayAdapter(ArrayList<AllFrBalanceTrayReport> frList, Context context) {
        this.frList = frList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_all_fr_bal_tray, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvFrName.setText(frList.get(position).getFrName());
        holder.tvBalSmall.setText(""+frList.get(position).getBalanceSmall());
        holder.tvBalBig.setText(""+frList.get(position).getBalanceBig());
        holder.tvBalLid.setText(""+frList.get(position).getBalanceLead());

        if(position%2==0){
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }else{
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.colorList));
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, FrTrayReportEightDaysActivity.class);
                intent.putExtra("frId",frList.get(position).getFrId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return frList.size();
    }


}
