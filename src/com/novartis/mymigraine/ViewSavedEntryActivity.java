package com.novartis.mymigraine;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.Constant;
import com.novartis.mymigraine.common.DateTimePicker;
import com.novartis.mymigraine.common.DateTimePicker.DateWatcher;
import com.novartis.mymigraine.common.IMigraineEvent;
import com.novartis.mymigraine.common.IMigraineEventChangedListener;
import com.novartis.mymigraine.common.Utility;
import com.novartis.mymigraine.database.DataBaseHelper;
import com.novartis.mymigraine.fragment.HeadacheStep1Fragment;
import com.novartis.mymigraine.fragment.HeadacheStep2Fragment;
import com.novartis.mymigraine.fragment.HeadacheStep3Fragment;
import com.novartis.mymigraine.fragment.HeadacheStep4Fragment;
import com.novartis.mymigraine.fragment.HeadacheStep5Fragment;
import com.novartis.mymigraine.fragment.HeadacheStep6Fragment;
import com.novartis.mymigraine.model.MigraineEvent;

public class ViewSavedEntryActivity extends SherlockFragmentActivity implements
		OnClickListener, DateWatcher, IMigraineEvent,
		IMigraineEventChangedListener {
	private int mYear, minYear, maxYear;
	private int mMonth, minMonth, maxMonth;
	private int mDay, minDay, maxDay;
	private HeadacheStep1Fragment step1Fragment;
	private HeadacheStep2Fragment step2Fragment;
	private HeadacheStep3Fragment step3Fragment;
	private HeadacheStep4Fragment step4Fragment;
	private HeadacheStep5Fragment step5Fragment;
	private HeadacheStep6Fragment step6Fragment;
	private Button btnEntry, btnHeadacheYes, btnHeadacheNo;
	private MigraineEvent event;
	private Calendar entryCal;
	private Button btnSavedEntryCancel, btnSavedEntryDone;
	LinearLayout stepFragmentsPanel;
	/**
	 * Database Helper class to Access the database.
	 */
	private DataBaseHelper dbhelper;
	private MyMigraineApp mymigraineapp;

	private ActionBar actionBar;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_saved_entry);
		actionBar = getSupportActionBar();
		createActionBar();
		stepFragmentsPanel = (LinearLayout) findViewById(R.id.stepFragmentsPanel);
		if (getIntent().getExtras().containsKey(Constant.KEY_EVENT))
			event = (MigraineEvent) getIntent().getExtras().getSerializable(
					Constant.KEY_EVENT);
		mymigraineapp = (MyMigraineApp) this.getApplicationContext();
		dbhelper = mymigraineapp.getDbhelper();
		btnEntry = (Button) findViewById(R.id.btnEntry);
		btnEntry.setOnClickListener(this);
		btnHeadacheYes = (Button) findViewById(R.id.btnHeadacheYes);
		btnHeadacheYes.setOnClickListener(this);
		btnHeadacheNo = (Button) findViewById(R.id.btnHeadacheNo);
		btnHeadacheNo.setOnClickListener(this);

		btnSavedEntryCancel = (Button) findViewById(R.id.btnSavedEntryCancel);
		btnSavedEntryCancel.setOnClickListener(this);

		btnSavedEntryDone = (Button) findViewById(R.id.btnSavedEntryDone);
		btnSavedEntryDone.setOnClickListener(this);

		// get the current date
		final Calendar c = Calendar.getInstance();
		maxYear = c.get(Calendar.YEAR);
		maxMonth = c.get(Calendar.MONTH);
		maxDay = c.get(Calendar.DAY_OF_MONTH);

		entryCal = Calendar.getInstance();
		if (event != null && event.getDateTime() != null)
			entryCal.setTime(event.getDateTime());

		mYear = entryCal.get(Calendar.YEAR);
		mMonth = entryCal.get(Calendar.MONTH);
		mDay = entryCal.get(Calendar.DAY_OF_MONTH);
		updateDisplay();

		step1Fragment = new HeadacheStep1Fragment();
		step2Fragment = new HeadacheStep2Fragment();
		step3Fragment = new HeadacheStep3Fragment();
		step4Fragment = new HeadacheStep4Fragment();
		step5Fragment = new HeadacheStep5Fragment();
		step6Fragment = new HeadacheStep6Fragment();

		addFragmentInPanel();
		if (event != null && event.isHasHeadache()) {
			btnHeadacheYes.setSelected(true);
		} else {
			btnHeadacheNo.setSelected(true);
			hideStep123();
		}

	}

	
	private void createActionBar() {
		
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(getResources().getDrawable(
				R.drawable.ic_actionbar_title));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	
	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		
		}
		return true;
	}

	
	private void addFragmentInPanel() {
		android.support.v4.app.FragmentManager fragmentManager = this
				.getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.stepFragmentsPanel, step1Fragment,
				HeadacheStep1Fragment.TAG);
		fragmentTransaction.add(R.id.stepFragmentsPanel, step2Fragment,
				HeadacheStep2Fragment.TAG);
		fragmentTransaction.add(R.id.stepFragmentsPanel, step3Fragment,
				HeadacheStep3Fragment.TAG);
		fragmentTransaction.add(R.id.stepFragmentsPanel, step4Fragment,
				HeadacheStep4Fragment.TAG);
		fragmentTransaction.add(R.id.stepFragmentsPanel, step5Fragment,
				HeadacheStep5Fragment.TAG);
		fragmentTransaction.add(R.id.stepFragmentsPanel, step6Fragment,
				HeadacheStep6Fragment.TAG);
		fragmentTransaction.commit();
	}

	private void hideSavedEntryButton() {
		
		Button btnSavedThisEntry = step6Fragment.getSavedEntryButton();
	
		if (btnSavedThisEntry != null)
			btnSavedThisEntry.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {

		super.onResume();
		hideSavedEntryButton();
	}

	@Override
	public void onClick(View v) {
		if (v == btnEntry) {
			// Create the dialog
			final Dialog mDateTimeDialog = new Dialog(this);
			// Inflate the root layout
			final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater()
					.inflate(R.layout.date_time_dialog, null);
			// Grab widget instance
			final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
					.findViewById(R.id.DateTimePicker);
			mDateTimePicker.initData(entryCal);
			mDateTimePicker.setDateChangedListener(this);

			// Update demo TextViews when the "OK" button is clicked
			((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime))
					.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							mDateTimePicker.clearFocus();

							mDay = mDateTimePicker.getDay();
							mMonth = mDateTimePicker.getMonthAsNumber();
							mYear = mDateTimePicker.getYearAsNumber();

							entryCal.set(Calendar.YEAR, mYear);
							entryCal.set(Calendar.MONTH, mMonth);
							entryCal.set(Calendar.DAY_OF_MONTH, mDay);
							event.setDateTime(entryCal.getTime());
							updateDisplay();
							mDateTimeDialog.dismiss();
						}
					});

			// Setup TimePicker
			// No title on the dialog window
			mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			// Set the dialog content view
			mDateTimeDialog.setContentView(mDateTimeDialogView);
			// Display the dialog
			mDateTimeDialog.show();
		} else if (v == btnHeadacheYes) {
			btnHeadacheNo.setSelected(false);
			btnHeadacheYes.setSelected(true);
			event.setHasHeadache(true);
			showStep123();

		} else if (v == btnHeadacheNo) {
			btnHeadacheNo.setSelected(true);
			btnHeadacheYes.setSelected(false);
			event.setHasHeadache(false);
			hideStep123();
		} else if (v == btnSavedEntryCancel) {
			setResult(RESULT_CANCELED);
			onBackPressed();
		} else if (v == btnSavedEntryDone) {

			if (!event.isHasHeadache()) {
				event.setStartHour(0);
				event.setStartMinute(0);
				event.setDuration(0);
				event.setIntensity(null);
				event.setLocations(null);
				event.setWarnings(null);
				event.setSymptoms(null);
				event.setTreatments(null);
				event.setReliefs(null);
			}
			boolean isSuccessfull = dbhelper.updateMigraineEventEntry(event);
			if (isSuccessfull) {
				getIntent().putExtra(Constant.KEY_EVENT, event);
				setResult(RESULT_OK, getIntent());
				onBackPressed();
			} else {
				Utility.showAlert(this, "Error", "Updating entry failed.");
			}

		}

	}

	private void hideStep123() {
		android.support.v4.app.FragmentManager fragmentManager = this
				.getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.hide(step1Fragment);
		fragmentTransaction.hide(step2Fragment);
		fragmentTransaction.hide(step3Fragment);
		fragmentTransaction.commit();

	}

	private void showStep123() {
		android.support.v4.app.FragmentManager fragmentManager = this
				.getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.show(step1Fragment);
		fragmentTransaction.show(step2Fragment);
		fragmentTransaction.show(step3Fragment);
		fragmentTransaction.commit();
	}

	// updates the date in the TextView
	private void updateDisplay() {
		SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM dd");
		Calendar cal = Calendar.getInstance();
		{
			if (cal.get(Calendar.YEAR) == mYear
					&& cal.get(Calendar.MONTH) == mMonth
					&& cal.get(Calendar.DAY_OF_MONTH) == mDay) {

				btnEntry.setText("Today");
			} else if (cal.get(Calendar.YEAR) == mYear
					&& cal.get(Calendar.MONTH) == mMonth
					&& cal.get(Calendar.DAY_OF_MONTH) - 1 == mDay) {

				btnEntry.setText("Yesterday");
			} else {
				cal.set(mYear, mMonth, mDay);
				btnEntry.setText(format.format(cal.getTime()));
			}

		}

	}

	@Override
	public void onDateChanged(Calendar c) {

	}

	@Override
	public void createMigraineEvent() {

	}

	@Override
	public MigraineEvent getMigraineEvent() {
		return event;
	}

	@Override
	public void onEventChanged(MigraineEvent event) {
		this.event = event;

	}

	@Override
	protected void onStart() {

		super.onStart();
		FlurryAgent.onStartSession(this,
				getString(R.string.flurry_analytics_api_key));
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

}
