package com.novartis.mymigraine;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.DateTimePicker;
import com.novartis.mymigraine.common.DateTimePicker.DateWatcher;
import com.novartis.mymigraine.common.GeneratePDF;
import com.novartis.mymigraine.common.SharedPrefUtility;
import com.novartis.mymigraine.common.Utility;
import com.novartis.mymigraine.common.View_PieChart;
import com.novartis.mymigraine.database.DataBaseHelper;
import com.novartis.mymigraine.model.DiaryLogDetailsModel;
import com.novartis.mymigraine.model.PieChartDetailsModel;

public class ShareActivity extends SherlockActivity implements OnClickListener, DateWatcher
{

	private ActionBar actionBar;
	private Button btnPrint, btnEmail;

	private LinearLayout linPTPiChart, linPTPiChartField, linCLForPTPiChart;

	private LinearLayout linTEBarChart, linCLForTEBarChart;

	private LinearLayout linSFBarChart, linCLForSFBarChart;

	private LinearLayout linPLBarChart, linCLForPLBarChart;

	private LinearLayout linPLevelBarChart, linPLevelBarChartField, linCLForPLevelPiChart;

	private RelativeLayout rellayout;

	int maxCount = 0;
	private String filterId = "1";

	private LayoutInflater inflater;

	private Bitmap bmpPotentialPiChart;
	private Bitmap bmpPainLevelImage;
	private Context context;
	private GeneratePDF generatePDF;

	private String startTime;
	private String endTime;

	private Button btnChooseDate, btnChoosePeriod;

	private ToggleButton toggleChart;
	private ToggleButton toggleDiary;

	private DataBaseHelper dbhelper;
	private MyMigraineApp mymigraineapp;

	private ArrayList<PieChartDetailsModel> PotentialTriggerArraylist;
	private ArrayList<PieChartDetailsModel> PainLevelArraylist;
	private ArrayList<PieChartDetailsModel> PainLocationArraylist;
	private ArrayList<PieChartDetailsModel> SymptomsFrequencyArraylist;
	private ArrayList<PieChartDetailsModel> TriggerExposureArraylist;

	// private String time;
	private SharedPreferences preferences;
	private String filterVia = "";
	private int periodTime = -1;
	private Calendar entryCal;
	private int mYear;
	private int mMonth;
	private int mDay;
	private String from;
	private LinkedHashMap<String, LinkedHashMap<Integer, ArrayList<DiaryLogDetailsModel>>> hasmap;

	private boolean flagForPdf = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.share);
		actionBar = getSupportActionBar();
		createActionBar();
		initialization();
		context = this;
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
		endTime = s.format(new Date());

		from = getIntent().getStringExtra("from");

		if (from.equalsIgnoreCase("diary"))
		{
			rellayout.setVisibility(View.GONE);
		}
		else
		{
			rellayout.setVisibility(View.VISIBLE);
		}

		preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

		entryCal = Calendar.getInstance();

		getpreferenceValue();

	}

	private void initialization()
	{

		rellayout = (RelativeLayout) findViewById(R.id.relativeLayout7);

		btnPrint = (Button) findViewById(R.id.share_btn_print);
		btnPrint.setOnClickListener(this);

		btnEmail = (Button) findViewById(R.id.share_btn_email);
		btnEmail.setOnClickListener(this);

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

		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		generatePDF = new GeneratePDF();

		PotentialTriggerArraylist = new ArrayList<PieChartDetailsModel>();
		PainLevelArraylist = new ArrayList<PieChartDetailsModel>();
		PainLocationArraylist = new ArrayList<PieChartDetailsModel>();
		SymptomsFrequencyArraylist = new ArrayList<PieChartDetailsModel>();
		TriggerExposureArraylist = new ArrayList<PieChartDetailsModel>();

		mymigraineapp = (MyMigraineApp) this.getApplicationContext();
		dbhelper = mymigraineapp.getDbhelper();

		btnChooseDate = (Button) findViewById(R.id.filter_btn_chooseDate);
		btnChooseDate.setOnClickListener(this);

		btnChoosePeriod = (Button) findViewById(R.id.filter_btn_setPeriod);
		btnChoosePeriod.setOnClickListener(this);

		toggleChart = (ToggleButton) findViewById(R.id.share_toggleChart);
		toggleDiary = (ToggleButton) findViewById(R.id.share_toggleDiary);

		toggleChart.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{

				flagForPdf = false;
			}
		});

		toggleDiary.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				flagForPdf = false;
			}
		});

	}

	private void getpreferenceValue()
	{

		startTime = preferences.getString(getString(R.string.datetime), getPreviousDateTime(60));

		filterVia = preferences.getString(getString(R.string.filterVia), "");

		if (filterVia.equalsIgnoreCase("date"))
		{
			btnChooseDate.setBackgroundColor(getResources().getColor(R.color.green));
			btnChoosePeriod.setBackgroundColor(getResources().getColor(R.color.light_gray));
			mDay = preferences.getInt(getString(R.string.day), entryCal.get(Calendar.DAY_OF_MONTH));
			mMonth = preferences.getInt(getString(R.string.month), entryCal.get(Calendar.MONTH));
			mYear = preferences.getInt(getString(R.string.year), entryCal.get(Calendar.YEAR));

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

		if (preferences.getBoolean(getString(R.string.warningSign), true))
			filterId += ",3";
		if (preferences.getBoolean(getString(R.string.location), false))
			filterId += ",2";
		if (preferences.getBoolean(getString(R.string.symptoms), false))
			filterId += ",4";
		if (preferences.getBoolean(getString(R.string.diet), false))
			filterId += ",7,8,9";
		if (preferences.getBoolean(getString(R.string.lifestyle), false))
			filterId += ",10";
		if (preferences.getBoolean(getString(R.string.environment), false))
			filterId += ",11";
		if (SharedPrefUtility.isMenstruationQuestionAsk(this))
			filterId += ",12";
		if (preferences.getBoolean(getString(R.string.notes), false))
			filterId += ",13";

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
	public void onClick(View v)
	{

		if (v == btnPrint)
		{

			if (Utility.isNetworkConnectionAvailable(this))
			{

				if (toggleChart.isChecked() == true || toggleDiary.isChecked() == true)
				{

					final int size = dbhelper.getdata(startTime);

					if (size > 0)
					{

						if (!flagForPdf)
						{

							final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Generate PDF File...");
							progressDialog.setCancelable(true);
							progressDialog.setCanceledOnTouchOutside(false);

							if (toggleChart.isChecked())
								removeViewAndDrawChart();

							if (toggleDiary.isChecked())
								hasmap = dbhelper.getDiaryLogDetails(filterId, startTime);

							final Handler handler = new Handler();
							handler.postDelayed(new Runnable()
							{
								@Override
								public void run()
								{

									final String time = getFormattedDate(startTime);

									generatePDF.generatePDF(bmpPotentialPiChart, linPTPiChartField.getDrawingCache(),
											linTEBarChart.getDrawingCache(), linSFBarChart.getDrawingCache(),
											linPLBarChart.getDrawingCache(), bmpPainLevelImage,
											linPLevelBarChartField.getDrawingCache(), context, progressDialog,
											toggleChart.isChecked(), toggleDiary.isChecked(), hasmap,
											preferences.getBoolean(getString(R.string.notes), false), time, periodTime);
									// Mihir 20-07-2013
									File file = new File(Environment.getExternalStorageDirectory() + File.separator
											+ "Pdffiles" + File.separator + getString(R.string.pdf_file_name));
									Uri path = Uri.fromFile(file);
									Intent printIntent = new Intent(ShareActivity.this, PrintPDFActivity.class);
									printIntent.setDataAndType(path, getString(R.string.pdfdatatype));
									printIntent.putExtra(getString(R.string.title), "Title");
									startActivity(printIntent);

									flagForPdf = true;
								}
							}, 2000);
						}
						else
						{

							File file = new File(Environment.getExternalStorageDirectory() + File.separator
									+ "Pdffiles" + File.separator + getString(R.string.pdf_file_name));
							Uri path = Uri.fromFile(file);
							Intent printIntent = new Intent(ShareActivity.this, PrintPDFActivity.class);
							printIntent.setDataAndType(path, getString(R.string.pdfdatatype));
							printIntent.putExtra(getString(R.string.title), "Title");
							startActivity(printIntent);
						}
					}
					else
					{
						Utility.showAlert(this, getString(R.string.app_name), "No migraine event available.");
					}
				}
				else
				{
					Utility.showAlert(this, getString(R.string.app_name), "Please enable at least one option.");
				}
			}
			else
			{
				Utility.showAlert(this, getString(R.string.app_name), "Please check your internet connection.");
			}

		}
		else if (v == btnEmail)
		{

			if (Utility.isNetworkConnectionAvailable(this))
			{

				if (toggleChart.isChecked() == true || toggleDiary.isChecked() == true)
				{

					final int size = dbhelper.getdata(startTime);

					if (size > 0)
					{
						if (!flagForPdf)
						{

							final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Generate PDF File...");
							progressDialog.setCancelable(true);
							progressDialog.setCanceledOnTouchOutside(false);

							if (toggleChart.isChecked())
								removeViewAndDrawChart();

							if (toggleDiary.isChecked())
								hasmap = dbhelper.getDiaryLogDetails(filterId, startTime);

							final Handler handler = new Handler();
							handler.postDelayed(new Runnable()
							{

								@Override
								public void run()
								{

									final String time = getFormattedDate(startTime);

									generatePDF.generatePDF(bmpPotentialPiChart, linPTPiChartField.getDrawingCache(),
											linTEBarChart.getDrawingCache(), linSFBarChart.getDrawingCache(),
											linPLBarChart.getDrawingCache(), bmpPainLevelImage,
											linPLevelBarChartField.getDrawingCache(), context, progressDialog,
											toggleChart.isChecked(), toggleDiary.isChecked(), hasmap,
											preferences.getBoolean(getString(R.string.notes), false), time, periodTime);

									// do email
									sendEmail();
									flagForPdf = true;
								}

							}, 2000);
						}
						else
						{

							sendEmail();
						}
					}
					else
					{
						Utility.showAlert(this, getString(R.string.app_name), "No migraine event available.");
					}

				}
				else
				{
					Utility.showAlert(this, getString(R.string.app_name), "Please enable at least one option.");
				}
			}
			else
			{
				Utility.showAlert(this, getString(R.string.app_name), "Please check your internet connection.");
			}

		}
		else if (v == btnChooseDate)
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

					startTime = result_string + " 00:00:00";

					filterVia = "date";

					periodTime = -1;

					mYear = calendar.get(Calendar.YEAR);
					mMonth = cal.get(Calendar.MONTH);
					mDay = mDateTimePicker.getDay();

					Editor editor = preferences.edit();
					editor.putString(getString(R.string.datetime), startTime);
					editor.putString(getString(R.string.filterVia), filterVia);
					editor.putInt(getString(R.string.day), mDay);
					editor.putInt(getString(R.string.month), mMonth);
					editor.putInt(getString(R.string.year), mYear);
					editor.putInt(getString(R.string.periodTime), 0);
					editor.commit();

					btnChooseDate.setBackgroundColor(getResources().getColor(R.color.green));
					btnChoosePeriod.setBackgroundColor(getResources().getColor(R.color.light_gray));

					flagForPdf = false;

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
						startTime = getPreviousDateTime(60);
					}
					else if (position == 1)
					{
						startTime = getPreviousDateTime(60);
					}
					else if (position == 2)
					{
						startTime = getPreviousDateTime(30);
					}
					else if (position == 3)
					{
						startTime = getPreviousDateTime(15);
					}
					else if (position == 4)
					{
						startTime = getPreviousDateTime(0);
					}
					else if (position == 5)
					{
						startTime = getPreviousDateTime(1);
					}

					filterVia = "period";
					periodTime = position;

					Editor editor = preferences.edit();
					editor.putString(getString(R.string.datetime), startTime);
					editor.putString(getString(R.string.filterVia), filterVia);
					editor.putInt(getString(R.string.periodTime), periodTime);
					editor.commit();

					setCurrentDate();

					btnChooseDate.setBackgroundColor(getResources().getColor(R.color.light_gray));
					btnChoosePeriod.setBackgroundColor(getResources().getColor(R.color.green));

					flagForPdf = false;

					dialog.dismiss();
				}
			});

			AlertDialog alertDialog = builder.create();
			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.setCancelable(true);
			alertDialog.setCanceledOnTouchOutside(true);
			alertDialog.show();

		}

	}

	private void sendEmail()
	{
		// Mihir 20-07-2013
		File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Pdffiles" + File.separator
				+ getString(R.string.pdf_file_name));
		Uri path = Uri.fromFile(file);

		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Migraine Triggers Information");

		emailIntent.putExtra(Intent.EXTRA_STREAM, path);

		final String bodyText = "Attached is the data you have been collecting with the My Migraine Triggers� diary, by the makers of Excedrin�. If you notice any entries you would like to modify you may edit them within the application. Simply choose to view previous entries from the My Diary tab and choose the date to edit."
				+ "\n\nPlease remember to print this out and bring it with you to your next appointment with your doctor or forward it to him/her via email. Only a doctor can determine if your headache is a migraine. The data may help with your doctor's diagnosis and treatment recommendation."
				+ "\n\nSincerely," + "\nThe Excedrin� Migraine Team";

		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, bodyText);

		ShareActivity.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

	}

	public void removeViewAndDrawChart()
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

		PotentialTriggerArraylist.clear();
		TriggerExposureArraylist.clear();
		SymptomsFrequencyArraylist.clear();
		PainLocationArraylist.clear();
		PainLevelArraylist.clear();
		maxCount = 0;

		drawChart();
	}

	private void drawChart()
	{

		PotentialTriggerArraylist = dbhelper.getpotentialTriggerChartData(startTime, endTime);

		for (int i = 0; i < PotentialTriggerArraylist.size(); i++)
		{
			maxCount = maxCount + PotentialTriggerArraylist.get(i).getCount();
		}

		/**
		 * for Potential Trigger PI Chart
		 */

		int size = 220;

		bmpPotentialPiChart = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		View_PieChart piechart = new View_PieChart(this);
		piechart.setLayoutParams(new LayoutParams(size, size));
		piechart.setGeometry(size, size, 2, 2, 2, 2, 2130837504);
		piechart.setData(PotentialTriggerArraylist, maxCount);
		piechart.invalidate();
		piechart.draw(new Canvas(bmpPotentialPiChart));
		piechart = null;
		ImageView mImageView = new ImageView(this);
		mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mImageView.setImageBitmap(bmpPotentialPiChart);
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

		/**
		 * for Pain Level PI Chart
		 */
		maxCount = 0;
		PainLevelArraylist = dbhelper.getPainLevelChartData(startTime, endTime);

		for (int i = 0; i < PainLevelArraylist.size(); i++)
		{
			maxCount = maxCount + PainLevelArraylist.get(i).getCount();
		}

		bmpPainLevelImage = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		View_PieChart piechart1 = new View_PieChart(this);
		piechart1.setLayoutParams(new LayoutParams(size, size));
		piechart1.setGeometry(size, size, 2, 2, 2, 2, 2130837504);
		piechart1.setData(PainLevelArraylist, maxCount);
		piechart1.invalidate();
		piechart1.draw(new Canvas(bmpPainLevelImage));
		piechart1 = null;

		ImageView mImageView1 = new ImageView(this);
		mImageView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mImageView1.setImageBitmap(bmpPainLevelImage);
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

		/**
		 * for Trigger Exposure Bar Chart
		 */

		TriggerExposureArraylist = dbhelper.getTriggerExposureChartData(startTime, endTime);

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
					// TODO Auto-generated method stub
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

		/**
		 * for Symptom Frequency Bar Chart
		 */
		SymptomsFrequencyArraylist = dbhelper.getSymptomsFrequencyChartData(startTime, endTime);

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
		linSFBarChart.setDrawingCacheEnabled(true);
		linSFBarChart.buildDrawingCache(true);

		/**
		 * for Pain Location Bar Chart
		 */

		PainLocationArraylist = dbhelper.getPainLocationChartData(startTime, endTime);

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
		linPLBarChart.setDrawingCacheEnabled(true);
		linPLBarChart.buildDrawingCache(true);

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

		String time = result_string + " 00:00:00";

		return time;
	}

	@Override
	public void onDateChanged(Calendar c)
	{

		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

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
}
