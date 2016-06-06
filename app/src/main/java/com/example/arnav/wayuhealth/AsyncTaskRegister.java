package com.example.arnav.wayuhealth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.appspot.wayuconnectdev.loginWC.LoginWC;
import com.google.api.client.http.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Arnav on 03/06/2016.
 */
public class AsyncTaskRegister extends AsyncTask<Void, JSONObject, JSONObject> {

    Context contextRegister;
    Activity activityRegister;
    String firstName, lastName, fullname, email, password, country, state;
    private final String KEY_SUCCESS = "success";
    private final String KEY_ERROR = "error";
    ProgressDialog progressDialogRegister;

    AsyncTaskRegister(Context context, Bundle bundle){
        this.contextRegister = context;
        activityRegister = (Activity)context;

        this.firstName = bundle.getString("firstName", "");
        this.lastName = bundle.getString("lastName", "");
        this.email = bundle.getString("email", "");
        this.password = bundle.getString("password", "");
        this.country = bundle.getString("country", "");
        this.state = bundle.getString("state", "");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialogRegister = new ProgressDialog(activityRegister);
        progressDialogRegister.setTitle("WayuHealth");
        progressDialogRegister.setMessage("Registering...");
        progressDialogRegister.setIndeterminate(false);
        progressDialogRegister.show();
    }

    @Override
    protected JSONObject doInBackground(Void... params) {

        LoginWC apiServiceHandle = AppConstants.getApiServiceHandle();
        JSONObject responseJson = null;
        try {
            LoginWC.Patsignup signUp = apiServiceHandle.patsignup();
            signUp
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setEmail(email != null ? email.toLowerCase().trim() : null)
                    .setPassword(password.trim())
                    .setCountry(country)
                    //.setGcmRegid(gcm_regid != null ? gcm_regid.trim() : null)
                    .setDeviceRegid("")
                    .setState(state).setUserAgreement(true)
                    .setDeviceType("android");

            HttpResponse httpResponse = signUp.executeUnparsed();
            String response = httpResponse.parseAsString();
            responseJson = new JSONObject(response);
            Log.e("JSON", response);
        }catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseJson;
    }

        @Override
    protected void onPostExecute(JSONObject result) {

            try {
                if (null == result) {
                    progressDialogRegister.dismiss();
                    return;
                }
                String successRes = result.getString(KEY_SUCCESS);
                Integer errorRes = result.getInt(KEY_ERROR);

                if (Integer.parseInt(successRes) == 1) {
                    AppData.sharedPreferences = contextRegister.getSharedPreferences(AppData.mySharedPreferences, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = AppData.sharedPreferences.edit();
                    editor.putString("firstName", firstName);
                    editor.putString("lastName", lastName);
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.putString("country", country);
                    editor.putString("state", state);
                    editor.commit();

                    Intent i = new Intent(activityRegister, Acknowledgement.class);
                    activityRegister.startActivity(i);
                    activityRegister.finish();
                } else if (errorRes == 1) {
                    progressDialogRegister.dismiss();
                    //TODO: showExistingUserMessage();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // TODO: handle exception
            }

        super.onPostExecute(result);
    }
}
