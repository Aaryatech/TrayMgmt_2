package com.ats.traymanagement.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ats.traymanagement.R;
import com.ats.traymanagement.fragment.AddRouteFragment;
import com.ats.traymanagement.fragment.FranchiseFragment;

public class BottomNavigationActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    ActionBar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_add_route:
                    toolbar.setTitle(R.string.title_add_route);
                    fragment = new AddRouteFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_franchise:
                    toolbar.setTitle(R.string.title_franchise);
                    fragment = new FranchiseFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_vehicle_out:
                    toolbar.setTitle(R.string.title_vehicle_out);
                    return true;
                case R.id.navigation_vehicle_in:
                    toolbar.setTitle(R.string.title_vehicle_in);
                    return true;
                case R.id.navigation_reports:
                    toolbar.setTitle(R.string.title_reports);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Add Route");

        loadFragment(new AddRouteFragment());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        frameLayout = findViewById(R.id.frame_container);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
