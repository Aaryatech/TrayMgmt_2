package com.ats.traymanagement.fragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.activity.HomeActivity;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.model.FrTrayCount;
import com.ats.traymanagement.model.FranchiseByRoute;
import com.ats.traymanagement.model.Info;
import com.ats.traymanagement.model.Route;
import com.ats.traymanagement.model.RouteListData;
import com.ats.traymanagement.model.TrayMgmtDetailData;
import com.ats.traymanagement.model.TrayMgmtHeaderData;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FranchiseFragment extends Fragment implements View.OnClickListener {

    private TextView tvSelectFr, tvNext, tvExtraTray, tvSelectFrId;
    private EditText edSmall, edBig, edLarge, edXL;
    private ListView lvFrList;

    private ArrayList<FranchiseByRoute> franchiseArray = new ArrayList<>();
    private ArrayList<TrayMgmtDetailData> trayMgmtDetailDataArray = new ArrayList<>();

    TrayMgmtDetailListAdapter detailListAdapter;

    int routeId, headerId;

    TrayMgmtHeaderData headerData = new TrayMgmtHeaderData();

    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_franchise, container, false);

        tvSelectFr = view.findViewById(R.id.tvFranchise_SelectFr);
        tvSelectFrId = view.findViewById(R.id.tvFranchise_SelectFrId);
        tvNext = view.findViewById(R.id.tvFranchise_Next);
        tvExtraTray = view.findViewById(R.id.tvFranchise_ExtraTray);
        edSmall = view.findViewById(R.id.edFranchise_Small);
        edBig = view.findViewById(R.id.edFranchise_Big);
        edLarge = view.findViewById(R.id.edFranchise_Large);
        edXL = view.findViewById(R.id.edFranchise_XL);
        lvFrList = view.findViewById(R.id.lvFrList);

        tvNext.setOnClickListener(this);
        tvExtraTray.setOnClickListener(this);
        tvSelectFr.setOnClickListener(this);

        try {
            String headerStr = getArguments().getString("headerBean");
            Gson gson = new Gson();
            headerData = gson.fromJson(headerStr, TrayMgmtHeaderData.class);

            tvExtraTray.setText("Extra tray - " + headerData.getExtraTrayOut());

            routeId = getArguments().getInt("routeId");
            headerId = getArguments().getInt("headerId");
            String routeName = getArguments().getString("routeName");
            getActivity().setTitle("" + routeName);
            Log.e("Route Id : ", "--------------------" + routeId);
            getAllFranchise(routeId,headerId);
        } catch (Exception e) {
            Log.e("Exception Route Id : ", "--------------------" + e.getMessage());
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvFranchise_Next) {

            if (tvSelectFrId.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Franchise", Toast.LENGTH_SHORT).show();
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
                saveTrayMgmtDetail(trayMgmtDetailData);

            }

        } else if (view.getId() == R.id.tvFranchise_ExtraTray) {
            extraTrayDialog();
        } else if (view.getId() == R.id.tvFranchise_SelectFr) {
            showFranchiseDialog();
        }
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

        final DialogFranchiseListAdapter franchiseAdapter = new DialogFranchiseListAdapter(getActivity(), franchiseArray);
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

    public void extraTrayDialog() {
        final Dialog openDialog = new Dialog(getContext());
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

        edExtraTray.setText(""+headerData.getExtraTrayOut());
        edExtraTray.setSelection(edExtraTray.getText().length());

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edExtraTray.getText().toString().isEmpty()) {
                    edExtraTray.setError("Required");
                    edExtraTray.requestFocus();
                } else {
                    int extraTray = Integer.parseInt(edExtraTray.getText().toString());
                    headerData.setExtraTrayOut(extraTray);

                    openDialog.dismiss();

                    updateVehicleOutTray(headerId, extraTray);
                }
            }
        });

        openDialog.show();
    }

    public void saveTrayMgmtDetail(TrayMgmtDetailData trayMgmtDetailData) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
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
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                                getAllTrayMgmtDetailsByHeader(headerId);
                                getAllFranchise(routeId,headerId);
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
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void getAllFranchise(int routeId,int headerId) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<FranchiseByRoute>> allFranchiseByRoute = Constants.myInterface.getAllFranchiseByRoute(routeId,headerId);
            allFranchiseByRoute.enqueue(new Callback<ArrayList<FranchiseByRoute>>() {
                @Override
                public void onResponse(Call<ArrayList<FranchiseByRoute>> call, Response<ArrayList<FranchiseByRoute>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<FranchiseByRoute> data = response.body();
                            commonDialog.dismiss();
                            franchiseArray.clear();
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
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllTrayMgmtDetailsByHeader(int headerId) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            final Call<ArrayList<TrayMgmtDetailData>> trayMgmtDetailDataCall = Constants.myInterface.getTrayMgmtDetailByHeaderId(headerId);
            trayMgmtDetailDataCall.enqueue(new Callback<ArrayList<TrayMgmtDetailData>>() {
                @Override
                public void onResponse(Call<ArrayList<TrayMgmtDetailData>> call, Response<ArrayList<TrayMgmtDetailData>> response) {
                    try {
                        if (response.body() != null) {
                            commonDialog.dismiss();
                            trayMgmtDetailDataArray.clear();
                            ArrayList<TrayMgmtDetailData> data = response.body();
                            trayMgmtDetailDataArray = data;
                            Log.e("DATA : ", "------------------" + trayMgmtDetailDataArray);

                            detailListAdapter = new TrayMgmtDetailListAdapter(getContext(), trayMgmtDetailDataArray);
                            lvFrList.setAdapter(detailListAdapter);

                            tvSelectFr.setText("");
                            tvSelectFrId.setText("");
                            edSmall.setText("0");
                            edBig.setText("0");
                            edLarge.setText("0");
                            edXL.setText("0");

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
                public void onFailure(Call<ArrayList<TrayMgmtDetailData>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("Franchise : ", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
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
                holder = new ViewHolder();
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

                    tvSelectFrId.setText("" + displayedValues.get(position).getFrId());
                    tvSelectFr.setText("" + displayedValues.get(position).getFrName());
                    dialog.dismiss();
                    getFrTray(displayedValues.get(position).getFrId());

                }
            });

            return v;
        }
    }

    public void getFrTray(int frId) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
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
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class TrayMgmtDetailListAdapter extends BaseAdapter {

        private ArrayList<TrayMgmtDetailData> originalValues;
        private ArrayList<TrayMgmtDetailData> displayedValues;
        LayoutInflater inflater;

        public TrayMgmtDetailListAdapter(Context context, ArrayList<TrayMgmtDetailData> trayMgmtDetailData) {
            this.originalValues = trayMgmtDetailData;
            this.displayedValues = trayMgmtDetailData;
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


        public class ViewHolder {
            TextView tvFrName, tvSmall, tvBig, tvLarge, tvXL;
        }

        @Override
        public View getView(final int position, View v, ViewGroup parent) {
            ViewHolder holder = null;

            if (v == null) {
                v = inflater.inflate(R.layout.custom_tray_mgmt_detail_list_item, null);
                holder = new ViewHolder();
                holder.tvFrName = v.findViewById(R.id.tvCustomTrayDetail_FrName);
                holder.tvSmall = v.findViewById(R.id.tvCustomTrayDetail_Small);
                holder.tvBig = v.findViewById(R.id.tvCustomTrayDetail_Big);
                holder.tvLarge = v.findViewById(R.id.tvCustomTrayDetail_Large);
                holder.tvXL = v.findViewById(R.id.tvCustomTrayDetail_XL);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.tvFrName.setText("" + displayedValues.get(position).getFrName());
            holder.tvSmall.setText("" + displayedValues.get(position).getOuttraySmall());
            holder.tvBig.setText("" + displayedValues.get(position).getOuttrayBig());
            holder.tvLarge.setText("" + displayedValues.get(position).getOuttrayLead());
            holder.tvXL.setText("" + displayedValues.get(position).getOuttrayExtra());


            return v;
        }
    }

    public void updateVehicleOutTray(int headerId, final int tray) {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
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
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                                Log.e("DATA : ", "---------------" + data);

                                headerData.setExtraTrayOut(tray);
                                tvExtraTray.setText("Extra tray - " + headerData.getExtraTrayOut());

                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                            Log.e("updateVehicleOutTray : ", " NULL-----");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                        Log.e("updateVehicleOutTray : ", " Exception-----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                    Log.e("updateVehicleOutTray : ", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
