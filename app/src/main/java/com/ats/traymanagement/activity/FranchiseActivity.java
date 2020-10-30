package com.ats.traymanagement.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.ats.traymanagement.model.FrIdNamesList;
import com.ats.traymanagement.model.FrList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FranchiseActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edFromDate, edToDate, edFr;
    private TextView tvFrId, tvFromDate, tvToDate;
    private Button btnSearch;

    int yyyy, mm, dd;
    long fromDateMillis, toDateMillis;

    ArrayList<FrIdNamesList> frList = new ArrayList<>();

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franchise);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle("Franchise Return Tray");

        edFromDate = findViewById(R.id.edFromDate);
        edToDate = findViewById(R.id.edToDate);
        edFr = findViewById(R.id.edFr);
        tvFrId = findViewById(R.id.tvFrId);
        tvFromDate = findViewById(R.id.tvFromDate);
        tvToDate = findViewById(R.id.tvToDate);
        btnSearch = findViewById(R.id.btnSearch);

        edFromDate.setOnClickListener(this);
        edToDate.setOnClickListener(this);
        edFr.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        fromDateMillis = calendar.getTimeInMillis();
        toDateMillis = calendar.getTimeInMillis();

        edFromDate.setText("" + sdf1.format(calendar.getTimeInMillis()));
        tvFromDate.setText("" + sdf2.format(calendar.getTimeInMillis()));

        edToDate.setText("" + sdf1.format(calendar.getTimeInMillis()));
        tvToDate.setText("" + sdf2.format(calendar.getTimeInMillis()));


        getAllFranchise();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edFromDate) {

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


            DatePickerDialog dialog = new DatePickerDialog(FranchiseActivity.this, fromDtListener, yr, mn, dy);
            dialog.show();

        } else if (view.getId() == R.id.edToDate) {

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


            DatePickerDialog dialog = new DatePickerDialog(FranchiseActivity.this, toDtListener, yr, mn, dy);
            dialog.show();

        } else if (view.getId() == R.id.edFr) {
            showFranchiseDialog();

        } else if (view.getId() == R.id.btnSearch) {

            String strFromDate = tvFromDate.getText().toString().trim();
            String strToDate = tvToDate.getText().toString().trim();
            String strFr = edFr.getText().toString().trim();
            String strFrId = tvFrId.getText().toString().trim();

            if (strFromDate.isEmpty()) {
                edFromDate.setError("Required");
            }
//            else if (strToDate.isEmpty()) {
//                edFromDate.setError(null);
//                edToDate.setError("Required");
//            }
            else if (strFrId.isEmpty()) {
                edFromDate.setError(null);
                edToDate.setError(null);
                edFr.setError("Required");
            } else {
                edFromDate.setError(null);
                edToDate.setError(null);
                edFr.setError(null);

                Intent intent = new Intent(FranchiseActivity.this, FrReturnTrayUpdateActivity.class);
                intent.putExtra("FromDate", strFromDate);
                intent.putExtra("ToDate", strFromDate);
               // intent.putExtra("ToDate", strToDate);
                intent.putExtra("FrName", strFr);
                intent.putExtra("FrId", strFrId);
                startActivity(intent);

            }


        }
    }

    public void showFranchiseDialog() {
        dialog = new Dialog(FranchiseActivity.this, android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dialog_list_layout, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ListView lvList = dialog.findViewById(R.id.lvDialog_List);
        EditText edSearch = dialog.findViewById(R.id.edDialog_Search);

        final DialogFranchiseListAdapter franchiseAdapter = new DialogFranchiseListAdapter(FranchiseActivity.this, frList);
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

        private ArrayList<FrIdNamesList> originalValues;
        private ArrayList<FrIdNamesList> displayedValues;
        LayoutInflater inflater;

        public DialogFranchiseListAdapter(Context context, ArrayList<FrIdNamesList> frArrayList) {
            this.originalValues = frArrayList;
            this.displayedValues = frArrayList;
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
                    ArrayList<FrIdNamesList> filteredArrayList = new ArrayList<FrIdNamesList>();

                    if (originalValues == null) {
                        originalValues = new ArrayList<FrIdNamesList>(displayedValues);
                    }

                    if (charSequence == null || charSequence.length() == 0) {
                        results.count = originalValues.size();
                        results.values = originalValues;
                    } else {
                        charSequence = charSequence.toString().toLowerCase();
                        for (int i = 0; i < originalValues.size(); i++) {
                            String name = originalValues.get(i).getFrName();
                            if (name.toLowerCase().startsWith(charSequence.toString()) || name.toLowerCase().contains(charSequence.toString())) {
                                filteredArrayList.add(new FrIdNamesList(originalValues.get(i).getFrId(), originalValues.get(i).getFrName()));
                            }
                        }
                        results.count = filteredArrayList.size();
                        results.values = filteredArrayList;
                    }

                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    displayedValues = (ArrayList<FrIdNamesList>) filterResults.values;
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
            DialogFranchiseListAdapter.ViewHolder holder = null;

            if (v == null) {
                v = inflater.inflate(R.layout.custom_dialog_route_list_item, null);
                holder = new DialogFranchiseListAdapter.ViewHolder();
                holder.tvName = v.findViewById(R.id.tvDialogRoute_Name);
                holder.llItem = v.findViewById(R.id.llDialogRoute_Item);
                v.setTag(holder);
            } else {
                holder = (DialogFranchiseListAdapter.ViewHolder) v.getTag();
            }

            holder.tvName.setText("" + displayedValues.get(position).getFrName());

            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    tvFrId.setText("" + displayedValues.get(position).getFrId());
                    edFr.setText("" + displayedValues.get(position).getFrName());
                    dialog.dismiss();

                }
            });

            return v;
        }
    }


    public void getAllFranchise() {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<FrList> allFranchise = Constants.myInterface.getAllFranchisee();
            allFranchise.enqueue(new Callback<FrList>() {
                @Override
                public void onResponse(Call<FrList> call, Response<FrList> response) {
                    try {
                        if (response.body() != null) {
                            FrList data = response.body();
                            commonDialog.dismiss();
                            frList.clear();

                            if (data.getFrIdNamesList() != null) {
                                if (data.getFrIdNamesList().size() > 0) {
                                    for (int i = 0; i < data.getFrIdNamesList().size(); i++) {
                                        frList.add(data.getFrIdNamesList().get(i));
                                    }
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
                public void onFailure(Call<FrList> call, Throwable t) {
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