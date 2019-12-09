package com.israt.tourmate.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.israt.tourmate.Fragment.DashboardFragment;
import com.israt.tourmate.Fragment.ExpenseFragment;
import com.israt.tourmate.Fragment.InfomationFragment;
import com.israt.tourmate.Fragment.MomentFragment;
import com.israt.tourmate.R;

public class TripsActivity extends AppCompatActivity {

    private ImageView informationIv,expenseIv,momentIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        setTitle("Trip");

        init();

        fragment(new InfomationFragment());

        informationIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment(new InfomationFragment());
            }
        });

        expenseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment(new ExpenseFragment());
            }
        });

        momentIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment(new MomentFragment());
            }
        });






    }

    private void init() {

        informationIv = findViewById(R.id.informationIv);
        expenseIv = findViewById(R.id.expenseIv);
        momentIv = findViewById(R.id.momentIv);

    }

    public Fragment fragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentView, fragment);
        ft.commit();
        return fragment;
    }
}
