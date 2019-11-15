package com.israt.tourmate.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.israt.tourmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfomationFragment extends Fragment {


    public InfomationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_infomation, container, false);
    }

}
