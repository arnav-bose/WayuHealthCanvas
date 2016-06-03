package com.example.arnav.wayuhealth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class Dashboard extends AppCompatActivity {

    Toolbar toolbarDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbarDashboard = (Toolbar)findViewById(R.id.toolbarDashboard);
        setSupportActionBar(toolbarDashboard);
        getSupportActionBar().setTitle("Dashboard");

    }
}
