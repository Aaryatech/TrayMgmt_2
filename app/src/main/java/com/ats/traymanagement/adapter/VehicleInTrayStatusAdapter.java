package com.ats.traymanagement.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.activity.TrayStatusActivity;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.model.Info;
import com.ats.traymanagement.model.TrayMgmtDetailData;
import com.ats.traymanagement.model.TrayMgtDetailsList;
import com.ats.traymanagement.model.VehicleInTrayStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MAXADMIN on 8/3/2018.
 */

public class VehicleInTrayStatusAdapter extends RecyclerView.Adapter<VehicleInTrayStatusAdapter.MyViewHolder> {


    private ArrayList<VehicleInTrayStatus> trayStatusList;
    private Context context;
    private String date;
    private int type;

    public class MyViewHolder extends RecyclerView.ViewHolder {
//        public TextView tvFrName, tvSmall, tvBig, tvLarge, tvSmall1, tvBig1, tvLarge1, tvSmall2, tvBig2, tvLarge2;
//        LinearLayout llOut, llIn, llIn1;

        LinearLayout llOutTray, llInTray, llInTray1, llTotal;
        public TextView tvFrName, tvOutTray_Date, tvOutTray_Small, tvOutTray_Big, tvOutTray_Lead;
        public TextView tvInTray_Date1, tvInTray_Small1, tvInTray_Big1, tvInTray_Lead1, tvInTray_Date2, tvInTray_Small2, tvInTray_Big2, tvInTray_Lead2;
        public TextView tvInTray1_Date1, tvInTray1_Small1, tvInTray1_Big1, tvInTray1_Lead1, tvInTray1_Date2, tvInTray1_Small2, tvInTray1_Big2, tvInTray1_Lead2, tvInTray1_Date3, tvInTray1_Small3, tvInTray1_Big3, tvInTray1_Lead3;
        public TextView tvTotal, tvTotal_Small, tvTotal_Big, tvTotal_Lead;

        public MyViewHolder(View view) {
            super(view);
//            tvFrName = view.findViewById(R.id.tvInTrayStatus_FrName);
//            tvSmall = view.findViewById(R.id.tvInTrayStatus_Small);
//            tvBig = view.findViewById(R.id.tvInTrayStatus_Big);
//            tvLarge = view.findViewById(R.id.tvInTrayStatus_Large);
//            tvSmall1 = view.findViewById(R.id.tvInTrayStatus_Small1);
//            tvBig1 = view.findViewById(R.id.tvInTrayStatus_Big1);
//            tvLarge1 = view.findViewById(R.id.tvInTrayStatus_Large1);
//            tvSmall2 = view.findViewById(R.id.tvInTrayStatus_Small2);
//            tvBig2 = view.findViewById(R.id.tvInTrayStatus_Big2);
//            tvLarge2 = view.findViewById(R.id.tvInTrayStatus_Large2);
//            llOut = view.findViewById(R.id.llInTrayStatus_OutTray);
//            llIn = view.findViewById(R.id.llInTrayStatus_InTray);
//            llIn1 = view.findViewById(R.id.llInTrayStatus_InTray1);
//

            llOutTray = view.findViewById(R.id.llInTray_OutTray);
            llInTray = view.findViewById(R.id.llInTray_InTray);
            llInTray1 = view.findViewById(R.id.llInTray_InTray1);
            llTotal = view.findViewById(R.id.llInTray_Total);

            tvFrName = view.findViewById(R.id.tvInTrayStatus_FrName);
            tvOutTray_Date = view.findViewById(R.id.tvInTray_OutTray_Date);
            tvOutTray_Small = view.findViewById(R.id.tvInTray_OutTray_Small);
            tvOutTray_Big = view.findViewById(R.id.tvInTray_OutTray_Big);
            tvOutTray_Lead = view.findViewById(R.id.tvInTray_OutTray_Lead);
            tvInTray_Date1 = view.findViewById(R.id.tvInTray_InTray_Date1);
            tvInTray_Small1 = view.findViewById(R.id.tvInTray_InTray_Small1);
            tvInTray_Big1 = view.findViewById(R.id.tvInTray_InTray_Big1);
            tvInTray_Lead1 = view.findViewById(R.id.tvInTray_InTray_Lead1);
            tvInTray_Date2 = view.findViewById(R.id.tvInTray_InTray_Date2);
            tvInTray_Small2 = view.findViewById(R.id.tvInTray_InTray_Small2);
            tvInTray_Big2 = view.findViewById(R.id.tvInTray_InTray_Big2);
            tvInTray_Lead2 = view.findViewById(R.id.tvInTray_InTray_Lead2);

            tvInTray1_Date1 = view.findViewById(R.id.tvInTray_InTray1_Date1);
            tvInTray1_Small1 = view.findViewById(R.id.tvInTray_InTray1_Small1);
            tvInTray1_Big1 = view.findViewById(R.id.tvInTray_InTray1_Big1);
            tvInTray1_Lead1 = view.findViewById(R.id.tvInTray_InTray1_Lead1);

            tvInTray1_Date2 = view.findViewById(R.id.tvInTray_InTray1_Date2);
            tvInTray1_Small2 = view.findViewById(R.id.tvInTray_InTray1_Small2);
            tvInTray1_Big2 = view.findViewById(R.id.tvInTray_InTray1_Big2);
            tvInTray1_Lead2 = view.findViewById(R.id.tvInTray_InTray1_Lead2);
            tvInTray1_Date3 = view.findViewById(R.id.tvInTray_InTray1_Date3);
            tvInTray1_Small3 = view.findViewById(R.id.tvInTray_InTray1_Small3);
            tvInTray1_Big3 = view.findViewById(R.id.tvInTray_InTray1_Big3);
            tvInTray1_Lead3 = view.findViewById(R.id.tvInTray_InTray1_Lead3);

            tvTotal = view.findViewById(R.id.tvInTray_Total);
            tvTotal_Small = view.findViewById(R.id.tvInTray_Total_Small);
            tvTotal_Big = view.findViewById(R.id.tvInTray_Total_Big);
            tvTotal_Lead = view.findViewById(R.id.tvInTray_Total_Lead);


        }
    }

    public VehicleInTrayStatusAdapter(ArrayList<VehicleInTrayStatus> trayStatusList, Context context, int type) {
        this.trayStatusList = trayStatusList;
        this.context = context;
        this.type = type;
    }

    public VehicleInTrayStatusAdapter(ArrayList<VehicleInTrayStatus> trayStatusList, Context context, int type, String date) {
        this.trayStatusList = trayStatusList;
        this.context = context;
        this.type = type;
        this.date = date;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_vehicle_in_tray_status, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if (trayStatusList.get(position).getTrayMgtDetailsList().size() > 0) {

            holder.tvFrName.setText(trayStatusList.get(position).getTrayMgtDetailsList().get(0).getFrName());

            int totalTray = 0, totalSmall = 0, totalBig = 0, totalLead = 0;
            for (int i = 0; i < trayStatusList.get(position).getTrayMgtDetailsList().size(); i++) {
                TrayMgtDetailsList detailsList = trayStatusList.get(position).getTrayMgtDetailsList().get(i);
                Log.e("DETAIL  ", "--------------------------" + detailsList);

                if (detailsList.getOuttrayDate().equalsIgnoreCase(date)) {
                    holder.llOutTray.setVisibility(View.VISIBLE);
                    holder.tvOutTray_Small.setText("" + detailsList.getOuttraySmall());
                    holder.tvOutTray_Big.setText("" + detailsList.getOuttrayBig());
                    holder.tvOutTray_Lead.setText("" + detailsList.getOuttrayLead());
                    holder.tvOutTray_Date.setText("" + detailsList.getOuttrayDate());

                } else if (detailsList.getIntrayDate().equalsIgnoreCase(date)) {
                    holder.llInTray.setVisibility(View.VISIBLE);

                    holder.tvInTray_Date1.setText("" + detailsList.getOuttrayDate());
                    holder.tvInTray_Small1.setText("" + detailsList.getOuttraySmall());
                    holder.tvInTray_Big1.setText("" + detailsList.getOuttrayBig());
                    holder.tvInTray_Lead1.setText("" + detailsList.getOuttrayLead());

                    holder.tvInTray_Date2.setText("" + detailsList.getIntrayDate());
                    holder.tvInTray_Small2.setText("" + detailsList.getIntraySmall());
                    holder.tvInTray_Big2.setText("" + detailsList.getIntrayBig());
                    holder.tvInTray_Lead2.setText("" + detailsList.getIntrayLead());

                    totalSmall = totalSmall + detailsList.getIntraySmall();
                    totalBig = totalBig + detailsList.getIntrayBig();
                    totalLead = totalLead + detailsList.getIntrayLead();


                } else if (detailsList.getIntrayDate1().equalsIgnoreCase(date)) {
                    holder.llInTray1.setVisibility(View.VISIBLE);

                    holder.tvInTray1_Date1.setText("" + detailsList.getOuttrayDate());
                    holder.tvInTray1_Small1.setText("" + detailsList.getOuttraySmall());
                    holder.tvInTray1_Big1.setText("" + detailsList.getOuttrayBig());
                    holder.tvInTray1_Lead1.setText("" + detailsList.getOuttrayLead());

                    holder.tvInTray1_Date2.setText("" + detailsList.getIntrayDate());
                    holder.tvInTray1_Small2.setText("" + detailsList.getIntraySmall());
                    holder.tvInTray1_Big2.setText("" + detailsList.getIntrayBig());
                    holder.tvInTray1_Lead2.setText("" + detailsList.getIntrayLead());

                    holder.tvInTray1_Date3.setText("" + detailsList.getIntrayDate1());
                    holder.tvInTray1_Small3.setText("" + detailsList.getIntraySmall1());
                    holder.tvInTray1_Big3.setText("" + detailsList.getIntrayBig1());
                    holder.tvInTray1_Lead3.setText("" + detailsList.getIntrayLead1());

                    totalSmall = totalSmall + detailsList.getIntraySmall1();
                    totalBig = totalBig + detailsList.getIntrayBig1();
                    totalLead = totalLead + detailsList.getIntrayLead1();

                }
            }

            holder.llTotal.setVisibility(View.VISIBLE);

            totalTray = totalSmall + totalBig + totalLead;

            holder.tvTotal.setText("" + totalTray);
            holder.tvTotal_Small.setText("" + totalSmall);
            holder.tvTotal_Big.setText("" + totalBig);
            holder.tvTotal_Lead.setText("" + totalLead);
        }

    }

    @Override
    public int getItemCount() {
        return trayStatusList.size();
    }


}
