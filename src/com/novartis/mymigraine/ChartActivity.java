package com.novartis.mymigraine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.adapter.GalleryAdapter;
import com.novartis.mymigraine.common.Constant;
import com.novartis.mymigraine.common.Utility;
import com.novartis.mymigraine.common.View_PieChart;
import com.novartis.mymigraine.database.DataBaseHelper;
import com.novartis.mymigraine.model.PieChartDetailsModel;

public class ChartActivity extends SherlockActivity implements OnTouchListener, OnItemClickListener,
		android.view.GestureDetector.OnGestureListener, OnClickListener
{

	private ActionBar actionbar;
	private Gallery chartGallery;
	private ArrayList<String> chartNameArray;
	private GalleryAdapter adapter;
	private String itemslabel[] = { "Potential Triggers", "Trigger Exposure", "Symptom Frequency", "Pain Location",
			"Pain Level" };
	private ViewFlipper viewFlipper;
	private float lastX;
	private int position = 0;

	private LinearLayout linPTPiChart, linPTPiChartField, linCLForPTPiChart;

	private LinearLayout linTEBarChart, linCLForTEBarChart;

	private LinearLayout linSFBarChart, linCLForSFBarChart;

	private LinearLayout linPLBarChart, linCLForPLBarChart;

	private LinearLayout linPLevelBarChart, linPLevelBarChartField, linCLForPLevelPiChart;

	private TextView txt_chartCount;

	private ScrollView scrollForPT, scrollForTE, scrollForSF, scrollForPLocation, scrollForPLevel;

	private TextView txtShare, txtFilter;
	private SharedPreferences preferences;

	private String filterVia;

	int maxCount = 0;
	private LayoutInflater inflater;

	private GestureDetector gd;
	private static final int SWIPE_MIN_DISTANCE = 80;
	private static final int SWIPE_THRESHOLD_VELOCITY = 130;

	/**
	 * Database Helper class to Access the database.
	 */
	private DataBaseHelper dbhelper;
	private MyMigraineApp mymigraineapp;

	private ArrayList<PieChartDetailsModel> PotentialTriggerArraylist;
	private ArrayList<PieChartDetailsModel> PainLevelArraylist;
	private ArrayList<PieChartDetailsModel> PainLocationArraylist;
	private ArrayList<PieChartDetailsModel> SymptomsFrequencyArraylist;
	private ArrayList<PieChartDetailsModel> TriggerExposureArraylist;

	private String startTime;
	private String endTime;
	private int periodTime = -1;

	private TextView txt_PTNorecord, txt_TENorecord, txt_SFNorecord, txt_PLNorecod, txt_PLevelNorecord;

	private TextView txt_PTtime, txt_TEtime, txt_SFtime, txt_PLtime, txt_PLevelTime;

	private MenuItem menuItemMyDiary, menuItemChart, menuItemDiaryLog;

	private ProgressBar progressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home);
		actionbar = getSupportActionBar();
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_title));
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setHomeButtonEnabled(true);

		initialization();

		MarginLayoutParams mlp = (MarginLayoutParams) chartGallery.getLayoutParams();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mlp.setMargins(-(metrics.widthPixels / 2), mlp.topMargin, mlp.rightMargin, mlp.bottomMargin);

		for (int i = 0; i < itemslabel.length; i++)
		{
			chartNameArray.add(itemslabel[i]);
		}

		adapter = new GalleryAdapter(this, chartNameArray);
		chartGallery.setAdapter(adapter);

		txt_chartCount.setText(getResources().getString(R.string.chartSize, 1));
	}

	private void initialization()
	{
		viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
		viewFlipper.setOnTouchListener(this);
		gd = new GestureDetector(this);

		preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

		progressbar = (ProgressBar) findViewById(R.id.progressBar1);

		txtShare = (TextView) findViewById(R.id.home_share);
		txtFilter = (TextView) findViewById(R.id.home_filter);

		txtShare.setOnClickListener(this);
		txtFilter.setOnClickListener(this);

		scrollForPT = (ScrollView) findViewById(R.id.home_rel_center_top_potential_trigger_scroll);
		scrollForPT.setOnTouchListener(this);

		scrollForTE = (ScrollView) findViewById(R.id.home_rel_center_top_trigger_exposure_scroll);
		scrollForTE.setOnTouchListener(this);

		scrollForSF = (ScrollView) findViewById(R.id.home_rel_center_top_symptom_frequency_scroll);
		scrollForSF.setOnTouchListener(this);

		scrollForPLocation = (ScrollView) findViewById(R.id.home_rel_center_top_pain_location_scroll);
		scrollForPLocation.setOnTouchListener(this);

		scrollForPLevel = (ScrollView) findViewById(R.id.home_rel_center_top_pain_level_scroll);
		scrollForPLevel.setOnTouchListener(this);

		txt_chartCount = (TextView) findViewById(R.id.home_bottom_chartSizeCount);

		chartGallery = (Gallery) findViewById(R.id.addphoto_activity_imagegallery);
		chartNameArray = new ArrayList<String>();
		chartGallery.setOnItemClickListener(this);

		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		PotentialTriggerArraylist = new ArrayList<PieChartDetailsModel>();
		PainLevelArraylist = new ArrayList<PieChartDetailsModel>();
		PainLocationArraylist = new ArrayList<PieChartDetailsModel>();
		SymptomsFrequencyArraylist = new ArrayList<PieChartDetailsModel>();
		TriggerExposureArraylist = new ArrayList<PieChartDetailsModel>();

		linPTPiChart = (LinearLayout) findViewById(R.id.home_rel_center_below_pie);
		linPTPiChartField = (LinearLayout) findViewById(R.id.lin_bottom);
		linCLForPTPiChart = new LinearLayout(this);
		linCLForPTPiChart.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		linCLForPTPiChart.setOrientation(LinearLayout.VERTICAL);

		linTEBarChart = (LinearLayout) findViewById(R.id.home_rel_center_trigger_exposure_lin);
		linCLForTEBarChart = new LinearLayout(this);
		linCLForTEBarChart.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		linCLForTEBarChart.setOrientation(LinearLayout.VERTICAL);

		linSFBarChart = (LinearLayout) findViewById(R.id.home_rel_center_symptom_frequency_lin);
		linCLForSFBarChart = new LinearLayout(this);
		linCLForSFBarChart.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		linCLForSFBarChart.setOrientation(LinearLayout.VERTICAL);

		linPLBarChart = (LinearLayout) findViewById(R.id.home_rel_center_pain_location_lin);
		linCLForPLBarChart = new LinearLayout(this);
		linCLForPLBarChart.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		linCLForPLBarChart.setOrientation(LinearLayout.VERTICAL);

		linPLevelBarChart = (LinearLayout) findViewById(R.id.home_rel_center_below_pie_pain_level);
		linPLevelBarChartField = (LinearLayout) findViewById(R.id.home_rel_center_below_pie_lin_pain_level);
		linCLForPLevelPiChart = new LinearLayout(this);
		linCLForPLevelPiChart.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		linCLForPLevelPiChart.setOrientation(LinearLayout.VERTICAL);

		mymigraineapp = (MyMigraineApp) this.getApplicationContext();
		dbhelper = mymigraineapp.getDbhelper();

		txt_PTNorecord = (TextView) findViewById(R.id.home_rel_center_txt_NoRecord);
		txt_TENorecord = (TextView) findViewById(R.id.home_rel_center_top_trigger_exposure_scroll_txt_NoRecord);
		txt_SFNorecord = (TextView) findViewById(R.id.home_rel_center_top_symptom_frequency_scroll_txt_NoRecord);
		txt_PLNorecod = (TextView) findViewById(R.id.home_rel_center_top_pain_location_scroll_txt_NoRecord);
		txt_PLevelNorecord = (TextView) findViewById(R.id.home_rel_center_below_pain_level_txt_NoRecord);

		txt_PTtime = (TextView) findViewById(R.id.home_rel_center_top_potential_trigger_txt_subtitle);
		txt_TEtime = (TextView) findViewById(R.id.home_rel_center_top_trigger_exposure_txt_subtitle);
		txt_SFtime = (TextView) findViewById(R.id.home_rel_center_top_symptom_frequency_txt_subtitle);
		txt_PLtime = (TextView) findViewById(R.id.home_rel_center_top_pain_location_txt_subtitle);
		txt_PLevelTime = (TextView) findViewById(R.id.home_rel_center_top_pain_level_txt_subtitle);

		if (!preferences.getBoolean(getString(R.string.learnChart), false))
		{
			Utility.showLearnDialog(this, "Using the Charts");
		}

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

		String result_string = calendar.get(Calendar.YEAR) + "-" + month + "-" + calendar.get(Calendar.DAY_OF_MONTH);

		String time = result_string + " 00:00:00";

		return time;
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		txt_PTNorecord.setVisibility(View.GONE);
		txt_TENorecord.setVisibility(View.GONE);
		txt_SFNorecord.setVisibility(View.GONE);
		txt_PLNorecod.setVisibility(View.GONE);
		txt_PLevelNorecord.setVisibility(View.GONE);

		PotentialTriggerArraylist.clear();
		TriggerExposureArraylist.clear();
		SymptomsFrequencyArraylist.clear();
		PainLocationArraylist.clear();
		PainLevelArraylist.clear();
		maxCount = 0;

		startTime = preferences.getString(getString(R.string.datetime), getPreviousDateTime(60));
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		endTime = s.format(new Date());

		periodTime = preferences.getInt(getString(R.string.periodTime), 0);
		filterVia = preferences.getString(getString(R.string.filterVia), "");
		if (filterVia.equalsIgnoreCase("date") || periodTime != 0)
		{
			txt_PTtime.setText(getString(R.string.PT_SubTitle) + " since " + getFormattedDate(startTime) + ".");
			txt_TEtime.setText(getString(R.string.TE_SubTitle) + " since " + getFormattedDate(startTime) + ".");
			txt_SFtime.setText(getString(R.string.SF_SubTitle) + " since " + getFormattedDate(startTime) + ".");
			txt_PLtime.setText(getString(R.string.PL_SubTitle) + " since " + getFormattedDate(startTime) + ".");
			txt_PLevelTime.setText(getString(R.string.PLevel_SubTitle) + " since " + getFormattedDate(startTime) + ".");

		}
		else
		{
			txt_PTtime.setText(getString(R.string.PT_SubTitle) + ".");
			txt_TEtime.setText(getString(R.string.TE_SubTitle) + ".");
			txt_SFtime.setText(getString(R.string.SF_SubTitle) + ".");
			txt_PLtime.setText(getString(R.string.PL_SubTitle) + ".");
			txt_PLevelTime.setText(getString(R.string.PLevel_SubTitle) + ".");
		}

		progressbar.setVisibility(View.VISIBLE);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable()
		{

			@Override
			public void run()
			{
				drawChart();
				progressbar.setVisibility(View.GONE);
			}
		}, 1000);
	}

	private String getFormattedDate(String date)
	{

		if (!date.equals("0000-00-00"))
		{
			SimpleDateFormat currentFormatter = new SimpleDateFormat("yyyy-MM-dd");
			Date dateObj;
			try
			{
				dateObj = currentFormatter.parse(date);
				SimpleDateFormat postFormatter = new SimpleDateFormat("MM/dd/yyyy");
				return postFormatter.format(dateObj);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
				return null;
			}

		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		getSupportMenuInflater().inflate(R.menu.activity_migraine_trigger_main, menu);
		menuItemMyDiary = menu.findItem(R.id.menu_mydiary);
		menuItemChart = menu.findItem(R.id.menu_charts);
		menuItemDiaryLog = menu.findItem(R.id.menu_diary_log);
		activateChart();
		return true;

	}

	private void activateChart()
	{
		menuItemMyDiary.setIcon(R.drawable.ic_tab_mydiary_unselected);
		menuItemChart.setIcon(R.drawable.ic_tab_charts_selected);
		menuItemDiaryLog.setIcon(R.drawable.ic_tab_diarylog_unselected);
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
		case R.id.menu_mydiary:

			Intent intent = new Intent(this, MigraineTriggerMainActivity.class);
			startActivity(intent);

			break;
		case R.id.menu_charts:
			break;
		case R.id.menu_diary_log:

			Intent intentDiarylog = new Intent(this, DiaryLogDetailsActivity.class);
			startActivity(intentDiarylog);
			break;
		case R.id.menu_try_excedrin:
			Intent i = new Intent(this, TryExcedrinActivity.class);
			startActivity(i);
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
		case R.id.menu_setting:
			Intent intentsetting = new Intent(this, SettingActivity.class);
			startActivity(intentsetting);
			break;

		}
		return true;
	}

	private void drawChart()
	{

		linPTPiChart.removeAllViews();
		linPTPiChartField.removeAllViews();
		linCLForPTPiChart.removeAllViews();
		linTEBarChart.removeAllViews();
		linCLForTEBarChart.removeAllViews();
		linSFBarChart.removeAllViews();
		linCLForSFBarChart.removeAllViews();
		linPLBarChart.removeAllViews();
		linCLForPLBarChart.removeAllViews();
		linPLevelBarChart.removeAllViews();
		linPLevelBarChartField.removeAllViews();
		linCLForPLevelPiChart.removeAllViews();

		PotentialTriggerArraylist = dbhelper.getpotentialTriggerChartData(startTime, endTime);

		int size = getResources().getInteger(R.integer.pi_chart_size);

		if (PotentialTriggerArraylist != null && PotentialTriggerArraylist.size() > 0)
		{

			for (int i = 0; i < PotentialTriggerArraylist.size(); i++)
			{
				maxCount = maxCount + PotentialTriggerArraylist.get(i).getCount();
			}

			/**
			 * for Potential Trigger PI Chart
			 */
			final Bitmap mBaggroundImage;
			mBaggroundImage = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
			View_PieChart piechart = new View_PieChart(this);
			piechart.setLayoutParams(new LayoutParams(size, size));
			piechart.setGeometry(size, size, 2, 2, 2, 2, 2130837504);
			piechart.setData(PotentialTriggerArraylist, maxCount);
			piechart.invalidate();
			piechart.draw(new Canvas(mBaggroundImage));
			piechart = null;
			ImageView mImageView = new ImageView(this);
			mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			mImageView.setImageBitmap(mBaggroundImage);
			linPTPiChart.addView(mImageView);

			for (int j = 0; j < PotentialTriggerArraylist.size(); j++)
			{

				View convertView = inflater.inflate(R.layout.chart_xml, null);

				TextView txt_percentage = (TextView) convertView.findViewById(R.id.chart_txt_per);
				TextView txt_name = (TextView) convertView.findViewById(R.id.chart_txt_name);
				RelativeLayout rel_layout = (RelativeLayout) convertView.findViewById(R.id.rel_bg);

				txt_percentage.setText(String.valueOf(PotentialTriggerArraylist.get(j).count) + "%");
				txt_name.setText(PotentialTriggerArraylist.get(j).answer);
				rel_layout.setBackgroundColor(PotentialTriggerArraylist.get(j).color);

				linCLForPTPiChart.addView(convertView);
			}
			linPTPiChartField.addView(linCLForPTPiChart);
			linPTPiChartField.setDrawingCacheEnabled(true);
			linPTPiChartField.buildDrawingCache(true);
		}
		else
		{
			txt_PTNorecord.setVisibility(View.VISIBLE);
		}

		PainLevelArraylist = dbhelper.getPainLevelChartData(startTime, endTime);
		if (PainLevelArraylist != null && PainLevelArraylist.size() > 0)
		{
			maxCount = 0;
			for (int i = 0; i < PainLevelArraylist.size(); i++)
			{
				maxCount = maxCount + PainLevelArraylist.get(i).getCount();
			}

			/**
			 * for Pain Level PI Chart
			 */

			final Bitmap PainLevelImage;
			PainLevelImage = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
			View_PieChart piechart1 = new View_PieChart(this);
			piechart1.setLayoutParams(new LayoutParams(size, size));
			piechart1.setGeometry(size, size, 2, 2, 2, 2, 2130837504);
			piechart1.setData(PainLevelArraylist, maxCount);
			piechart1.invalidate();
			piechart1.draw(new Canvas(PainLevelImage));
			piechart1 = null;

			ImageView mImageView1 = new ImageView(this);
			mImageView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			mImageView1.setImageBitmap(PainLevelImage);
			linPLevelBarChart.addView(mImageView1);

			for (int i = 0; i < PainLevelArraylist.size(); i++)
			{
				View convertView = inflater.inflate(R.layout.chart_xml, null);

				TextView txt_percentage = (TextView) convertView.findViewById(R.id.chart_txt_per);
				TextView txt_name = (TextView) convertView.findViewById(R.id.chart_txt_name);
				RelativeLayout rel_layout = (RelativeLayout) convertView.findViewById(R.id.rel_bg);

				txt_percentage.setText(String.valueOf(PainLevelArraylist.get(i).count) + "%");
				txt_name.setText(PainLevelArraylist.get(i).answer);
				rel_layout.setBackgroundColor(PainLevelArraylist.get(i).color);

				linCLForPLevelPiChart.addView(convertView);
			}
			linPLevelBarChartField.addView(linCLForPLevelPiChart);
			linPLevelBarChartField.setDrawingCacheEnabled(true);
			linPLevelBarChartField.buildDrawingCache(true);
		}
		else
		{
			txt_PLevelNorecord.setVisibility(View.VISIBLE);
		}

		/**
		 * for Trigger Exposure Bar Chart
		 */

		TriggerExposureArraylist = dbhelper.getTriggerExposureChartData(startTime, endTime);
		if (TriggerExposureArraylist != null && TriggerExposureArraylist.size() > 0)
		{

			for (int j = 0; j < TriggerExposureArraylist.size(); j++)
			{

				View convertView = inflater.inflate(R.layout.barchart, null);

				TextView txt_percentage = (TextView) convertView.findViewById(R.id.barchart_percentage_txt);
				TextView txt_name = (TextView) convertView.findViewById(R.id.barchart_txt);
				SeekBar seekbar = (SeekBar) convertView.findViewById(R.id.seekBar1);

				seekbar.setOnTouchListener(new OnTouchListener()
				{

					@Override
					public boolean onTouch(View v, MotionEvent event)
					{

						return true;
					}
				});

				txt_percentage.setText(String.valueOf(TriggerExposureArraylist.get(j).count) + "%");
				txt_name.setText(TriggerExposureArraylist.get(j).answer);
				seekbar.setMax(100);
				seekbar.setProgress(TriggerExposureArraylist.get(j).count);

				linCLForTEBarChart.addView(convertView);
			}
			linTEBarChart.addView(linCLForTEBarChart);
			linTEBarChart.setDrawingCacheEnabled(true);
			linTEBarChart.buildDrawingCache(true);
		}
		else
		{
			txt_TENorecord.setVisibility(View.VISIBLE);
		}

		/**
		 * for Symptom Frequency Bar Chart
		 */
		SymptomsFrequencyArraylist = dbhelper.getSymptomsFrequencyChartData(startTime, endTime);

		if (SymptomsFrequencyArraylist != null && SymptomsFrequencyArraylist.size() > 0)
		{

			for (int j = 0; j < SymptomsFrequencyArraylist.size(); j++)
			{

				View convertView = inflater.inflate(R.layout.barchart, null);

				TextView txt_percentage = (TextView) convertView.findViewById(R.id.barchart_percentage_txt);
				TextView txt_name = (TextView) convertView.findViewById(R.id.barchart_txt);
				SeekBar seekbar = (SeekBar) convertView.findViewById(R.id.seekBar1);

				seekbar.setOnTouchListener(new OnTouchListener()
				{

					@Override
					public boolean onTouch(View v, MotionEvent event)
					{
						// TODO Auto-generated method stub
						return true;
					}
				});

				txt_percentage.setText(String.valueOf(SymptomsFrequencyArraylist.get(j).count) + "%");
				txt_name.setText(SymptomsFrequencyArraylist.get(j).answer);
				seekbar.setMax(100);
				seekbar.setProgress(SymptomsFrequencyArraylist.get(j).count);

				linCLForSFBarChart.addView(convertView);
			}
			linSFBarChart.addView(linCLForSFBarChart);
		}
		else
		{
			txt_SFNorecord.setVisibility(View.VISIBLE);
		}

		/**
		 * for Pain Location Bar Chart
		 */
		PainLocationArraylist = dbhelper.getPainLocationChartData(startTime, endTime);

		if (PainLevelArraylist != null && PainLevelArraylist.size() > 0)
		{

			for (int j = 0; j < PainLocationArraylist.size(); j++)
			{

				View convertView = inflater.inflate(R.layout.barchart, null);

				TextView txt_percentage = (TextView) convertView.findViewById(R.id.barchart_percentage_txt);
				TextView txt_name = (TextView) convertView.findViewById(R.id.barchart_txt);
				SeekBar seekbar = (SeekBar) convertView.findViewById(R.id.seekBar1);

				seekbar.setOnTouchListener(new OnTouchListener()
				{

					@Override
					public boolean onTouch(View v, MotionEvent event)
					{
						// TODO Auto-generated method stub
						return true;
					}
				});

				txt_percentage.setText(String.valueOf(PainLocationArraylist.get(j).count) + "%");
				txt_name.setText(PainLocationArraylist.get(j).answer);
				seekbar.setMax(100);
				seekbar.setProgress(PainLocationArraylist.get(j).count);

				linCLForPLBarChart.addView(convertView);
			}

			linPLBarChart.addView(linCLForPLBarChart);
		}
		else
		{
			txt_PLNorecod.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3)
	{

		viewFlipper.setInAnimation(null);
		viewFlipper.setOutAnimation(null);

		position = pos;

		if (pos == 0)
		{

			viewFlipper.setDisplayedChild(0);
			txt_chartCount.setText(getResources().getString(R.string.chartSize, 1));
		}
		else if (pos == 1)
		{
			viewFlipper.setDisplayedChild(1);
			txt_chartCount.setText(getResources().getString(R.string.chartSize, 2));
		}
		else if (pos == 2)
		{
			viewFlipper.setDisplayedChild(2);
			txt_chartCount.setText(getResources().getString(R.string.chartSize, 3));
		}
		else if (pos == 3)
		{
			viewFlipper.setDisplayedChild(3);
			txt_chartCount.setText(getResources().getString(R.string.chartSize, 4));
		}
		else if (pos == 4)
		{
			viewFlipper.setDisplayedChild(4);
			txt_chartCount.setText(getResources().getString(R.string.chartSize, 5));
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{

		if (v == viewFlipper)
		{
			swipeFlipper(event);
		}
		else
		{
			return gd.onTouchEvent(event);
		}

		return true;
	}

	private void swipeFlipper(MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			lastX = event.getX();
			break;
		case MotionEvent.ACTION_UP:

			float currentX = event.getX();
			if (lastX < currentX)
			{
				if (viewFlipper.getDisplayedChild() == 0)
					break;
				viewFlipper.setInAnimation(ChartActivity.this, R.anim.in_from_left);
				viewFlipper.setOutAnimation(ChartActivity.this, R.anim.out_to_right);
				viewFlipper.showPrevious();
				txt_chartCount.setText(getResources().getString(R.string.chartSize, position));
				position = position - 1;
				chartGallery.setSelection(position);

			}
			else if (lastX > currentX)
			{
				if (viewFlipper.getDisplayedChild() == 4)
					break;

				viewFlipper.setOutAnimation(ChartActivity.this, R.anim.out_to_left);
				viewFlipper.setInAnimation(ChartActivity.this, R.anim.in_from_right);
				viewFlipper.showNext();
				position = position + 1;
				chartGallery.setSelection(position);
				txt_chartCount.setText(getResources().getString(R.string.chartSize, position + 1));
			}

			break;
		}

	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{

		if (e1 != null && e2 != null)
		{
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
			{
				if (position < 4)
				{
					viewFlipper.setOutAnimation(ChartActivity.this, R.anim.out_to_left);
					viewFlipper.setInAnimation(ChartActivity.this, R.anim.in_from_right);
					viewFlipper.showNext();
					position = position + 1;
					chartGallery.setSelection(position);
					txt_chartCount.setText(getResources().getString(R.string.chartSize, position + 1));
				}

			}
			else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
			{

				if (position > 0)
				{
					viewFlipper.setInAnimation(ChartActivity.this, R.anim.in_from_left);
					viewFlipper.setOutAnimation(ChartActivity.this, R.anim.out_to_right);
					viewFlipper.showPrevious();
					txt_chartCount.setText(getResources().getString(R.string.chartSize, position));
					position = position - 1;
					chartGallery.setSelection(position);

				}
			}
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v)
	{

		if (v == txtShare)
		{
			Intent i = new Intent(ChartActivity.this, ShareActivity.class);
			i.putExtra("from", "chart");
			startActivity(i);
		}
		else if (v == txtFilter)
		{
			Intent i = new Intent(ChartActivity.this, FilterActivity.class);
			i.putExtra("from", "chart");
			startActivity(i);
		}
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

}
