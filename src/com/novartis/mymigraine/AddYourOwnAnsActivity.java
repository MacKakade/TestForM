package com.novartis.mymigraine;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.adapter.AddYourAnsListAdaptor;
import com.novartis.mymigraine.common.Constant;
import com.novartis.mymigraine.common.Utility;
import com.novartis.mymigraine.database.DataBaseHelper;
import com.novartis.mymigraine.model.ChoiseItem;

public class AddYourOwnAnsActivity extends SherlockActivity implements OnClickListener
{

	private TextView txtViewAddYourAns;
	private EditText editTextAnswer;
	private ListView listViewAddYourAns;
	private Button btnCancel, btnDone;
	private AddYourAnsListAdaptor adapter;

	private ArrayList<ChoiseItem> dataList;

	private String question = "", answer = "";

	private int questionId;
	private boolean isEntryDeleted;
	private ActionBar actionBar;

	public boolean isEntryDeleted()
	{
		return isEntryDeleted;
	}

	public void setEntryDeleted(boolean isEntryDeleted)
	{
		this.isEntryDeleted = isEntryDeleted;
	}

	Intent intent;
	/**
	 * Database Helper class to Access the database.
	 */
	private DataBaseHelper dbhelper;
	private MyMigraineApp mymigraineapp;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_your_own_ans);
		actionBar = getSupportActionBar();
		createActionBar();
		mymigraineapp = (MyMigraineApp) this.getApplicationContext();
		dbhelper = mymigraineapp.getDbhelper();
		intent = getIntent();
		if (intent.getExtras().containsKey(Constant.KEY_QUESTION_ID))
			questionId = intent.getExtras().getInt(Constant.KEY_QUESTION_ID);

		if (intent.getExtras().containsKey(Constant.KEY_QUESTION))
			question = getIntent().getExtras().getString(Constant.KEY_QUESTION);
		else
			question = getString(R.string.add_your_own_item);

		if (intent.getExtras().containsKey(Constant.KEY_ANSWER))
			answer = getIntent().getExtras().getString(Constant.KEY_ANSWER);

		txtViewAddYourAns = (TextView) findViewById(R.id.txtViewAddYourAns);
		txtViewAddYourAns.setText(question);
		editTextAnswer = (EditText) findViewById(R.id.editTextAnswer);

		if (answer != null && answer.length() > 0)
			editTextAnswer.setText(answer.trim());

		listViewAddYourAns = (ListView) findViewById(R.id.listViewAddYourAns);

		createPrevCustomQuestionList(questionId);
		if (questionId != Constant.QUESTION_NOTES)
		{
			adapter = new AddYourAnsListAdaptor(this, dataList);
			listViewAddYourAns.setAdapter(adapter);
		}
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		btnDone = (Button) findViewById(R.id.btnDone);
		btnDone.setOnClickListener(this);

	}

	private void createActionBar()
	{

		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_title));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	private void createPrevCustomQuestionList(int questionId)
	{

		switch (questionId)
		{
		case Constant.QUESTION_LOCATION_OF_PAIN_ID:
			dataList = dbhelper.getMyOwnLocationOfPain();
			break;
		case Constant.QUESTION_WARNING_SIGN_ID:
			dataList = dbhelper.getMyOwnWarningSigns();
			break;
		case Constant.QUESTION_SYMPTOMS_ID:
			dataList = dbhelper.getMyOwnSymptoms();
			break;
		case Constant.QUESTION_TREATMENT_ID:
			dataList = dbhelper.getMyOwnTreatments();
			break;
		case Constant.QUESTION_RELEIF_ID:
			dataList = dbhelper.getMyOwnReliefs();
			break;
		case Constant.QUESTION_FOOD_ID:
			dataList = dbhelper.getMyOwnFoods();
			break;
		case Constant.QUESTION_LIFE_STYLE_ID:
			dataList = dbhelper.getMyOwnLifeStyle();
			break;
		case Constant.QUESTION_ENVIRONMENT_ID:
			dataList = dbhelper.getMyOwnEnvironment();
			break;
		case Constant.QUESTION_NOTES:
			listViewAddYourAns.setVisibility(View.GONE);
			editTextAnswer.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			editTextAnswer.setSingleLine(false);
			editTextAnswer.setMaxLines(16);
			editTextAnswer.setHeight((int) Utility.getPixelValue(this, 120));
			break;

		}

	}

	@Override
	public void onClick(View v)
	{

		if (v == btnCancel)
		{

			setResult(RESULT_CANCELED);
			onBackPressed();
		}
		else if (v == btnDone)
		{

			if (questionId != Constant.QUESTION_NOTES && editTextAnswer.getText().toString().trim().length() > 0
					|| isEntryDeleted)
			{

				intent.putExtra(Constant.KEY_ANSWER, editTextAnswer.getText().toString());
				if (isEntryDeleted)
				{
					dbhelper.deleteAnswer(adapter.getDeletedItemIds());
				}
				setResult(RESULT_OK, intent);
			}
			else if (questionId == Constant.QUESTION_NOTES)
			{
				intent.putExtra(Constant.KEY_ANSWER, editTextAnswer.getText().toString());
				setResult(RESULT_OK, intent);
			}
			else
			{
				setResult(RESULT_CANCELED);
			}

			onBackPressed();

		}

	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			onBackPressed();
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