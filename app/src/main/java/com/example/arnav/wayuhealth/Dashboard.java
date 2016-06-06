package com.example.arnav.wayuhealth;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class Dashboard extends AppCompatActivity {



    Toolbar toolbarDashboard;
    TabLayout tabLayoutDashboard;
    ViewPager viewPagerDashboard;
    ViewPagerAdapterDashboard viewPagerAdapterDashboard;
    Button buttonTakePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        //Toolbar
        toolbarDashboard = (Toolbar)findViewById(R.id.toolbarDashboard);
        setSupportActionBar(toolbarDashboard);
        getSupportActionBar().setTitle("Dashboard");

        //TabLayout
        tabLayoutDashboard = (TabLayout)findViewById(R.id.tabLayoutDashboard);

        //ViewPager
        viewPagerDashboard = (ViewPager)findViewById(R.id.viewPagerDashboard);

        //ViewPagerAdapter
        viewPagerAdapterDashboard = new ViewPagerAdapterDashboard(getSupportFragmentManager());

        viewPagerDashboard.setAdapter(viewPagerAdapterDashboard);

        final TabLayout.Tab takePicture = tabLayoutDashboard.newTab();
        final TabLayout.Tab viewPicture = tabLayoutDashboard.newTab();

        takePicture.setText("Take Picture");
        viewPicture.setText("View Picture");

        tabLayoutDashboard.addTab(takePicture, 0);
        tabLayoutDashboard.addTab(viewPicture, 1);

        tabLayoutDashboard.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        tabLayoutDashboard.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));

        tabLayoutDashboard.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPagerDashboard));
        viewPagerDashboard.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutDashboard));


        //Title Change For Selected Tab
        tabLayoutDashboard.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        viewPagerDashboard.setCurrentItem(0);
                        toolbarDashboard.setTitle("Take Picture");
                        break;
                    case 1:
                        viewPagerDashboard.setCurrentItem(1);
                        toolbarDashboard.setTitle("View Picture");
                        break;
                    default:
                        viewPagerDashboard.setCurrentItem(tab.getPosition());
                        toolbarDashboard.setTitle("WayuHealth");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }
}
