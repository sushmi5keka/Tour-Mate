package com.israt.tourmate.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.israt.tourmate.Fragment.ExpenseAddFragment;
import com.israt.tourmate.Fragment.ExpenseDetailsFragment;
import com.israt.tourmate.PojoClass.Expense;
import com.israt.tourmate.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private List<Expense> expenses;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public ExpenseAdapter(List<Expense> expenses, Context context) {
        this.expenses = expenses;
        this.context = context;
    }

    private View getView;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_model_design,parent,false);
       getView= view;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     final Expense expense = expenses.get(position);



        final String tripId = expense.getTripId();
        final String expanseId = expense.getExpenseId();

        holder.ExnameTv.setText(expense.getExpenseName());
        holder.ExamountTv.setText(expense.getExpenseAmount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();


                bundle.putString("name", expense.getExpenseName());
                bundle.putString("amount", expense.getExpenseAmount());
               ExpenseDetailsFragment detailsFragment = new ExpenseDetailsFragment();
                detailsFragment.setArguments(bundle);


                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView, detailsFragment)
                        .addToBackStack(null).commit();


            }
        });

        holder.ExdeleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure to delete ?");
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_delete_forever_black_24dp);

                //when click yes
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth firebaseAuth;
                        DatabaseReference databaseReference;
                        firebaseAuth = FirebaseAuth.getInstance();
                        databaseReference = FirebaseDatabase.getInstance().getReference();

                        String expenseId = expense.getExpenseId();
                        String userId = firebaseAuth.getCurrentUser().getUid();
                        DatabaseReference deleteRef = databaseReference.child("user").child(userId).child("Trips").child(tripId).child("Expense").child(expanseId);
                        notifyDataSetChanged();
                        dialog.cancel();
                        deleteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                });

                //when click no
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.create();
                builder.show();
            }
        });

        holder.ExupdateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestUpdate(expense);
            }
        });
    }

    private void requestUpdate(Expense expense) {

        ExpenseAddFragment addTripExpenseBottomSheet = new ExpenseAddFragment();
        Bundle bundle=new Bundle();
        bundle.putString("exId",expense.getExpenseId());
        bundle.putString("tripId",expense.getTripId());
        bundle.putString("exName",expense.getExpenseName());
        bundle.putString("exAmount",expense.getExpenseAmount());

        addTripExpenseBottomSheet.setArguments(bundle);

        AppCompatActivity activity=(AppCompatActivity) getView.getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView,addTripExpenseBottomSheet).addToBackStack(null).commit();


    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ExnameTv,ExamountTv;
        private ImageView ExupdateIV,ExdeleteIv;
        private FirebaseAuth firebaseAuth;
        private DatabaseReference databaseReference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ExnameTv=itemView.findViewById(R.id.expenseNameTv);
            ExamountTv=itemView.findViewById(R.id.expenseAmountTv);
            ExupdateIV=itemView.findViewById(R.id.exUpdateIv);
            ExdeleteIv=itemView.findViewById(R.id.exDeleteIv);
            firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            String userId = currentUser.getUid();

        }
    }
}
