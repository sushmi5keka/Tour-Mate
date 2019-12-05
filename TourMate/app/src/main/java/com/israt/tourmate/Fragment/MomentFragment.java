package com.israt.tourmate.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.israt.tourmate.Adapter.MomentAdapter;
import com.israt.tourmate.PojoClass.Moment;
import com.israt.tourmate.PojoClass.StaticData;
import com.israt.tourmate.PojoClass.Trip;
import com.israt.tourmate.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MomentFragment extends Fragment {

    private Context context;
    private FloatingActionButton addMomentfab;

    private static final int PERMISSION_CODE = 788;
    private static final int REQUEST_TAKE_PHOTO = 10 ;

    private Uri uri;
    private ProgressDialog progressDialog;
    private boolean isPermissionGranated =false;
    private String currentPhotoPath;

    private RecyclerView recyclerView;
    private MomentAdapter adapter;
    private DatabaseReference momentRef;
    private List<Moment> momentList =new ArrayList<>();

    private AlertDialog alertDialog;

    private FirebaseAuth firebaseAuth;

    public MomentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moment, container, false);
        context = container.getContext();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getActivity());
        addMomentfab = view.findViewById(R.id.addMementFab);

        addMomentfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view1 = getLayoutInflater().inflate(R.layout.add_moment_alartdialog, null);

                ImageView cameraIv = view1.findViewById(R.id.cameraIv);
                ImageView galleryIv = view1.findViewById(R.id.galleryIv);


                builder.setView(view1);

                alertDialog = builder.create();

//                final AlertDialog alertDialog = builder.create();
//                alertDialog.setCanceledOnTouchOutside(false);

                cameraIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        openCamera();

                    }
                });

                galleryIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        getImageFromGallery();

                    }
                });

                alertDialog.show();

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle=getArguments();
        final String  tripId = bundle.getString("tripId");
        String userId = firebaseAuth.getCurrentUser().getUid();
        momentRef = FirebaseDatabase.getInstance()
                .getReference()
//                .child("Trips")
//                .child(StaticData.tripsID)

                .child("user")
                .child(userId)
                .child("Trips")
                .child(tripId)
                .child("Moments");
        momentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                momentList.clear();
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Moment moment = d.getValue(Moment.class);
                    momentList.add(moment);
                }
                recyclerView =view.findViewById(R.id.momentRecyclerView);
                adapter = new MomentAdapter(getActivity(), momentList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void openCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.israt.tourmate",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void getImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 0);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 0) {
                uri = data.getData();
                firebaseStorageUpload(uri);
            }else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                //setPic();
                Uri fileUri = Uri.fromFile(new File(currentPhotoPath));

                firebaseStorageUpload(fileUri);
            }
//            } else if (requestCode == 1) {
//                Bundle bundle = data.getExtras();
//                Bitmap bitmap = (Bitmap) bundle.get("data");
//                imageIv.setImageBitmap(bitmap);
//            }
        }

    }

    private void firebaseStorageUpload(Uri imgUir){


        progressDialog.setMessage("Uploading..");
        progressDialog.show();
        final Bitmap mainBitmap;
        Bitmap outbitmap = null;

        try {
            mainBitmap =
                    MediaStore
                            .Images
                            .Media
                            .getBitmap(getActivity().getContentResolver(),imgUir);
            outbitmap = Bitmap.createScaledBitmap(mainBitmap,
                    (int) ( mainBitmap.getWidth()*0.3),
                    (int)( mainBitmap.getHeight()*0.3),
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        outbitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] data = baos.toByteArray();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference imgRef =
                storageReference.child("moment").child(imgUir.getLastPathSegment());
        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return imgRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                progressDialog.dismiss();
//                String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                String userId = firebaseAuth.getCurrentUser().getUid();
                Bundle bundle=getArguments();
                final String  tripId = bundle.getString("tripId");
                DatabaseReference ref  = FirebaseDatabase
                        .getInstance()
                        .getReference()
//                        .child("Trips")
//                        .child(StaticData.tripsID)
                        .child("user")
                        .child(userId)
                        .child("Trips")
                        .child(tripId)
                        .child("Moments");
                DatabaseReference keyref= ref.push();
                String key = keyref.getKey();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                String date = dateFormat.format(new Date());
                Moment moment = new Moment(key,date,task.getResult().toString());
                keyref.setValue(moment);
                alertDialog.dismiss();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        alertDialog.dismiss();
                    }
                });


    }

    public void checkStoragePermission(){
        if(ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE);
        }else {
            isPermissionGranated =true;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] !=PackageManager.PERMISSION_GRANTED
                && grantResults[1] != PackageManager.PERMISSION_GRANTED){
            isPermissionGranated = false;
            checkStoragePermission();
        }else {
            isPermissionGranated =true;
        }
    }


}
