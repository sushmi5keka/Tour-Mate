package com.israt.tourmate.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.israt.tourmate.Fragment.DashboardFragment;
import com.israt.tourmate.Fragment.MomentFragment;
import com.israt.tourmate.Fragment.ProfileFragment;
import com.israt.tourmate.Fragment.TripsFragment;
import com.israt.tourmate.Fragment.UserProfileFragment;
import com.israt.tourmate.MapAction.MapsActivity;
import com.israt.tourmate.R;
import com.israt.tourmate.WeatherAction.WeatherActivity;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Dashboard");

        init();

        fragment(new TripsFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.navBar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

//                    case R.id.dashboard:
//                        fragment(new DashboardFragment());
//                        setTitle("Dashboard");
//                        return true;
                    case R.id.trips:
                        fragment(new TripsFragment());
                        setTitle("Trip");
                        return true;
                    case R.id.profile:
                        fragment(new ProfileFragment());
                        setTitle("User Profile");
                        return true;
                }

                return false;
            }
        });
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Fragment fragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentView, fragment);
        ft.commit();
        return fragment;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mf = getMenuInflater();
        mf.inflate(R.menu.top_right_manu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nearBy:
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                break;
            case R.id.weather:
                startActivity(new Intent(MainActivity.this, WeatherActivity.class));
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.exit:
                firebaseAuth.signOut();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

