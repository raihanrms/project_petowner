package com.example.petowner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText registerFullname, registerEmail, registerPassword, registerConfPassword;
    Button registerButton;
    TextView gotoLogin;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerFullname = findViewById(R.id.registerFullname);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfPassword = findViewById(R.id.registerConfPassword);
        registerButton = findViewById(R.id.registerButton);
        gotoLogin = findViewById(R.id.gotoLogin);
        fAuth = FirebaseAuth.getInstance();

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //extract the data from the form

                String fullname = registerFullname.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                String confPass = registerConfPassword.getText().toString();

                if(fullname.isEmpty()){
                    registerFullname.setError("Full name is required");
                    return;
                }
                if(email.isEmpty()){
                    registerEmail.setError("Email is required");
                    return;
                }
                if(password.isEmpty()){
                    registerPassword.setError("Password is required");
                    return;
                }
                if(confPass.isEmpty()){
                    registerPassword.setError("confirm password is required");
                    return;
                }
                if(!password.equals(confPass)){
                    registerConfPassword.setError("Password Do not Match");
                    return;
                }

                Toast.makeText(RegisterActivity.this, "Data Validated", Toast.LENGTH_SHORT).show();

                fAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //send user to next page
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }
}