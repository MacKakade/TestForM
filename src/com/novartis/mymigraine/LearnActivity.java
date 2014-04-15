package com.novartis.mymigraine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.Constant;

public class LearnActivity extends SherlockActivity implements OnClickListener
{
	private Button btnWhatisMigraine, btnCopingWithMigraine, btnMigraineTriggers, btnUsingThisApp;
	private ActionBar actionbar;
	private MenuItem menuItemMyDiary, menuItemChart, menuItemLearn;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn);
		actionbar = getSupportActionBar();
		createActionBar();

		btnWhatisMigraine = (Button) findViewById(R.id.btnWhatisMigraine);
		btnWhatisMigraine.setOnClickListener(this);
		btnCopingWithMigraine = (Button) findViewById(R.id.btnCopingWithMigraine);
		btnCopingWithMigraine.setOnClickListener(this);
		btnMigraineTriggers = (Button) findViewById(R.id.btnMigraineTriggers);
		btnMigraineTriggers.setOnClickListener(this);
		btnUsingThisApp = (Button) findViewById(R.id.btnUsingThisApp);
		btnUsingThisApp.setOnClickListener(this);
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
	public void onClick(View v)
	{
		if (v == btnWhatisMigraine)
		{
			Intent whtIsMigraineIntent = new Intent(LearnActivity.this, LearnDetailActivity.class);
			whtIsMigraineIntent.putExtra(Constant.Url, Constant.VIEW_WHAT_IS_MIGRAINE_ASSET_URL);
			startActivity(whtIsMigraineIntent);
		}
		else if (v == btnCopingWithMigraine)
		{
			Intent copingWithMigraineIntent = new Intent(LearnActivity.this, LearnDetailActivity.class);
			copingWithMigraineIntent.putExtra(Constant.Url, Constant.VIEW_COPING_WITH_MIGRAINES_ASSET_URL);
			startActivity(copingWithMigraineIntent);
		}
		else if (v == btnMigraineTriggers)
		{
			Intent migraineTriggersIntent = new Intent(LearnActivity.this, LearnDetailActivity.class);
			migraineTriggersIntent.putExtra(Constant.Url, Constant.VIEW_MIGRAINE_TRIGGERS_ASSET_URL);
			startActivity(migraineTriggersIntent);
		}
		else if (v == btnUsingThisApp)
		{
			Intent usingThisAppIntent = new Intent(LearnActivity.this, LearnDetailActivity.class);
			usingThisAppIntent.putExtra(Constant.Url, Constant.VIEW_USING_THIS_APP_ASSET_URL);
			startActivity(usingThisAppIntent);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		getSupportMenuInflater().inflate(R.menu.activity_learn, menu);
		menuItemMyDiary = menu.findItem(R.id.menu_mydiary);
		menuItemChart = menu.findItem(R.id.menu_charts);
		menuItemLearn = menu.findItem(R.id.menu_learn);
		activateLearn();
		return true;

	}

	private void activateLearn()
	{
		menuItemMyDiary.setIcon(R.drawable.ic_tab_mydiary_unselected);
		menuItemChart.setIcon(R.drawable.ic_tab_charts_unselected);
		menuItemLearn.setIcon(R.drawable.ic_tab_learn_selected);
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
			break;
		case R.id.menu_help:
			Intent usingThisAppIntent = new Intent(this, HelpActivity.class);
			usingThisAppIntent.putExtra(Constant.Url, Constant.VIEW_USING_THIS_APP_ASSET_URL);
			startActivity(usingThisAppIntent);
			finish();
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
