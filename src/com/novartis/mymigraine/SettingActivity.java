package com.novartis.mymigraine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.SharedPrefUtility;

public class SettingActivity extends SherlockActivity implements OnCheckedChangeListener
{

	private Button btn_setting;
	private ToggleButton btnYesNo;
	boolean yesno = true;
	private ActionBar actionBar;
	private String from;
	private SharedPreferences preference;

	// private ActionBar actionbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		actionBar = getSupportActionBar();
		createActionBar();

		from = getIntent().getStringExtra("from");
		preference = getSharedPreferences(getResources().getString(com.novartis.mymigraine.R.string.app_name),
				Context.MODE_PRIVATE);

		btnYesNo = (ToggleButton) findViewById(R.id.setting_switch);
		btnYesNo.setOnCheckedChangeListener(this);

		yesno = preference.getBoolean(SharedPrefUtility.ASK_MENSTRUATION_QUESTION, true);

		if (yesno)
		{
			btnYesNo.setChecked(true);
		}
		else
		{
			btnYesNo.setChecked(false);
		}

		btn_setting = (Button) findViewById(R.id.setting_btn_continue);

		btn_setting.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				SharedPrefUtility.saveAskMenstruationQuestion(SettingActivity.this, yesno);

				if (from != null && !from.equalsIgnoreCase("") && from.equalsIgnoreCase("TCScreen"))
				{
					Intent i = new Intent(SettingActivity.this, MigraineTriggerMainActivity.class);
					startActivity(i);
				}
				finish();
			}
		});

	}

	private void createActionBar()
	{

		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_title_no_pad));
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{

		yesno = isChecked;
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
