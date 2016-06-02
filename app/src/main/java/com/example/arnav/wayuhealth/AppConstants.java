package com.example.arnav.wayuhealth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.appspot.firststepgaedev.login.Login;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;


public class AppConstants {

	public static final String WEB_CLIENT_ID = "357933939625-ga42sr4je1jaulunf1ch4b6h183vmugd.apps.googleusercontent.com";

	public static final String AUDIENCE = "server:client_id:" + WEB_CLIENT_ID;

	/**
	 * Class instance of the JSON factory.
	 */
	public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

	/**
	 * Class instance of the HTTP transport.
	 */
	public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();


	public static int countGoogleAccounts(Context context) {
		AccountManager am = AccountManager.get(context);
		Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
		if (accounts == null || accounts.length < 1) {
			return 0;
		} else {
			return accounts.length;
		}
	}

	/**
	 * First Step Login api service handle to access the API.
	 */
	public static Login getApiServiceHandle() {
		// Use a builder to help formulate the API request.
		Login.Builder loginFS = new Login.Builder(AppConstants.HTTP_TRANSPORT,
				AppConstants.JSON_FACTORY,null);

		//loginFS.setRootUrl("http://182.64.105.236:8080/_ah/api/");
		return loginFS.build();
	}

	public static boolean checkGooglePlayServicesAvailable(Activity activity) {
		final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
			showGooglePlayServicesAvailabilityErrorDialog(activity, connectionStatusCode);
			return false;
		}
		return true;
	}

	public static void showGooglePlayServicesAvailabilityErrorDialog(final Activity activity,
			final int connectionStatusCode) {
		final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
						connectionStatusCode, activity, REQUEST_GOOGLE_PLAY_SERVICES);
				dialog.show();
			}
		});
	}

}
