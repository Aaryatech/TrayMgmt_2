package com.ats.traymanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.traymanagement.R;
import com.ats.traymanagement.activity.TrayStatusActivity;
import com.ats.traymanagement.model.TrayMgmtHeaderDisplayList;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by MAXADMIN on 21/2/2018.
 */

public class VehicleStatusListAdapter extends RecyclerView.Adapter<VehicleStatusListAdapter.MyViewHolder> {


    private ArrayList<TrayMgmtHeaderDisplayList> vehicleList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvRoute, tvType, tvTrayStatus, tvDate, tvExtraTrayStatus;

        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvCustomVehicleList_Date);
            tvName = view.findViewById(R.id.tvCustomVehicleList_Name);
            tvRoute = view.findViewById(R.id.tvCustomVehicleList_Route);
            tvTrayStatus = view.findViewById(R.id.tvCustomVehicleList_TrayStatus);
            tvType = view.findViewById(R.id.tvCustomVehicleList_Type);
            tvExtraTrayStatus = view.findViewById(R.id.tvCustomVehicleList_ExtraTrayStatus);
        }
    }

    public VehicleStatusListAdapter(ArrayList<TrayMgmtHeaderDisplayList> vehicleList, Context context) {
        this.vehicleList = vehicleList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_vehicle_status_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvName.setText(vehicleList.get(position).getTranId()+"_"+vehicleList.get(position).getVehNo() + " " + vehicleList.get(position).getDriverName());
        holder.tvRoute.setText(vehicleList.get(position).getRouteName());

        if (vehicleList.get(position).getVehStatus() == 0) {
            holder.tvType.setText("LOADING");
            holder.tvType.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else if (vehicleList.get(position).getVehStatus() == 1) {
            holder.tvType.setText("OUT");
            holder.tvType.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else if (vehicleList.get(position).getVehStatus() == 2) {
            holder.tvType.setText("IN");
            holder.tvType.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }


        if (vehicleList.get(position).getVehStatus() == 2) {
            try {
                String date = vehicleList.get(position).getTranDate();
                String yyyy = date.substring(0, 4);
                String mm = date.substring(5, 7);
                String dd = date.substring(8, 10);

                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObj = sdf.parse(vehicleList.get(position).getVehIntime());
                String timeStr = new SimpleDateFormat("K:mm a").format(dateObj);

                holder.tvDate.setText(dd + "-" + mm + "-" + yyyy );

            } catch (Exception e) {
                holder.tvDate.setText(vehicleList.get(position).getTranDate());
            }

            holder.tvExtraTrayStatus.setText("Extra tray- " + vehicleList.get(position).getExtraTrayIn());
        } else if (vehicleList.get(position).getVehStatus() == 1) {

            try {
                String date = vehicleList.get(position).getTranDate();
                String yyyy = date.substring(0, 4);
                String mm = date.substring(5, 7);
                String dd = date.substring(8, 10);

                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObj = sdf.parse(vehicleList.get(position).getVehOuttime());
                String timeStr = new SimpleDateFormat("K:mm a").format(dateObj);

                holder.tvDate.setText(dd + "-" + mm + "-" + yyyy );

            } catch (Exception e) {
                holder.tvDate.setText(vehicleList.get(position).getTranDate());
            }

            holder.tvExtraTrayStatus.setText("Extra tray- " + vehicleList.get(position).getExtraTrayOut());
        } else {
            try {
                String date = vehicleList.get(position).getTranDate();
                String yyyy = date.substring(0, 4);
                String mm = date.substring(5, 7);
                String dd = date.substring(8, 10);

                holder.tvDate.setText(dd + "-" + mm + "-" + yyyy);

            } catch (Exception e) {
                holder.tvDate.setText(vehicleList.get(position).getTranDate());
            }

            holder.tvExtraTrayStatus.setText("Extra tray- " + vehicleList.get(position).getExtraTrayOut());
        }

        holder.tvTrayStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                String bean = gson.toJson(vehicleList.get(position));

                if (vehicleList.get(position).getVehStatus() == 2) {
                    Intent intent = new Intent(context, TrayStatusActivity.class);
                    intent.putExtra("headerId", vehicleList.get(position).getTranId());
                    intent.putExtra("headerBean", bean);
                    intent.putExtra("type", 2);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, TrayStatusActivity.class);
                    intent.putExtra("headerId", vehicleList.get(position).getTranId());
                    intent.putExtra("headerBean", bean);
                    intent.putExtra("type", 0);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }


}
