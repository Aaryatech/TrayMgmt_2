package com.ats.traymanagement.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.adapter.InTrayDetailAdapter;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.model.InTrayDetail;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FrReturnTrayUpdateActivity extends AppCompatActivity {

    private TextView tvFrName,tvFrId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    String fromDate,toDate,frName;
    int frId;

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fr_return_tray_update);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle("Return Tray List");

        tvFrName=findViewById(R.id.tvFrName);
        tvFrId=findViewById(R.id.tvFrId);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        recyclerView=findViewById(R.id.recyclerView);

        try{
            fromDate=getIntent().getStringExtra("FromDate");
            toDate=getIntent().getStringExtra("ToDate");
            frName=getIntent().getStringExtra("FrName");
            frId=Integer.parseInt(getIntent().getStringExtra("FrId"));

            tvFrName.setText(""+frName);

            getFrReturnTray(fromDate,toDate,frId);

        }catch (Exception e){
            e.printStackTrace();
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                try {
                    if (frId > 0) {
                        getFrReturnTray(fromDate,toDate,frId);
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
        getFrReturnTray(fromDate,toDate,frId);

    }



    public void getFrReturnTray(String fromDate,String toDate,int frId) {
        Log.e("PARAMETER ","--------------- FROM - "+fromDate+"          TO - "+toDate+"           FR - "+frId);

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<InTrayDetail>> frTray = Constants.myInterface.getFrReturnTrayInData(fromDate,toDate,frId);
            frTray.enqueue(new Callback<ArrayList<InTrayDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<InTrayDetail>> call, Response<ArrayList<InTrayDetail>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<InTrayDetail> data = response.body();
                            commonDialog.dismiss();

                            Log.e("Franchise TRAY : ", " ---------------------------------- "+data);

                            InTrayDetailAdapter adapter = new InTrayDetailAdapter(data, FrReturnTrayUpdateActivity.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FrReturnTrayUpdateActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);

                        } else {
                            commonDialog.dismiss();
                            Log.e("Franchise : ", " NULL-----");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Franchise : ", " Exception-----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<InTrayDetail>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("Franchise : ", " OnFailure-----" + t.getMessage());
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

}