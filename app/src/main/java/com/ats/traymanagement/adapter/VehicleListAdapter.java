package com.ats.traymanagement.adapter;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.activity.HomeActivity;
import com.ats.traymanagement.activity.LoginActivity;
import com.ats.traymanagement.activity.TrayStatusActivity;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.fragment.AddRouteFragment;
import com.ats.traymanagement.fragment.FranchiseFragment;
import com.ats.traymanagement.fragment.VehicleListFragment;
import com.ats.traymanagement.model.ErrorMessage;
import com.ats.traymanagement.model.Info;
import com.ats.traymanagement.model.TrayMgmtDetailData;
import com.ats.traymanagement.model.TrayMgmtHeaderData;
import com.ats.traymanagement.model.TrayMgmtHeaderDisplayList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by MAXADMIN on 15/2/2018.
 */

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.MyViewHolder> {

    private ArrayList<TrayMgmtHeaderDisplayList> vehicleList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvRoute, tvType, tvTrayStatus, tvExtraTray, tvExtraTrayIn, tvDate, tvDiesel;
        public ImageView ivDelete;
        public LinearLayout llDiesel;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvVehicleList_Name);
            tvRoute = view.findViewById(R.id.tvVehicleList_Route);
            tvType = view.findViewById(R.id.tvVehicleList_Type);
            tvTrayStatus = view.findViewById(R.id.tvVehicleList_TrayStatus);
            tvExtraTray = view.findViewById(R.id.tvVehicleList_ExtraTrayOut);
            tvExtraTrayIn = view.findViewById(R.id.tvVehicleList_ExtraTrayIn);
            tvDate = view.findViewById(R.id.tvVehicleList_Date);
            ivDelete = view.findViewById(R.id.ivVehicleList_Delete);
            tvDiesel = view.findViewById(R.id.tvVehicleList_Diesel);
            llDiesel = view.findViewById(R.id.llVehicleList_Diesel);

        }
    }

    public VehicleListAdapter(ArrayList<TrayMgmtHeaderDisplayList> vehicleList, Context context) {
        this.vehicleList = vehicleList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_vehicle_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvName.setText(vehicleList.get(position).getVehNo() + " " + vehicleList.get(position).getDriverName());
        holder.tvRoute.setText(vehicleList.get(position).getRouteName());

        try {
            String dateStr = vehicleList.get(position).getTranDate();
            String yyyy = dateStr.substring(0, 4);
            String mm = dateStr.substring(5, 7);
            String dd = dateStr.substring(8, 10);
            holder.tvDate.setText(dd + "-" + mm + "-" + yyyy);
        } catch (Exception e) {
        }

        holder.tvType.setText("OUT");
        holder.tvType.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_layout));

        holder.tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //vehicleOutDialog(vehicleList.get(position).getTranId());
                getVehInLastRec(vehicleList.get(position).getTranId(),vehicleList.get(position).getVehId());
            }
        });


        holder.tvTrayStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                String bean = gson.toJson(vehicleList.get(position));

                Intent intent = new Intent(context, TrayStatusActivity.class);
                intent.putExtra("headerId", vehicleList.get(position).getTranId());
                intent.putExtra("headerBean", bean);
                intent.putExtra("type", 1);
                context.startActivity(intent);
            }
        });

        if (vehicleList.get(position).getIsSameDay() == 1) {
            holder.tvTrayStatus.setVisibility(View.GONE);
        }

        holder.tvExtraTray.setVisibility(View.VISIBLE);
        holder.tvExtraTrayIn.setVisibility(View.INVISIBLE);

        if (vehicleList.get(position).getExtraTrayOut() == "") {
            holder.tvExtraTray.setText(" Extra Tray Out - 0 ");
        } else {

            try {
                String str[] = vehicleList.get(position).getExtraTrayOut().split("#");
                int sm = 0, bg = 0, ld = 0;
                if (str.length == 3) {
                    if (!str[0].isEmpty() || str[0] != "") {
                        sm = Integer.parseInt(str[0]);
                    } else {
                        sm = 0;
                    }

                    if (!str[1].isEmpty() || str[1] != "") {
                        bg = Integer.parseInt(str[1]);
                    } else {
                        bg = 0;
                    }

                    if (!str[2].isEmpty() || str[2] != "") {
                        ld = Integer.parseInt(str[2]);
                    } else {
                        ld = 0;
                    }
                }

                holder.tvExtraTray.setText(" Extra Tray Out - " + (sm + bg + ld));

            } catch (Exception e) {
                holder.tvExtraTray.setText(" Extra Tray Out - 0");

            }


        }

        holder.tvExtraTray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extraTrayDialog(vehicleList.get(position).getTranId(), vehicleList.get(position).getExtraTrayOut(), "out");
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                builder.setTitle("Caution");
                builder.setMessage("Do You Want To Delete Record ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteHeader(vehicleList.get(position).getTranId());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public void vehicleOutDialog(final int headerId, final float lastKm) {
        final Dialog openDialog = new Dialog(context);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.custom_vehicle_out_dialog);
        openDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.dimAmount = 0.75f;
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.x = 100;
        wlp.y = 100;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        final EditText edLastKm = openDialog.findViewById(R.id.edVehicleOutDialog_LastKm);
        final EditText edOutKm = openDialog.findViewById(R.id.edVehicleOutDialog_StartKm);
        final EditText edOutTime = openDialog.findViewById(R.id.edVehicleOutDialog_OutTime);
        TextView tvSubmit = openDialog.findViewById(R.id.tvVehicleOutDialog_Submit);

        edLastKm.setText(""+lastKm);

        edOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edOutTime.setText(selectedHour + ":" + selectedMinute + ":00");
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String km=edOutKm.getText().toString().trim();

                if (edOutKm.getText().toString().isEmpty()) {
                    edOutKm.setError("Required");
                    edOutKm.requestFocus();
                }else if (Float.parseFloat(km)<lastKm || Float.parseFloat(km)<=0){
                    edOutKm.setError("Enter OUT km greater than last IN km.");
                    edOutKm.requestFocus();
                }
                /*else if (edOutTime.getText().toString().isEmpty()) {
                    edOutTime.setError("Required");
                    edOutTime.requestFocus();
                }*/
                else {

                    float outKm = Float.parseFloat(edOutKm.getText().toString());
                    //String outTime = edOutTime.getText().toString();

                    openDialog.dismiss();
                    updateVehicleOut(headerId, outKm);

                }
            }
        });

        openDialog.show();
    }

    public void updateVehicleOut(int headerId, float outKm) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> infoCall = Constants.myInterface.updateVehicleOutData(headerId, outKm);
            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            Info data = response.body();
                            if (data.isError()) {
                                commonDialog.dismiss();
                                Log.e("updateVehicleOut: ", " ERROR-----" + data.getMessage());
                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                Log.e("DATA : ", "---------------" + data);

                                HomeActivity activity = (HomeActivity) context;
                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.frame_container, new VehicleListFragment(), "Home");
                                ft.commit();
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(context, "Unable To Process", Toast.LENGTH_SHORT).show();
                            Log.e("updateVehicleOut : ", " NULL-----");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(context, "Unable To Process", Toast.LENGTH_SHORT).show();
                        Log.e("updateVehicleOut : ", " Exception-----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(context, "Unable To Process", Toast.LENGTH_SHORT).show();
                    Log.e("updateVehicleOut : ", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void extraTrayDialog(final int headerId, String exTrays, String type) {
        final Dialog openDialog = new Dialog(context);
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

        final EditText edExtraTraySmall = openDialog.findViewById(R.id.edExtraTraySmall);
        final EditText edExtraTrayBig = openDialog.findViewById(R.id.edExtraTrayBig);
        final EditText edExtraTrayLids = openDialog.findViewById(R.id.edExtraTrayLids);
        final TextView tvLabel = openDialog.findViewById(R.id.tvExTrayLabel);
        TextView tvSubmit = openDialog.findViewById(R.id.tvExtraTray_Submit);

        if (type.equalsIgnoreCase("out")) {
            tvLabel.setText("Extra Tray Out");
        } else if (type.equalsIgnoreCase("in")) {
            tvLabel.setText("Extra Tray In");
        } else {
            tvLabel.setText("Extra Tray");
        }

        //edExtraTray.setSelection(edExtraTray.getText().length());
        try {
            Log.e("TRAYS - ", "" + exTrays);
            String[] str = exTrays.split("#");
            String sm = "0", bg = "0", ld = "0";
            if (str.length == 3) {
                if (!str[0].isEmpty() || str[0] != "") {
                    sm = str[0];
                } else {
                    sm = "0";
                }

                if (!str[1].isEmpty() || str[1] != "") {
                    bg = str[1];
                } else {
                    bg = "0";
                }

                if (!str[2].isEmpty() || str[2] != "") {
                    ld = str[2];
                } else {
                    ld = "0";
                }
            }

            edExtraTraySmall.setText(sm);
            edExtraTrayBig.setText(bg);
            edExtraTrayLids.setText(ld);

        } catch (Exception e) {
            e.printStackTrace();
            edExtraTraySmall.setText("0");
            edExtraTrayBig.setText("0");
            edExtraTrayLids.setText("0");

        }

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String small = edExtraTraySmall.getText().toString().trim();
                String big = edExtraTrayBig.getText().toString().trim();
                String lids = edExtraTrayLids.getText().toString().trim();
                String tot = "";

                if (small == "" || small.isEmpty()) {
                    small = "0";
                }

                if (big == "" || big.isEmpty()) {
                    big = "0";
                }

                if (lids == "" || lids.isEmpty()) {
                    lids = "0";
                }

                tot = small + "#" + big + "#" + lids;
                openDialog.dismiss();
                updateVehicleOutTray(headerId, tot);

               /* if (edExtraTray.getText().toString().isEmpty()) {
                    edExtraTray.setError("Required");
                    edExtraTray.requestFocus();
                } else {
                    String extraTray = edExtraTray.getText().toString();
                    openDialog.dismiss();
                    updateVehicleOutTray(headerId, tot);
                }*/
            }
        });

        openDialog.show();
    }

    public void updateVehicleOutTray(final int headerId, final String tray) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
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
                                Toast.makeText(context, "Unable To Process", Toast.LENGTH_SHORT).show();
                                Log.e("VehicleListAdapter: ", " ERROR-----" + data.getMessage());
                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                                Log.e("DATA : ", "---------------" + data);

                                HomeActivity activity = (HomeActivity) context;
                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.frame_container, new VehicleListFragment(), "Home");
                                ft.commit();

                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(context, "Unable To Process", Toast.LENGTH_SHORT).show();
                            Log.e("VehicleListAdapter : ", " NULL-----");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(context, "Unable To Process", Toast.LENGTH_SHORT).show();
                        Log.e("VehicleListAdapter : ", " Exception-----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(context, "Unable To Process", Toast.LENGTH_SHORT).show();
                    Log.e("VehicleListAdapter : ", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void deleteHeader(final int headerId) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> infoCall = Constants.myInterface.deleteVehicleHeaderAndDetail(headerId);
            infoCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {
                            ErrorMessage data = response.body();
                            if (data.isError()) {
                                commonDialog.dismiss();
                                Toast.makeText(context, "Unable To Delete", Toast.LENGTH_SHORT).show();
                                Log.e("VehicleListAdapter: ", " ERROR-----" + data.getMessage());
                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                                Log.e("DATA : ", "---------------" + data);

                                HomeActivity activity = (HomeActivity) context;
                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.frame_container, new VehicleListFragment(), "Home");
                                ft.commit();

                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(context, "Unable To Delete", Toast.LENGTH_SHORT).show();
                            Log.e("VehicleListAdapter : ", " NULL-----");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(context, "Unable To Delete", Toast.LENGTH_SHORT).show();
                        Log.e("VehicleListAdapter : ", " Exception-----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(context, "Unable To Process", Toast.LENGTH_SHORT).show();
                    Log.e("VehicleListAdapter : ", " OnFailure-----" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    //VEH IN LAST RECORD
    public void getVehInLastRec(final int headerId, int vehId) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            final float[] km = {0};

            Call<TrayMgmtHeaderData> infoCall = Constants.myInterface.getVehInLastRec(vehId);
            infoCall.enqueue(new Callback<TrayMgmtHeaderData>() {
                @Override
                public void onResponse(Call<TrayMgmtHeaderData> call, Response<TrayMgmtHeaderData> response) {
                    try {
                        if (response.body() != null) {
                            TrayMgmtHeaderData data = response.body();
                            km[0] =data.getVehInkm();
                            commonDialog.dismiss();
                        } else {
                            commonDialog.dismiss();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        vehicleOutDialog(headerId, km[0]);
                    }

                    vehicleOutDialog(headerId, km[0]);
                    commonDialog.dismiss();
                }

                @Override
                public void onFailure(Call<TrayMgmtHeaderData> call, Throwable t) {
                    commonDialog.dismiss();
                    vehicleOutDialog(headerId, km[0]);
                }
            });

        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
