package com.example.arnav.wayuhealth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Toolbar toolbarRegister;
    Spinner spinnerRegisterCountry;
    Spinner spinnerRegisterState;
    TextView textViewRegisterLogin;
    EditText editTextRegisterFirstName, editTextRegisterLastName, editTextRegisteremail, editTextRegisterPassword;
    Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editTextRegisterFirstName = (EditText) findViewById(R.id.editTextRegisterFirstName);
        editTextRegisterLastName = (EditText)findViewById(R.id.editTextRegisterLastName);
        editTextRegisteremail = (EditText)findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword = (EditText)findViewById(R.id.editTextRegisterPassword);
        buttonRegister = (Button)findViewById(R.id.buttonRegister);

        textViewRegisterLogin = (TextView)findViewById(R.id.textViewRegisterLogin);
        toolbarRegister = (Toolbar)findViewById(R.id.toolbarRegister);
        setSupportActionBar(toolbarRegister);
        getSupportActionBar().setTitle("Register");

        spinnerRegisterCountry = (Spinner)findViewById(R.id.spinnerRegisterCountry);
        spinnerRegisterState = (Spinner)findViewById(R.id.spinnerRegisterState);

        spinnerRegisterCountry.setOnItemSelectedListener(this);

        final List<String> registerCountries;
        String[] countries = getResources().getStringArray(R.array.country);
        registerCountries = Arrays.asList(countries);

        ArrayAdapter<String> arrayAdapterCountries = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, registerCountries);

        arrayAdapterCountries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerRegisterCountry.setAdapter(arrayAdapterCountries);

        //Link to Login
        textViewRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
            }
        });

        //Button Click Register
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextRegisterFirstName.getText().toString().equals("") &&
                    !editTextRegisterLastName.getText().toString().equals("") &&
                    !editTextRegisteremail.getText().toString().equals("") &&
                    !editTextRegisterPassword.getText().toString().equals("")){

                    Bundle bundleUserDetails = new Bundle();
                    String firstName = editTextRegisterFirstName.getText().toString();
                    String lastName = editTextRegisterLastName.getText().toString();
                    String email = editTextRegisteremail.getText().toString();
                    String password = editTextRegisterPassword.getText().toString();
                    bundleUserDetails.putString("firstName", firstName);
                    bundleUserDetails.putString("lastName", lastName);
                    bundleUserDetails.putString("email", email);
                    bundleUserDetails.putString("password", password);
                    bundleUserDetails.putString("country", String.valueOf(spinnerRegisterCountry.getSelectedItem()));
                    bundleUserDetails.putString("state", String.valueOf(spinnerRegisterState.getSelectedItem()));
                    RegisterTask registerTask = new RegisterTask(Register.this, bundleUserDetails);
                    registerTask.execute();
                }

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        parent.getItemAtPosition(position);
        if (position == 0) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.australia, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRegisterState.setAdapter(adapter);
        }
        else if (position == 1) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bangladesh, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRegisterState.setAdapter(adapter);
        }
        else if (position == 2) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.canada, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRegisterState.setAdapter(adapter);
        }
        else if (position == 3) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.india, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRegisterState.setAdapter(adapter);
        }
        else if (position == 4) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.pakistan, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRegisterState.setAdapter(adapter);
        }
        else if (position == 5) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.srilanka, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRegisterState.setAdapter(adapter);
        }
        else if (position == 6) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.singapore, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRegisterState.setAdapter(adapter);
        }
        else if (position == 7) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.usa, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRegisterState.setAdapter(adapter);
        }
        else if (position == 8) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.uk, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRegisterState.setAdapter(adapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
