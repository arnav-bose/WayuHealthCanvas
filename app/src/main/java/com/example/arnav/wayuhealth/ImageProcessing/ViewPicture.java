package com.example.arnav.wayuhealth.ImageProcessing;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arnav.wayuhealth.AppData;
import com.example.arnav.wayuhealth.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPicture extends Fragment {

    public ViewPicture() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_picture, container, false);

        AppData.sharedPreferences = getContext().getSharedPreferences(AppData.mySharedPreferences, Context.MODE_PRIVATE);
        String email = AppData.sharedPreferences.getString("email", "");
        String sessionKey = AppData.sharedPreferences.getString("session_key", "");
        String memberID = AppData.sharedPreferences.getString("mem_id", "");
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("session_key", sessionKey);
        bundle.putString("member_id", memberID);

        AsyncTaskGetUrls asyncTaskGetUrls = new AsyncTaskGetUrls(getContext(), bundle);
        asyncTaskGetUrls.execute();

        return view;
    }

}
