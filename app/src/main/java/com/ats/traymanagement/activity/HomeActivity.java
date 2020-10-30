package com.ats.traymanagement.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ats.traymanagement.R;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.fragment.AddRouteFragment;
import com.ats.traymanagement.fragment.FranchiseFragment;
import com.ats.traymanagement.fragment.HomeFragment;
import com.ats.traymanagement.fragment.VehicleInListFragment;
import com.ats.traymanagement.fragment.VehicleListFragment;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    public static LinearLayout llHome, llAddRoute, llFranchise, llVehicleOut, llVehicleIn, llHomeBG, llAddRouteBG, llFranchiseBG, llVehicleOutBG, llVehicleInBG;
    public static TextView tvHome, tvAddRoute, tvFranchise, tvVehicleOut, tvVehicleIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Add Route");

        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
            String username = pref.getString("username", "");
            if (username.isEmpty() || username == null) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }

        } catch (Exception e) {
        }

        llHome = findViewById(R.id.llHomeMenu_Home);
        llAddRoute = findViewById(R.id.llHomeMenu_AddRoute);
        llVehicleOut = findViewById(R.id.llHomeMenu_VehicleOut);
        llVehicleIn = findViewById(R.id.llHomeMenu_VehicleIn);

        llHomeBG = findViewById(R.id.llHomeMenu_HomeBG);
        llAddRouteBG = findViewById(R.id.llHomeMenu_AddRouteBG);
        llVehicleOutBG = findViewById(R.id.llHomeMenu_VehicleOutBG);
        llVehicleInBG = findViewById(R.id.llHomeMenu_VehicleInBG);

        tvHome = findViewById(R.id.tvHomeMenu_Home);
        tvAddRoute = findViewById(R.id.tvHomeMenu_AddRoute);
        tvVehicleOut = findViewById(R.id.tvHomeMenu_VehicleOut);
        tvVehicleIn = findViewById(R.id.tvHomeMenu_VehicleIn);

        llAddRoute.setOnClickListener(this);
        llHome.setOnClickListener(this);
        llVehicleOut.setOnClickListener(this);
        llVehicleIn.setOnClickListener(this);

        llHomeBG.setBackground(getResources().getDrawable(R.drawable.circle_layout_selected));
        tvHome.setTextColor(Color.parseColor("#ffffff"));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, new HomeFragment(), "Exit");
        transaction.commit();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.llHomeMenu_Home) {

            setTitle("Home");

            llHomeBG.setBackground(getResources().getDrawable(R.drawable.circle_layout_selected));
            tvAddRoute.setTextColor(getResources().getColor(R.color.colorWhite));

            llAddRouteBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
            tvAddRoute.setTextColor(getResources().getColor(R.color.menu_text_color));

            llVehicleOutBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
            tvVehicleOut.setTextColor(getResources().getColor(R.color.menu_text_color));

            llVehicleInBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
            tvVehicleIn.setTextColor(getResources().getColor(R.color.menu_text_color));

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, new HomeFragment(), "Exit");
            transaction.commit();

        } else if (view.getId() == R.id.llHomeMenu_AddRoute) {

            setTitle("Add Route");

            llHomeBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
            tvAddRoute.setTextColor(getResources().getColor(R.color.menu_text_color));

            llAddRouteBG.setBackground(getResources().getDrawable(R.drawable.circle_layout_selected));
            tvAddRoute.setTextColor(getResources().getColor(R.color.colorWhite));

            llVehicleOutBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
            tvVehicleOut.setTextColor(getResources().getColor(R.color.menu_text_color));

            llVehicleInBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
            tvVehicleIn.setTextColor(getResources().getColor(R.color.menu_text_color));

            loadFragment(new AddRouteFragment());

        } else if (view.getId() == R.id.llHomeMenu_VehicleOut) {

            setTitle("Vehicle Out");

            llHomeBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
            tvAddRoute.setTextColor(getResources().getColor(R.color.menu_text_color));

            llAddRouteBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
            tvAddRoute.setTextColor(getResources().getColor(R.color.menu_text_color));

            llVehicleOutBG.setBackground(getResources().getDrawable(R.drawable.circle_layout_selected));
            tvVehicleOut.setTextColor(getResources().getColor(R.color.colorWhite));

            llVehicleInBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
            tvVehicleIn.setTextColor(getResources().getColor(R.color.menu_text_color));

            loadFragment(new VehicleListFragment());

        } else if (view.getId() == R.id.llHomeMenu_VehicleIn) {

            setTitle("Vehicle In");

            llHomeBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
            tvAddRoute.setTextColor(getResources().getColor(R.color.menu_text_color));

            llAddRouteBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
            tvAddRoute.setTextColor(getResources().getColor(R.color.menu_text_color));

            llVehicleOutBG.setBackground(getResources().getDrawable(R.drawable.circle_layout));
            tvVehicleOut.setTextColor(getResources().getColor(R.color.menu_text_color));

            llVehicleInBG.setBackground(getResources().getDrawable(R.drawable.circle_layout_selected));
            tvVehicleIn.setTextColor(getResources().getColor(R.color.colorWhite));

            loadFragment(new VehicleInListFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, "Home");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment exit = getSupportFragmentManager().findFragmentByTag("Exit");
        Fragment home = getSupportFragmentManager().findFragmentByTag("Home");
        Fragment addRouteFragment = getSupportFragmentManager().findFragmentByTag("AddRouteFragment");

        if (exit instanceof HomeFragment && exit.isVisible()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
            //builder.setTitle("Confirm Action");
            builder.setMessage("Exit Application ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
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
        } else if (home instanceof AddRouteFragment && home.isVisible() ||
                home instanceof FranchiseFragment && home.isVisible() ||
                home instanceof VehicleListFragment && home.isVisible() ||
                home instanceof VehicleInListFragment && home.isVisible()) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, new HomeFragment(), "Exit");
            transaction.commit();

        } else if (addRouteFragment instanceof FranchiseFragment && addRouteFragment.isVisible()) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, new AddRouteFragment(), "Home");
            transaction.commit();

        } else {
            super.onBackPressed();
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
        MenuItem item = menu.findItem(R.id.action_logout);
        item.setVisible(true);

        MenuItem itemReports = menu.findItem(R.id.action_reports);
        itemReports.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
                builder.setTitle("Logout");
                builder.setMessage("Do You Want To Logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();

                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
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

                return true;


            case R.id.action_reports:

                View vItem = findViewById(R.id.action_reports);

                PopupMenu popupMenu = new PopupMenu(HomeActivity.this, vItem);
                popupMenu.getMenuInflater().inflate(R.menu.report_sub_menu, popupMenu.getMenu());

                SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
                int isAdmin=pref.getInt("isAdmin",0);

                Log.e("MENU"," --------> "+popupMenu.getMenu().getItem(0));

                if (isAdmin==1){

                    popupMenu.getMenu().getItem(0).setVisible(true);
                }else{
                    popupMenu.getMenu().getItem(0).setVisible(false);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu_report) {
                            startActivity(new Intent(HomeActivity.this, FranchiseWiseReportActivity.class));
                        } else if (menuItem.getItemId() == R.id.menu_update) {
                            startActivity(new Intent(HomeActivity.this, FranchiseActivity.class));
                        }
                        return true;
                    }
                });
                popupMenu.show();

            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
