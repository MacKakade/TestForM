package com.novartis.mymigraine;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.adapter.ViewPreviousEntryListAdaptor;
import com.novartis.mymigraine.common.Constant;
import com.novartis.mymigraine.database.DataBaseHelper;
import com.novartis.mymigraine.model.Environment;
import com.novartis.mymigraine.model.Fasting;
import com.novartis.mymigraine.model.Food;
import com.novartis.mymigraine.model.LifeStyle;
import com.novartis.mymigraine.model.Location;
import com.novartis.mymigraine.model.Menstruate;
import com.novartis.mymigraine.model.MigraineEvent;
import com.novartis.mymigraine.model.Relief;
import com.novartis.mymigraine.model.SkippedMeal;
import com.novartis.mymigraine.model.Symptom;
import com.novartis.mymigraine.model.Treatment;
import com.novartis.mymigraine.model.Warning;

public class ViewPreviousEntryListActivity extends SherlockActivity implements OnItemClickListener
{

	private ViewPreviousEntryListAdaptor adapter;
	private ListView listViewPreviousEntry;
	private ArrayList<MigraineEvent> dataList;
	private int clickedPosition;
	private static final int REQ_CODE_VIEW_ENTRY = 10;
	/**
	 * Database Helper class to Access the database.
	 */
	private DataBaseHelper dbhelper;
	private MyMigraineApp mymigraineapp;
	private MenuItem menuItemMyDiary, menuItemChart;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_previous_entry_list);
		actionBar = getSupportActionBar();
		createActionBar();
		mymigraineapp = (MyMigraineApp) this.getApplicationContext();
		dbhelper = mymigraineapp.getDbhelper();
		try
		{
			dataList = dbhelper.getMigraineEvents();
		}
		catch (ParseException e)
		{
			dataList = new ArrayList<MigraineEvent>();
			e.printStackTrace();
		}
		listViewPreviousEntry = (ListView) findViewById(R.id.listViewPreviousEntry);
		adapter = new ViewPreviousEntryListAdaptor(this, dataList);
		listViewPreviousEntry.setAdapter(adapter);
		listViewPreviousEntry.setOnItemClickListener(this);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQ_CODE_VIEW_ENTRY)
		{
			if (resultCode == RESULT_OK)
			{
				MigraineEvent e = (MigraineEvent) data.getExtras().get(Constant.KEY_EVENT);
				dataList.set(clickedPosition, e);
				Collections.sort(dataList, new MigraineEventDateComparator());
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		clickedPosition = position;
		MigraineEvent event = dataList.get(position);
		if (event.isHasHeadache())
		{
			event.setLocations(getLocationOfPainsData(event));
			event.setWarnings(getWarningSignData(event));
			event.setSymptoms(getSymptomsData(event));
			event.setTreatments(getTreatmentData(event));
			event.setReliefs(getReliefData(event));
		}
		event.setSkippedMeals(getSkippedMealData(event));
		event.setFasting(getFasting(event));
		event.setFoods(getFoodData(event));
		event.setLifeStyles(getLifeStyleData(event));
		event.setEnvironments(getEnvironmentData(event));
		event.setMenstruating(getMenstruate(event));

		Intent i = new Intent(this, ViewSavedEntryActivity.class);
		i.putExtra(Constant.KEY_EVENT, event);
		startActivityForResult(i, REQ_CODE_VIEW_ENTRY);

	}

	public ArrayList<Location> getLocationOfPainsData(MigraineEvent event)
	{
		int allLocSize, savedLocSize;
		ArrayList<Location> allLocations = dbhelper.getLocationOfPain();
		ArrayList<Location> savedLocation = dbhelper.getLocationOfPain(event.getEventId());
		allLocSize = allLocations.size();
		savedLocSize = savedLocation.size();
		for (int i = 0; i < allLocSize; i++)
		{
			for (int j = 0; j < savedLocSize; j++)
			{
				if (allLocations.get(i).getId() == savedLocation.get(j).getId())
				{
					allLocations.get(i).setChecked(true);
				}
			}
		}
		return allLocations;

	}

	public ArrayList<Warning> getWarningSignData(MigraineEvent event)
	{
		int allWarningSingSize, savedWarningSingSize;
		ArrayList<Warning> allWarnings = dbhelper.getWarningSigns();
		ArrayList<Warning> savedWarnings = dbhelper.getWarningSigns(event.getEventId());
		allWarningSingSize = allWarnings.size();
		savedWarningSingSize = savedWarnings.size();
		for (int i = 0; i < allWarningSingSize; i++)
		{
			for (int j = 0; j < savedWarningSingSize; j++)
			{
				if (allWarnings.get(i).getId() == savedWarnings.get(j).getId())
				{
					allWarnings.get(i).setChecked(true);
				}
			}
		}
		return allWarnings;

	}

	public ArrayList<Symptom> getSymptomsData(MigraineEvent event)
	{
		int allSymptomSize, savedSymptomSize;
		ArrayList<Symptom> allSymptoms = dbhelper.getSymptoms();
		ArrayList<Symptom> savedSymptoms = dbhelper.getSymptoms(event.getEventId());
		allSymptomSize = allSymptoms.size();
		savedSymptomSize = savedSymptoms.size();
		for (int i = 0; i < allSymptomSize; i++)
		{
			for (int j = 0; j < savedSymptomSize; j++)
			{
				if (allSymptoms.get(i).getId() == savedSymptoms.get(j).getId())
				{
					allSymptoms.get(i).setChecked(true);
				}
			}
		}
		return allSymptoms;

	}

	public ArrayList<Treatment> getTreatmentData(MigraineEvent event)
	{
		int allSize, savedSize;
		ArrayList<Treatment> allTreatment = dbhelper.getTreatments();
		ArrayList<Treatment> savedTreatment = dbhelper.getTreatments(event.getEventId());
		allSize = allTreatment.size();
		savedSize = savedTreatment.size();
		for (int i = 0; i < allSize; i++)
		{
			for (int j = 0; j < savedSize; j++)
			{
				if (allTreatment.get(i).getId() == savedTreatment.get(j).getId())
				{
					allTreatment.get(i).setChecked(true);
				}
			}
		}
		return allTreatment;

	}

	public ArrayList<Relief> getReliefData(MigraineEvent event)
	{
		int allSize, savedSize;
		ArrayList<Relief> allRelief = dbhelper.getReliefs();
		ArrayList<Relief> savedRelief = dbhelper.getReliefs(event.getEventId());
		allSize = allRelief.size();
		savedSize = savedRelief.size();
		for (int i = 0; i < allSize; i++)
		{
			for (int j = 0; j < savedSize; j++)
			{
				if (allRelief.get(i).getId() == savedRelief.get(j).getId())
				{
					allRelief.get(i).setChecked(true);
				}
			}
		}
		return allRelief;

	}

	public ArrayList<SkippedMeal> getSkippedMealData(MigraineEvent event)
	{
		int allSize, savedSize;
		ArrayList<SkippedMeal> allSkippedMeals = dbhelper.getSkippedMeals();
		ArrayList<SkippedMeal> savedSkippedMeals = dbhelper.getSkippedMeals(event.getEventId());
		allSize = allSkippedMeals.size();
		savedSize = savedSkippedMeals.size();
		for (int i = 0; i < allSize; i++)
		{
			for (int j = 0; j < savedSize; j++)
			{
				if (allSkippedMeals.get(i).getId() == savedSkippedMeals.get(j).getId())
				{
					allSkippedMeals.get(i).setChecked(true);
				}
			}
		}
		return allSkippedMeals;

	}

	public Fasting getFasting(MigraineEvent event)
	{
		Fasting fasting = dbhelper.getFasting(event.getEventId());
		return fasting;
	}

	public Menstruate getMenstruate(MigraineEvent event)
	{
		Menstruate menstruate = dbhelper.getMenstruate(event.getEventId());
		return menstruate;
	}

	public ArrayList<Food> getFoodData(MigraineEvent event)
	{
		int allSize, savedSize;
		ArrayList<Food> allFoods = dbhelper.getFoods();
		ArrayList<Food> savedFoods = dbhelper.getFoods(event.getEventId());
		allSize = allFoods.size();
		savedSize = savedFoods.size();
		for (int i = 0; i < allSize; i++)
		{
			for (int j = 0; j < savedSize; j++)
			{
				if (allFoods.get(i).getId() == savedFoods.get(j).getId())
				{
					allFoods.get(i).setChecked(true);
				}
			}
		}
		return allFoods;

	}

	public ArrayList<LifeStyle> getLifeStyleData(MigraineEvent event)
	{
		int allSize, savedSize;
		ArrayList<LifeStyle> allLifeStyles = dbhelper.getLifeStyle();
		ArrayList<LifeStyle> savedLifeStyles = dbhelper.getLifeStyle(event.getEventId());
		allSize = allLifeStyles.size();
		savedSize = savedLifeStyles.size();
		for (int i = 0; i < allSize; i++)
		{
			for (int j = 0; j < savedSize; j++)
			{
				if (allLifeStyles.get(i).getId() == savedLifeStyles.get(j).getId())
				{
					allLifeStyles.get(i).setChecked(true);
				}
			}
		}
		return allLifeStyles;

	}

	public ArrayList<Environment> getEnvironmentData(MigraineEvent event)
	{
		int allSize, savedSize;
		ArrayList<Environment> allEnvironments = dbhelper.getEnvironment();
		ArrayList<Environment> savedEnvironments = dbhelper.getEnvironment(event.getEventId());
		allSize = allEnvironments.size();
		savedSize = savedEnvironments.size();
		for (int i = 0; i < allSize; i++)
		{
			for (int j = 0; j < savedSize; j++)
			{
				if (allEnvironments.get(i).getId() == savedEnvironments.get(j).getId())
				{
					allEnvironments.get(i).setChecked(true);
				}
			}
		}
		return allEnvironments;

	}

	public class MigraineEventDateComparator implements Comparator<MigraineEvent>
	{

		@Override
		public int compare(MigraineEvent lhs, MigraineEvent rhs)
		{

			return rhs.getDateTime().compareTo(lhs.getDateTime());
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
