package com.novartis.mymigraine;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.Constant;
import com.novartis.mymigraine.common.IMigraineEvent;
import com.novartis.mymigraine.common.IMigraineEventChangedListener;
import com.novartis.mymigraine.fragment.HeadacheStep1Fragment;
import com.novartis.mymigraine.fragment.HeadacheStep2Fragment;
import com.novartis.mymigraine.fragment.HeadacheStep3Fragment;
import com.novartis.mymigraine.fragment.HeadacheStep4Fragment;
import com.novartis.mymigraine.fragment.HeadacheStep5Fragment;
import com.novartis.mymigraine.fragment.HeadacheStep6Fragment;
import com.novartis.mymigraine.model.MigraineEvent;

public class HeadachePositiveStepsActivity extends SherlockFragmentActivity implements OnClickListener, IMigraineEvent,
		IMigraineEventChangedListener
{
	private TextView txtViewSteps;
	private ImageButton btnStepPrevious, btnStepNext;
	private int step = 1;
	private String stepString = "Step " + step + " of 6";
	private MigraineEvent event;
	ActionBar actionBar;
	private Calendar eventCreateCal;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_headache_positive_steps);
		actionBar = getSupportActionBar();
		createActionBar();

		if (getIntent().getExtras().containsKey(Constant.KEY_TIMESTAMP))

			eventCreateCal = (Calendar) getIntent().getExtras().get(Constant.KEY_TIMESTAMP);
		else
			eventCreateCal = Calendar.getInstance();

		txtViewSteps = (TextView) findViewById(R.id.txtViewSteps);
		txtViewSteps.setText(stepString);
		btnStepPrevious = (ImageButton) findViewById(R.id.btnStepPrevious);
		btnStepPrevious.setOnClickListener(this);
		btnStepNext = (ImageButton) findViewById(R.id.btnStepNext);
		btnStepNext.setOnClickListener(this);
		createMigraineEvent();
		loadFragment(step);
		btnStepPrevious.setEnabled(false);
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
	public void createMigraineEvent()
	{
		event = new MigraineEvent();
		event.setHasHeadache(true);
		event.setDateTime(eventCreateCal.getTime());
	}

	@Override
	public MigraineEvent getMigraineEvent()
	{
		return event;
	}

	@Override
	public void onClick(View v)
	{
		if (v == btnStepNext)
		{

			if (step <= 6)
			{
				step++;
				if (step == 6)
					btnStepNext.setEnabled(false);
				else if (step > 1)
					btnStepPrevious.setEnabled(true);

				loadFragment(step);

			}

		}
		else if (v == btnStepPrevious)
		{

			if (step >= 1)
			{
				step--;
				if (step == 1)
					btnStepPrevious.setEnabled(false);
				else if (step <= 6)
					btnStepNext.setEnabled(true);

				loadFragment(step);

			}

		}

	}

	private void loadFragment(int step)
	{

		android.support.v4.app.FragmentManager fragmentManager = this.getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		android.support.v4.app.Fragment fragment = null;
		switch (step)
		{
		case 1:
			fragment = new HeadacheStep1Fragment();
			break;
		case 2:
			fragment = new HeadacheStep2Fragment();
			break;
		case 3:
			fragment = new HeadacheStep3Fragment();
			break;
		case 4:
			fragment = new HeadacheStep4Fragment();
			break;
		case 5:
			fragment = new HeadacheStep5Fragment();
			break;
		case 6:
			fragment = new HeadacheStep6Fragment();
			break;
		default:
			fragment = new HeadacheStep1Fragment();
			break;
		}
		stepString = "Step " + step + " of 6";
		txtViewSteps.setText(stepString);

		fragmentTransaction.replace(R.id.stepFragmentContainer, fragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onEventChanged(MigraineEvent event)
	{
		this.event = event;
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
