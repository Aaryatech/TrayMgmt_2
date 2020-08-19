package com.ats.traymanagement.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ats.traymanagement.R;
import com.ats.traymanagement.model.FrTrayReportData;

import java.util.ArrayList;

public class FrTrayReportAdapter extends RecyclerView.Adapter<FrTrayReportAdapter.MyViewHolder>  {


    private ArrayList<FrTrayReportData> trayDetailsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate,tvOpSmall, tvOpLead, tvOpBig,tvRecSmall, tvRecLead, tvRecBig,tvRetSmall, tvRetLead, tvRetBig,tvBalSmall, tvBalLead, tvBalBig;

        public MyViewHolder(View view) {
            super(view);

            tvDate = view.findViewById(R.id.tvDate);
            tvOpSmall = view.findViewById(R.id.tvOpSmall);
            tvOpLead = view.findViewById(R.id.tvOpLead);
            tvOpBig = view.findViewById(R.id.tvOpBig);
            tvRecSmall = view.findViewById(R.id.tvRecSmall);
            tvRecLead = view.findViewById(R.id.tvRecLead);
            tvRecBig = view.findViewById(R.id.tvRecBig);
            tvRetSmall = view.findViewById(R.id.tvRetSmall);
            tvRetLead = view.findViewById(R.id.tvRetLead);
            tvRetBig = view.findViewById(R.id.tvRetBig);
            tvBalSmall = view.findViewById(R.id.tvBalSmall);
            tvBalLead = view.findViewById(R.id.tvBalLead);
            tvBalBig = view.findViewById(R.id.tvBalBig);
        }
    }


    public FrTrayReportAdapter(ArrayList<FrTrayReportData> trayDetailsList) {
        this.trayDetailsList = trayDetailsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_fr_tray_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FrTrayReportData details = trayDetailsList.get(position);

        holder.tvDate.setText("" + details.getDateStr());

        holder.tvOpSmall.setText("" + details.getOpeningSmall());
        holder.tvOpLead.setText("" + details.getOpeningLead());
        holder.tvOpBig.setText("" + details.getOpeningBig());

        holder.tvRecSmall.setText("" + details.getReceivedSmall());
        holder.tvRecLead.setText("" + details.getReceivedLead());
        holder.tvRecBig.setText("" + details.getReceivedBig());

        holder.tvRetSmall.setText("" + details.getReturnSmall());
        holder.tvRetLead.setText("" + details.getReturnLead());
        holder.tvRetBig.setText("" + details.getReturnBig());

        holder.tvBalSmall.setText("" + details.getBalSmall());
        holder.tvBalLead.setText("" + details.getBalLead());
        holder.tvBalBig.setText("" + details.getBalBig());


    }

    @Override
    public int getItemCount() {
        return trayDetailsList.size();
    }

}
