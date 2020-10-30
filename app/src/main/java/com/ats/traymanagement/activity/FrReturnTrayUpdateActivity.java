package com.ats.traymanagement.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.adapter.InTrayDetailAdapter;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.model.InTrayDetail;
import com.ats.traymanagement.model.Info;
import com.ats.traymanagement.model.TrayDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FrReturnTrayUpdateActivity extends AppCompatActivity {

    private TextView tvFrName, tvFrId, tvDate, tvSmall, tvLids, tvBig, tvUpdate, tvBalSmall, tvBalBig, tvBalLid, tvTotalBal;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    String fromDate, toDate, frName;
    int frId;
    int vehInTrayId=0;

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fr_return_tray_update);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle("Return Tray List");

        tvFrName = findViewById(R.id.tvFrName);
        tvFrId = findViewById(R.id.tvFrId);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);

        tvDate = findViewById(R.id.tvDate);
        tvSmall = findViewById(R.id.tvSmall);
        tvLids = findViewById(R.id.tvLids);
        tvBig = findViewById(R.id.tvBig);
        tvUpdate = findViewById(R.id.tvUpdate);

        tvBalSmall = findViewById(R.id.tvBalSmall);
        tvBalBig = findViewById(R.id.tvBalBig);
        tvBalLid = findViewById(R.id.tvBalLid);
        tvTotalBal = findViewById(R.id.tvTotalBal);

        try {
            fromDate = getIntent().getStringExtra("FromDate");
            toDate = getIntent().getStringExtra("ToDate");
            frName = getIntent().getStringExtra("FrName");
            frId = Integer.parseInt(getIntent().getStringExtra("FrId"));

            tvFrName.setText("" + frName);

            tvDate.setText("" + fromDate);

            getBalTraySum(frId, 0);
            getFrReturnTray(fromDate, toDate, frId);

        } catch (Exception e) {
            e.printStackTrace();
        }


        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int totSmall = 0, totBig = 0, totLid = 0, entSmall = 0, entBig = 0, entLid = 0;

                try {
                    totSmall = Integer.parseInt(tvBalSmall.getText().toString().trim())+Integer.parseInt(tvSmall.getText().toString().trim());
                } catch (Exception e) {
                }
                try {
                    totBig = Integer.parseInt(tvBalBig.getText().toString().trim())+Integer.parseInt(tvBig.getText().toString().trim());
                } catch (Exception e) {
                }
                try {
                    totLid = Integer.parseInt(tvBalLid.getText().toString().trim())+Integer.parseInt(tvLids.getText().toString().trim());
                } catch (Exception e) {
                }
                try {
                    entSmall = Integer.parseInt(tvSmall.getText().toString().trim());
                } catch (Exception e) {
                }
                try {
                    entBig = Integer.parseInt(tvBig.getText().toString().trim());
                } catch (Exception e) {
                }
                try {
                    entLid = Integer.parseInt(tvLids.getText().toString().trim());
                } catch (Exception e) {
                }

                new showDialog(FrReturnTrayUpdateActivity.this, frId, fromDate, totSmall, totBig, totLid, entSmall, entBig, entLid).show();
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                try {
                    if (frId > 0) {
                        getFrReturnTray(fromDate, toDate, frId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("REFRESH_DATA")) {
                    handlePushNotification(intent);
                }
            }
        };
    }

    @Override
    public void onPause() {
        Log.e("FR RET", "  ON PAUSE");

        LocalBroadcastManager.getInstance(FrReturnTrayUpdateActivity.this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("FR RET", "  ON RESUME");

        LocalBroadcastManager.getInstance(FrReturnTrayUpdateActivity.this).registerReceiver(mBroadcastReceiver,
                new IntentFilter("REFRESH_DATA"));
    }


    private void handlePushNotification(Intent intent) {

        Log.e("handlePushNotification", "------------------------------------**********");
        getBalTraySum(frId, 0);
        getFrReturnTray(fromDate, toDate, frId);

    }


    public void getFrReturnTray(String fromDate, String toDate, int frId) {
        Log.e("PARAMETER ", "--------------- FROM - " + fromDate + "          TO - " + toDate + "           FR - " + frId);

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            final Call<ArrayList<InTrayDetail>> frTray = Constants.myInterface.getFrReturnTrayInData(fromDate, toDate, frId);
            frTray.enqueue(new Callback<ArrayList<InTrayDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<InTrayDetail>> call, Response<ArrayList<InTrayDetail>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<InTrayDetail> data = response.body();
                            commonDialog.dismiss();

                            Log.e("Franchise TRAY : ", " ---------------------------------- " + data);

                            InTrayDetailAdapter adapter = new InTrayDetailAdapter(data, FrReturnTrayUpdateActivity.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FrReturnTrayUpdateActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);

                            if (response.body().size() > 0) {
                                int small = 0, big = 0, lids = 0;
                                String date = "";
                                for (InTrayDetail tray : response.body()) {
                                    small = small + tray.getIntraySmall();
                                    big = big + tray.getIntrayBig();
                                    lids = lids + tray.getIntrayLead();
                                    date = tray.getIntrayDate();
                                    vehInTrayId=tray.getTranIntrayId();
                                }

                                tvSmall.setText("" + small);
                                tvBig.setText("" + big);
                                tvLids.setText("" + lids);

                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                                Date d = sdf1.parse(date);
                                tvDate.setText("" + sdf.format(d.getTime()));



                            }else{
                                tvSmall.setText("" + 0);
                                tvBig.setText("" + 0);
                                tvLids.setText("" + 0);

                            }

                        } else {
                            commonDialog.dismiss();
                            Log.e("Franchise : ", " NULL-----");

                            tvSmall.setText("" + 0);
                            tvBig.setText("" + 0);
                            tvLids.setText("" + 0);

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Franchise : ", " Exception-----" + e.getMessage());
                        tvSmall.setText("" + 0);
                        tvBig.setText("" + 0);
                        tvLids.setText("" + 0);

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<InTrayDetail>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("Franchise : ", " OnFailure-----" + t.getMessage());
                    tvSmall.setText("" + 0);
                    tvBig.setText("" + 0);
                    tvLids.setText("" + 0);

                }
            });

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void getBalTraySum(int frId, int status) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TrayDetails>> trayListCall = Constants.myInterface.getBalTraySum(frId, status);
            trayListCall.enqueue(new Callback<ArrayList<TrayDetails>>() {
                @Override
                public void onResponse(Call<ArrayList<TrayDetails>> call, Response<ArrayList<TrayDetails>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<TrayDetails> data = response.body();
                            commonDialog.dismiss();
                            // Log.e("TRAY : ", "Tray Details-------------BAL--------------" + data);

                            if (data.size() > 0) {

                                tvBalSmall.setText("" + data.get(0).getBalanceSmall());
                                tvBalBig.setText("" + data.get(0).getBalanceBig());
                                tvBalLid.setText("" + data.get(0).getBalanceLead());

                                tvTotalBal.setText("" + (data.get(0).getBalanceSmall() + data.get(0).getBalanceBig() + data.get(0).getBalanceLead()));

                            } else {

                                tvBalSmall.setText("0");
                                tvBalBig.setText("0");
                                tvBalLid.setText("0");
                                tvTotalBal.setText("0");

                            }


                        } else {
                            commonDialog.dismiss();
                            Log.e("TRAY : ", " NULL");

                            tvBalSmall.setText("0");
                            tvBalBig.setText("0");
                            tvBalLid.setText("0");
                            tvTotalBal.setText("0");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("TRAY : ", " Exception : " + e.getMessage());
                        e.printStackTrace();

                        tvBalSmall.setText("0");
                        tvBalBig.setText("0");
                        tvBalLid.setText("0");
                        tvTotalBal.setText("0");
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TrayDetails>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("TRAY : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();

                    tvBalSmall.setText("0");
                    tvBalBig.setText("0");
                    tvBalLid.setText("0");
                    tvTotalBal.setText("0");

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    public class showDialog extends Dialog {

        EditText edSmall, edBig, edLarge;
        TextView tvSubmit, tvSmall, tvBig, tvLids;
        InTrayDetail model;
        int totSmall, totBig, totLid, entSmall, entBig, entLid, frId;
        String date;

        public showDialog(@NonNull Context context) {
            super(context);
        }

        public showDialog(@NonNull Context context, int frId, String date, int totSmall, int totBig, int totLid, int entSmall, int entBig, int entLid) {
            super(context);
            this.totSmall = totSmall;
            this.totBig = totBig;
            this.totLid = totLid;

            this.entSmall = entSmall;
            this.entBig = entBig;
            this.entLid = entLid;

            this.frId = frId;
            this.date = date;
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

            tvSmall.setText("" + entSmall);
            tvBig.setText("" + entBig);
            tvLids.setText("" + entLid);

            tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String smallStr = edSmall.getText().toString().trim();
                    String bigStr = edBig.getText().toString().trim();
                    String lidStr = edLarge.getText().toString().trim();
                    int small = entSmall, big = entBig, lid = entLid;
                    try {
                        small = Integer.parseInt(smallStr);
                    } catch (Exception e) {
                        small = entSmall;
                    }

                    try {
                        big = Integer.parseInt(bigStr);
                    } catch (Exception e) {
                        big = entBig;
                    }

                    try {
                        lid = Integer.parseInt(lidStr);
                    } catch (Exception e) {
                        lid = entLid;
                    }

                    if (smallStr.isEmpty()) {
                        edSmall.setError("required");
                        edSmall.requestFocus();
                    } else if (small > totSmall) {
                        edSmall.setError("enter less than total balance");
                        edSmall.requestFocus();
                    } else if (bigStr.isEmpty()) {
                        edSmall.setError(null);
                        edBig.setError("required");
                        edBig.requestFocus();
                    } else if (big > totBig) {
                        edSmall.setError(null);
                        edBig.setError("enter less than total balance");
                        edBig.requestFocus();
                    } else if (lidStr.isEmpty()) {
                        edBig.setError(null);
                        edLarge.setError("required");
                        edLarge.requestFocus();
                    } else if (lid > totLid) {
                        edBig.setError(null);
                        edLarge.setError("enter less than total balance");
                        edLarge.requestFocus();
                    } else {
                        edSmall.setError(null);
                        edBig.setError(null);
                        edLarge.setError(null);

                        updateTray(frId, date, small, big, lid);
                        dismiss();

                    }

                }

            });
        }

    }


    public void updateTray(final int frId, String date, final int small, final int big, final int lid) {

        if (Constants.isOnline(FrReturnTrayUpdateActivity.this)) {
            final CommonDialog commonDialog = new CommonDialog(FrReturnTrayUpdateActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            final Call<Info> infoCall = Constants.myInterface.updateReturnTrayNew(frId, date, small, big, lid);
            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            Info info = response.body();

                            if (!info.isError()) {
                                commonDialog.dismiss();
                                Intent pushNotificationIntent = new Intent();
                                pushNotificationIntent.setAction("REFRESH_DATA");
                                LocalBroadcastManager.getInstance(FrReturnTrayUpdateActivity.this).sendBroadcast(pushNotificationIntent);

                                try{
                                    vehInTrayId=Integer.parseInt(info.getMessage());
                                }catch (Exception e){}

                                updateReturnTrayValuesByAdmin(frId,small,big,lid,vehInTrayId);

                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(FrReturnTrayUpdateActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(FrReturnTrayUpdateActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                            Log.e("Tray : Submit", "   NULL---");

                        }

                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(FrReturnTrayUpdateActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                        Log.e("Tray : Submit", "   Exception---" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(FrReturnTrayUpdateActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                    Log.e("Tray : Submit", "   ONFailure---" + t.getMessage());
                    t.printStackTrace();
                }
            });


        }

    }



    public void updateReturnTrayValuesByAdmin(int frId, int small, int big, int lid,int vehIntrayId) {

        if (Constants.isOnline(FrReturnTrayUpdateActivity.this)) {
            final CommonDialog commonDialog = new CommonDialog(FrReturnTrayUpdateActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            final Call<Info> infoCall = Constants.myInterface.updateReturnTrayValuesByAdmin(frId, small, big, lid,vehIntrayId);
            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            Info info = response.body();

                            if (!info.isError()) {
                                commonDialog.dismiss();
                                Intent pushNotificationIntent = new Intent();
                                pushNotificationIntent.setAction("REFRESH_DATA");
                                LocalBroadcastManager.getInstance(FrReturnTrayUpdateActivity.this).sendBroadcast(pushNotificationIntent);

                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(FrReturnTrayUpdateActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(FrReturnTrayUpdateActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                            Log.e("Tray : Submit", "   NULL---");

                        }

                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(FrReturnTrayUpdateActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                        Log.e("Tray : Submit", "   Exception---" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(FrReturnTrayUpdateActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                    Log.e("Tray : Submit", "   ONFailure---" + t.getMessage());
                    t.printStackTrace();
                }
            });


        }

    }

}