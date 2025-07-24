package com.app.movieticketbookingapp.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import com.app.movieticketbookingapp.R;
import com.app.movieticketbookingapp.component.TopMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserDashboard extends TopMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_dashboard);

        Toolbar toolbar = findViewById(R.id.user_dashboard_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_user_dashboard);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.user_container_frame, new BookingFragment())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_booking) {
                selectedFragment = new BookingFragment();
            } else if (item.getItemId() == R.id.nav_current_booked) {
                selectedFragment = new CurrentBookedFragment();
            } else if (item.getItemId() == R.id.nav_history) {
                selectedFragment = new HistoryFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.user_container_frame, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}
