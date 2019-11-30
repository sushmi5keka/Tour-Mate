package com.israt.tourmate.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.israt.tourmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationDetailsFragment extends Fragment {


    private TextView nameTv,locationTv,budgetTv,startTv,endTv;

    public InformationDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_information_details, container, false);

        nameTv=view.findViewById(R.id.tripNameTv);
        locationTv=view.findViewById(R.id.tripLocationTv);
//        budgetTv=view.findViewById(R.id.tripBudgetEt);
        startTv=view.findViewById(R.id.tripStartDateTv);
        endTv=view.findViewById(R.id.tripEndDateTv);


        Bundle args = getArguments();


        String tname = getArguments().getString("name");
        String tlocatiom = getArguments().getString("location");
//        String tbudget = getArguments().getString("budget");
        String tstart = getArguments().getString("startD");
        String tend = getArguments().getString("endD");

        nameTv.setText(tname);
        locationTv.setText(tlocatiom);
//        budgetTv.setText(tbudget);
        startTv.setText(tstart);
        endTv.setText(tend);


        return view;
    }


}
