package com.ats.traymanagement.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.adapter.VehicleInListAdapter;
import com.ats.traymanagement.adapter.VehicleStatusListAdapter;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.fragment.VehicleListFragment;
import com.ats.traymanagement.model.TrayMgmtHeaderDisplayList;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleStatusActivity extends AppCompatActivity {

    private RecyclerView rvVehicleList;
    private ArrayList<TrayMgmtHeaderDisplayList> headerDataArrayList = new ArrayList<>();

    VehicleStatusListAdapter adapter;

    int yyyy, mm, dd;
    long dateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_status);
        setTitle("All Vehicle Status");

        rvVehicleList = findViewById(R.id.rvVehicleStatusList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String todaysDate = sdf.format(cal.getTimeInMillis());

        getAllTrayMgmtHeaders(todaysDate);

    }

    public void getAllTrayMgmtHeaders(String date) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TrayMgmtHeaderDisplayList>> headersByDateAndStatus = Constants.myInterface.getAllVehicleList(date);
            headersByDateAndStatus.enqueue(new Callback<ArrayList<TrayMgmtHeaderDisplayList>>() {
                @Override
                public void onResponse(Call<ArrayList<TrayMgmtHeaderDisplayList>> call, Response<ArrayList<TrayMgmtHeaderDisplayList>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<TrayMgmtHeaderDisplayList> data = response.body();
                            commonDialog.dismiss();
                            headerDataArrayList = data;

                            adapter = new VehicleStatusListAdapter(headerDataArrayList, VehicleStatusActivity.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VehicleStatusActivity.this);
                            rvVehicleList.setLayoutManager(mLayoutManager);
                            rvVehicleList.setItemAnimator(new DefaultItemAnimator());
                            rvVehicleList.setAdapter(adapter);

                        } else {
                            commonDialog.dismiss();
                            Log.e("getAllTrayMgmtHeaders", " NULL-----");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("getAllTrayMgmtHeaders", " Exception-----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TrayMgmtHeaderDisplayList>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("getAllTrayMgmtHeaders", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_filter);
        item.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                new showDateDialog(this).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public class showDateDialog extends Dialog {

        EditText edDate;
        TextView tvDate, tvSubmit;

        public showDateDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            // setTitle("Filter");
            setContentView(R.layout.custom_date_filter_dialog);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.dimAmount = 0.75f;
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.x = 100;
            wlp.y = 100;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            edDate = findViewById(R.id.edCustomFilter_Date);
            tvDate = findViewById(R.id.tvCustomFilter_Date);
            tvSubmit = findViewById(R.id.tvCustomFilter_Submit);

            edDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (dateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(dateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    }
                    DatePickerDialog dialog = new DatePickerDialog(VehicleStatusActivity.this, dateListener, yr, mn, dy);
                    dialog.show();
                }
            });


            tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edDate.getText().toString().isEmpty()) {
                        edDate.setError("Select Date");
                        edDate.requestFocus();
                    } else {
                        dismiss();

                        String selectedDate = tvDate.getText().toString();
                        getAllTrayMgmtHeaders(selectedDate);

                    }
                }
            });
        }

        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;
                edDate.setText(dd + "-" + mm + "-" + yyyy);
                tvDate.setText(yyyy + "-" + mm + "-" + dd);

                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                dateMillis = calendar.getTimeInMillis();
            }
        };

    }


}
