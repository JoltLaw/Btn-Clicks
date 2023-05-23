package com.example.btnclicks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText userEmail;
    EditText userPassword;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEmail = findViewById(R.id.editTextTextEmailAddress);
        userPassword = findViewById(R.id.editTextTextPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    public void logIn(View view) {



        if (userEmail.getText().toString().trim().length() == 0 || userEmail.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please Fill In All Fields", Toast.LENGTH_SHORT).show();
        } else {

            mAuth.signInWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent btnIntent = new Intent(getApplicationContext(), btnclicks.class);
                                btnIntent.putExtra("email", userEmail.getText().toString());
                                startActivity(btnIntent);
                            } else {
                                mAuth.createUserWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    Intent btnIntent = new Intent(getApplicationContext(), btnclicks.class);
                                                    btnIntent.putExtra("email", userEmail.getText().toString());
                                                    startActivity(btnIntent);
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Login and Sign up failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });
                 }
               }


}