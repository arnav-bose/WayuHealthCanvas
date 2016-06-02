package com.example.arnav.wayuhealth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    Button buttonLogin;
    Toolbar toolbarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        toolbarLogin = (Toolbar)findViewById(R.id.toolbarLogin);
        setSupportActionBar(toolbarLogin);
        toolbarLogin.setTitle("Login");

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextUsername.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")){
                    String username = editTextUsername.getText().toString();
                    String password = editTextPassword.getText().toString();
                    AsyncTaskLogin asyncTaskLogin = new AsyncTaskLogin(Login.this);
                    asyncTaskLogin.execute(username, password);
                }
                else{
                    Toast.makeText(Login.this, "Please enter the details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
