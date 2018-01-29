package com.example.rachel.vetapplove;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText etUsername,etPasswrd;
    Button btnSignIn,btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsername=(EditText)findViewById(R.id.etUsername);
        etPasswrd=(EditText)findViewById(R.id.etPasswrd);
        btnSignIn=(Button)findViewById(R.id.btnSignIn);
        btnSignUp=(Button)findViewById(R.id.btnSignUp);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

    }

    private void signup() {
        Intent intent=new Intent(getApplicationContext(),SignUp.class);
        startActivity(intent);
    }

    private void registerUser() {
        String username=etUsername.getText().toString().toLowerCase();
        String passwrd=etPasswrd.getText().toString().toLowerCase();
    }
}
