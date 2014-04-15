package com.novartis.mymigraine;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Window;

import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.SharedPrefUtility;

/**
 * Purpose: This class is used for displaying SplashScreen.
 * 
 * @author IndiaNIC
 */
public class SplashActivity extends Activity implements Runnable, Callback {

	/**
	 * Handler class
	 */
	private Handler handler;
	/**
	 * Thread class
	 */
	private Thread thread;
	private Calendar entryCal;
	private boolean isThreadRunning = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);

		entryCal=Calendar.getInstance();
		
		setPreferenceValue();

		handler = new Handler(this);
		thread = new Thread(this);
		thread.start();
	}

	private void setPreferenceValue() {

		SharedPreferences preferences = getSharedPreferences(
				getString(R.string.app_name), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(getString(R.string.warningSign), true);

		editor.putBoolean(getString(R.string.location), true);

		editor.putBoolean(getString(R.string.symptoms), true);

		editor.putBoolean(getString(R.string.diet), true);

		editor.putBoolean(getString(R.string.lifestyle), true);

		editor.putBoolean(getString(R.string.environment), true);

		editor.putBoolean(getString(R.string.notes), true);

		editor.putString(getString(R.string.datetime), getPreviousDateTime(60));
		
		
		editor.putString(getString(R.string.filterVia), "period");
		editor.putInt(getString(R.string.periodTime), 0);
		
		editor.putInt(getString(R.string.day), entryCal.get(Calendar.DAY_OF_MONTH));
		editor.putInt(getString(R.string.month), entryCal.get(Calendar.MONTH));
		editor.putInt(getString(R.string.year), entryCal.get(Calendar.YEAR));
		
		editor.commit();

	}

	private String getPreviousDateTime(int i) {

		Date currentDate = new Date();
		String month = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DAY_OF_YEAR, -i);

		if (calendar.get(Calendar.MONTH) == 9
				|| calendar.get(Calendar.MONTH) == 10
				|| calendar.get(Calendar.MONTH) == 11) {
			month = String.valueOf((calendar.get(Calendar.MONTH) + 1));
		} else {
			month = "0" + String.valueOf(calendar.get(Calendar.MONTH) + 1);
		}

		String result_string = calendar.get(Calendar.YEAR) + "-" + month + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH);

		String time = result_string + " 00:00:00";

		return time;
	}

	/**
	 * called when thread start.
	 */
	@Override
	public void run() {
		try {
			
			Thread.sleep(2000);
			
			if(!isThreadRunning) return;
			
			handler.sendEmptyMessage(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * called for handling a message.
	 */

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == 0) {
			if (SharedPrefUtility.isAppLoadFirstTime(this)) {
				Intent i = new Intent(SplashActivity.this,
						TermsAndConditions.class);
				startActivity(i);
				finish();
			}
			else
			{
				Intent i = new Intent(SplashActivity.this,
						MigraineTriggerMainActivity.class);
				startActivity(i);
				finish();
			}
		}
		return true;
	}
	
	@Override
	public void onBackPressed() {
		isThreadRunning = false;
		super.onBackPressed();
	}
	@Override
	protected void onStart() {
		
		super.onStart();
		FlurryAgent.onStartSession(this, getString(R.string.flurry_analytics_api_key));
	}
	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

}
