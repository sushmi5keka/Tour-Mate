package com.israt.tourmate.Fragment;


import android.app.AlertDialog;
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
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.israt.tourmate.Adapter.ExpenseAdapter;
import com.israt.tourmate.PojoClass.Expense;
import com.israt.tourmate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private List<Expense> expenseList= new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Context context;
    String tripId;
    private FloatingActionButton addfab;

    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);


        Bundle bundle=getArguments();
        final String  tripId = bundle.getString("tripId");

        addfab = view.findViewById(R.id.addFab);

        addfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ExpenseAddFragment expenseAddFragment = new ExpenseAddFragment(tripId);
                FragmentManager manager=getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.fragmentView,expenseAddFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


        context = container.getContext();

        recyclerView = view.findViewById(R.id.recyclearViewExpenseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ExpenseAdapter(expenseList, getActivity());
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();

        final DatabaseReference expenseInfo = databaseReference.child("user").child(userId).child("Trips").child(tripId).child("Expense");

        expenseInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    expenseList.clear();
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        Expense expense = data.getValue(Expense.class);

                        expenseList.add(expense);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
