package com.israt.tourmate.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.israt.tourmate.Activity.TripsActivity;
import com.israt.tourmate.Adapter.TripAdapter;
import com.israt.tourmate.PojoClass.Trip;
import com.israt.tourmate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TripsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private List<Trip> tripList= new ArrayList<>();


    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


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

        context = container.getContext();

        recyclerView = view.findViewById(R.id.recyclerViewAddTour);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TripAdapter(tripList, getActivity());
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();

        final DatabaseReference tripInfo = databaseReference.child("user").child(userId).child("Trips");

        tripInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tripList.clear();
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        Trip trip = data.getValue(Trip.class);

                        tripList.add(trip);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




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
