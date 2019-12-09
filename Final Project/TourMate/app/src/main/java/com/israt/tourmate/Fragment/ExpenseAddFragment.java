package com.israt.tourmate.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.israt.tourmate.PojoClass.Expense;
import com.israt.tourmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseAddFragment extends Fragment {

    private EditText exNameET,amountET;
    private Button saveBTN,updateBTN;
    private String exName,exAmount;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String tripId,userId,expenseId;
    private  String ExName,ExAmount;

    public ExpenseAddFragment(String tripId) {
        this.tripId = tripId;
    }

    public ExpenseAddFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_add, container, false);

        init(view);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userId = currentUser.getUid();

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exName=exNameET.getText().toString();

                if (exName.matches("")) {
                    Toast.makeText(getActivity(), "Please Enter Expense Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                exAmount=amountET.getText().toString();
                if (exAmount.matches("")) {
                    Toast.makeText(getActivity(), "Please Enter Expense Amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                getData(exName,exAmount);

            }
        });

        updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExName=exNameET.getText().toString();
                ExAmount=amountET.getText().toString();


                updateDB(ExName,ExAmount);
            }
        });


        // Trip update process

        Bundle bundle=getArguments();
        if(bundle!=null){
            TextView ad=view.findViewById(R.id.addTv);
            TextView update=view.findViewById(R.id.updateTv);
            ad.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);
            saveBTN.setVisibility(View.GONE);
            updateBTN.setVisibility(View.VISIBLE);

            tripId=bundle.getString("tripId");
            expenseId=bundle.getString("exId");
            String  upname=bundle.getString("exName");
            String  upamount=bundle.getString("exAmount");

            exNameET.setText(upname);
            amountET.setText(upamount);




        }


        return view;


    }

    private void updateDB(String exName, String exAmount) {
        DatabaseReference expenseData = databaseReference.child("user").child(userId).child("Trips").child(tripId);

        Expense expense= new Expense(tripId,expenseId,exName,exAmount);

        expenseData.child("Expense").child(expenseId).setValue(expense).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    exNameET.setText("");
                    amountET.setText("");
//                    ExpenseFragment upFragment=new ExpenseFragment();
//                    FragmentManager manager=getActivity().getSupportFragmentManager();
//                    FragmentTransaction ft = manager.beginTransaction();
//                    ft.replace(R.id.fragmentView,upFragment);
//                    ft.addToBackStack(null);
//                    ft.commit();

                    Toast.makeText(getContext(), "Successfully Update Expense", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getData(String exName,String exAmount) {

        DatabaseReference expenseData = databaseReference.child("user").child(userId).child("Trips").child(tripId);
        String expenseId = expenseData.push().getKey();
        Expense expense= new Expense(tripId,expenseId,exName,exAmount);

        expenseData.child("Expense").child(expenseId).setValue(expense).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    exNameET.setText("");
                    amountET.setText("");

//                    ExpenseFragment expenseFragment=new ExpenseFragment();
//                    FragmentManager manager=getActivity().getSupportFragmentManager();
//                    FragmentTransaction ft = manager.beginTransaction();
//                    ft.replace(R.id.fragmentView,expenseFragment);
//                    ft.addToBackStack(null);
//                    ft.commit();

                    Toast.makeText(getContext(), "Successfully Add Expense", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void init(View view) {


        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        exNameET=view.findViewById(R.id.expenseNameEt);
        amountET=view.findViewById(R.id.amountEt);
        saveBTN=view.findViewById(R.id.ExSaveBtn);
        updateBTN=view.findViewById(R.id.ExUpdateBtn);
    }

}
