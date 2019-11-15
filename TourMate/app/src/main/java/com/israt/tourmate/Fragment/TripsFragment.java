package com.israt.tourmate.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.israt.tourmate.Activity.TripsActivity;
import com.israt.tourmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TripsFragment extends Fragment {

    private LinearLayout addTourLL;
    private Context context;

    public TripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trips, container, false);
        context = container.getContext();

        addTourLL = view.findViewById(R.id.addTourLL);

        addTourLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TripsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
