package com.novartis.mymigraine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.Constant;
import com.novartis.mymigraine.common.SharedPrefUtility;
import com.novartis.mymigraine.common.Utility;
import com.novartis.mymigraine.database.DataBaseHelper;
import com.novartis.mymigraine.model.DiaryLogDetailsModel;

public class DiaryLogDetailsActivity extends SherlockActivity implements OnClickListener
{

	private ActionBar actionbar;
	private DataBaseHelper dbhelper;
	private MyMigraineApp mymigraineapp;
	private SharedPreferences preferences;

	private LinearLayout rel_main;

	private String filterId = "1";
	private String dateTime;
	private MenuItem menuItemMyDiary, menuItemChart, menuItemDiaryLog;
	private TextView txtShare, txtFilter;

	private LinkedHashMap<String, LinkedHashMap<Integer, ArrayList<DiaryLogDetailsModel>>> hasmap;

	private TextView txt_noRecord;
	private boolean isGalaxyS4 = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.log_details);

		initialization();

		actionbar = getSupportActionBar();
		actionbar.setDisplayShowTitleEnabled(false);

		actionbar.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_title));
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setHomeButtonEnabled(true);

		final DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		if (metrics.widthPixels >= 1080 && metrics.heightPixels >= 1920)
		{
			isGalaxyS4 = true;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		getSupportMenuInflater().inflate(R.menu.activity_migraine_trigger_main, menu);
		menuItemMyDiary = menu.findItem(R.id.menu_mydiary);
		menuItemChart = menu.findItem(R.id.menu_charts);
		menuItemDiaryLog = menu.findItem(R.id.menu_diary_log);
		activateDiaryLog();
		return true;

	}

	private void activateDiaryLog()
	{
		menuItemMyDiary.setIcon(R.drawable.ic_tab_mydiary_unselected);
		menuItemChart.setIcon(R.drawable.ic_tab_charts_unselected);
		menuItemDiaryLog.setIcon(R.drawable.ic_tab_diarylog_selected);

	}

	

	@Override
	protected void onResume()
	{
		super.onResume();

		filterId = "1";
		rel_main.removeAllViews();

		getpreferenceValue();

		
		hasmap = dbhelper.getDiaryLogDetails(filterId, dateTime);

		if (hasmap.size() > 0)
		{
			txt_noRecord.setVisibility(View.GONE);
			generateDiaryLog();
		}
		else
			txt_noRecord.setVisibility(View.VISIBLE);

	}

	private void initialization()
	{
		mymigraineapp = (MyMigraineApp) this.getApplicationContext();
		dbhelper = mymigraineapp.getDbhelper();

		preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

		rel_main = (LinearLayout) findViewById(R.id.logDetails_rel_main);

		txtShare = (TextView) findViewById(R.id.home_share);
		txtFilter = (TextView) findViewById(R.id.home_filter);

		txtShare.setOnClickListener(this);
		txtFilter.setOnClickListener(this);

		hasmap = new LinkedHashMap<String, LinkedHashMap<Integer, ArrayList<DiaryLogDetailsModel>>>();

		txt_noRecord = (TextView) findViewById(R.id.home_filter_txt_NoRecord);

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
			
			Intent intentDiarylog = new Intent(this, ChartActivity.class);
			startActivity(intentDiarylog);
			
			break;
		case R.id.menu_diary_log:
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

	private void getpreferenceValue()
	{

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

		dateTime = preferences.getString(getString(R.string.datetime), getPreviousDateTime(60));

		if (!preferences.getBoolean(getString(R.string.learnDiaryLog), false))
		{
			Utility.showLearnDialog(this, "Using the Diary Log");
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
	public void onClick(View v)
	{
		if (v == txtShare)
		{
			Intent i = new Intent(DiaryLogDetailsActivity.this, ShareActivity.class);
			i.putExtra("from", "diary");
			startActivity(i);
		}
		else if (v == txtFilter)
		{
			Intent i = new Intent(DiaryLogDetailsActivity.this, FilterActivity.class);
			i.putExtra("from", "diary");
			startActivity(i);
		}

	}

	private void generateDiaryLog()
	{

		Iterator<String> keys = hasmap.keySet().iterator();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		while (keys.hasNext())
		{
			String key = (String) keys.next();
			LayoutParams paramMonth = new LayoutParams(LayoutParams.FILL_PARENT, isGalaxyS4 ? 120 : 80);
			LinearLayout linMonth = new LinearLayout(this);
			linMonth.setLayoutParams(paramMonth);
			linMonth.setBackgroundColor(getResources().getColor(R.color.dark_green));
			TextView txtMonth = new TextView(this);
			txtMonth.setText(key);
			txtMonth.setPadding(10, 10, 10, 10);
			txtMonth.setTextColor(Color.WHITE);
			txtMonth.setTextSize(25);
			txtMonth.setTypeface(null, Typeface.BOLD);
			linMonth.addView(txtMonth);

			rel_main.addView(linMonth);

			LinkedHashMap<Integer, ArrayList<DiaryLogDetailsModel>> eventsIds = hasmap.get(key);

			Log.e("Event id", "Event id= " + eventsIds);

			Iterator<Integer> eventKeys = eventsIds.keySet().iterator();

			while (eventKeys.hasNext())
			{
				Integer string = (Integer) eventKeys.next(); // event id
				Log.e("string", "string= " + string);
				/*
				 * Data main layout
				 */
				ArrayList<DiaryLogDetailsModel> logDetailsModels = eventsIds.get(string);

				LayoutParams paramDataMain = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				LayoutParams paramDate = new LayoutParams(isGalaxyS4 ? 140 : 100, LayoutParams.FILL_PARENT);

				LinearLayout linDataMain = new LinearLayout(this);
				linDataMain.setLayoutParams(paramDataMain);
				linDataMain.setOrientation(LinearLayout.HORIZONTAL);

				try
				{
					calendar.setTime(s.parse(logDetailsModels.get(0).getDate_time()));
					calendar.set(Calendar.HOUR_OF_DAY, logDetailsModels.get(0).getStart_hour());

				}
				catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				/*
				 * Date Linear layout
				 */
				LinearLayout lindate = new LinearLayout(this);
				lindate.setLayoutParams(paramDate);
				lindate.setBackgroundDrawable(getResources().getDrawable(R.drawable.date_1));
				lindate.setGravity(Gravity.CENTER_HORIZONTAL);
				TextView txtDate = new TextView(this);
				txtDate.setText(new SimpleDateFormat("dd").format(calendar.getTime()));
				txtDate.setMinimumHeight(80);
				txtDate.setPadding(0, 10, 0, 0);
				txtDate.setTextColor(getResources().getColor(R.color.green));
				txtDate.setTextSize(30);
				txtDate.setTypeface(null, Typeface.BOLD);
				lindate.addView(txtDate);
				linDataMain.addView(lindate);
				

				LinearLayout linAnswerMain = new LinearLayout(this);
				linAnswerMain.setLayoutParams(paramDataMain);
				linAnswerMain.setOrientation(LinearLayout.VERTICAL);

				LayoutParams paramAnswerChild = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
						6f); // 5f

				LayoutParams paramCategory = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
				paramCategory.weight = 2f;

				LayoutParams paramAnswer = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
				paramAnswer.weight = 4f; // 3f

				int questionId = 0;
				ArrayList<Integer> list = new ArrayList<Integer>();

				for (int i = 0; i < logDetailsModels.size(); i++)
				{
					questionId = logDetailsModels.get(i).getRef_question_id();

					
					if (list.contains(questionId))
					{
						continue;
					}
					LinearLayout linAnswerChild = null;

					if (questionId == Constant.QUESTION_SKIP_MEAL_ID || questionId == Constant.QUESTION_FASTING_ID
							|| questionId == Constant.QUESTION_FOOD_ID)
					{
						linAnswerChild = (LinearLayout) linAnswerMain.findViewWithTag("" + string
								+ Constant.QUESTION_SKIP_MEAL_ID);
						if (linAnswerChild == null)
						{
							linAnswerChild = (LinearLayout) linAnswerMain.findViewWithTag("" + string
									+ Constant.QUESTION_FASTING_ID);
							if (linAnswerChild == null)
							{
								linAnswerChild = (LinearLayout) linAnswerMain.findViewWithTag("" + string
										+ Constant.QUESTION_FOOD_ID);
							}
						}
					}

					if (linAnswerChild == null)
					{
						linAnswerChild = new LinearLayout(this);
						linAnswerChild.setLayoutParams(paramAnswerChild);
						linAnswerChild.setOrientation(LinearLayout.HORIZONTAL);
						linAnswerChild.setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_data));
						linAnswerChild.setTag("" + string + questionId);
						linAnswerMain.addView(linAnswerChild);
					}
					else
						linAnswerChild.setTag("" + string + questionId);

					if (logDetailsModels.get(i).getRef_question_id() == 1)
					{

						if (logDetailsModels.get(i).getRef_answer_id() == 1)
						{
							linAnswerChild.setBackgroundColor(getResources().getColor(R.color.mild));
						}
						else if (logDetailsModels.get(i).getRef_answer_id() == 2)
						{
							linAnswerChild.setBackgroundColor(getResources().getColor(R.color.moderate));
						}
						else if (logDetailsModels.get(i).getRef_answer_id() == 3)
						{
							linAnswerChild.setBackgroundColor(getResources().getColor(R.color.severe));
						}
					}
					TextView txtAnswer = null;
					String ans = "";
					if (questionId == Constant.QUESTION_SKIP_MEAL_ID || questionId == Constant.QUESTION_FASTING_ID
							|| questionId == Constant.QUESTION_FOOD_ID)
					{
						txtAnswer = (TextView) linAnswerChild.findViewWithTag(Constant.QUESTION_SKIP_MEAL_ID);
						if (txtAnswer == null)
						{
							txtAnswer = (TextView) linAnswerChild.findViewWithTag(Constant.QUESTION_FASTING_ID);
							if (txtAnswer == null)
							{
								txtAnswer = (TextView) linAnswerChild.findViewWithTag(Constant.QUESTION_FOOD_ID);
							}
						}
					}

					if (txtAnswer == null)
					{
						txtAnswer = new TextView(this);
						txtAnswer.setTag(questionId);
						linAnswerChild.addView(txtAnswer);
					}
					else
					{
						txtAnswer.setTag(questionId);
						ans = txtAnswer.getText().toString() + "\n";
					}

					for (int j = i; j < logDetailsModels.size(); j++)
					{
						// Log.d("QuestionId", "" +
						// logDetailsModels.get(j).getRef_question_id());

						if (questionId == logDetailsModels.get(j).getRef_question_id())
						{

							if (questionId == Constant.QUESTION_SKIP_MEAL_ID)
							{
								
								if (ans.split("Skipped", -1).length>1)
									ans += logDetailsModels.get(j).getAnswer().replace("Skipped ", "") + ", ";
								else
									ans += logDetailsModels.get(j).getAnswer() + ", ";
							}
							else
								ans += logDetailsModels.get(j).getAnswer() + "\n";
							// Log.d("ans",
							// logDetailsModels.get(j).getAnswer());
						}
					}
					if (ans.endsWith(", "))
					{
						ans = ans.substring(0, ans.length() - 2);

					}
					list.add(questionId);

					/*
					 * if have headache then show list entry with changed bg
					 * color and headache info
					 */
					txtAnswer.setPadding(10, 10, 0, 10);
					txtAnswer.setLayoutParams(paramAnswer);
					txtAnswer.setTextSize(13); // 15
					txtAnswer.setTypeface(null, Typeface.NORMAL);
					if (logDetailsModels.get(i).getRef_question_id() == 1)
					{

						SimpleDateFormat hourformat = new SimpleDateFormat("hh");
						SimpleDateFormat ampmFormat = new SimpleDateFormat("a");
						final String hourslabel;
						final String minute;
						if (logDetailsModels.get(0).getStart_minute() < 10)
						{
							minute = "0" + logDetailsModels.get(0).getStart_minute();
						}
						else
						{
							minute = String.valueOf(logDetailsModels.get(0).getStart_minute());
						}
						hourslabel = (logDetailsModels.get(i).getDuration() / 60) == 1 ? "hour" : "hours";
						txtAnswer.setText("" + hourformat.format(calendar.getTime()) + ":" + minute + " "
								+ ampmFormat.format(calendar.getTime()) + ", lasted for "
								+ logDetailsModels.get(0).getDuration() / 60 + " " + hourslabel);

						txtAnswer.setTextColor(getResources().getColor(R.color.white));

					}
					else
					{

						txtAnswer.setText(ans.trim());
						txtAnswer.setTextColor(getResources().getColor(R.color.black));
					}
					TextView txtCategory = null;
					if (linAnswerChild.findViewWithTag("Diet") == null)
					{
						txtCategory = new TextView(this);
						txtCategory.setLayoutParams(paramCategory);
						txtCategory.setGravity(Gravity.RIGHT);
						txtCategory.setPadding(0, 10, 10, 10);
						txtCategory.setTextSize(13); // 15
						txtCategory.setTypeface(null, Typeface.NORMAL);
						linAnswerChild.addView(txtCategory);
					}
					else
					{
						txtCategory = (TextView) linAnswerChild.findViewWithTag("Diet");
					}

					if (logDetailsModels.get(i).getRef_question_id() == 1)
					{

						txtCategory.setText("Headache");
						txtCategory.setTextColor(getResources().getColor(R.color.white));

					}
					else
					{
						txtCategory.setTextColor(getResources().getColor(R.color.black));
						if (logDetailsModels.get(i).getRef_question_id() == 3)
							txtCategory.setText("Warning signs");
						else if (logDetailsModels.get(i).getRef_question_id() == 2)
							txtCategory.setText("Location");
						else if (logDetailsModels.get(i).getRef_question_id() == 4)
							txtCategory.setText("Symptoms");
						else if (logDetailsModels.get(i).getRef_question_id() == 7
								|| logDetailsModels.get(i).getRef_question_id() == 8
								|| logDetailsModels.get(i).getRef_question_id() == 9)
						{
							txtCategory.setText("Diet");
							txtCategory.setTag("Diet");
						}
						else if (logDetailsModels.get(i).getRef_question_id() == 10)
							txtCategory.setText("Lifestyle");
						else if (logDetailsModels.get(i).getRef_question_id() == 11)
							txtCategory.setText("Environment");
						else if (logDetailsModels.get(i).getRef_question_id() == 12)
							txtCategory.setText("Menstruation");
						else if (logDetailsModels.get(i).getRef_question_id() == 13)
							txtCategory.setText("Notes");
					}

					
				}
				
				/**
				 * for Notes layout
				 */

				if (preferences.getBoolean(getString(R.string.notes), false)
						&& logDetailsModels.get(0).getNotes() != null
						&& !logDetailsModels.get(0).getNotes().equalsIgnoreCase(""))
				{
					LinearLayout linNotes = new LinearLayout(this);
					linNotes.setLayoutParams(paramAnswerChild);
					linNotes.setOrientation(LinearLayout.HORIZONTAL);
					linNotes.setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_data));

					TextView txtNoteAnswer = new TextView(this);
					txtNoteAnswer.setPadding(10, 10, 0, 10);
					txtNoteAnswer.setLayoutParams(paramAnswer);
					txtNoteAnswer.setTextSize(13); // 15
					txtNoteAnswer.setTypeface(null, Typeface.NORMAL);
					txtNoteAnswer.setText(logDetailsModels.get(0).getNotes());
					txtNoteAnswer.setTextColor(getResources().getColor(R.color.black));

					TextView txtNoteCategory = new TextView(this);
					txtNoteCategory.setLayoutParams(paramCategory);
					txtNoteCategory.setGravity(Gravity.RIGHT);
					txtNoteCategory.setPadding(0, 10, 10, 10);
					txtNoteCategory.setTextSize(13);// 15
					txtNoteCategory.setTypeface(null, Typeface.NORMAL);
					txtNoteCategory.setTextColor(getResources().getColor(R.color.black));
					txtNoteCategory.setText("Notes");

					linNotes.addView(txtNoteAnswer);
					linNotes.addView(txtNoteCategory);

					linAnswerMain.addView(linNotes);
				}

				// }

				if (logDetailsModels.size() == 1 && logDetailsModels.get(0).getHasHedache() == 0
						&& logDetailsModels.get(0).getRef_question_id() == 8
						&& !preferences.getBoolean(getString(R.string.notes), false))
				{
				}
				else
				{
					linDataMain.addView(linAnswerMain);
					rel_main.addView(linDataMain);
					LayoutParams paramDivider = new LayoutParams(LayoutParams.FILL_PARENT, 2);
					paramDivider.setMargins(0, 0, 0, 2);
					View view = new View(this);
					view.setLayoutParams(paramDivider);
					view.setBackgroundColor(getResources().getColor(R.color.devider_color));
					rel_main.addView(view);
				}

			}

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
