package com.ats.traymanagement.activity;

import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.adapter.AllFrBalTrayAdapter;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.model.AllFrBalanceTrayReport;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FranchiseWiseReportActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    AllFrBalTrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franchise_wise_report);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle("Franchise Balance Tray Report");

        recyclerView = findViewById(R.id.recyclerView);

        getTrayReport();
    }


    public void getTrayReport() {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<AllFrBalanceTrayReport>> infoCall = Constants.myInterface.getAllFrBalTray();
            infoCall.enqueue(new Callback<ArrayList<AllFrBalanceTrayReport>>() {
                @Override
                public void onResponse(Call<ArrayList<AllFrBalanceTrayReport>> call, Response<ArrayList<AllFrBalanceTrayReport>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<AllFrBalanceTrayReport> data = response.body();
                            commonDialog.dismiss();
                            Log.e("TRAY Report : ", "Info Date---------------------------" + data);

                           // Collections.reverse(data);

                            adapter = new AllFrBalTrayAdapter(data,FranchiseWiseReportActivity.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FranchiseWiseReportActivity.this);
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
                public void onFailure(Call<ArrayList<AllFrBalanceTrayReport>> call, Throwable t) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        searchViewItem.setVisible(true);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                adapter.getFilter().filter(query);
             /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
}