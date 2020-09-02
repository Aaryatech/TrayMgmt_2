package com.ats.traymanagement.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.model.Driver;
import com.ats.traymanagement.model.Info;
import com.ats.traymanagement.model.Route;
import com.ats.traymanagement.model.RouteListData;
import com.ats.traymanagement.model.TrayMgmtHeaderData;
import com.ats.traymanagement.model.Vehicle;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRouteFragment extends Fragment implements View.OnClickListener {


    private ArrayList<Route> routeArray = new ArrayList<>();
    private ArrayList<Vehicle> vehicleArray = new ArrayList<>();
    private ArrayList<Driver> driverArray = new ArrayList<>();

    private TextView tvRouteId, tvVehicleId, tvDriverId, tvSubmit, tvDate;
    private EditText edRoute, edVehicle, edDriver, edDate, edExtraTray;
    private CheckBox cbIsSameDay;
    private LinearLayout llExtraTray;
    Dialog dialog;

    long dateMillis;
    int yyyy, mm, dd;

    String todaysDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_route, container, false);

        edRoute = view.findViewById(R.id.edAddRoute_Route);
        edVehicle = view.findViewById(R.id.edAddRoute_Vehicle);
        edDriver = view.findViewById(R.id.edAddRoute_Driver);
        tvRouteId = view.findViewById(R.id.tvAddRoute_RouteId);
        tvVehicleId = view.findViewById(R.id.tvAddRoute_VehicleId);
        tvDriverId = view.findViewById(R.id.tvAddRoute_DriverId);
        tvSubmit = view.findViewById(R.id.tvAddRoute_Submit);
        edDate = view.findViewById(R.id.edAddRoute_Date);
        tvDate = view.findViewById(R.id.tvAddRoute_Date);

        edExtraTray = view.findViewById(R.id.edAddRoute_ExtraTray);
        llExtraTray = view.findViewById(R.id.llAddRoute_ExtraTray);
        cbIsSameDay = view.findViewById(R.id.cbAddRoute_IsSameDay);

        llExtraTray.setVisibility(View.GONE);

        edDate.setOnClickListener(this);
        edRoute.setOnClickListener(this);
        edVehicle.setOnClickListener(this);
        edDriver.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);

        cbIsSameDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    llExtraTray.setVisibility(View.VISIBLE);
                } else {
                    llExtraTray.setVisibility(View.GONE);
                }
            }
        });

        // getServerDate();
        getAllRoute();
        getAllVehicle();
        getAllDriver();

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
                                for (int i = 0; i < data.getRoute().size(); i++) {
                                    routeArray.add(data.getRoute().get(i));
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
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
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
                            vehicleArray = data;
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
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
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
                            driverArray = data;
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
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edAddRoute_Date) {
            showDateDialog();
        } else if (view.getId() == R.id.edAddRoute_Route) {
            showDialog("Route");
        } else if (view.getId() == R.id.edAddRoute_Vehicle) {
            showDialog("Vehicle");
        } else if (view.getId() == R.id.edAddRoute_Driver) {
            showDialog("Driver");
        } else if (view.getId() == R.id.tvAddRoute_Submit) {

            if (tvRouteId.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Route", Toast.LENGTH_SHORT).show();
                edRoute.requestFocus();
            } else if (tvVehicleId.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                edVehicle.requestFocus();
            } else if (tvDriverId.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Driver", Toast.LENGTH_SHORT).show();
                edDriver.requestFocus();
            } else {

                int routeId = Integer.parseInt(tvRouteId.getText().toString());
                int vehicleId = Integer.parseInt(tvVehicleId.getText().toString());
                int driverId = Integer.parseInt(tvDriverId.getText().toString());
                String vehicleNo = edVehicle.getText().toString();
                String route = edRoute.getText().toString();
                // String date = tvDate.getText().toString();
                String extraTray = "";

                String date = "0000-00-00";

                if (cbIsSameDay.isChecked()) {
                    if (edExtraTray.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Insert Extra Tray", Toast.LENGTH_SHORT).show();
                        edExtraTray.requestFocus();
                    } else {
                        extraTray = edExtraTray.getText().toString();
                        TrayMgmtHeaderData headerData = new TrayMgmtHeaderData(0, date, vehicleId, driverId, routeId, vehicleNo, "", "", 0f, 0f, 0f, 0f, 0, 0, extraTray, "", 0, 1);
                        saveTrayMgmtHeader(headerData, route, 1);
                    }
                } else {
                    TrayMgmtHeaderData headerData = new TrayMgmtHeaderData(0, date, vehicleId, driverId, routeId, vehicleNo, "", "", 0f, 0f, 0f, 0f, 0, 0, extraTray, "", 0, 0);
                    saveTrayMgmtHeader(headerData, route, 0);
                    Log.e("DATE : ", "------------------------" + date);
                }
            }
        }
    }

    public void showDateDialog() {
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
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), dateListener, yr, mn, dy);
        dialog.show();
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

    public void saveTrayMgmtHeader(TrayMgmtHeaderData trayMgmtHeaderData, final String routeName, final int tag) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<TrayMgmtHeaderData> headerDataCall = Constants.myInterface.saveTrayMgmtHeader(trayMgmtHeaderData);
            headerDataCall.enqueue(new Callback<TrayMgmtHeaderData>() {
                @Override
                public void onResponse(Call<TrayMgmtHeaderData> call, Response<TrayMgmtHeaderData> response) {
                    try {
                        if (response.body() != null) {
                            TrayMgmtHeaderData data = response.body();
                            if (data.isError()) {
                                commonDialog.dismiss();
                                Log.e("Add Route : ", " ERROR-----" + data.getMessage());
                                if (data.getMessage().equalsIgnoreCase("TrayMgtHeader Not Saved .")) {
                                    Toast.makeText(getActivity(), "Vehicle Already Exist", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                Log.e("DATA : ", "---------------" + data);

                                TrayMgmtHeaderData headerData = new TrayMgmtHeaderData(data.getTranId(), data.getTranDate(), data.getVehId(), data.getDrvId(), data.getRouteId(), data.getVehNo(), data.getVehOuttime(), data.getVehIntime(), data.getVehOutkm(), data.getVehInkm(), data.getVehRunningKm(), data.getDiesel(), data.getVehStatus(), data.getDelStatus(), data.getExtraTrayOut(), data.getExtraTrayIn(), data.getVehIsRegular(), data.getIsSameDay());
                                Gson gson = new Gson();
                                String jsonHeaderData = gson.toJson(headerData);

                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_container, new VehicleListFragment(), "Home");
                                transaction.commit();

                               /* if (tag == 1) {
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frame_container, new VehicleListFragment(), "Home");
                                    transaction.commit();
                                } else {
                                    Fragment adf = new FranchiseFragment();
                                    Bundle args = new Bundle();
                                    args.putInt("routeId", data.getRouteId());
                                    args.putString("routeName", routeName);
                                    args.putInt("headerId", data.getTranId());
                                    args.putString("headerBean", jsonHeaderData);
                                    adf.setArguments(args);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, adf, "AddRouteFragment").commit();
                                }*/


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
                public void onFailure(Call<TrayMgmtHeaderData> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("Add Route : ", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }

    public void showDialog(final String type) {
        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dialog_list_layout, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ListView lvList = dialog.findViewById(R.id.lvDialog_List);
        EditText edSearch = dialog.findViewById(R.id.edDialog_Search);

        if (type.equalsIgnoreCase("Route")) {
            final DialogRouteListAdapter routeAdapter = new DialogRouteListAdapter(getActivity(), routeArray);
            lvList.setAdapter(routeAdapter);

            edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        if (routeAdapter != null) {
                            routeAdapter.getFilter().filter(charSequence.toString());
                        }

                    } catch (Exception e) {
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


        } else if (type.equalsIgnoreCase("Vehicle")) {
            final DialogVehicleListAdapter vehilceAdapter = new DialogVehicleListAdapter(getActivity(), vehicleArray);
            lvList.setAdapter(vehilceAdapter);

            edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        if (vehilceAdapter != null) {
                            vehilceAdapter.getFilter().filter(charSequence.toString());
                        }

                    } catch (Exception e) {
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        } else if (type.equalsIgnoreCase("Driver")) {
            final DialogDriverListAdapter driverAdapter = new DialogDriverListAdapter(getActivity(), driverArray);
            lvList.setAdapter(driverAdapter);

            edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        if (driverAdapter != null) {
                            driverAdapter.getFilter().filter(charSequence.toString());
                        }

                    } catch (Exception e) {
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }


        dialog.show();
    }

    public class DialogRouteListAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Route> originalValues;
        private ArrayList<Route> displayedValues;
        LayoutInflater inflater;

        public DialogRouteListAdapter(Context context, ArrayList<Route> routeArrayList) {
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
                    ArrayList<Route> filteredArrayList = new ArrayList<Route>();

                    if (originalValues == null) {
                        originalValues = new ArrayList<Route>(displayedValues);
                    }

                    if (charSequence == null || charSequence.length() == 0) {
                        results.count = originalValues.size();
                        results.values = originalValues;
                    } else {
                        charSequence = charSequence.toString().toLowerCase();
                        for (int i = 0; i < originalValues.size(); i++) {
                            String name = originalValues.get(i).getRouteName();
                            if (name.toLowerCase().startsWith(charSequence.toString()) || name.toLowerCase().contains(charSequence.toString())) {
                                filteredArrayList.add(new Route(originalValues.get(i).getRouteId(), originalValues.get(i).getRouteName(), originalValues.get(i).getDelStatus()));
                            }
                        }
                        results.count = filteredArrayList.size();
                        results.values = filteredArrayList;
                    }

                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    displayedValues = (ArrayList<Route>) filterResults.values;
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
                holder = new ViewHolder();
                holder.tvName = v.findViewById(R.id.tvDialogRoute_Name);
                holder.llItem = v.findViewById(R.id.llDialogRoute_Item);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.tvName.setText("" + displayedValues.get(position).getRouteName());

            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edRoute.setText("" + displayedValues.get(position).getRouteName());
                    tvRouteId.setText("" + displayedValues.get(position).getRouteId());
                    dialog.dismiss();
                }
            });

            return v;
        }
    }

    public class DialogVehicleListAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Vehicle> originalValues;
        private ArrayList<Vehicle> displayedValues;
        LayoutInflater inflater;

        public DialogVehicleListAdapter(Context context, ArrayList<Vehicle> routeArrayList) {
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
                    ArrayList<Vehicle> filteredArrayList = new ArrayList<Vehicle>();

                    if (originalValues == null) {
                        originalValues = new ArrayList<Vehicle>(displayedValues);
                    }

                    if (charSequence == null || charSequence.length() == 0) {
                        results.count = originalValues.size();
                        results.values = originalValues;
                    } else {
                        charSequence = charSequence.toString().toLowerCase();
                        for (int i = 0; i < originalValues.size(); i++) {
                            String name = originalValues.get(i).getVehNo();
                            if (name.toLowerCase().startsWith(charSequence.toString()) || name.toLowerCase().contains(charSequence.toString())) {
                                filteredArrayList.add(new Vehicle(originalValues.get(i).getVehId(), originalValues.get(i).getVehNo(), originalValues.get(i).getMakeId(), originalValues.get(i).getVehEngNo(), originalValues.get(i).getVehChesiNo(), originalValues.get(i).getVehColor(), originalValues.get(i).getPurchaseDate(), originalValues.get(i).getRegDate(), originalValues.get(i).getDealerId(), originalValues.get(i).getFuelType(), originalValues.get(i).getVehTypeId(), originalValues.get(i).getVariantId(), originalValues.get(i).getVehCompAvg(), originalValues.get(i).getVehStandAvg(), originalValues.get(i).getVehMiniAvg(), originalValues.get(i).getDelStatus(), originalValues.get(i).getFreqKIm(), originalValues.get(i).getWheelChangeFreq(), originalValues.get(i).getBattaryChangeFreq(), originalValues.get(i).getAcChangeFreq()));
                            }
                        }
                        results.count = filteredArrayList.size();
                        results.values = filteredArrayList;
                    }

                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    displayedValues = (ArrayList<Vehicle>) filterResults.values;
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
                holder = new ViewHolder();
                holder.tvName = v.findViewById(R.id.tvDialogRoute_Name);
                holder.llItem = v.findViewById(R.id.llDialogRoute_Item);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.tvName.setText("" + displayedValues.get(position).getVehNo());

            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edVehicle.setText("" + displayedValues.get(position).getVehNo());
                    tvVehicleId.setText("" + displayedValues.get(position).getVehId());
                    dialog.dismiss();
                }
            });

            return v;
        }
    }

    public class DialogDriverListAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Driver> originalValues;
        private ArrayList<Driver> displayedValues;
        LayoutInflater inflater;

        public DialogDriverListAdapter(Context context, ArrayList<Driver> routeArrayList) {
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
                    ArrayList<Driver> filteredArrayList = new ArrayList<Driver>();

                    if (originalValues == null) {
                        originalValues = new ArrayList<Driver>(displayedValues);
                    }

                    if (charSequence == null || charSequence.length() == 0) {
                        results.count = originalValues.size();
                        results.values = originalValues;
                    } else {
                        charSequence = charSequence.toString().toLowerCase();
                        for (int i = 0; i < originalValues.size(); i++) {
                            String name = originalValues.get(i).getDriverName();
                            if (name.toLowerCase().startsWith(charSequence.toString()) || name.toLowerCase().contains(charSequence.toString())) {
                                filteredArrayList.add(new Driver(originalValues.get(i).getDriverId(), originalValues.get(i).getDriverName(), originalValues.get(i).getAddress1(), originalValues.get(i).getAddress2(), originalValues.get(i).getMobile1(), originalValues.get(i).getMobile2(), originalValues.get(i).getMobile3(), originalValues.get(i).getDriverDob(), originalValues.get(i).getJoiningDate(), originalValues.get(i).getLicNo(), originalValues.get(i).getLicExpireDate(), originalValues.get(i).getDelStatus()));
                            }
                        }
                        results.count = filteredArrayList.size();
                        results.values = filteredArrayList;
                    }

                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    displayedValues = (ArrayList<Driver>) filterResults.values;
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
                holder = new ViewHolder();
                holder.tvName = v.findViewById(R.id.tvDialogRoute_Name);
                holder.llItem = v.findViewById(R.id.llDialogRoute_Item);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.tvName.setText("" + displayedValues.get(position).getDriverName());

            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edDriver.setText("" + displayedValues.get(position).getDriverName());
                    tvDriverId.setText("" + displayedValues.get(position).getDriverId());
                    dialog.dismiss();
                }
            });

            return v;
        }
    }

    public void getServerDate() {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
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

                            try {
                                String tempDate = data.getMessage();

                                String dd = tempDate.substring(0, 2);
                                String mm = tempDate.substring(3, 5);
                                String yy = tempDate.substring(6, 10);

                                todaysDate = yy + "-" + mm + "-" + dd;
                                Log.e("TRAY : ", " --------------------" + todaysDate);
                            } catch (Exception e) {
                                todaysDate = data.getMessage();
                            }

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
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
