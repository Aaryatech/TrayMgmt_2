package com.ats.traymanagement.adapter;

import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.activity.HomeActivity;
import com.ats.traymanagement.activity.TrayStatusActivity;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.model.Info;
import com.ats.traymanagement.model.TrayMgmtDetailData;
import com.ats.traymanagement.model.TrayMgmtHeaderDisplayList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MAXADMIN on 16/2/2018.
 */

public class TrayStatusListAdapter extends RecyclerView.Adapter<TrayStatusListAdapter.MyViewHolder> {

    private ArrayList<TrayMgmtDetailData> trayStatusList;
    private Context context;
    private String headerBean;
    private int type,vehStatus;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFrName, tvTotal, tvSmall, tvBig, tvLarge, tvXl;
        ImageView ivEdit;

        public MyViewHolder(View view) {
            super(view);
            tvFrName = view.findViewById(R.id.tvTrayStatus_FrName);
            tvTotal = view.findViewById(R.id.tvTrayStatus_Total);
            tvSmall = view.findViewById(R.id.tvTrayStatus_Small);
            tvBig = view.findViewById(R.id.tvTrayStatus_Big);
            tvLarge = view.findViewById(R.id.tvTrayStatus_Large);
            tvXl = view.findViewById(R.id.tvTrayStatus_XL);
            ivEdit = view.findViewById(R.id.ivTrayStatus_Menu);
        }
    }

    public TrayStatusListAdapter(ArrayList<TrayMgmtDetailData> trayStatusList, Context context, int type,int vehStatus) {
        this.trayStatusList = trayStatusList;
        this.context = context;
        this.type = type;
        this.vehStatus = vehStatus;
    }

    public TrayStatusListAdapter(ArrayList<TrayMgmtDetailData> trayStatusList, Context context, int type, String bean,int vehStatus) {
        this.trayStatusList = trayStatusList;
        this.context = context;
        this.headerBean = bean;
        this.type = type;
        this.vehStatus = vehStatus;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_tray_status_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvFrName.setText(trayStatusList.get(position).getFrName());
        holder.tvSmall.setText("" + trayStatusList.get(position).getOuttraySmall());
        holder.tvBig.setText("" + trayStatusList.get(position).getOuttrayBig());
        holder.tvLarge.setText("" + trayStatusList.get(position).getOuttrayLead());
        holder.tvXl.setText("" + trayStatusList.get(position).getOuttrayExtra());

        if (type == 1) {
            holder.ivEdit.setVisibility(View.VISIBLE);
        } else {
            if (trayStatusList.get(position).getTrayStatus()==1){
                holder.ivEdit.setVisibility(View.VISIBLE);
            }else{
                holder.ivEdit.setVisibility(View.GONE);
            }

        }

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTrayDialog(trayStatusList.get(position).getTranId(), trayStatusList.get(position).getTranDetailId(), trayStatusList.get(position).getFrId(), trayStatusList.get(position).getFrName(), trayStatusList.get(position).getOuttraySmall(), trayStatusList.get(position).getOuttrayBig(), trayStatusList.get(position).getOuttrayLead(), trayStatusList.get(position).getOuttrayExtra());
            }
        });

    }

    @Override
    public int getItemCount() {
        return trayStatusList.size();
    }


    public void updateTrayDialog(final int headerId, final int detailId, final int frId, final String frName, final int small, final int big, final int large, final int xl) {

        final Dialog openDialog = new Dialog(context);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.custom_add_franchise_layout);
        openDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.dimAmount = 0.75f;
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.x = 100;
        wlp.y = 100;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        final EditText edSmall = openDialog.findViewById(R.id.edAddFranchise_Small);
        final EditText edBig = openDialog.findViewById(R.id.edAddFranchise_Big);
        final EditText edLarge = openDialog.findViewById(R.id.edAddFranchise_Large);
        final EditText edXL = openDialog.findViewById(R.id.edAddFranchise_XL);
        TextView tvSubmit = openDialog.findViewById(R.id.tvAddFranchise_Next);
        TextView tvSelectFr = openDialog.findViewById(R.id.tvAddFranchise_SelectFr);
        TextView tvExtraTray = openDialog.findViewById(R.id.tvAddFranchise_ExtraTray);

        tvSelectFr.setVisibility(View.GONE);
        tvExtraTray.setVisibility(View.GONE);

//        edSmall.setText("" + small);
//        edBig.setText("" + big);
//        edLarge.setText("" + large);
//        edXL.setText("" + xl);

        if (small==0){
            edSmall.setText("");
        }else{
            edSmall.setText("" + small);
        }

        if (big==0){
            edBig.setText("");
        }else{
            edBig.setText("" + big);
        }

        if (large==0){
            edLarge.setText("");
        }else{
            edLarge.setText("" + large);
        }

        edXL.setText("" + xl);

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int sm=0,bg=0,lg=0;

                if (edSmall.getText().toString().trim().isEmpty()){
                    sm=0;
                }else{
                    sm = Integer.parseInt(edSmall.getText().toString());
                }

                if (edBig.getText().toString().trim().isEmpty()){
                    bg=0;
                }else{
                    bg = Integer.parseInt(edBig.getText().toString());
                }

                if (edLarge.getText().toString().trim().isEmpty()){
                    lg=0;
                }else{
                    lg = Integer.parseInt(edLarge.getText().toString());
                }

                int xlg = Integer.parseInt(edXL.getText().toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                String todaysDate = sdf.format(cal.getTimeInMillis());

                TrayMgmtDetailData trayMgmtDetailData = new TrayMgmtDetailData(detailId, headerId, frId, frName, bg, sm, lg, xlg, todaysDate, 0, 0, 0, 0, "0000-00-00", 0, 0, 0, 0, "0000-00-00", 0, 0, 0, 0, "0000-00-00", 0, 0, 0, 0, 0f, 0f, 0f, 0f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                openDialog.dismiss();
                saveTrayMgmtDetail(trayMgmtDetailData);

                /* int sm = Integer.parseInt(edSmall.getText().toString());
                int bg = Integer.parseInt(edBig.getText().toString());
                int lg = Integer.parseInt(edLarge.getText().toString());
                int xlg = Integer.parseInt(edXL.getText().toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                String todaysDate = sdf.format(cal.getTimeInMillis());

                TrayMgmtDetailData trayMgmtDetailData = new TrayMgmtDetailData(detailId, headerId, frId, frName, bg, sm, lg, xlg, todaysDate, 0, 0, 0, 0, "0000-00-00", 0, 0, 0, 0, "0000-00-00", 0, 0, 0, 0, "0000-00-00", 0, 0, 0, 0, 0f, 0f, 0f, 0f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                openDialog.dismiss();
                saveTrayMgmtDetail(trayMgmtDetailData);*/




            }
        });

        openDialog.show();
    }


    public void saveTrayMgmtDetail(final TrayMgmtDetailData trayMgmtDetailData) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> infoCall = Constants.myInterface.saveTrayMgmtDetail(trayMgmtDetailData);
            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            Info data = response.body();
                            if (data.isError()) {
                                commonDialog.dismiss();
                                Log.e("Save Tray Detail : ", " ERROR-----" + data.getMessage());
                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                Log.e("BEAN : ", "---------------------------" + trayMgmtDetailData);

                                TrayStatusActivity activity=(TrayStatusActivity)context;
                                activity.finish();
                                Intent intent = new Intent(context, TrayStatusActivity.class);
                                intent.putExtra("headerId", trayMgmtDetailData.getTranId());
                                intent.putExtra("headerBean", headerBean);
                                intent.putExtra("type", 1);
                                context.startActivity(intent);

                            }
                        } else {
                            commonDialog.dismiss();
                            Log.e("Save Tray Detail : ", " NULL-----");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Save Tray Detail : ", " Exception-----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("Save Tray Detail : ", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
