package com.example.arnav.wayuhealth;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBarSplashScreen;
    Thread threadSplashScreen;
    TextView textViewProgressStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBarSplashScreen = (ProgressBar) findViewById(R.id.progressBarSplashScreen);
        textViewProgressStatus = (TextView)findViewById(R.id.textViewProgressStatus);
        progressBarSplashScreen.setVisibility(View.GONE);
        textViewProgressStatus.setText("Checking Connection...");

        threadSplashScreen = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBarSplashScreen.setVisibility(View.VISIBLE);
                        }
                    });
                    AppData.sharedPreferences = getSharedPreferences(AppData.mySharedPreferences, MODE_PRIVATE);

                    if(isNetworkAvailable()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewProgressStatus.setText("Connection Established. Verifying Session Key...");
                            }
                        });
                        //textViewProgressStatus.setText("Connection Established... Checking Session Key");
                        Bundle bundleAuthenticate = new Bundle();
                        String sessionKey = getSharedPreferences(AppData.mySharedPreferences,MODE_PRIVATE).getString("session_key", "");
                        String email = getSharedPreferences(AppData.mySharedPreferences,MODE_PRIVATE).getString("email", "");
                        if (sessionKey.equalsIgnoreCase("")){
                            Intent i = new Intent(SplashScreen.this, Login.class);
                            startActivity(i);
                            finish();
                        }
                        else if(email.equalsIgnoreCase("") || email.equalsIgnoreCase("") && sessionKey.equalsIgnoreCase("")){
                            Intent i = new Intent(SplashScreen.this, Register.class);
                            startActivity(i);
                            finish();
                        }
                        else{
                            bundleAuthenticate.putString("email", email);
                            bundleAuthenticate.putString("session_key", sessionKey);
                            AsyncTaskCheckSessionKey asyncTaskCheckSessionKey = new AsyncTaskCheckSessionKey(SplashScreen.this, bundleAuthenticate);
                            asyncTaskCheckSessionKey.execute();
                        }
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBarSplashScreen.setVisibility(View.GONE);
                                Toast.makeText(SplashScreen.this, "No Connection. Restart application.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        threadSplashScreen.start();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
