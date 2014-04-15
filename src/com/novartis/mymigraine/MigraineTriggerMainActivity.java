package com.novartis.mymigraine;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.BoundedDatePicker;
import com.novartis.mymigraine.common.BoundedDatePickerDialog;
import com.novartis.mymigraine.common.Constant;
import com.novartis.mymigraine.database.DataBaseHelper;

public class MigraineTriggerMainActivity extends SherlockActivity implements OnClickListener
{
	private int mYear, minYear, maxYear;
	private int mMonth, minMonth, maxMonth;
	private int mDay, minDay, maxDay;
	private Button btnNewEntry;
	private Button btnHeadacheYes;
	private Button btnHeadacheNo;
	private Button btnViewPreviousEntry;
	private BoundedDatePickerDialog picker;
	private MenuItem menuItemMyDiary, menuItemChart;
	private DataBaseHelper dbhelper;
	private MyMigraineApp mymigraineapp;
	ActionBar actionBar;
	private View v;
	private Calendar entryCal;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_migraine_trigger_main);
		actionBar = getSupportActionBar();
		createActionBar();
		btnNewEntry = (Button) findViewById(R.id.btnNewEntry);
		btnNewEntry.setOnClickListener(this);

		btnHeadacheYes = (Button) findViewById(R.id.btnHeadacheYes);
		btnHeadacheYes.setOnClickListener(this);

		btnHeadacheNo = (Button) findViewById(R.id.btnHeadacheNo);
		btnHeadacheNo.setOnClickListener(this);

		btnViewPreviousEntry = (Button) findViewById(R.id.btnViewPreviousEntry);
		btnViewPreviousEntry.setOnClickListener(this);

		mymigraineapp = (MyMigraineApp) this.getApplicationContext();
		dbhelper = mymigraineapp.getDbhelper();

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		entryCal = Calendar.getInstance();
		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = maxYear = c.get(Calendar.YEAR);
		mMonth = maxMonth = c.get(Calendar.MONTH);
		mDay = maxDay = c.get(Calendar.DAY_OF_MONTH);

		final Calendar minCal = Calendar.getInstance();
		minYear = minCal.get(Calendar.YEAR);
		minMonth = minCal.get(Calendar.MONTH) - 2;
		minDay = minCal.get(Calendar.DAY_OF_MONTH);

		updateDisplay();

		picker = new BoundedDatePickerDialog(this, mDateSetListener, minYear, minMonth, minDay, maxYear, maxMonth,
				maxDay);

		int count = dbhelper.getMigraineCount();
		if (count > 0)
		{
			btnViewPreviousEntry.setEnabled(true);
		}
		else
		{
			btnViewPreviousEntry.setEnabled(false);
		}

	}

	private void createActionBar()
	{

		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_title_no_pad));
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		getSupportMenuInflater().inflate(R.menu.activity_migraine_trigger_main, menu);
		menuItemMyDiary = menu.findItem(R.id.menu_mydiary);
		menuItemChart = menu.findItem(R.id.menu_charts);
		activateMyDiary();
		return true;

	}

	@Override
	protected void onStart()
	{

		super.onStart();
		FlurryAgent.onStartSession(this, getString(R.string.flurry_analytics_api_key));
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.menu_setting:
			Intent intentsetting = new Intent(this, SettingActivity.class);
			startActivity(intentsetting);
			break;
		case R.id.menu_charts:

			Intent intentChart = new Intent(this, ChartActivity.class);

			startActivity(intentChart);

			break;
		case R.id.menu_diary_log:
			Intent intentDiaryLog = new Intent(this, DiaryLogDetailsActivity.class);
			startActivity(intentDiaryLog);
			break;
		case R.id.menu_try_excedrin:
			Intent intentTryExcedrin = new Intent(this, TryExcedrinActivity.class);

			startActivity(intentTryExcedrin);
			break;
		case R.id.menu_learn:
			Intent learnIntent = new Intent(this, LearnActivity.class);
			startActivity(learnIntent);
			break;

		case R.id.menu_help:
			Intent usingThisAppIntent = new Intent(this, HelpActivity.class);
			usingThisAppIntent.putExtra(Constant.Url, Constant.VIEW_USING_THIS_APP_ASSET_URL);
			startActivity(usingThisAppIntent);
			break;

		}
		return true;
	}

	private void activateMyDiary()
	{
		menuItemMyDiary.setIcon(R.drawable.ic_tab_mydiary_selected);
		menuItemChart.setIcon(R.drawable.ic_tab_charts_unselected);
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
	{

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			entryCal.set(Calendar.YEAR, mYear);
			entryCal.set(Calendar.MONTH, monthOfYear);
			entryCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			updateDisplay();
		}
	};

	// updates the date in the TextView
	private void updateDisplay()
	{
		SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM dd");
		Calendar cal = Calendar.getInstance();
		{
			if (cal.get(Calendar.YEAR) == mYear && cal.get(Calendar.MONTH) == mMonth
					&& cal.get(Calendar.DAY_OF_MONTH) == mDay)
			{

				btnNewEntry.setText("Today");
			}
			else if (cal.get(Calendar.YEAR) == mYear && cal.get(Calendar.MONTH) == mMonth
					&& cal.get(Calendar.DAY_OF_MONTH) - 1 == mDay)
			{

				btnNewEntry.setText("Yesterday");
			}
			else
			{
				cal.set(mYear, mMonth, mDay);
				btnNewEntry.setText(format.format(cal.getTime()));
			}

		}

	}

	@Override
	public void onClick(View v)
	{
		if (v == btnNewEntry)
		{
			// Create the dialog
			final Dialog mDateTimeDialog = new Dialog(this);
			mDateTimeDialog.setCanceledOnTouchOutside(false);
			// Inflate the root layout
			final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(
					R.layout.bounded_date_time_dialog, null);
			// Grab widget instance
			final BoundedDatePicker mDateTimePicker = (BoundedDatePicker) mDateTimeDialogView
					.findViewById(R.id.BoundedDatePicker);
			mDateTimePicker.initData(entryCal);
			

			// Update demo TextViews when the "OK" button is clicked
			((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new OnClickListener()
			{

				public void onClick(View v)
				{
					mDateTimePicker.clearFocus();
					// TODO Auto-generated method stub

					mDay = mDateTimePicker.getDay();
					mMonth = mDateTimePicker.getMonthAsNumber();
					mYear = mDateTimePicker.getYearAsNumber();

					entryCal.set(Calendar.YEAR, mYear);
					entryCal.set(Calendar.MONTH, mMonth);
					entryCal.set(Calendar.DAY_OF_MONTH, mDay);
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
			// picker.show();

		}
		else if (v == btnHeadacheYes)
		{
			Intent i = new Intent(this, HeadachePositiveStepsActivity.class);
			i.putExtra(Constant.KEY_TIMESTAMP, entryCal);
			startActivity(i);

		}
		else if (v == btnHeadacheNo)
		{
			Intent i = new Intent(this, HeadacheNegativeStepsActivity.class);
			i.putExtra(Constant.KEY_TIMESTAMP, entryCal);
			startActivity(i);

		}
		else if (v == btnViewPreviousEntry)
		{
			Intent i = new Intent(this, ViewPreviousEntryListActivity.class);

			startActivity(i);
		}

	}

}
