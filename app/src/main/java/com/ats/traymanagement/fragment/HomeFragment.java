package com.ats.traymanagement.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.activity.HomeActivity;
import com.ats.traymanagement.activity.VehicleStatusActivity;
import com.ats.traymanagement.adapter.VehicleStatusListAdapter;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.model.Driver;
import com.ats.traymanagement.model.RouteListData;
import com.ats.traymanagement.model.TrayMgmtHeaderDisplayList;
import com.ats.traymanagement.model.Vehicle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rvVehicleList;

    private ArrayList<TrayMgmtHeaderDisplayList> headerDataArrayList = new ArrayList<>();

    VehicleStatusListAdapter adapter;

    int yyyy, mm, dd;
    long fromDateMillis,toDateMillis;

    private ArrayList<String> routeNameArray = new ArrayList<>();
    private ArrayList<Integer> routeIdArray = new ArrayList<>();

    private ArrayList<String> vehicleNameArray = new ArrayList<>();
    private ArrayList<Integer> vehicleIdArray = new ArrayList<>();

    private ArrayList<String> driverNameArray = new ArrayList<>();
    private ArrayList<Integer> driverIdArray = new ArrayList<>();

    private ArrayList<Integer> vehicleStatusArray = new ArrayList<>();
    private ArrayList<String> vehicleStatusNameArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Home");

        HomeActivity.llHomeBG.setBackground(getResources().getDrawable(R.drawable.circle_layout_selected));
        HomeActivity.tvHome.setTextColor(Color.parseColor("#ffffff"));

        HomeActivity.llAddRouteBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
        HomeActivity.tvAddRoute.setTextColor(getResources().getColor(R.color.menu_text_color));

        HomeActivity.llVehicleOutBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
        HomeActivity.tvVehicleOut.setTextColor(getResources().getColor(R.color.menu_text_color));

        HomeActivity.llVehicleInBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
        HomeActivity.tvVehicleIn.setTextColor(getResources().getColor(R.color.menu_text_color));


        rvVehicleList = view.findViewById(R.id.rvHomeVehicleStatusList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String todaysDate = sdf.format(cal.getTimeInMillis());
        fromDateMillis = cal.getTimeInMillis();
        toDateMillis = cal.getTimeInMillis();


        getAllTrayMgmtHeaders(todaysDate);
        getAllDriver();
        getAllRoute();
        getAllVehicle();

        vehicleStatusArray.clear();
        vehicleStatusNameArray.clear();

        vehicleStatusArray.add(0);
        vehicleStatusNameArray.add("Select Status");

        vehicleStatusArray.add(1);
        vehicleStatusNameArray.add("Loading");

        vehicleStatusArray.add(2);
        vehicleStatusNameArray.add("OUT");

        vehicleStatusArray.add(3);
        vehicleStatusNameArray.add("IN");


        setHasOptionsMenu(true);

        return view;
    }


    public void getAllRoute() {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<RouteListData> routeListDataCall = Constants.myInterface.getAllRouteList();
            routeListDataCall.enqueue(new Callback<RouteListData>() {
                @Override
                public void onResponse(Call<RouteListData> call, Response<RouteListData> response) {
                    try {
                        if (response.body() != null) {
                            RouteListData data = response.body();
                            if (data.getInfo().isError()) {
                                commonDialog.dismiss();
                                Log.e("Add Route : ", " ERROR-----" + data.getInfo().getMessage());
                            } else {
                                commonDialog.dismiss();
                                routeIdArray.clear();
                                routeNameArray.clear();
                                routeIdArray.add(0);
                                routeNameArray.add("Select Route");
                                for (int i = 0; i < data.getRoute().size(); i++) {
                                    routeIdArray.add(data.getRoute().get(i).getRouteId());
                                    routeNameArray.add(data.getRoute().get(i).getRouteName());
                                }

                            }
                        } else {
                            commonDialog.dismiss();
                            Log.e("Add Route : ", " NULL-----");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Add Route : ", " Exception-----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<RouteListData> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("Add Route : ", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            // Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllVehicle() {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Vehicle>> allVehicleList = Constants.myInterface.getAllVehicleList();
            allVehicleList.enqueue(new Callback<ArrayList<Vehicle>>() {
                @Override
                public void onResponse(Call<ArrayList<Vehicle>> call, Response<ArrayList<Vehicle>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<Vehicle> data = response.body();
                            commonDialog.dismiss();
                            vehicleIdArray.clear();
                            vehicleNameArray.clear();
                            vehicleIdArray.add(0);
                            vehicleNameArray.add("Select Vehicle");

                            if (data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    vehicleIdArray.add(data.get(i).getVehId());
                                    vehicleNameArray.add(data.get(i).getVehNo());
                                }
                            }
                        } else {
                            commonDialog.dismiss();
                            Log.e("Add Route : ", " NULL-----");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Add Route : ", " Exception-----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Vehicle>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("Add Route : ", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            // Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllDriver() {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Driver>> allDriverList = Constants.myInterface.getAllDriverList();
            allDriverList.enqueue(new Callback<ArrayList<Driver>>() {
                @Override
                public void onResponse(Call<ArrayList<Driver>> call, Response<ArrayList<Driver>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<Driver> data = response.body();
                            commonDialog.dismiss();
                            driverIdArray.clear();
                            driverNameArray.clear();

                            driverIdArray.add(0);
                            driverNameArray.add("Select Driver");

                            if (data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    driverIdArray.add(data.get(i).getDriverId());
                                    driverNameArray.add(data.get(i).getDriverName());
                                }
                            }

                        } else {
                            commonDialog.dismiss();
                            Log.e("Add Route : ", " NULL-----");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Add Route : ", " Exception-----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Driver>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("Add Route : ", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            //Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void getAllTrayMgmtHeaders(String date) {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TrayMgmtHeaderDisplayList>> headersByDateAndStatus = Constants.myInterface.getAllVehicleList(date,date);
            headersByDateAndStatus.enqueue(new Callback<ArrayList<TrayMgmtHeaderDisplayList>>() {
                @Override
                public void onResponse(Call<ArrayList<TrayMgmtHeaderDisplayList>> call, Response<ArrayList<TrayMgmtHeaderDisplayList>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<TrayMgmtHeaderDisplayList> data = response.body();
                            commonDialog.dismiss();

                            headerDataArrayList.clear();

                            headerDataArrayList = data;
                            adapter = new VehicleStatusListAdapter(headerDataArrayList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
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
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllTrayMgmtHeadersByVehType(String date,String to, final int vehType) {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TrayMgmtHeaderDisplayList>> headersByDateAndStatus = Constants.myInterface.getAllVehicleList(date,to);
            headersByDateAndStatus.enqueue(new Callback<ArrayList<TrayMgmtHeaderDisplayList>>() {
                @Override
                public void onResponse(Call<ArrayList<TrayMgmtHeaderDisplayList>> call, Response<ArrayList<TrayMgmtHeaderDisplayList>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<TrayMgmtHeaderDisplayList> data = response.body();
                            commonDialog.dismiss();
                            headerDataArrayList.clear();

                            if (vehType == 2) {
                                headerDataArrayList = data;

                                adapter = new VehicleStatusListAdapter(headerDataArrayList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                rvVehicleList.setLayoutManager(mLayoutManager);
                                rvVehicleList.setItemAnimator(new DefaultItemAnimator());
                                rvVehicleList.setAdapter(adapter);
                            } else if (vehType == 0) {
                                if (data.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        if (data.get(i).getIsSameDay() == 0) {
                                            headerDataArrayList.add(data.get(i));
                                        }
                                    }
                                    adapter = new VehicleStatusListAdapter(headerDataArrayList, getContext());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                    rvVehicleList.setLayoutManager(mLayoutManager);
                                    rvVehicleList.setItemAnimator(new DefaultItemAnimator());
                                    rvVehicleList.setAdapter(adapter);
                                }
                            } else if (vehType == 1) {
                                if (data.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        if (data.get(i).getIsSameDay() == 1) {
                                            headerDataArrayList.add(data.get(i));
                                        }
                                    }
                                    adapter = new VehicleStatusListAdapter(headerDataArrayList, getContext());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                    rvVehicleList.setLayoutManager(mLayoutManager);
                                    rvVehicleList.setItemAnimator(new DefaultItemAnimator());
                                    rvVehicleList.setAdapter(adapter);
                                }
                            }


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
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataByRoute(String date,String to, final int routeId, final int vehType) {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TrayMgmtHeaderDisplayList>> headersByDateAndStatus = Constants.myInterface.getAllVehicleList(date,to);
            headersByDateAndStatus.enqueue(new Callback<ArrayList<TrayMgmtHeaderDisplayList>>() {
                @Override
                public void onResponse(Call<ArrayList<TrayMgmtHeaderDisplayList>> call, Response<ArrayList<TrayMgmtHeaderDisplayList>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<TrayMgmtHeaderDisplayList> data = response.body();
                            commonDialog.dismiss();
                            headerDataArrayList.clear();
                            headerDataArrayList = data;

                            ArrayList<TrayMgmtHeaderDisplayList> tempArray = new ArrayList<>();

                            if (vehType == 2) {
                                for (int i = 0; i < headerDataArrayList.size(); i++) {
                                    if (headerDataArrayList.get(i).getRouteId() == routeId) {
                                        tempArray.add(headerDataArrayList.get(i));
                                    }
                                }
                            } else if (vehType == 0) {
                                for (int i = 0; i < headerDataArrayList.size(); i++) {
                                    if (headerDataArrayList.get(i).getRouteId() == routeId && headerDataArrayList.get(i).getIsSameDay() == 0) {
                                        tempArray.add(headerDataArrayList.get(i));
                                    }
                                }

                            } else if (vehType == 1) {
                                for (int i = 0; i < headerDataArrayList.size(); i++) {
                                    if (headerDataArrayList.get(i).getRouteId() == routeId && headerDataArrayList.get(i).getIsSameDay() == 1) {
                                        tempArray.add(headerDataArrayList.get(i));
                                    }
                                }
                            }

                            adapter = new VehicleStatusListAdapter(tempArray, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
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
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataByVehicle(String date,String to, final int vehicleId, final int vehType) {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TrayMgmtHeaderDisplayList>> headersByDateAndStatus = Constants.myInterface.getAllVehicleList(date,to);
            headersByDateAndStatus.enqueue(new Callback<ArrayList<TrayMgmtHeaderDisplayList>>() {
                @Override
                public void onResponse(Call<ArrayList<TrayMgmtHeaderDisplayList>> call, Response<ArrayList<TrayMgmtHeaderDisplayList>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<TrayMgmtHeaderDisplayList> data = response.body();
                            commonDialog.dismiss();
                            headerDataArrayList.clear();
                            headerDataArrayList = data;

                            ArrayList<TrayMgmtHeaderDisplayList> tempArray = new ArrayList<>();

                            if (vehType == 2) {
                                for (int i = 0; i < headerDataArrayList.size(); i++) {
                                    if (headerDataArrayList.get(i).getVehId() == vehicleId) {
                                        tempArray.add(headerDataArrayList.get(i));
                                    }
                                }
                            } else if (vehType == 0) {
                                for (int i = 0; i < headerDataArrayList.size(); i++) {
                                    if (headerDataArrayList.get(i).getVehId() == vehicleId && headerDataArrayList.get(i).getIsSameDay() == 0) {
                                        tempArray.add(headerDataArrayList.get(i));
                                    }
                                }
                            } else if (vehType == 1) {
                                for (int i = 0; i < headerDataArrayList.size(); i++) {
                                    if (headerDataArrayList.get(i).getVehId() == vehicleId && headerDataArrayList.get(i).getIsSameDay() == 1) {
                                        tempArray.add(headerDataArrayList.get(i));
                                    }
                                }
                            }
                            adapter = new VehicleStatusListAdapter(tempArray, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
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
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataByDriver(String date,String to, final int driverId, final int vehType) {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TrayMgmtHeaderDisplayList>> headersByDateAndStatus = Constants.myInterface.getAllVehicleList(date,to);
            headersByDateAndStatus.enqueue(new Callback<ArrayList<TrayMgmtHeaderDisplayList>>() {
                @Override
                public void onResponse(Call<ArrayList<TrayMgmtHeaderDisplayList>> call, Response<ArrayList<TrayMgmtHeaderDisplayList>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<TrayMgmtHeaderDisplayList> data = response.body();
                            commonDialog.dismiss();
                            headerDataArrayList.clear();
                            headerDataArrayList = data;

                            ArrayList<TrayMgmtHeaderDisplayList> tempArray = new ArrayList<>();

                            if (vehType == 2) {
                                for (int i = 0; i < headerDataArrayList.size(); i++) {
                                    if (headerDataArrayList.get(i).getDrvId() == driverId) {
                                        tempArray.add(headerDataArrayList.get(i));
                                    }
                                }
                            } else if (vehType == 0) {
                                for (int i = 0; i < headerDataArrayList.size(); i++) {
                                    if (headerDataArrayList.get(i).getDrvId() == driverId && headerDataArrayList.get(i).getIsSameDay() == 0) {
                                        tempArray.add(headerDataArrayList.get(i));
                                    }
                                }
                            } else if (vehType == 1) {
                                for (int i = 0; i < headerDataArrayList.size(); i++) {
                                    if (headerDataArrayList.get(i).getDrvId() == driverId && headerDataArrayList.get(i).getIsSameDay() == 1) {
                                        tempArray.add(headerDataArrayList.get(i));
                                    }
                                }
                            }

                            adapter = new VehicleStatusListAdapter(tempArray, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
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
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataByVehicleStatus(String date,String to, final int statusId, final int vehType) {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TrayMgmtHeaderDisplayList>> headersByDateAndStatus = Constants.myInterface.getAllVehicleList(date,to);
            headersByDateAndStatus.enqueue(new Callback<ArrayList<TrayMgmtHeaderDisplayList>>() {
                @Override
                public void onResponse(Call<ArrayList<TrayMgmtHeaderDisplayList>> call, Response<ArrayList<TrayMgmtHeaderDisplayList>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<TrayMgmtHeaderDisplayList> data = response.body();
                            commonDialog.dismiss();
                            headerDataArrayList.clear();
                            headerDataArrayList = data;

                            ArrayList<TrayMgmtHeaderDisplayList> tempArray = new ArrayList<>();

                            if (vehType == 2) {
                                for (int i = 0; i < headerDataArrayList.size(); i++) {
                                    if (headerDataArrayList.get(i).getVehStatus() == statusId) {
                                        tempArray.add(headerDataArrayList.get(i));
                                    }
                                }
                            } else if (vehType == 0) {
                                for (int i = 0; i < headerDataArrayList.size(); i++) {
                                    if (headerDataArrayList.get(i).getVehStatus() == statusId && headerDataArrayList.get(i).getIsSameDay() == 0) {
                                        tempArray.add(headerDataArrayList.get(i));
                                    }
                                }
                            } else if (vehType == 1) {
                                for (int i = 0; i < headerDataArrayList.size(); i++) {
                                    if (headerDataArrayList.get(i).getVehStatus() == statusId && headerDataArrayList.get(i).getIsSameDay() == 1) {
                                        tempArray.add(headerDataArrayList.get(i));
                                    }
                                }
                            }

                            adapter = new VehicleStatusListAdapter(tempArray, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
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
            // Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_filter);
        item.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                new FilterDialog(getActivity(), routeIdArray, routeNameArray, vehicleIdArray, vehicleNameArray, driverIdArray, driverNameArray, vehicleStatusArray, vehicleStatusNameArray).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //--------------------FILTER DIALOG-------------------------------------------

    public class FilterDialog extends Dialog {

        ArrayList<Integer> routeId;
        ArrayList<Integer> vehicleId;
        ArrayList<String> routeName;
        ArrayList<String> vehicleNo;
        ArrayList<Integer> driverId;
        ArrayList<String> driverName;
        ArrayList<Integer> statusId;
        ArrayList<String> statusName;

        public FilterDialog(@NonNull Context context) {
            super(context);
        }

        public FilterDialog(@NonNull Context context, ArrayList<Integer> routeIdList, ArrayList<String> routeNameList, ArrayList<Integer> vehicleIdList, ArrayList<String> vehicleNoList, ArrayList<Integer> driverIdList, ArrayList<String> driverNameList, ArrayList<Integer> statusId, ArrayList<String> statusName) {
            super(context);
            this.routeId = routeIdList;
            this.routeName = routeNameList;
            this.vehicleId = vehicleIdList;
            this.vehicleNo = vehicleNoList;
            this.driverId = driverIdList;
            this.driverName = driverNameList;
            this.statusId = statusId;
            this.statusName = statusName;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setTitle("Filter");
            setContentView(R.layout.custom_filter_dialog_layout);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lp);

            final RadioButton rbAll = findViewById(R.id.rbFilterDialog_All);
            final RadioButton rbRegular = findViewById(R.id.rbFilterDialog_Regular);
            final RadioButton rbIsSameDay = findViewById(R.id.rbFilterDialog_IsSameDay);

            final RadioButton rbDate = findViewById(R.id.rbFilterDialog_Date);
            final RadioButton rbRoute = findViewById(R.id.rbFilterDialog_Route);
            final RadioButton rbVehicle = findViewById(R.id.rbFilterDialog_Vehicle);
            final RadioButton rbDriver = findViewById(R.id.rbFilterDialog_Driver);
            final RadioButton rbVehicleStatus = findViewById(R.id.rbFilterDialog_VehicleStatus);

            final LinearLayout llDateLayout = findViewById(R.id.llFilterDialog_Date);
            final LinearLayout llVehicleLayout = findViewById(R.id.llFilterDialog_Vehicle);
            final LinearLayout llDriverLayout = findViewById(R.id.llFilterDialog_Driver);
            final LinearLayout llVehicleStatusLayout = findViewById(R.id.llFilterDialog_VehicleStatus);
            final LinearLayout llRouteLayout = findViewById(R.id.llFilterDialog_Route);

            final Spinner spRoute = findViewById(R.id.spFilterDialog_Route);
            final Spinner spVehicle = findViewById(R.id.spFilterDialog_Vehicle);
            final Spinner spDriver = findViewById(R.id.spFilterDialog_Driver);
            final Spinner spVehicleStatus = findViewById(R.id.spFilterDialog_VehicleStatus);

            final EditText edFromDate = findViewById(R.id.edFilterDialog_FromDate);
            final EditText edToDate = findViewById(R.id.edFilterDialog_ToDate);

            TextView tvSearch = findViewById(R.id.tvFilterDialog_Search);

            final TextView tvFromDate = findViewById(R.id.tvFilterDialog_FromDate);
            final TextView tvToDate = findViewById(R.id.tvFilterDialog_ToDate);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

            edFromDate.setText("" + sdf.format(fromDateMillis));
            tvFromDate.setText("" + sdf1.format(fromDateMillis));

            edToDate.setText("" + sdf.format(toDateMillis));
            tvToDate.setText("" + sdf1.format(toDateMillis));

            rbAll.setChecked(true);
            rbDate.setChecked(true);
            llDateLayout.setVisibility(View.VISIBLE);

            rbDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        llDateLayout.setVisibility(View.VISIBLE);
                        llRouteLayout.setVisibility(View.GONE);
                        llVehicleLayout.setVisibility(View.GONE);
                        llDriverLayout.setVisibility(View.GONE);
                        llVehicleStatusLayout.setVisibility(View.GONE);
                    }
                }
            });

            rbRoute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        llDateLayout.setVisibility(View.VISIBLE);
                        llRouteLayout.setVisibility(View.VISIBLE);
                        llVehicleLayout.setVisibility(View.GONE);
                        llDriverLayout.setVisibility(View.GONE);
                        llVehicleStatusLayout.setVisibility(View.GONE);
                    }
                }
            });

            rbDriver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        llDateLayout.setVisibility(View.VISIBLE);
                        llRouteLayout.setVisibility(View.GONE);
                        llVehicleLayout.setVisibility(View.GONE);
                        llDriverLayout.setVisibility(View.VISIBLE);
                        llVehicleStatusLayout.setVisibility(View.GONE);
                    }
                }
            });

            rbVehicle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        llDateLayout.setVisibility(View.VISIBLE);
                        llRouteLayout.setVisibility(View.GONE);
                        llVehicleLayout.setVisibility(View.VISIBLE);
                        llDriverLayout.setVisibility(View.GONE);
                        llVehicleStatusLayout.setVisibility(View.GONE);
                    }
                }
            });

            rbVehicleStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        llDateLayout.setVisibility(View.VISIBLE);
                        llRouteLayout.setVisibility(View.GONE);
                        llVehicleLayout.setVisibility(View.GONE);
                        llDriverLayout.setVisibility(View.GONE);
                        llVehicleStatusLayout.setVisibility(View.VISIBLE);
                    }
                }
            });


            ArrayAdapter<String> routeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, routeName);
            spRoute.setAdapter(routeAdapter);
            ArrayAdapter<String> vehicleAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, vehicleNo);
            spVehicle.setAdapter(vehicleAdapter);
            ArrayAdapter<String> driverAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, driverName);
            spDriver.setAdapter(driverAdapter);
            ArrayAdapter<String> vehicleStatusAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, statusName);
            spVehicleStatus.setAdapter(vehicleStatusAdapter);


            edFromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (fromDateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(fromDateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    }

                    DatePickerDialog.OnDateSetListener fromDtListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            yyyy = year;
                            mm = month + 1;
                            dd = dayOfMonth;
                            edFromDate.setText(dd + "-" + mm + "-" + yyyy);
                            tvFromDate.setText(yyyy + "-" + mm + "-" + dd);

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(yyyy, mm - 1, dd);
                            calendar.set(Calendar.MILLISECOND, 0);
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.HOUR, 0);
                            fromDateMillis = calendar.getTimeInMillis();
                        }
                    };


                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), fromDtListener, yr, mn, dy);
                    dialog.show();
                }
            });


            edToDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (toDateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(toDateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    }

                    DatePickerDialog.OnDateSetListener toDtListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            yyyy = year;
                            mm = month + 1;
                            dd = dayOfMonth;
                            edToDate.setText(dd + "-" + mm + "-" + yyyy);
                            tvToDate.setText(yyyy + "-" + mm + "-" + dd);

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(yyyy, mm - 1, dd);
                            calendar.set(Calendar.MILLISECOND, 0);
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.HOUR, 0);
                            toDateMillis = calendar.getTimeInMillis();
                        }
                    };


                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), toDtListener, yr, mn, dy);
                    dialog.show();
                }
            });


            tvSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int vehType = 0;
                    if (rbAll.isChecked()) {
                        vehType = 2;
                    } else if (rbRegular.isChecked()) {
                        vehType = 0;
                    } else if (rbIsSameDay.isChecked()) {
                        vehType = 1;
                    }

                    if (rbDate.isChecked()) {
                        if (edFromDate.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please Select From Date", Toast.LENGTH_SHORT).show();
                            edFromDate.requestFocus();
                        } else if (edToDate.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please Select To Date", Toast.LENGTH_SHORT).show();
                            edToDate.requestFocus();
                        }else {
                            String from = tvFromDate.getText().toString();
                            String to = tvToDate.getText().toString();
                            dismiss();
                            getAllTrayMgmtHeadersByVehType(from,to, vehType);
                        }
                    } else if (rbRoute.isChecked()) {
                        if (edFromDate.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please Select From Date", Toast.LENGTH_SHORT).show();
                            edFromDate.requestFocus();
                        } else if (edToDate.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please Select To Date", Toast.LENGTH_SHORT).show();
                            edToDate.requestFocus();
                        } else if (spRoute.getSelectedItemPosition() == 0) {
                            Toast.makeText(getActivity(), "Please Select Route", Toast.LENGTH_SHORT).show();
                            spRoute.requestFocus();
                        } else {
                            String from = tvFromDate.getText().toString();
                            String to = tvToDate.getText().toString();
                            int route = routeId.get(spRoute.getSelectedItemPosition());
                            dismiss();
                            getDataByRoute(from,to, route, vehType);

                        }
                    } else if (rbVehicle.isChecked()) {
                        if (edFromDate.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please Select From Date", Toast.LENGTH_SHORT).show();
                            edFromDate.requestFocus();
                        } else if (edToDate.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please Select To Date", Toast.LENGTH_SHORT).show();
                            edToDate.requestFocus();
                        } else if (spVehicle.getSelectedItemPosition() == 0) {
                            Toast.makeText(getActivity(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                            spVehicle.requestFocus();
                        } else {
                            String from = tvFromDate.getText().toString();
                            String to = tvToDate.getText().toString();
                            int vehicle = vehicleId.get(spVehicle.getSelectedItemPosition());
                            dismiss();
                            getDataByVehicle(from,to, vehicle, vehType);
                        }
                    } else if (rbDriver.isChecked()) {
                        if (edFromDate.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please Select From Date", Toast.LENGTH_SHORT).show();
                            edFromDate.requestFocus();
                        } else if (edToDate.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please Select To Date", Toast.LENGTH_SHORT).show();
                            edToDate.requestFocus();
                        } else if (spDriver.getSelectedItemPosition() == 0) {
                            Toast.makeText(getActivity(), "Please Select Driver", Toast.LENGTH_SHORT).show();
                            spDriver.requestFocus();
                        } else {
                            String from = tvFromDate.getText().toString();
                            String to = tvToDate.getText().toString();
                            int driver = driverId.get(spDriver.getSelectedItemPosition());
                            dismiss();
                            getDataByDriver(from,to, driver, vehType);
                        }
                    } else if (rbVehicleStatus.isChecked()) {
                        if (edFromDate.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please Select From Date", Toast.LENGTH_SHORT).show();
                            edFromDate.requestFocus();
                        } else if (edToDate.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please Select To Date", Toast.LENGTH_SHORT).show();
                            edToDate.requestFocus();
                        } else if (spVehicleStatus.getSelectedItemPosition() == 0) {
                            Toast.makeText(getActivity(), "Please Select Vehicle Status", Toast.LENGTH_SHORT).show();
                            spVehicleStatus.requestFocus();
                        } else {
                            String from = tvFromDate.getText().toString();
                            String to = tvToDate.getText().toString();
                            int vStatus = statusId.get(spVehicleStatus.getSelectedItemPosition());
                            dismiss();
                            getDataByVehicleStatus(from,to, (vStatus - 1), vehType);
                        }
                    }
                }
            });


        }
    }
}
