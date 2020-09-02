package com.ats.traymanagement.adapter;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.ats.traymanagement.activity.TrayStatusActivity;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.fragment.VehicleInListFragment;
import com.ats.traymanagement.fragment.VehicleListFragment;
import com.ats.traymanagement.model.Info;
import com.ats.traymanagement.model.TrayMgmtHeaderData;
import com.ats.traymanagement.model.TrayMgmtHeaderDisplayList;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleInListAdapter extends RecyclerView.Adapter<VehicleInListAdapter.MyViewHolder> {

    private ArrayList<TrayMgmtHeaderDisplayList> vehicleList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvRoute, tvType, tvTrayStatus, tvExtraTrayOut, tvExtraTrayIn, tvDate, tvDiesel;
        public ImageView ivDelete;
        public LinearLayout llDiesel;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvVehicleList_Name);
            tvRoute = view.findViewById(R.id.tvVehicleList_Route);
            tvType = view.findViewById(R.id.tvVehicleList_Type);
            tvTrayStatus = view.findViewById(R.id.tvVehicleList_TrayStatus);
            tvExtraTrayOut = view.findViewById(R.id.tvVehicleList_ExtraTrayOut);
            tvExtraTrayIn = view.findViewById(R.id.tvVehicleList_ExtraTrayIn);
            tvDate = view.findViewById(R.id.tvVehicleList_Date);
            ivDelete = view.findViewById(R.id.ivVehicleList_Delete);
            tvDiesel = view.findViewById(R.id.tvVehicleList_Diesel);
            llDiesel = view.findViewById(R.id.llVehicleList_Diesel);

        }
    }

    public VehicleInListAdapter(ArrayList<TrayMgmtHeaderDisplayList> vehicleList, Context context) {
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
        //  holder.tvExtraTray.setVisibility(View.INVISIBLE);
        holder.ivDelete.setVisibility(View.GONE);
        holder.llDiesel.setVisibility(View.VISIBLE);

        holder.tvName.setText(vehicleList.get(position).getTranId() + "_" + vehicleList.get(position).getVehNo() + " " + vehicleList.get(position).getDriverName());
        holder.tvRoute.setText(vehicleList.get(position).getRouteName());
        holder.tvDiesel.setText(vehicleList.get(position).getDiesel() + " lts");

        try {
            String dateStr = vehicleList.get(position).getTranDate();
            String yyyy = dateStr.substring(0, 4);
            String mm = dateStr.substring(5, 7);
            String dd = dateStr.substring(8, 10);

            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(vehicleList.get(position).getVehOuttime());
            String timeStr = new SimpleDateFormat("hh:mm a").format(dateObj);

            holder.tvDate.setText(dd + "-" + mm + "-" + yyyy + " " + timeStr);

        } catch (Exception e) {
            holder.tvDate.setText(vehicleList.get(position).getTranDate());
        }

        holder.tvType.setText("IN");
        holder.tvType.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_layout));

        holder.tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicleInDialog(vehicleList.get(position).getTranId(), vehicleList.get(position).getDiesel(), vehicleList.get(position).getExtraTrayOut(), vehicleList.get(position).getVehOutkm());
            }
        });

        if (vehicleList.get(position).getIsSameDay() == 1) {
            holder.tvTrayStatus.setVisibility(View.GONE);
        }

        Log.e("Ex - ", "" + vehicleList.get(position).getExtraTrayOut());

        if (vehicleList.get(position).getExtraTrayOut() == "") {
            holder.tvExtraTrayOut.setText(" Extra Tray Out - 0 ");
        } else {
            //holder.tvExtraTray.setText(" Extra Tray -" + vehicleList.get(position).getExtraTrayOut() + " ");

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

                holder.tvExtraTrayOut.setText(" Extra Tray Out - " + (sm + bg + ld));

            } catch (Exception e) {
                holder.tvExtraTrayOut.setText(" Extra Tray Out - 0");

            }
        }

        holder.tvExtraTrayOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extraTrayDialog(vehicleList.get(position).getTranId(), vehicleList.get(position).getExtraTrayOut(), "out");
            }
        });

        if (vehicleList.get(position).getExtraTrayOut() == "") {
            holder.tvExtraTrayIn.setText(" Extra Tray In - 0 ");
        } else {
            //holder.tvExtraTray.setText(" Extra Tray -" + vehicleList.get(position).getExtraTrayOut() + " ");

            try {
                String str[] = vehicleList.get(position).getExtraTrayIn().split("#");
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

                holder.tvExtraTrayIn.setText(" Extra Tray In - " + (sm + bg + ld));

            } catch (Exception e) {
                holder.tvExtraTrayIn.setText(" Extra Tray In - 0");
            }

            holder.tvExtraTrayIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    extraTrayDialog(vehicleList.get(position).getTranId(), vehicleList.get(position).getExtraTrayIn(), "in");
                }
            });

        }

        holder.tvTrayStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                String bean = gson.toJson(vehicleList.get(position));

                Intent intent = new Intent(context, TrayStatusActivity.class);
                intent.putExtra("headerId", vehicleList.get(position).getTranId());
                intent.putExtra("headerBean", bean);
                intent.putExtra("type", 2);
                context.startActivity(intent);
            }
        });

        holder.llDiesel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicleInDieselDialog(vehicleList.get(position).getTranId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
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
        tvSubmit.setVisibility(View.GONE);

        if (type.equalsIgnoreCase("Out")) {
            tvLabel.setText("Extra Tray Out");
        } else if (type.equalsIgnoreCase("In")) {
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

        openDialog.show();
    }

    public void vehicleInDialog(final int headerId, final float diesel, String outTray, final float startKm) {
        final Dialog openDialog = new Dialog(context);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.custom_vehicle_in_dialog);
        openDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.dimAmount = 0.75f;
        wlp.x = 100;
        wlp.y = 100;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        final EditText edEndKm = openDialog.findViewById(R.id.edVehicleInDialog_EndKm);
        final EditText edStartKm = openDialog.findViewById(R.id.edVehicleInDialog_StartKm);
        final EditText edInTime = openDialog.findViewById(R.id.edVehicleInDialog_InTime);
        final EditText edInTray = openDialog.findViewById(R.id.edVehicleInDialog_InTray);
        final EditText edInSmall = openDialog.findViewById(R.id.edExSmall);
        final EditText edInBig = openDialog.findViewById(R.id.edExBig);
        final EditText edInLids = openDialog.findViewById(R.id.edExLids);
        final EditText edOutSmall = openDialog.findViewById(R.id.edExOutSmall);
        final EditText edOutBig = openDialog.findViewById(R.id.edExOutBig);
        final EditText edOutLids = openDialog.findViewById(R.id.edExOutLids);
        final EditText edDiesel = openDialog.findViewById(R.id.edVehicleInDialog_Diesel);
        TextView tvSubmit = openDialog.findViewById(R.id.tvVehicleInDialog_Submit);

        edStartKm.setText(""+startKm);

        try {
            Log.e("TRAYS - ", "" + outTray);
            String[] str = outTray.split("#");
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

            edOutSmall.setText(sm);
            edOutBig.setText(bg);
            edOutLids.setText(ld);

        } catch (Exception e) {
            e.printStackTrace();
            edOutSmall.setText("0");
            edOutBig.setText("0");
            edOutLids.setText("0");

        }


        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String endKm=edEndKm.getText().toString().trim();

                if (edEndKm.getText().toString().isEmpty()) {
                    edEndKm.setError("Required");
                    edEndKm.requestFocus();
                } else if (Float.parseFloat(endKm) < startKm || Float.parseFloat(endKm) > (startKm + 900)) {
                    edEndKm.setError("End Km is greater than start km.");
                    edEndKm.requestFocus();
                } else if (diesel <= 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    builder.setTitle("Caution");
                    builder.setMessage("Diesel Is Not Entered");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
               /* else if (edDiesel.getText().toString().isEmpty()) {
                    edDiesel.setError("Required");
                    edDiesel.requestFocus();
                }*/
                else {

                    float inKm = Float.parseFloat(edEndKm.getText().toString());
                    //String inTime = edInTime.getText().toString();
                    // String inTray = edInTray.getText().toString();
                    //float diesel = Float.parseFloat(edDiesel.getText().toString());


                    //---------------------------------
                    String small = edInSmall.getText().toString().trim();
                    String big = edInBig.getText().toString().trim();
                    String lids = edInLids.getText().toString().trim();
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
                    //---------------------------------

                    openDialog.dismiss();
                    updateVehicleIn(headerId, inKm, tot);

                }
            }
        });


        openDialog.show();
    }

    public void vehicleInDieselDialog(final int headerId) {
        final Dialog openDialog = new Dialog(context);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.update_diesel_dialog);
        openDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.dimAmount = 0.75f;
        wlp.x = 100;
        wlp.y = 100;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        final EditText edDiesel = openDialog.findViewById(R.id.edDieselDialog);
        TextView tvSubmit = openDialog.findViewById(R.id.tvDieselDialog_Submit);

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edDiesel.getText().toString().isEmpty()) {
                    edDiesel.setError("Required");
                    edDiesel.requestFocus();
                } else {
                    float diesel = Float.parseFloat(edDiesel.getText().toString());
                    openDialog.dismiss();
                    updateVehicleDiesel(headerId, diesel);
                }
            }
        });


        openDialog.show();
    }


    public void updateVehicleIn(int headerId, float inKm, String inTray) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> infoCall = Constants.myInterface.updateVehicleInData(headerId, inKm, inTray);
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
                                ft.replace(R.id.frame_container, new VehicleInListFragment(), "Home");
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

    public void updateVehicleDiesel(int headerId, float diesel) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> infoCall = Constants.myInterface.updateDiesel(headerId, diesel);
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
                                ft.replace(R.id.frame_container, new VehicleInListFragment(), "Home");
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
}
