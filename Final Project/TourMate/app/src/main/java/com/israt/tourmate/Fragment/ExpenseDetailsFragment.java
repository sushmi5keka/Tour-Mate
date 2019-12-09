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
public class ExpenseDetailsFragment extends Fragment {

    private TextView nameTv,amountTv;

    public ExpenseDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_expense_details, container, false);

        nameTv=view.findViewById(R.id.exNameTv);
        amountTv=view.findViewById(R.id.exAmountTv);

        Bundle args = getArguments();

        String ename = getArguments().getString("name");
        String eamount = getArguments().getString("amount");


        nameTv.setText(ename);
        amountTv.setText(eamount);



        return view;
    }

}
