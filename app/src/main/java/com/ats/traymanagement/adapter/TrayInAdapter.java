package com.ats.traymanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.traymanagement.R;
import com.ats.traymanagement.model.TrayInData;

import java.util.ArrayList;

public class TrayInAdapter extends RecyclerView.Adapter<TrayInAdapter.MyViewHolder> {


    private ArrayList<TrayInData> trayList;
    private Context context;

    public TrayInAdapter(ArrayList<TrayInData> trayList, Context context) {
        this.trayList = trayList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOutDate, tvOutSmall, tvOutBig, tvOutLead, tvInDate, tvInSmall, tvInBig, tvInLead, tvBalSmall, tvBalBig, tvBalLead,tvFrName;

        public MyViewHolder(View view) {
            super(view);
            tvFrName = view.findViewById(R.id.tvFrName);
            tvOutDate = view.findViewById(R.id.tvOutDate);
            tvOutSmall = view.findViewById(R.id.tvOutSmall);
            tvOutBig = view.findViewById(R.id.tvOutBig);
            tvOutLead = view.findViewById(R.id.tvOutLead);
            tvInDate = view.findViewById(R.id.tvInDate);
            tvInSmall = view.findViewById(R.id.tvInSmall);
            tvInBig = view.findViewById(R.id.tvInBig);
            tvInLead = view.findViewById(R.id.tvInLead);
            tvBalSmall = view.findViewById(R.id.tvBalSmall);
            tvBalBig = view.findViewById(R.id.tvBalBig);
            tvBalLead = view.findViewById(R.id.tvBalLead);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tray_in, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {


        TrayInData model = trayList.get(position);

        myViewHolder.tvFrName.setText(""+model.getFrName());
        myViewHolder.tvOutDate.setText("");
        myViewHolder.tvOutSmall.setText("" + model.getOuttraySmall());
        myViewHolder.tvOutBig.setText("" + model.getOuttrayBig());
        myViewHolder.tvOutLead.setText("" + model.getOuttrayLead());
        myViewHolder.tvInDate.setText("");
        myViewHolder.tvInSmall.setText("" + model.getIntraySmall());
        myViewHolder.tvInBig.setText("" + model.getIntrayBig());
        myViewHolder.tvInLead.setText("" + model.getIntrayLead());
        myViewHolder.tvBalSmall.setText("" + model.getBalanceSmall());
        myViewHolder.tvBalBig.setText("" + model.getBalanceBig());
        myViewHolder.tvBalLead.setText("" + model.getBalanceLead());


    }

    @Override
    public int getItemCount() {
        return trayList.size();
    }


}
