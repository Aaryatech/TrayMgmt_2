package com.ats.traymanagement.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.model.InTrayDetail;
import com.ats.traymanagement.model.TrayDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InTrayDetailAdapter extends RecyclerView.Adapter<InTrayDetailAdapter.MyViewHolder> {


    private ArrayList<InTrayDetail> trayList;
    private Context context;

    public InTrayDetailAdapter(ArrayList<InTrayDetail> trayList, Context context) {
        this.trayList = trayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_in_tray_detail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        final InTrayDetail model = trayList.get(i);

        try{

            SimpleDateFormat sdf1=new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");

            Date date=sdf2.parse(model.getIntrayDate());

            myViewHolder.tvDate.setText("" + sdf1.format(date.getTime()));

        }catch (Exception e){
            e.printStackTrace();
            myViewHolder.tvDate.setText("" +model.getIntrayDate());
        }


        myViewHolder.tvSmall.setText("" + model.getIntraySmall());
        myViewHolder.tvLids.setText("" + model.getIntrayLead());
        myViewHolder.tvBig.setText("" + model.getIntrayBig());

        myViewHolder.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new showDialog(context, model).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return trayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDate, tvUpdate, tvSmall, tvLids, tvBig;

        public MyViewHolder(View view) {
            super(view);

            tvDate = view.findViewById(R.id.tvDate);
            tvUpdate = view.findViewById(R.id.tvUpdate);
            tvSmall = view.findViewById(R.id.tvSmall);
            tvLids = view.findViewById(R.id.tvLids);
            tvBig = view.findViewById(R.id.tvBig);

        }
    }


//-----------------------------DIALOG--------------------------

    public class showDialog extends Dialog {

        EditText edSmall, edBig, edLarge;
        TextView tvSubmit, tvSmall, tvBig, tvLids;
        InTrayDetail model;

        public showDialog(@NonNull Context context) {
            super(context);
        }

        public showDialog(@NonNull Context context, InTrayDetail tModel) {
            super(context);
            this.model = tModel;
            Log.e("MODEL ", "******************* " + model);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_update_bal_tray);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.dimAmount = 0.75f;
            wlp.gravity = Gravity.CENTER;
            wlp.x = 100;
            wlp.y = 100;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            edSmall = findViewById(R.id.edSmall);
            edBig = findViewById(R.id.edBig);
            edLarge = findViewById(R.id.edLids);
            tvSubmit = findViewById(R.id.tvSubmit);

            tvSmall = findViewById(R.id.tvSmall);
            tvBig = findViewById(R.id.tvBig);
            tvLids = findViewById(R.id.tvLids);

            tvSmall.setText("" + model.getIntraySmall());
            tvBig.setText("" + model.getIntrayBig());
            tvLids.setText("" + model.getIntrayLead());

            tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int enteredSmall = model.getIntraySmall();
                    int enteredBig = model.getIntrayBig();
                    int enteredLid = model.getIntrayLead();

                    try {
                        enteredSmall = Integer.parseInt(edSmall.getText().toString().trim());
                    } catch (Exception e) {
                        enteredSmall = model.getIntraySmall();
                    }

                    try {
                        enteredBig = Integer.parseInt(edBig.getText().toString().trim());
                    } catch (Exception e) {
                        enteredBig = model.getIntrayBig();
                    }

                    try {
                        enteredLid = Integer.parseInt(edLarge.getText().toString().trim());
                    } catch (Exception e) {
                        enteredLid = model.getIntrayLead();
                    }


                    model.setExInt1(enteredSmall);
                    model.setExInt2(enteredBig);
                    model.setExVar1(enteredLid);

                    updateTray(model);
                    dismiss();

                }

            });
        }

    }


    public void updateTray(InTrayDetail inTrayDetail) {

        Log.e("PARAMETER ", " ---------------------------- " + inTrayDetail);

        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            final Call<TrayDetails> infoCall = Constants.myInterface.updateReturnTray(inTrayDetail);
            infoCall.enqueue(new Callback<TrayDetails>() {
                @Override
                public void onResponse(Call<TrayDetails> call, Response<TrayDetails> response) {
                    try {
                        if (response.body() != null) {
                            TrayDetails info = response.body();

                            if (info.getTranDetailId() > 0) {
                                commonDialog.dismiss();
                                Intent pushNotificationIntent = new Intent();
                                pushNotificationIntent.setAction("REFRESH_DATA");
                                LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotificationIntent);

                            }else{
                                commonDialog.dismiss();
                                Toast.makeText(context, "Unable To Process", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(context, "Unable To Process", Toast.LENGTH_SHORT).show();
                            Log.e("Tray : Submit", "   NULL---");

                        }

                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(context, "Unable To Process", Toast.LENGTH_SHORT).show();
                        Log.e("Tray : Submit", "   Exception---" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<TrayDetails> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(context, "Unable To Process", Toast.LENGTH_SHORT).show();
                    Log.e("Tray : Submit", "   ONFailure---" + t.getMessage());
                    t.printStackTrace();
                }
            });


        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

}
