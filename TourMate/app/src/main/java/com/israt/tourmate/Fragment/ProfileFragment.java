package com.israt.tourmate.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.israt.tourmate.PojoClass.User;
import com.israt.tourmate.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String userId;
    private StorageReference storageReference;
    private ImageView userIv;
    private TextView firstNameTv, lastname, emailTv;

    private ProgressDialog dialog;

    private User userDetails;




    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        init(view);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userId = currentUser.getUid();

        getDataFromfirebase();


        return view;
    }

    private void init(View view) {
        userIv = view.findViewById(R.id.userIV);
        firstNameTv = view.findViewById(R.id.fristNameTv);
        lastname = view.findViewById(R.id.lastNameTv);
        emailTv = view.findViewById(R.id.emailTv);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

    }


    private void getDataFromfirebase() {
        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("user").child(userId);

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    //for(DataSnapshot data: dataSnapshot.getChildren()) {

                    User user = dataSnapshot.getValue(User.class);
                    // Picasso.get().load(user.getPicture()).into(userIv);
                    firstNameTv.setText(user.getFirstName());
                    lastname.setText(user.getLastName());
                    emailTv.setText(user.getEmail());
                    // }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }




}
