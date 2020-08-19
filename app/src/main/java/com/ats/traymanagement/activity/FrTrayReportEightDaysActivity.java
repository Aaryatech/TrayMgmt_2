package com.ats.traymanagement.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.adapter.FrTrayReportAdapter;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.model.FrTrayReportData;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FrTrayReportEightDaysActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FrTrayReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fr_tray_report_eight_days);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle("Detail Report");

        recyclerView = findViewById(R.id.recyclerView);

        int frId = getIntent().getIntExtra("frId", 0);
        Log.e("FR_ID", "----------------------------- "+frId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        getTrayReportForEightDays(sdf.format(cal.getTimeInMillis()), frId);


    }


    public void getTrayReportForEightDays(String fromDate, int frId) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<FrTrayReportData>> infoCall = Constants.myInterface.getFrTrayReportData(frId, fromDate);
            infoCall.enqueue(new Callback<ArrayList<FrTrayReportData>>() {
                @Override
                public void onResponse(Call<ArrayList<FrTrayReportData>> call, Response<ArrayList<FrTrayReportData>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<FrTrayReportData> data = response.body();
                            commonDialog.dismiss();
                            Log.e("TRAY Report : ", "Info Date---------------------------" + data);

                            Collections.reverse(data);

                            adapter = new FrTrayReportAdapter(data);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FrTrayReportEightDaysActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);

                        } else {
                            commonDialog.dismiss();
                            Log.e("TRAY Report : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("TRAY Report : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<FrTrayReportData>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("TRAY Report : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();

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