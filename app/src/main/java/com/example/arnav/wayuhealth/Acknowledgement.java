package com.example.arnav.wayuhealth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Acknowledgement extends AppCompatActivity {

    Button buttonAcknowledgement;
    Toolbar toolbarAcknowledgement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acknowledgement);

        toolbarAcknowledgement = (Toolbar)findViewById(R.id.toolbarAcknowledgement);
        setSupportActionBar(toolbarAcknowledgement);
        getSupportActionBar().setTitle("Acknowledgement");

        buttonAcknowledgement = (Button)findViewById(R.id.buttonAcknowledgement);

        buttonAcknowledgement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Acknowledgement.this, Login.class);
                startActivity(i);
                finish();
            }
        });
    }
}
