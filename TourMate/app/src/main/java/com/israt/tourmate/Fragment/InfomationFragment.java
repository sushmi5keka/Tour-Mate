package com.israt.tourmate.Fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.israt.tourmate.Activity.TripsActivity;
import com.israt.tourmate.Adapter.TripAdapter;
import com.israt.tourmate.PojoClass.Trip;
import com.israt.tourmate.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfomationFragment extends Fragment {

    private EditText nameEt,locationEt,budgetEt;
    private Button insertBtn,updateBtn,startEt,endEt;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String finalID,tripname,location,budget,startDate,endDate;
    private Context context;
    public InfomationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_infomation, container, false);
        context =container.getContext();

        nameEt=view.findViewById(R.id.tripNameEt);
        locationEt=view.findViewById(R.id.tripLocationEt);
        budgetEt=view.findViewById(R.id.tripBudgetEt);
        startEt=view.findViewById(R.id.tripStartDateBtn);
        endEt=view.findViewById(R.id.tripEndDateBtn);
        insertBtn=view.findViewById(R.id.saveBtn);
        updateBtn=view.findViewById(R.id.updateBtn);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripname=nameEt.getText().toString();
                if (tripname.matches("")) {
                    Toast.makeText(getActivity(), "Please Enter Tripname", Toast.LENGTH_SHORT).show();
                    return;
                }
                location=locationEt.getText().toString();
                if (location.matches("")) {
                    Toast.makeText(getActivity(), "Please Enter Location", Toast.LENGTH_SHORT).show();
                    return;
                }
                budget=budgetEt.getText().toString();
                if (budget.matches("")) {
                    Toast.makeText(getActivity(), "Please Enter Budget", Toast.LENGTH_SHORT).show();
                    return;
                }


                startDate=startEt.getText().toString();
                if (startDate.matches("")) {
                    Toast.makeText(getActivity(), "Please Enter Start Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                endDate=endEt.getText().toString();
                if (endDate.matches("")) {
                    Toast.makeText(getActivity(), "Please Enter End Date", Toast.LENGTH_SHORT).show();
                    return;
                }



                addInDB(tripname,location,budget,startDate,endDate);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripname=nameEt.getText().toString();
                location=locationEt.getText().toString();
                budget=budgetEt.getText().toString();
                startDate=startEt.getText().toString();
                endDate=endEt.getText().toString();

                updateDB(tripname,location,budget,startDate,endDate);
            }
        });

        startDate();
        endDate();

        // Trip update process

        Bundle bundle=getArguments();
        if(bundle!=null){
            insertBtn.setVisibility(View.INVISIBLE);
            updateBtn.setVisibility(View.VISIBLE);

            String  updateId=bundle.getString("id");
            String  uname=bundle.getString("name");
            String  ulocation=bundle.getString("location");
            String  ubudget=bundle.getString("budget");
            String  ustart=bundle.getString("start");
            String  uend=bundle.getString("end");


            nameEt.setText(uname);
            locationEt.setText(ulocation);
            budgetEt.setText(ubudget);
            startEt.setText(ustart);
            endEt.setText(uend);
            finalID=String.valueOf(updateId);
        }

        return view;

    }

    private void addInDB(final String tripname, final String location, final String budget, final String startDate, final String endDate) {

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String userId = currentUser.getUid();
        DatabaseReference tripReference = databaseReference.child("user").child(userId).child("Trips");
        String tripId = tripReference.push().getKey();


        Trip trip= new Trip(tripId,tripname,location,budget,startDate,endDate);
        tripReference.child(tripId).setValue(trip).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    TripsFragment tFragment=new TripsFragment();
                    FragmentManager manager=getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.fragmentView,tFragment);
                    ft.addToBackStack(null);
                    ft.commit();

                    Toast.makeText(getActivity(), "Data Added", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDB(String tripname, String location, String budget, String startDate, String endDate) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String userId = currentUser.getUid();
        DatabaseReference tripReference = databaseReference.child("user").child(userId).child("Trips");



        Trip trip= new Trip(finalID,tripname,location,budget,startDate,endDate);
        tripReference.child(finalID).setValue(trip).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    TripsFragment tFragment=new TripsFragment();
                    FragmentManager manager=getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.fragmentView,tFragment);
                    ft.addToBackStack(null);
                    ft.commit();

                    Toast.makeText(getActivity(), "Data Updated", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void endDate() {
        endEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }


            private void openDatePicker() {
                DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month= month+1;
                        String currentDate = year+"/"+month+"/"+ day;
                        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy/MM/dd");
                        Date date=null;
                        try {
                            date= dateFormat.parse(currentDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        endEt.setText(dateFormat.format(date));
                        long dateInmillis = date.getTime();


                    }
                };
                Calendar calendar=Calendar.getInstance();
                int year= calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int day= calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), dateSetListener,year,month,day);
                datePickerDialog.show();
            }


        });

    }

    private void startDate() {

        startEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }


            private void openDatePicker() {
                DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month= month+1;
                        String currentDate = year+"/"+month+"/"+ day;
                        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy/MM/dd");
                        Date date=null;
                        try {
                            date= dateFormat.parse(currentDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        startEt.setText(dateFormat.format(date));
                        long dateInmillis = date.getTime();


                    }
                };
                Calendar calendar=Calendar.getInstance();
                int year= calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int day= calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), dateSetListener,year,month,day);
                datePickerDialog.show();
            }


        });

    }






}
