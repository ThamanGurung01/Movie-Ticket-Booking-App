package com.app.movieticketbookingapp.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.app.movieticketbookingapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_frame, new MoviesFragment())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_movies) {
                selectedFragment = new MoviesFragment();
            } else if (item.getItemId() == R.id.nav_user) {
                selectedFragment = new UserFragment();
            } else if (item.getItemId() == R.id.nav_bookings) {
                selectedFragment = new BookingsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_frame, selectedFragment)
                        .commit();
            }

            return true;
        });

    }
}