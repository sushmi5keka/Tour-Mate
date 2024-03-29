package com.israt.tourmate.Fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.israt.tourmate.Activity.MainActivity;
import com.israt.tourmate.PojoClass.User;
import com.israt.tourmate.R;
import com.israt.tourmate.databinding.FragmentUserProfileEditBinding;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileEditFragment extends Fragment {


    private static final int GALLERY_REQUEST_CODE = 848;
    private static final int PERMISSION_CODE = 8972;
//    private AuthenticationListiner_ authenticationListiner;
    private FragmentUserProfileEditBinding binding;
    private boolean isPermistionGranted;
    private ProgressDialog dialog;
    private DatabaseReference userRef;
    private StorageReference storageReference;
    private boolean isLocalUri = false;
    private User userDetails = null;
    private boolean isupdate = false;
    private String updateLinkPhoto ="";



    private Uri ImagUrl_main;


    public UserProfileEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_profile_edit, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        authenticationListiner = (AuthenticationListiner_) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = new ProgressDialog(getActivity());
        checkPermistion();
        userRef = FirebaseDatabase.getInstance().getReference().child("User");
        storageReference = FirebaseStorage.getInstance().getReference();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            String name = user.getDisplayName();
            String email = user.getEmail();
//            String phone = user.getPhoneNumber();
            Uri photoUrl = user.getPhotoUrl();
            String uid = user.getUid();
            binding.fristNameEdt.setText(name);
//            binding.lastNameEdt.setText(email);
            binding.emailEdt.setText(email);
            if(photoUrl !=null) {
                Picasso.get().load(photoUrl).into(binding.addimagebtn);
            }
            ImagUrl_main = photoUrl;
            // binding.phoneET.setText(photoUrl.toString());

        }
        binding.addimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermistion();
                if(isPermistionGranted){
                    openGallery();
                }else {
                    Toast.makeText(getActivity(), "Please Allow Permission", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNmae = binding.fristNameEdt.getText().toString();
                String userNmae2 = binding.lastNameEdt.getText().toString();
                String email = binding.emailEdt.getText().toString();
//                String phone = binding.phoneET.getText().toString();
                if(userNmae.isEmpty()){
                    binding.fristNameEdt.setError(getString(R.string.required_field));
                }
                if(email.isEmpty()){
                    binding.emailEdt.setError(getString(R.string.required_field));
                }

                if(!userNmae.isEmpty() && !email.isEmpty() && (ImagUrl_main !=null || !updateLinkPhoto.isEmpty())){
                    setUserInfo(userNmae,email,ImagUrl_main);
                }else {
                    Toast.makeText(getActivity(), "Image not set", Toast.LENGTH_SHORT).show();
                }
            }
        });
        updateUserInformatipon();
    }

    private void updateUserInformatipon(){
        final String uid =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(uid !=null){
            if(!uid.isEmpty()){
                userRef.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null){
//                            binding.phoneET.setText(user.getPhone());
                            binding.emailEdt.setText(user.getEmail());
                            binding.fristNameEdt.setText(user.getFirstName());
                            binding.lastNameEdt.setText(user.getLastName());
                            Picasso.get().load(user.getPicture()).into(binding.addimagebtn);

                            updateLinkPhoto = user.getPicture();
                            isupdate = true;
                            // Toast.makeText(getActivity(), user.getImgURL(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

    }

    private void checkPermistion() {
        if ((ActivityCompat
                .checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) &&
                (ActivityCompat
                        .checkSelfPermission(getActivity()
                                ,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },PERMISSION_CODE);

        }else {
            isPermistionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==PERMISSION_CODE){

            if((grantResults[0] ==PackageManager.PERMISSION_GRANTED
                    && grantResults[1] ==PackageManager.PERMISSION_GRANTED
            )){
                isPermistionGranted = true;
            }else {
                checkPermistion();
            }
        }

    }

    private void setUserInfo(final String userNmae,final String email, Uri imageUri){
        dialog.setMessage("Uploading..");
        dialog.show();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(isLocalUri && !isupdate) {
            final StorageReference imgRef = storageReference.child("UserImg").child(imageUri.getLastPathSegment());

            UploadTask uploadTask = imgRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imgRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    Uri downloadUri = task.getResult();
                    DatabaseReference userRef =
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("User");
                    DatabaseReference userpush = userRef.child(user.getUid());
                    String key = userpush.getKey();
                    User userm = new User(
                            userNmae,
                            userNmae,
                            email,
                            downloadUri.toString(),
                            key
                    );
                    userpush.setValue(userm).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                dialog.dismiss();




                            } else {
                                Toast.makeText(getActivity(), "Database Not upload", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });

                }
            });
        }else if(isupdate){
            DatabaseReference userRef =
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("User");
            DatabaseReference userpush = userRef.child(user.getUid());
            String key = userpush.getKey();
            User userm = new User(
                    userNmae,
                    userNmae,
                    email,
                    updateLinkPhoto,
                    key

            );
            userpush.setValue(userm).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        dialog.dismiss();


                    } else {
                        Toast.makeText(getActivity(), "Database Not upload", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });

        }else {
            //user.getPhotoUrl().toString()

            DatabaseReference userRef =
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("User");
            DatabaseReference userpush = userRef.child(user.getUid());
            String key = userpush.getKey();
            User userm = new User(
                    userNmae,
                    userNmae,
                    email,
                    user.getPhotoUrl().toString(),
                    key

            );
            userpush.setValue(userm).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        dialog.dismiss();


                    } else {
                        Toast.makeText(getActivity(), "Database Not upload", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });

        }

    }
    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE){
            ImagUrl_main = data.getData();
            binding.addimagebtn.setImageURI(ImagUrl_main);
            isLocalUri = true;
            isupdate = false;
        }
    }

}
