package com.example.arnav.wayuhealth;

/**
 * Created by Arnav on 06/06/2016.
 */
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.arnav.wayuhealth.ImageProcessing.TakePicture;
import com.example.arnav.wayuhealth.ImageProcessing.ViewPicture;


public class ViewPagerAdapterDashboard extends FragmentStatePagerAdapter {

    public ViewPagerAdapterDashboard(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            TakePicture takePicture = new TakePicture();
            return takePicture;
        }
        else {
            ViewPicture viewPicture = new ViewPicture();
            return viewPicture;
        }
    }

    public int getCount(){
        return 2;
    }
}
