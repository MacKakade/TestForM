package com.novartis.mymigraine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.Constant;

@SuppressLint("SetJavaScriptEnabled")
public class HelpActivity extends SherlockActivity
{

	private RelativeLayout learnDetailPanel;
	private ActionBar actionbar;
	private MenuItem menuItemMyDiary, menuItemChart, menuItemHelp;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_detail);
		learnDetailPanel = (RelativeLayout) findViewById(R.id.learnDetailPanel);
		actionbar = getSupportActionBar();
		createActionBar();
		String url = getIntent().getExtras().getString(Constant.Url);
		WebView mWebView = new WebView(this);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		// mWebView.getSettings().setPluginsEnabled(true);
		mWebView.loadUrl(url);
		learnDetailPanel.addView(mWebView);
	}

	private void createActionBar()
	{
		actionbar = getSupportActionBar();
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_title));
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		getSupportMenuInflater().inflate(R.menu.activity_help, menu);
		menuItemMyDiary = menu.findItem(R.id.menu_mydiary);
		menuItemChart = menu.findItem(R.id.menu_charts);
		menuItemHelp = menu.findItem(R.id.menu_help);
		activateLearn();
		return true;

	}

	private void activateLearn()
	{
		menuItemMyDiary.setIcon(R.drawable.ic_tab_mydiary_unselected);
		menuItemChart.setIcon(R.drawable.ic_tab_charts_unselected);
		menuItemHelp.setIcon(R.drawable.ic_tab_learn_selected);
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			onBackPressed();
			finish();
			break;
		case R.id.menu_mydiary:
			Intent intentMydiary = new Intent(this, MigraineTriggerMainActivity.class);
			startActivity(intentMydiary);
			finish();
			break;
		case R.id.menu_charts:
			Intent intentChart = new Intent(this, ChartActivity.class);
			startActivity(intentChart);
			finish();
			break;
		case R.id.menu_diary_log:
			Intent intentDiaryLog = new Intent(this, DiaryLogDetailsActivity.class);
			startActivity(intentDiaryLog);
			finish();
			break;
		case R.id.menu_try_excedrin:
			Intent intentTryExcedrin = new Intent(this, TryExcedrinActivity.class);
			startActivity(intentTryExcedrin);
			finish();
			break;
		case R.id.menu_learn:
			Intent learnIntent = new Intent(this, LearnActivity.class);
			startActivity(learnIntent);
			finish();
			break;
		case R.id.menu_help:
			break;
		case R.id.menu_setting:
			Intent intentsetting = new Intent(this, SettingActivity.class);
			startActivity(intentsetting);
			break;
		}
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

}
