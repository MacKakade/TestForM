package com.novartis.mymigraine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.DateTimePicker;
import com.novartis.mymigraine.common.DateTimePicker.DateWatcher;

public class FilterActivity extends SherlockActivity implements OnClickListener, DateWatcher, OnCheckedChangeListener
{

	private Button btnChooseDate, btnChoosePeriod, btnCancel, btnDone;
	private CheckBox chk1, chk2, chk3, chk4, chk5, chk6, chk7;
	private String time;
	private String from;
	private RelativeLayout rel_category;
	private SharedPreferences preferences;
	private String filterVia = "";
	private int periodTime = -1;
	private Calendar entryCal;
	private int mYear;
	private int mMonth;
	private int mDay;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter);
		actionBar = getSupportActionBar();
		createActionBar();

		btnChooseDate = (Button) findViewById(R.id.filter_btn_chooseDate);
		btnChooseDate.setOnClickListener(this);

		btnChoosePeriod = (Button) findViewById(R.id.filter_btn_setPeriod);
		btnChoosePeriod.setOnClickListener(this);

		btnCancel = (Button) findViewById(R.id.filter_btn_cancel);
		btnCancel.setOnClickListener(this);

		btnDone = (Button) findViewById(R.id.filter_btn_done);
		btnDone.setOnClickListener(this);

		final float scale = getResources().getDisplayMetrics().density;
		chk1 = (CheckBox) findViewById(R.id.filter_chk_warning);
		chk1.setOnCheckedChangeListener(this);
		chk1.setPadding(chk1.getPaddingLeft() + (int) (0f * scale + 0.5f), chk1.getPaddingTop(),
				chk1.getPaddingRight(), chk1.getPaddingBottom());

		chk2 = (CheckBox) findViewById(R.id.filter_chk_location);
		chk2.setOnCheckedChangeListener(this);
		chk2.setPadding(chk2.getPaddingLeft() + (int) (0f * scale + 0.5f), chk2.getPaddingTop(),
				chk2.getPaddingRight(), chk2.getPaddingBottom());

		chk3 = (CheckBox) findViewById(R.id.filter_chk_symptoms);
		chk3.setOnCheckedChangeListener(this);
		chk3.setPadding(chk3.getPaddingLeft() + (int) (0f * scale + 0.5f), chk3.getPaddingTop(),
				chk3.getPaddingRight(), chk3.getPaddingBottom());

		chk4 = (CheckBox) findViewById(R.id.filter_chk_Diet);
		chk4.setOnCheckedChangeListener(this);
		chk4.setPadding(chk4.getPaddingLeft() + (int) (0f * scale + 0.5f), chk4.getPaddingTop(),
				chk4.getPaddingRight(), chk4.getPaddingBottom());

		chk5 = (CheckBox) findViewById(R.id.filter_chk_lifeStyle);
		chk5.setOnCheckedChangeListener(this);
		chk5.setPadding(chk5.getPaddingLeft() + (int) (0f * scale + 0.5f), chk5.getPaddingTop(),
				chk5.getPaddingRight(), chk5.getPaddingBottom());

		chk6 = (CheckBox) findViewById(R.id.filter_chk_Environment);
		chk6.setOnCheckedChangeListener(this);
		chk6.setPadding(chk6.getPaddingLeft() + (int) (0f * scale + 0.5f), chk6.getPaddingTop(),
				chk6.getPaddingRight(), chk6.getPaddingBottom());

		chk7 = (CheckBox) findViewById(R.id.filter_chk_notes);
		chk7.setOnCheckedChangeListener(this);
		chk7.setPadding(chk7.getPaddingLeft() + (int) (0f * scale + 0.5f), chk7.getPaddingTop(),
				chk7.getPaddingRight(), chk7.getPaddingBottom());

		rel_category = (RelativeLayout) findViewById(R.id.filter_rel_category);

		from = getIntent().getStringExtra("from");
		if (from.equalsIgnoreCase("diary"))
		{
			rel_category.setVisibility(View.VISIBLE);
		}
		else
		{
			rel_category.setVisibility(View.GONE);
		}

		preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

		entryCal = Calendar.getInstance();
		getpreferenceValue();
		disableDoneIfAllCheckboxUnselected();

	}

	private void disableDoneIfAllCheckboxUnselected()
	{
		if (chk1.isChecked() || chk2.isChecked() || chk3.isChecked() || chk4.isChecked() || chk5.isChecked()
				|| chk6.isChecked() || chk7.isChecked())
		{
			btnDone.setEnabled(true);
			btnDone.setTextColor(Color.WHITE);
		}
		else
		{
			btnDone.setEnabled(false);
			btnDone.setTextColor(Color.GRAY);
		}
	}

	private void getpreferenceValue()
	{

		if (preferences.getBoolean(getString(R.string.warningSign), true))
			chk1.setChecked(true);
		if (preferences.getBoolean(getString(R.string.location), false))
			chk2.setChecked(true);
		if (preferences.getBoolean(getString(R.string.symptoms), false))
			chk3.setChecked(true);
		if (preferences.getBoolean(getString(R.string.diet), false))
			chk4.setChecked(true);
		if (preferences.getBoolean(getString(R.string.lifestyle), false))
			chk5.setChecked(true);
		if (preferences.getBoolean(getString(R.string.environment), false))
			chk6.setChecked(true);
		if (preferences.getBoolean(getString(R.string.notes), false))
			chk7.setChecked(true);

		time = preferences.getString(getString(R.string.datetime), getPreviousDateTime(60));

		filterVia = preferences.getString(getString(R.string.filterVia), "");

		if (filterVia.equalsIgnoreCase("date"))
		{
			btnChooseDate.setBackgroundColor(getResources().getColor(R.color.green));
			btnChoosePeriod.setBackgroundColor(getResources().getColor(R.color.light_gray));

			mDay = preferences.getInt(getString(R.string.day), entryCal.get(Calendar.DAY_OF_MONTH));
			mMonth = preferences.getInt(getString(R.string.month), entryCal.get(Calendar.MONTH));
			mYear = preferences.getInt(getString(R.string.year), entryCal.get(Calendar.YEAR));
			periodTime = preferences.getInt(getString(R.string.periodTime), 0);

			entryCal.set(Calendar.YEAR, mYear);
			entryCal.set(Calendar.MONTH, mMonth);
			entryCal.set(Calendar.DAY_OF_MONTH, mDay);

		}
		else
		{
			btnChooseDate.setBackgroundColor(getResources().getColor(R.color.light_gray));
			btnChoosePeriod.setBackgroundColor(getResources().getColor(R.color.green));
			periodTime = preferences.getInt(getString(R.string.periodTime), 0);
		}

	}

	private void createActionBar()
	{
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_title));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
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

		}
		return true;
	}

	@Override
	public void onClick(View v)
	{

		if (v == btnChooseDate)
		{

			// Create the dialog
			final Dialog mDateTimeDialog = new Dialog(this);
			// Inflate the root layout
			final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(
					R.layout.date_time_dialog, null);
			// Grab widget instance
			final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
					.findViewById(R.id.DateTimePicker);
			mDateTimePicker.initData(entryCal);
			mDateTimePicker.setDateChangedListener(this);

			// Update demo TextViews when the "OK" button is clicked
			((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new OnClickListener()
			{

				public void onClick(View v)
				{
					mDateTimePicker.clearFocus();
					
					Calendar calendar = Calendar.getInstance();

				

					Date date;
					String month = null;
					Calendar cal = null;
					try
					{
						date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(mDateTimePicker.getMonth());
						cal = Calendar.getInstance();
						cal.setTime(date);

						if (cal.get(Calendar.MONTH) == 9 || cal.get(Calendar.MONTH) == 10
								|| cal.get(Calendar.MONTH) == 11)
						{
							month = String.valueOf((cal.get(Calendar.MONTH) + 1));
						}
						else
						{
							month = "0" + String.valueOf(cal.get(Calendar.MONTH) + 1);
						}

					}
					catch (ParseException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String result_string = calendar.get(Calendar.YEAR) + "-" + month + "-";

					if (mDateTimePicker.getDay() < 10)
						result_string += "0" + mDateTimePicker.getDay();
					else
						result_string += mDateTimePicker.getDay();

					time = result_string + " 00:00:00";

					// Log.e("storeValue", "storeValue = " + time);

					filterVia = "date";

					periodTime = -1;

					btnChooseDate.setBackgroundColor(getResources().getColor(R.color.green));
					btnChoosePeriod.setBackgroundColor(getResources().getColor(R.color.light_gray));

					mYear = calendar.get(Calendar.YEAR);
					mMonth = cal.get(Calendar.MONTH);
					mDay = mDateTimePicker.getDay();

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

		}
		else if (v == btnChoosePeriod)
		{

			final String[] array = new String[] { "All data", "Last 60 days", "Last 30 days", "Past 2 weeks", "Today",
					"Yesterday" };

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setSingleChoiceItems(array, periodTime, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int position)
				{
					if (position == 0)
					{
						time = getPreviousDateTime(60);
					}
					else if (position == 1)
					{
						time = getPreviousDateTime(60);
					}
					else if (position == 2)
					{
						time = getPreviousDateTime(30);
					}
					else if (position == 3)
					{
						time = getPreviousDateTime(15);
					}
					else if (position == 4)
					{
						time = getPreviousDateTime(0);
					}
					else if (position == 5)
					{
						time = getPreviousDateTime(1);
					}

					filterVia = "period";
					periodTime = position;

					setCurrentDate();

					btnChooseDate.setBackgroundColor(getResources().getColor(R.color.light_gray));
					btnChoosePeriod.setBackgroundColor(getResources().getColor(R.color.green));

					dialog.dismiss();
				}
			});

			AlertDialog alertDialog = builder.create();
			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.setCancelable(true);
			alertDialog.setCanceledOnTouchOutside(true);
			alertDialog.show();

		}
		else if (v == btnCancel)
		{
			finish();
		}
		else if (v == btnDone)
		{
			storeValue();
			finish();
		}

	}

	@Override
	public void onDateChanged(Calendar c)
	{

		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

	}

	public void storeValue()
	{

		Editor editor = preferences.edit();
		if (chk1.isChecked())
			editor.putBoolean(getString(R.string.warningSign), true);
		else
			editor.putBoolean(getString(R.string.warningSign), false);

		if (chk2.isChecked())
			editor.putBoolean(getString(R.string.location), true);
		else
			editor.putBoolean(getString(R.string.location), false);

		if (chk3.isChecked())
			editor.putBoolean(getString(R.string.symptoms), true);
		else
			editor.putBoolean(getString(R.string.symptoms), false);

		if (chk4.isChecked())
			editor.putBoolean(getString(R.string.diet), true);
		else
			editor.putBoolean(getString(R.string.diet), false);

		if (chk5.isChecked())
			editor.putBoolean(getString(R.string.lifestyle), true);
		else
			editor.putBoolean(getString(R.string.lifestyle), false);

		if (chk6.isChecked())
			editor.putBoolean(getString(R.string.environment), true);
		else
			editor.putBoolean(getString(R.string.environment), false);

		if (chk7.isChecked())
			editor.putBoolean(getString(R.string.notes), true);
		else
			editor.putBoolean(getString(R.string.notes), false);

		// Log.e("storeValue", "storeValue = " + time);

		editor.putString(getString(R.string.datetime), time);
		editor.putString(getString(R.string.filterVia), filterVia);

		if (filterVia.equalsIgnoreCase("date"))
		{
			editor.putInt(getString(R.string.day), mDay);
			editor.putInt(getString(R.string.month), mMonth);
			editor.putInt(getString(R.string.year), mYear);
			editor.putInt(getString(R.string.periodTime), 0);
		}
		else
		{
			editor.putInt(getString(R.string.periodTime), periodTime);
		}

		editor.commit();
	}

	private String getPreviousDateTime(int i)
	{

		Date currentDate = new Date();
		String month = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DAY_OF_YEAR, -i);

		if (calendar.get(Calendar.MONTH) == 9 || calendar.get(Calendar.MONTH) == 10
				|| calendar.get(Calendar.MONTH) == 11)
		{
			month = String.valueOf((calendar.get(Calendar.MONTH) + 1));
		}
		else
		{
			month = "0" + String.valueOf(calendar.get(Calendar.MONTH) + 1);
		}

		String result_string = calendar.get(Calendar.YEAR) + "-" + month + "-";

		if (calendar.get(Calendar.DAY_OF_MONTH) < 10)
			result_string += "0" + calendar.get(Calendar.DAY_OF_MONTH);
		else
			result_string += calendar.get(Calendar.DAY_OF_MONTH);

		time = result_string + " 00:00:00";

		return time;
	}

	public void setCurrentDate()
	{

		Calendar cal = Calendar.getInstance();

		entryCal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		entryCal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		entryCal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));

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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		disableDoneIfAllCheckboxUnselected();

	}

}
