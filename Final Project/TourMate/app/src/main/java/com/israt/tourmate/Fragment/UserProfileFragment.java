package com.israt.tourmate.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.israt.tourmate.Activity.MainActivity;
import com.israt.tourmate.PojoClass.User;
import com.israt.tourmate.R;
import com.israt.tourmate.databinding.FragmentUserProfileBinding;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;

    private DatabaseReference databaseReference;
    private DatabaseReference userRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private User userDetails;

    private ProgressDialog dialog;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialog = new ProgressDialog(getActivity());

        databaseReference = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        userRef = databaseReference.child("User").child(user.getUid());

        firebaseUserget();

        binding.profileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), UserProfileFragment.class);
                startActivity(intent);

            }
        });

    }

    private void firebaseUserget() {
        dialog.setMessage("Loading..");
        dialog.show();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDetails = dataSnapshot.getValue(User.class);

                if (userDetails != null){
                    binding.fristNameTv.setText(userDetails.getFirstName());
                    binding.lastNameTv.setText(userDetails.getLastName());
                    binding.emailTv.setText(userDetails.getEmail());
                    Picasso.get().load(userDetails.getPicture()).into(binding.userIV);
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });





    }

}
