package com.example.eoghan.scanner;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginBtn;
    private EditText emEt;
    private EditText pwEt;
    private Button backBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pDialog;
    private Button newUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        loginBtn = (Button) findViewById(R.id.lgBtn);
        emEt = (EditText) findViewById(R.id.etEmail);
        pwEt = (EditText) findViewById(R.id.editTextPassword);
        backBtn = (Button) findViewById(R.id.button3);

        pDialog = new ProgressDialog(this);
        loginBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        getSupportActionBar().setTitle("Login Page");


        if (firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), AdminAreaActivity.class));
        }
    }

    public void userLogin(){
        String email = emEt.getText().toString().trim();
        final String password = pwEt.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Can not leave Email field blank", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Can not leave Password field blank", Toast.LENGTH_LONG).show();
            return;
        }

        pDialog.setMessage("Processing");
        pDialog.show();


        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pDialog.dismiss();
                        if (task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), AdminAreaActivity.class));

                        }else if (password.length() < 6){

                            Toast.makeText(LoginActivity.this, "Fail, password must be atleast 6 characters", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(LoginActivity.this, "Fail, Username or Password Incorrect", Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == loginBtn) {
            userLogin();
        }
        if (v == backBtn) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }




    }

}
