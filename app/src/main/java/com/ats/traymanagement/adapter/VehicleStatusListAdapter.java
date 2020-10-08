package com.ats.traymanagement.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ats.traymanagement.R;
import com.ats.traymanagement.activity.TrayStatusActivity;
import com.ats.traymanagement.model.TrayMgmtHeaderDisplayList;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by MAXADMIN on 21/2/2018.
 */

public class VehicleStatusListAdapter extends RecyclerView.Adapter<VehicleStatusListAdapter.MyViewHolder> {


    private ArrayList<TrayMgmtHeaderDisplayList> vehicleList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvRoute, tvType, tvTrayStatus, tvDate, tvExtraTrayStatusOut, tvExtraTrayStatusIn, tvOutKm, tvInKm, tvRunningKm, tvDiesel;

        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvCustomVehicleList_Date);
            tvName = view.findViewById(R.id.tvCustomVehicleList_Name);
            tvRoute = view.findViewById(R.id.tvCustomVehicleList_Route);
            tvTrayStatus = view.findViewById(R.id.tvCustomVehicleList_TrayStatus);
            tvType = view.findViewById(R.id.tvCustomVehicleList_Type);
            tvExtraTrayStatusOut = view.findViewById(R.id.tvCustomVehicleList_ExtraTrayStatusOut);
            tvExtraTrayStatusIn = view.findViewById(R.id.tvCustomVehicleList_ExtraTrayStatusIn);
            tvOutKm = view.findViewById(R.id.tvCustomVehicleList_OutKm);
            tvInKm = view.findViewById(R.id.tvCustomVehicleList_InKm);
            tvRunningKm = view.findViewById(R.id.tvCustomVehicleList_RunningKm);
            tvDiesel = view.findViewById(R.id.tvCustomVehicleList_diesel);

        }
    }

    public VehicleStatusListAdapter(ArrayList<TrayMgmtHeaderDisplayList> vehicleList, Context context) {
        this.vehicleList = vehicleList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_vehicle_status_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvName.setText(vehicleList.get(position).getTranId() + "_" + vehicleList.get(position).getVehNo() + " " + vehicleList.get(position).getDriverName());
        holder.tvRoute.setText(vehicleList.get(position).getRouteName());

        Log.e("VEH LIST - ", "" + vehicleList.get(position));

        if (vehicleList.get(position).getVehStatus() == 0) {
            holder.tvType.setText("LOADING");
            holder.tvType.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        } else if (vehicleList.get(position).getVehStatus() == 1) {
            holder.tvType.setText("OUT");
            holder.tvType.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));

        } else if (vehicleList.get(position).getVehStatus() == 2) {
            holder.tvType.setText("IN");
            holder.tvType.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }

        holder.tvOutKm.setText("Out Km - " + vehicleList.get(position).getVehOutkm());
        holder.tvInKm.setText("In Km - " + vehicleList.get(position).getVehInkm());
        if ((vehicleList.get(position).getVehInkm() - vehicleList.get(position).getVehOutkm()) > 0) {
            holder.tvRunningKm.setText("Running Km - " + (vehicleList.get(position).getVehInkm() - vehicleList.get(position).getVehOutkm()));
        } else {
            holder.tvRunningKm.setText("Running Km - 0");
        }

        holder.tvDiesel.setText("Diesel - " + vehicleList.get(position).getDiesel());


        if (vehicleList.get(position).getVehStatus() == 2) {

            holder.tvExtraTrayStatusOut.setVisibility(View.VISIBLE);
            holder.tvExtraTrayStatusIn.setVisibility(View.VISIBLE);


            try {
                String date = vehicleList.get(position).getTranDate();
                String yyyy = date.substring(0, 4);
                String mm = date.substring(5, 7);
                String dd = date.substring(8, 10);

                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObj = sdf.parse(vehicleList.get(position).getVehIntime());
                String timeStr = new SimpleDateFormat("K:mm a").format(dateObj);

                holder.tvDate.setText(dd + "-" + mm + "-" + yyyy);

            } catch (Exception e) {
                holder.tvDate.setText(vehicleList.get(position).getTranDate());
            }

            //holder.tvExtraTrayStatus.setText("Extra tray- " + vehicleList.get(position).getExtraTrayIn());

            if (vehicleList.get(position).getExtraTrayOut() == "") {
                holder.tvExtraTrayStatusOut.setText(" Extra Tray Out - 0 ");
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
                    holder.tvExtraTrayStatusOut.setText(" Extra Tray Out - " + (sm + bg + ld));

                } catch (Exception e) {
                    holder.tvExtraTrayStatusOut.setText(" Extra Tray Out - 0");
                }
            }

            holder.tvExtraTrayStatusOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    extraTrayDialog(vehicleList.get(position).getTranId(), vehicleList.get(position).getExtraTrayOut(), "out");
                }
            });


            if (vehicleList.get(position).getExtraTrayIn() == "") {
                holder.tvExtraTrayStatusIn.setText(" Extra Tray In - 0 ");
            } else {
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
                    holder.tvExtraTrayStatusIn.setText(" Extra Tray In -" + (sm + bg + ld));

                } catch (Exception e) {
                    holder.tvExtraTrayStatusIn.setText(" Extra Tray In - 0");
                }
            }

            holder.tvExtraTrayStatusIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    extraTrayDialog(vehicleList.get(position).getTranId(), vehicleList.get(position).getExtraTrayIn(), "In");
                }
            });


        } else if (vehicleList.get(position).getVehStatus() == 1) {

            holder.tvExtraTrayStatusOut.setVisibility(View.VISIBLE);
            holder.tvExtraTrayStatusIn.setVisibility(View.INVISIBLE);

            try {
                String date = vehicleList.get(position).getTranDate();
                String yyyy = date.substring(0, 4);
                String mm = date.substring(5, 7);
                String dd = date.substring(8, 10);

                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObj = sdf.parse(vehicleList.get(position).getVehOuttime());
                String timeStr = new SimpleDateFormat("K:mm a").format(dateObj);

                holder.tvDate.setText(dd + "-" + mm + "-" + yyyy);

            } catch (Exception e) {
                holder.tvDate.setText(vehicleList.get(position).getTranDate());
            }

            // holder.tvExtraTrayStatus.setText("Extra tray- " + vehicleList.get(position).getExtraTrayOut());
            if (vehicleList.get(position).getExtraTrayOut() == "") {
                holder.tvExtraTrayStatusOut.setText(" Extra Tray Out - 0 ");
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
                    holder.tvExtraTrayStatusOut.setText(" Extra Tray Out - " + (sm + bg + ld));

                } catch (Exception e) {
                    holder.tvExtraTrayStatusOut.setText(" Extra Tray Out - 0");
                }
            }

            holder.tvExtraTrayStatusOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    extraTrayDialog(vehicleList.get(position).getTranId(), vehicleList.get(position).getExtraTrayOut(), "out");
                }
            });

        } else {

            holder.tvExtraTrayStatusOut.setVisibility(View.VISIBLE);
            holder.tvExtraTrayStatusIn.setVisibility(View.INVISIBLE);

            try {
                String date = vehicleList.get(position).getTranDate();
                String yyyy = date.substring(0, 4);
                String mm = date.substring(5, 7);
                String dd = date.substring(8, 10);

                holder.tvDate.setText(dd + "-" + mm + "-" + yyyy);

            } catch (Exception e) {
                holder.tvDate.setText(vehicleList.get(position).getTranDate());
            }

            //holder.tvExtraTrayStatus.setText("Extra tray- " + vehicleList.get(position).getExtraTrayOut());
            if (vehicleList.get(position).getExtraTrayOut() == "") {
                holder.tvExtraTrayStatusOut.setText(" Extra Tray Out - 0 ");
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
                    holder.tvExtraTrayStatusOut.setText(" Extra Tray Out - " + (sm + bg + ld));

                } catch (Exception e) {
                    holder.tvExtraTrayStatusOut.setText(" Extra Tray Out - 0 ");
                }
            }

            holder.tvExtraTrayStatusOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    extraTrayDialog(vehicleList.get(position).getTranId(), vehicleList.get(position).getExtraTrayOut(), "out");
                }
            });

        }

        holder.tvTrayStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                String bean = gson.toJson(vehicleList.get(position));

                if (vehicleList.get(position).getVehStatus() == 2) {
                    Intent intent = new Intent(context, TrayStatusActivity.class);
                    intent.putExtra("headerId", vehicleList.get(position).getTranId());
                    intent.putExtra("headerBean", bean);
                    intent.putExtra("type", 2);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, TrayStatusActivity.class);
                    intent.putExtra("headerId", vehicleList.get(position).getTranId());
                    intent.putExtra("headerBean", bean);
                    intent.putExtra("type", 0);
                    intent.putExtra("vehStatus", vehicleList.get(position).getVehStatus());

                    context.startActivity(intent);
                }
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


}
