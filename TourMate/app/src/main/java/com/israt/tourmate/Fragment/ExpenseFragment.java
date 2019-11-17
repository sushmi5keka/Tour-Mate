package com.israt.tourmate.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.israt.tourmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {


    private Context context;
    private FloatingActionButton addfab;

    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        context = container.getContext();

        addfab = view.findViewById(R.id.addFab);

        addfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view1 = getLayoutInflater().inflate(R.layout.add_expence_alertdialog, null);

                final EditText expenseNameEt = view.findViewById(R.id.expenseNameEt);
                final EditText expenseAmount = view.findViewById(R.id.expenseAmountEt);
                Button addBtn = view1.findViewById(R.id.addBtn);
                Button cancleBtn = view1.findViewById(R.id.cancelBtn);

                builder.setView(view1);

                final AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);

//                addBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                             ///add methodes///
////                        alertDialog.dismiss();
//
//                    }
//                });

                cancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });


        return view;
    }

}
