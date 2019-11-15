package com.israt.tourmate.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.israt.tourmate.R;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstNameEt, lastNameEt, emailEt, passwordEt, confirmPasswordEt;
    private Button signUpBtn;
    private ProgressBar progressBar;
    private String firstName, lastName, email, password, confirnPassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                firstName = firstNameEt.getText().toString().trim();
                lastName = lastNameEt.getText().toString().trim();
                email = emailEt.getText().toString().trim();
                password = passwordEt.getText().toString().trim();
                confirnPassword = confirmPasswordEt.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || confirnPassword.isEmpty()) {

                    progressBar.setVisibility(View.GONE);

                    if (email.isEmpty()) {
                        emailEt.setError("Enter your email");
                    } else if (password.isEmpty()) {
                        passwordEt.setError("Enter a password minimum 6 degit");
                    } else {
                        confirmPasswordEt.setError("Re-write password");
                    }

                } else {
                    if (password.equals(confirnPassword)) {

                        signUp(firstName, lastName, email, password);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, "Password Doesn't match", Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });
    }

    private void signUp(final String firstName, final String lastName, final String email, final String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    String userId = firebaseAuth.getCurrentUser().getUid();

                    DatabaseReference reference = databaseReference.child("user").child(userId);

                    HashMap<String, Object> userInfo = new HashMap<>();
                    userInfo.put("firstName", firstName);
                    userInfo.put("lastName", lastName);
                    userInfo.put("email", email);
//                    userInfo.put("password", password);

                    reference.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, "1nd" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "2nd" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void init() {

        firstNameEt = findViewById(R.id.firstNameEt);
        lastNameEt = findViewById(R.id.lastNameEt);
        emailEt = findViewById(R.id.emailEt);
        confirmPasswordEt = findViewById(R.id.confirmPasswordSEt);
        passwordEt = findViewById(R.id.passwordSEt);
        signUpBtn = findViewById(R.id.signUpBtn);
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

}
