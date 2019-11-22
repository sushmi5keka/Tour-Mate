package com.israt.tourmate.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.israt.tourmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MomentFragment extends Fragment {

    private Context context;
    private FloatingActionButton addMomentfab;

    public MomentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moment, container, false);
        context = container.getContext();

        addMomentfab = view.findViewById(R.id.addMementFab);

        addMomentfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view1 = getLayoutInflater().inflate(R.layout.add_moment_alartdialog, null);

                ImageView cameraIv = view1.findViewById(R.id.cameraIv);
                ImageView galleryIv = view1.findViewById(R.id.galleryIv);


                builder.setView(view1);

                final AlertDialog alertDialog = builder.create();
//                alertDialog.setCanceledOnTouchOutside(false);

                cameraIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                galleryIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                alertDialog.show();

            }
        });

        return view;
    }

}
