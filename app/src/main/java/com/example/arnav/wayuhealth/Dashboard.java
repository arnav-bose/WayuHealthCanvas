package com.example.arnav.wayuhealth;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.example.arnav.wayuhealth.ImageProcessing.TakePicture;

public class Dashboard extends AppCompatActivity {



    Toolbar toolbarDashboard;
    TabLayout tabLayoutDashboard;
    ViewPager viewPagerDashboard;
    ViewPagerAdapterDashboard viewPagerAdapterDashboard;
    public static Uri uriFinalImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
        }
        else{
            uriFinalImage = Uri.parse(bundle.getString("uriFinalImage", ""));
            TakePicture takePicture = new TakePicture();
        }

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
