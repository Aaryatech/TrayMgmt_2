package com.ats.traymanagement.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.adapter.TrayInAdapter;
import com.ats.traymanagement.adapter.TrayStatusListAdapter;
import com.ats.traymanagement.adapter.VehicleInTrayStatusAdapter;
import com.ats.traymanagement.adapter.VehicleListAdapter;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.fragment.VehicleListFragment;
import com.ats.traymanagement.model.FrTrayCount;
import com.ats.traymanagement.model.FranchiseByRoute;
import com.ats.traymanagement.model.Info;
import com.ats.traymanagement.model.TrayInData;
import com.ats.traymanagement.model.TrayMgmtDetailData;
import com.ats.traymanagement.model.TrayMgmtHeaderData;
import com.ats.traymanagement.model.TrayMgmtHeaderDisplayList;
import com.ats.traymanagement.model.TrayMgtDetailsList;
import com.ats.traymanagement.model.VehicleInTrayStatus;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrayStatusActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvList;
    private FloatingActionButton fab;
    TrayStatusListAdapter adapter;
    VehicleInTrayStatusAdapter adapterIn;

    private TextView tvTotal, tvExtra, tvSmall, tvBig, tvLarge, tvXL, tvVehicleNo, tvDriverName, tvRoute, tvTotalLabel, tvExtralabel;

    TrayMgmtHeaderDisplayList headerBean;

    private ArrayList<FranchiseByRoute> franchiseArray = new ArrayList<>();

    Dialog dialog;

    TrayInAdapter inAdapter;

    int type, headerId;
    String todaysDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray_status);
        setTitle("Tray Status");

        rvList = findViewById(R.id.rvTrayStatusList);
        fab = findViewById(R.id.fabTrayStatusActivity_Add);
        fab.setOnClickListener(this);

        tvTotalLabel = findViewById(R.id.tvTrayStatusActivity_TotalLabel);
        tvTotal = findViewById(R.id.tvTrayStatusActivity_Total);
        tvExtralabel = findViewById(R.id.tvTrayStatusActivity_ExtraLabel);
        tvExtra = findViewById(R.id.tvTrayStatusActivity_Extra);
        tvSmall = findViewById(R.id.tvTrayStatusActivity_Small);
        tvBig = findViewById(R.id.tvTrayStatusActivity_Big);
        tvLarge = findViewById(R.id.tvTrayStatusActivity_Large);
        tvXL = findViewById(R.id.tvTrayStatusActivity_XL);
        tvVehicleNo = findViewById(R.id.TrayStatusActivity_VehicleNo);
        tvDriverName = findViewById(R.id.TrayStatusActivity_DriverName);
        tvRoute = findViewById(R.id.TrayStatusActivity_Route);

        headerId = getIntent().getIntExtra("headerId", 0);
        type = getIntent().getIntExtra("type", 0);

        Gson gson = new Gson();
        String headerStr = getIntent().getStringExtra("headerBean");
        headerBean = gson.fromJson(headerStr, TrayMgmtHeaderDisplayList.class);

        if (type == 1) {
            fab.setVisibility(View.VISIBLE);
            getAllTrayMgmtDetailsByHeader(headerId);
        } else if (type == 2) {
            fab.setVisibility(View.GONE);
            // getServerDate(headerId, todaysDate);
            String beanDate = headerBean.getTranDate();

            getTrayInData(headerId);
           // getAllTrayMgmtDetailsByHeaderForIn(headerId, beanDate);


        } else {
            getAllTrayMgmtDetailsByHeader(headerId);
            fab.setVisibility(View.GONE);
        }


        if (headerBean != null) {
            tvVehicleNo.setText("" + headerBean.getVehNo());
            tvDriverName.setText("" + headerBean.getDriverName());
            tvRoute.setText("" + headerBean.getRouteName());
        }

        tvExtra.setText("" + headerBean.getExtraTrayOut());

        Log.e("Header Bean : ", "----------------------------" + headerBean);

        // getTrayMgmtHeaders(headerId);

        getAllFranchise(headerBean.getRouteId(), headerId);

    }

    public void getAllTrayMgmtDetailsByHeader(final int headerId) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            final Call<ArrayList<TrayMgmtDetailData>> trayMgmtDetailDataCall = Constants.myInterface.getTrayMgmtDetailByHeaderId(headerId);
            trayMgmtDetailDataCall.enqueue(new Callback<ArrayList<TrayMgmtDetailData>>() {
                @Override
                public void onResponse(Call<ArrayList<TrayMgmtDetailData>> call, Response<ArrayList<TrayMgmtDetailData>> response) {
                    try {
                        if (response.body() != null) {
                            commonDialog.dismiss();
                            ArrayList<TrayMgmtDetailData> data = response.body();
                            Log.e("DATA : ", "------------------" + data);

                            Gson gson = new Gson();
                            String bean = gson.toJson(headerBean);

                            adapter = new TrayStatusListAdapter(data, TrayStatusActivity.this, type, bean);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TrayStatusActivity.this);
                            rvList.setLayoutManager(mLayoutManager);
                            rvList.setItemAnimator(new DefaultItemAnimator());
                            rvList.setAdapter(adapter);

                            int total = 0, small = 0, big = 0, large = 0, xl = 0;
                            for (int i = 0; i < data.size(); i++) {
                                small = small + data.get(i).getOuttraySmall();
                                big = big + data.get(i).getOuttrayBig();
                                large = large + data.get(i).getOuttrayLead();
                                xl = xl + data.get(i).getOuttrayExtra();
                            }

                            tvSmall.setText("" + small);
                            tvBig.setText("" + big);
                            tvLarge.setText("" + large);
                            tvXL.setText("" + xl);
                            tvTotal.setText("" + (small + big + large + xl));


                        } else {
                            commonDialog.dismiss();
                            Log.e("getAllTrayMgmtDetails :", " NULL-----");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("getAllTrayMgmtDetails :", " Exception-----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TrayMgmtDetailData>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("getAllTrayMgmtDetails :", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllTrayMgmtDetailsByHeaderForIn(final int headerId, final String date) {
        Log.e("HEADER ID", "--------------------" + headerId);
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            final Call<ArrayList<VehicleInTrayStatus>> trayMgmtDetailDataCall = Constants.myInterface.getTrayMgmtDetailByHeaderIdAndDateForIn(headerId, date);
            trayMgmtDetailDataCall.enqueue(new Callback<ArrayList<VehicleInTrayStatus>>() {
                @Override
                public void onResponse(Call<ArrayList<VehicleInTrayStatus>> call, Response<ArrayList<VehicleInTrayStatus>> response) {
                    try {
                        if (response.body() != null) {
                            commonDialog.dismiss();
                            ArrayList<VehicleInTrayStatus> data = response.body();
                            Log.e("DATA : ", "-----------------****-" + response.body());
                            //  Log.e("DETAIL COUNT", "-----------------------------------" + data.get(0).getTrayMgtDetailsList().size());

                            String dateStr = "";
                            try {
                                String beanDate = date;
                                String yyyy = beanDate.substring(0, 4);
                                String mm = beanDate.substring(5, 7);
                                String dd = beanDate.substring(8, 10);

                                dateStr = dd + "-" + mm + "-" + yyyy;
                                Log.e("DATE PARAMETER", "------------------------" + dateStr);

                                adapterIn = new VehicleInTrayStatusAdapter(data, TrayStatusActivity.this, type, dateStr);


                            } catch (Exception e) {
                            }


                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TrayStatusActivity.this);
                            rvList.setLayoutManager(mLayoutManager);
                            rvList.setItemAnimator(new DefaultItemAnimator());
                            rvList.setAdapter(adapterIn);

                            int total = 0, small = 0, big = 0, lead = 0;
                            for (int i = 0; i < data.size(); i++) {

                                if (data.get(i).getTrayMgtDetailsList().size() > 0) {

                                    for (int j = 0; j < data.get(i).getTrayMgtDetailsList().size(); j++) {

                                        TrayMgtDetailsList detailsList = data.get(i).getTrayMgtDetailsList().get(j);

                                        if (dateStr.equalsIgnoreCase(detailsList.getIntrayDate())) {
                                            small = small + detailsList.getIntraySmall();
                                            big = big + detailsList.getIntrayBig();
                                            lead = lead + detailsList.getIntrayLead();
                                        }

                                        if (dateStr.equalsIgnoreCase(detailsList.getIntrayDate1())) {
                                            small = small + detailsList.getIntraySmall1();
                                            big = big + detailsList.getIntrayBig1();
                                            lead = lead + detailsList.getIntrayLead1();
                                        }
                                    }
                                }
                            }

                            tvSmall.setText("" + small);
                            tvBig.setText("" + big);
                            tvLarge.setText("" + lead);
                            tvTotal.setText("" + (small + big + lead));
                            tvTotalLabel.setText("Total In Tray : ");
                            tvExtra.setVisibility(View.GONE);
                            tvExtralabel.setVisibility(View.GONE);


                        } else {
                            commonDialog.dismiss();
                            Log.e("getAllTrayMgmtDetails :", " NULL----****-");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("getAllTrayMgmtDetails :", " Exception---****--" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<VehicleInTrayStatus>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("getAllTrayMgmtDetails :", " OnFailure----****-" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getServerDate(final int headerId, final String date) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> infoCall = Constants.myInterface.getServerDate();
            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            Info data = response.body();
                            commonDialog.dismiss();
                            Log.e("TRAY : ", "Info Date---------------------------" + data);

                            todaysDate = data.getMessage();

                            getAllTrayMgmtDetailsByHeaderForIn(headerId);

                        } else {
                            commonDialog.dismiss();
                            Log.e("TRAY : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("TRAY : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("TRAY : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllTrayMgmtDetailsByHeaderForIn(final int headerId) {
        Log.e("HEADER ID", "--------------------" + headerId);
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            final Call<ArrayList<VehicleInTrayStatus>> trayMgmtDetailDataCall = Constants.myInterface.getTrayMgmtDetailByHeaderIdForIn(headerId);
            trayMgmtDetailDataCall.enqueue(new Callback<ArrayList<VehicleInTrayStatus>>() {
                @Override
                public void onResponse(Call<ArrayList<VehicleInTrayStatus>> call, Response<ArrayList<VehicleInTrayStatus>> response) {
                    try {
                        if (response.body() != null) {
                            commonDialog.dismiss();
                            ArrayList<VehicleInTrayStatus> data = response.body();
                            Log.e("DATA : ", "-----------------****-" + data);

                            Gson gson = new Gson();
                            String bean = gson.toJson(headerBean);

                            adapterIn = new VehicleInTrayStatusAdapter(data, TrayStatusActivity.this, type, todaysDate);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TrayStatusActivity.this);
                            rvList.setLayoutManager(mLayoutManager);
                            rvList.setItemAnimator(new DefaultItemAnimator());
                            rvList.setAdapter(adapterIn);

                            int total = 0, small = 0, big = 0, lead = 0;
                            for (int i = 0; i < data.size(); i++) {

                                if (data.get(i).getTrayMgtDetailsList().size() > 0) {

                                    for (int j = 0; j < data.get(i).getTrayMgtDetailsList().size(); j++) {

                                        TrayMgtDetailsList detailsList = data.get(i).getTrayMgtDetailsList().get(j);

                                        if (todaysDate.equalsIgnoreCase(detailsList.getIntrayDate())) {
                                            small = small + detailsList.getIntraySmall();
                                            big = big + detailsList.getIntrayBig();
                                            lead = lead + detailsList.getIntrayLead();
                                        }

                                        if (todaysDate.equalsIgnoreCase(detailsList.getIntrayDate1())) {
                                            small = small + detailsList.getIntraySmall1();
                                            big = big + detailsList.getIntrayBig1();
                                            lead = lead + detailsList.getIntrayLead1();
                                        }

                                    }

                                }

                            }

                            tvSmall.setText("" + small);
                            tvBig.setText("" + big);
                            tvLarge.setText("" + lead);
                            tvTotal.setText("" + (small + big + lead));
                            tvTotalLabel.setText("Total In Tray : ");
                            tvExtra.setVisibility(View.GONE);
                            tvExtralabel.setVisibility(View.GONE);


                        } else {
                            commonDialog.dismiss();
                            Log.e("getAllTrayMgmtDetails :", " NULL----****-");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("getAllTrayMgmtDetails :", " Exception---****--" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<VehicleInTrayStatus>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("getAllTrayMgmtDetails :", " OnFailure----****-" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    //-------------------------------------------------------------


    public void extraTrayDialog() {
        final Dialog openDialog = new Dialog(this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.custom_extra_tray_layout);
        openDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.dimAmount = 0.75f;
        wlp.x = 100;
        wlp.y = 100;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        final EditText edExtraTray = openDialog.findViewById(R.id.edExtraTray);
        TextView tvSubmit = openDialog.findViewById(R.id.tvExtraTray_Submit);

        edExtraTray.setSelection(edExtraTray.getText().length());

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edExtraTray.getText().toString().isEmpty()) {
                    edExtraTray.setError("Required");
                    edExtraTray.requestFocus();
                } else {
                    int extraTray = Integer.parseInt(edExtraTray.getText().toString());
                    openDialog.dismiss();
                    updateVehicleOutTray(headerId, extraTray);
                }
            }
        });

        openDialog.show();
    }

    public void getAllFranchise(int routeId, int headerId) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<FranchiseByRoute>> allFranchiseByRoute = Constants.myInterface.getAllFranchiseByRoute(routeId, headerId);
            allFranchiseByRoute.enqueue(new Callback<ArrayList<FranchiseByRoute>>() {
                @Override
                public void onResponse(Call<ArrayList<FranchiseByRoute>> call, Response<ArrayList<FranchiseByRoute>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<FranchiseByRoute> data = response.body();
                            commonDialog.dismiss();
                            franchiseArray = data;

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
                public void onFailure(Call<ArrayList<FranchiseByRoute>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("Franchise : ", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabTrayStatusActivity_Add) {
            new showAddFranchiseDialog(this).show();
        }
    }

    public class showAddFranchiseDialog extends Dialog {

        EditText edSmall, edBig, edLarge, edXL;
        TextView tvSelectFr, tvSave, tvExtraTray, tvSelectFrId;

        public showAddFranchiseDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_add_franchise_layout);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.dimAmount = 0.75f;
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.x = 100;
            wlp.y = 100;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            edSmall = findViewById(R.id.edAddFranchise_Small);
            edBig = findViewById(R.id.edAddFranchise_Big);
            edLarge = findViewById(R.id.edAddFranchise_Large);
            edXL = findViewById(R.id.edAddFranchise_XL);
            tvSelectFr = findViewById(R.id.tvAddFranchise_SelectFr);
            tvSelectFrId = findViewById(R.id.tvAddFranchise_SelectFrId);
            tvSave = findViewById(R.id.tvAddFranchise_Next);
            tvExtraTray = findViewById(R.id.tvAddFranchise_ExtraTray);

            tvExtraTray.setVisibility(View.GONE);

            tvSelectFr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFranchiseDialog();
                }
            });

            tvExtraTray.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    extraTrayDialog();
                }
            });

            tvSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (tvSelectFrId.getText().toString().isEmpty()) {
                        tvSelectFr.setError("Required");
                        tvSelectFr.requestFocus();
                    } else {
                        int frId = Integer.parseInt(tvSelectFrId.getText().toString());

                        int small = Integer.parseInt(edSmall.getText().toString());
                        int big = Integer.parseInt(edBig.getText().toString());
                        int large = Integer.parseInt(edLarge.getText().toString());
                        int xl = Integer.parseInt(edXL.getText().toString());

                        String frName = tvSelectFr.getText().toString();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar cal = Calendar.getInstance();
                        String todaysDate = sdf.format(cal.getTimeInMillis());

                        TrayMgmtDetailData trayMgmtDetailData = new TrayMgmtDetailData(0, headerId, frId, frName, big, small, large, xl, todaysDate, 0, 0, 0, 0, "0000-00-00", 0, 0, 0, 0, "0000-00-00", 0, 0, 0, 0, "0000-00-00", 0, 0, 0, 0, 0f, 0f, 0f, 0f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        dismiss();
                        saveTrayMgmtDetail(trayMgmtDetailData);
                    }
                }
            });
        }

        public void showFranchiseDialog() {
            dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.custom_dialog_list_layout, null, false);
            dialog.setContentView(v);
            dialog.setCancelable(true);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            ListView lvList = dialog.findViewById(R.id.lvDialog_List);
            EditText edSearch = dialog.findViewById(R.id.edDialog_Search);

            final DialogFranchiseListAdapter franchiseAdapter = new DialogFranchiseListAdapter(getContext(), franchiseArray);
            lvList.setAdapter(franchiseAdapter);

            edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        if (franchiseAdapter != null) {
                            franchiseAdapter.getFilter().filter(charSequence.toString());
                        }

                    } catch (Exception e) {
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            dialog.show();
        }

        public class DialogFranchiseListAdapter extends BaseAdapter implements Filterable {

            private ArrayList<FranchiseByRoute> originalValues;
            private ArrayList<FranchiseByRoute> displayedValues;
            LayoutInflater inflater;

            public DialogFranchiseListAdapter(Context context, ArrayList<FranchiseByRoute> routeArrayList) {
                this.originalValues = routeArrayList;
                this.displayedValues = routeArrayList;
                inflater = LayoutInflater.from(context);
            }

            @Override
            public int getCount() {
                return displayedValues.size();
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public Filter getFilter() {
                Filter filter = new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence charSequence) {
                        FilterResults results = new FilterResults();
                        ArrayList<FranchiseByRoute> filteredArrayList = new ArrayList<FranchiseByRoute>();

                        if (originalValues == null) {
                            originalValues = new ArrayList<FranchiseByRoute>(displayedValues);
                        }

                        if (charSequence == null || charSequence.length() == 0) {
                            results.count = originalValues.size();
                            results.values = originalValues;
                        } else {
                            charSequence = charSequence.toString().toLowerCase();
                            for (int i = 0; i < originalValues.size(); i++) {
                                String name = originalValues.get(i).getFrName();
                                if (name.toLowerCase().startsWith(charSequence.toString()) || name.toLowerCase().contains(charSequence.toString())) {
                                    filteredArrayList.add(new FranchiseByRoute(originalValues.get(i).getFrId(), originalValues.get(i).getFrName(), originalValues.get(i).getFrCode()));
                                }
                            }
                            results.count = filteredArrayList.size();
                            results.values = filteredArrayList;
                        }

                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                        displayedValues = (ArrayList<FranchiseByRoute>) filterResults.values;
                        notifyDataSetChanged();
                    }
                };

                return filter;
            }

            public class ViewHolder {
                TextView tvName;
                LinearLayout llItem;
            }

            @Override
            public View getView(final int position, View v, ViewGroup parent) {
                ViewHolder holder = null;

                if (v == null) {
                    v = inflater.inflate(R.layout.custom_dialog_route_list_item, null);
                    holder = new DialogFranchiseListAdapter.ViewHolder();
                    holder.tvName = v.findViewById(R.id.tvDialogRoute_Name);
                    holder.llItem = v.findViewById(R.id.llDialogRoute_Item);
                    v.setTag(holder);
                } else {
                    holder = (ViewHolder) v.getTag();
                }

                holder.tvName.setText("" + displayedValues.get(position).getFrName());

                holder.llItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        edSmall.setText("0");
                        edBig.setText("0");
                        edLarge.setText("0");
                        edXL.setText("0");
//
                        tvSelectFrId.setText("" + displayedValues.get(position).getFrId());
                        tvSelectFr.setText("" + displayedValues.get(position).getFrName());
                        dialog.dismiss();
                       // getFrTray(displayedValues.get(position).getFrId());

                    }
                });

                return v;
            }
        }

        public void getFrTray(int frId) {
            if (Constants.isOnline(getContext())) {
                final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
                commonDialog.show();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                final String date = sdf.format(cal.getTimeInMillis());

                Call<ArrayList<FrTrayCount>> frTrayCall = Constants.myInterface.getFrTrayCount(frId, "2018-02-18");
                frTrayCall.enqueue(new Callback<ArrayList<FrTrayCount>>() {
                    @Override
                    public void onResponse(Call<ArrayList<FrTrayCount>> call, Response<ArrayList<FrTrayCount>> response) {
                        try {
                            if (response.body() != null) {
                                ArrayList<FrTrayCount> data = response.body();
                                commonDialog.dismiss();

                                for (int i = 0; i < data.size(); i++) {

                                    try {
                                        if (data.get(i).getTrayType() == 1) {
                                            edSmall.setText("" + (int) Math.ceil(data.get(i).getNoOfTray()));
                                        }
                                    } catch (Exception e) {
                                        edSmall.setText("0");
                                    }

                                    try {
                                        if (data.get(i).getTrayType() == 2) {
                                            edBig.setText("" + (int) Math.ceil(data.get(i).getNoOfTray()));
                                        }
                                    } catch (Exception e) {
                                        edBig.setText("0");
                                    }

                                    try {
                                        if (data.get(i).getTrayType() == 3) {
                                            edLarge.setText("" + (int) Math.ceil(data.get(i).getNoOfTray()));
                                        }
                                    } catch (Exception e) {
                                        edLarge.setText("0");
                                    }

                                    try {
                                        if (data.get(i).getTrayType() == 4) {
                                            edXL.setText("" + (int) Math.ceil(data.get(i).getNoOfTray()));
                                        }
                                    } catch (Exception e) {
                                        edXL.setText("0");
                                    }


                                }


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
                    public void onFailure(Call<ArrayList<FrTrayCount>> call, Throwable t) {
                        commonDialog.dismiss();
                        Log.e("Franchise : ", " OnFailure-----" + t.getMessage());
                    }
                });

            } else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void updateVehicleOutTray(final int headerId, final int tray) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> infoCall = Constants.myInterface.updateVehicleOutTray(headerId, tray);
            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            Info data = response.body();
                            if (data.isError()) {
                                commonDialog.dismiss();
                                Log.e("updateVehicleOutTray: ", " ERROR-----" + data.getMessage());
                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(TrayStatusActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                Log.e("DATA : ", "---------------" + data);
                                tvExtra.setText("" + tray);
                                getTrayMgmtHeaders(headerId);
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(TrayStatusActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                            Log.e("updateVehicleOutTray : ", " NULL-----");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(TrayStatusActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                        Log.e("updateVehicleOutTray : ", " Exception-----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(TrayStatusActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                    Log.e("updateVehicleOutTray : ", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveTrayMgmtDetail(TrayMgmtDetailData trayMgmtDetailData) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
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
                                Toast.makeText(TrayStatusActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                getAllTrayMgmtDetailsByHeader(headerId);
                                getAllFranchise(headerBean.getRouteId(), headerId);
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
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getTrayMgmtHeaders(int headerId) {
        if (Constants.isOnline(this)) {

            Call<TrayMgmtHeaderDisplayList> headersByDateAndStatus = Constants.myInterface.getHeaderById(headerId);
            headersByDateAndStatus.enqueue(new Callback<TrayMgmtHeaderDisplayList>() {
                @Override
                public void onResponse(Call<TrayMgmtHeaderDisplayList> call, Response<TrayMgmtHeaderDisplayList> response) {
                    try {
                        if (response.body() != null) {
                            TrayMgmtHeaderDisplayList data = response.body();
                            Log.e("HEADER : ", "-------------------------" + data);
                            tvExtra.setText("" + data.getExtraTrayOut());

                        } else {
                            Log.e("getAllTrayMgmtHeaders", " NULL-----");
                        }
                    } catch (Exception e) {
                        Log.e("getAllTrayMgmtHeaders", " Exception-----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<TrayMgmtHeaderDisplayList> call, Throwable t) {
                    Log.e("getAllTrayMgmtHeaders", " OnFailure-----" + t.getMessage());
                }
            });

        }
    }


    public void getTrayInData(final int headerId) {
        Log.e("HEADER ID", "--------------------" + headerId);
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            final Call<ArrayList<TrayInData>> trayMgmtDetailDataCall = Constants.myInterface.getTrayInData(headerId);
            trayMgmtDetailDataCall.enqueue(new Callback<ArrayList<TrayInData>>() {
                @Override
                public void onResponse(Call<ArrayList<TrayInData>> call, Response<ArrayList<TrayInData>> response) {
                    try {
                        if (response.body() != null) {
                            commonDialog.dismiss();
                            ArrayList<TrayInData> data = response.body();
                            Log.e("DATA : ", "-----------------****-" + response.body());

                            inAdapter = new TrayInAdapter(data, TrayStatusActivity.this);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TrayStatusActivity.this);
                            rvList.setLayoutManager(mLayoutManager);
                            rvList.setItemAnimator(new DefaultItemAnimator());
                            rvList.setAdapter(inAdapter);

                            int small = 0, big = 0, lead = 0;

                            if (data.size() > 0) {


                                for (int i = 0; i < data.size(); i++) {

                                    small = small + data.get(i).getIntraySmall();
                                    big = big + data.get(i).getIntrayBig();
                                    lead = lead + data.get(i).getIntrayLead();

                                }

                            }


                            tvSmall.setText("" + small);
                            tvBig.setText("" + big);
                            tvLarge.setText("" + lead);
                            tvTotal.setText("" + (small + big + lead));
                            tvTotalLabel.setText("Total In Tray : ");
                            tvExtra.setVisibility(View.GONE);
                            tvExtralabel.setVisibility(View.GONE);


                        } else {
                            commonDialog.dismiss();
                            Log.e("getTrayInData :", " NULL----****-");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("getTrayInData :", " Exception---****--" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TrayInData>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("getTrayInData :", " OnFailure----****-" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }




}
