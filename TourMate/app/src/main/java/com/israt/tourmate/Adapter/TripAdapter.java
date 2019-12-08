package com.israt.tourmate.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.israt.tourmate.Fragment.ExpenseFragment;
import com.israt.tourmate.Fragment.InfomationFragment;
import com.israt.tourmate.Fragment.InformationDetailsFragment;
//import com.israt.tourmate.Fragment.TripInfoFragment;
import com.israt.tourmate.Fragment.MomentFragment;
import com.israt.tourmate.PojoClass.Trip;
import com.israt.tourmate.R;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {
    private List<Trip> trips;
    private Context context;
    private View getView;
    public TripAdapter(List<Trip> trips, Context context) {
        this.trips = trips;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.information_model_design,parent,false);
        getView=view;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Trip trip=trips.get(position);
        holder.nameTv.setText(trip.getName());
//        holder.budgetTv.setText(trip.getBudget());
        holder.startTv.setText(trip.getStartD());
        holder.endTv.setText(trip.getEndD());


        holder.detailsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();


                bundle.putString("name",trip.getName());
                bundle.putString("location", trip.getLocation());
                bundle.putString("budget",trip.getBudget());
                bundle.putString("startD",trip.getStartD() );

                bundle.putString("endD",trip.getEndD());

                InformationDetailsFragment detailsFragment=new InformationDetailsFragment();
                detailsFragment.setArguments(bundle);


                AppCompatActivity activity=(AppCompatActivity) getView.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView,detailsFragment).addToBackStack(null).commit();


            }
        });

        holder.expenseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpenseFragment expenseFragment=new ExpenseFragment();
                Bundle bundle=new Bundle();


                bundle.putString("tripId",trip.getTripId());


                expenseFragment.setArguments(bundle);
                AppCompatActivity  activity=(AppCompatActivity) getView.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView,expenseFragment).addToBackStack(null).commit();


            }
        });

        holder.momentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MomentFragment momentFragment=new MomentFragment();
                Bundle bundle=new Bundle();


                bundle.putString("tripId",trip.getTripId());


                momentFragment.setArguments(bundle);
                AppCompatActivity  activity=(AppCompatActivity) getView.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView,momentFragment).addToBackStack(null).commit();


            }
        });

        holder.tripsDeleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Are you sure to delete ?");
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_delete_forever_black_24dp);

                //when click yes
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth firebaseAuth;
                        DatabaseReference databaseReference;
                        firebaseAuth = FirebaseAuth.getInstance();
                        databaseReference = FirebaseDatabase.getInstance().getReference();

                        String tripId = trip.getTripId();
                        String userId = firebaseAuth.getCurrentUser().getUid();
                        DatabaseReference deleteRef = databaseReference.child("user").child(userId).child("Trips").child(tripId);
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
                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.create();
                builder.show();


            }
        });

        holder.tipsUpdateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestUpdate(trip);
            }
        });

    }

    private void requestUpdate(Trip currentTrip) {



        //pass urgument data and call update fragment from adapter

        InfomationFragment updateTripFragment=new InfomationFragment();
        Bundle bundle=new Bundle();

        bundle.putString("name",currentTrip.getName());
        bundle.putString("id",currentTrip.getTripId());

        bundle.putString("location",currentTrip.getLocation());
        bundle.putString("budget",currentTrip.getBudget());
        bundle.putString("start",currentTrip.getStartD());
        bundle.putString("end",currentTrip.getEndD());
        updateTripFragment.setArguments(bundle);

        AppCompatActivity  activity=(AppCompatActivity) getView.getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView,updateTripFragment).addToBackStack(null).commit();


    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv,budgetTv,startTv,endTv,expenseTv,detailsTv,momentTV;
        private ImageView popupIV;
        private FirebaseAuth firebaseAuth;
        private DatabaseReference databaseReference;
        private  ImageView tripsDeleteIv,tipsUpdateIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           nameTv=itemView.findViewById(R.id.tripNameTv) ;
//           budgetTv=itemView.findViewById(R.id.tripBudgetTv) ;
           startTv=itemView.findViewById(R.id.tripStartDateTv) ;
           endTv=itemView.findViewById(R.id.tripEndDateTv) ;
            expenseTv = itemView.findViewById(R.id.expenseTV);
            detailsTv = itemView.findViewById(R.id.detailsTV);
            momentTV = itemView.findViewById(R.id.momentTV);
//            popupIV=itemView.findViewById(R.id.popupIv);
            firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();

            tripsDeleteIv = itemView.findViewById(R.id.tripDeleteIv);
            tipsUpdateIv= itemView.findViewById(R.id.tripUpdateIv);

        }
    }
}
