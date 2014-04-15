package com.novartis.mymigraine.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.novartis.mymigraine.AddYourOwnAnsActivity;
import com.novartis.mymigraine.MyMigraineApp;
import com.novartis.mymigraine.R;
import com.novartis.mymigraine.ViewSavedEntryActivity;
import com.novartis.mymigraine.common.AutoScaleTextView;
import com.novartis.mymigraine.common.Constant;
import com.novartis.mymigraine.common.IMigraineEvent;
import com.novartis.mymigraine.common.IMigraineEventChangedListener;
import com.novartis.mymigraine.common.SharedPrefUtility;
import com.novartis.mymigraine.common.Utility;
import com.novartis.mymigraine.database.DataBaseHelper;
import com.novartis.mymigraine.model.Environment;
import com.novartis.mymigraine.model.Menstruate;
import com.novartis.mymigraine.model.MigraineEvent;

public class HeadacheStep6Fragment extends Fragment implements OnClickListener
{
	public static final String TAG = "HeadachePositiveStep6Fragment";
	private IMigraineEventChangedListener migraineEventChangedListener;
	private TextView txtViewMenstrual;
	private View deviderMenstrual;
	private LinearLayout menstrualButtonPanel;
	private Button btnMenstrualNo, btnMenstrualYes, btnMenstrualDontAskMeAgain;
	private LayoutInflater mInflater;
	private LinearLayout environmentPanel;
	private ArrayList<Environment> environmentList;
	int deviderHeight;
	CheckBox environmentChkBox;
	AutoScaleTextView environmentText;
	ImageView environmentTag;
	private Button btnEnvironmentAddYourAns, btnNotesAddYourAns;
	View view;
	private MigraineEvent event;
	private Button btnSaveThisEntry;
	private String notes;
	Menstruate menstruate;
	private static final int REQ_CODE_ADD_YOUR_ANS_ENVIRONMENT = 8;
	private static final int REQ_CODE_ADD_NOTES = 9;
	/**
	 * Database Helper class to Access the database.
	 */
	private DataBaseHelper dbhelper;
	private MyMigraineApp mymigraineapp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mInflater = inflater;
		mymigraineapp = (MyMigraineApp) getActivity().getApplicationContext();
		dbhelper = mymigraineapp.getDbhelper();

		deviderHeight = (int) Utility.getPixelValue(getActivity(), 1);

		view = inflater.inflate(R.layout.fragment_headache_positive_step_six, container, false);
		event = ((IMigraineEvent) getActivity()).getMigraineEvent();
		createEnvironmentList();
		txtViewMenstrual = (TextView) view.findViewById(R.id.txtViewMenstrual);
		deviderMenstrual = (View) view.findViewById(R.id.deviderMenstrual);
		menstrualButtonPanel = (LinearLayout) view.findViewById(R.id.menstrualButtonPanel);
		btnMenstrualNo = (Button) view.findViewById(R.id.btnMenstrualNo);
		btnMenstrualNo.setOnClickListener(this);
		btnMenstrualYes = (Button) view.findViewById(R.id.btnMenstrualYes);
		btnMenstrualYes.setOnClickListener(this);
		btnMenstrualDontAskMeAgain = (Button) view.findViewById(R.id.btnMenstrualDontAskMeAgain);
		btnMenstrualDontAskMeAgain.setOnClickListener(this);

		btnNotesAddYourAns = (Button) view.findViewById(R.id.btnNotesAddYourAns);
		btnNotesAddYourAns.setOnClickListener(this);
		notes = event.getNotes();
		if (notes != null && notes.length() > 0)
			btnNotesAddYourAns.setText(getString(R.string.view_your_own_notes));
		else
			btnNotesAddYourAns.setText(getString(R.string.add_your_own_answer));

		btnSaveThisEntry = (Button) view.findViewById(R.id.btnSaveThisEntry);
		btnSaveThisEntry.setOnClickListener(this);

		if (!SharedPrefUtility.isMenstruationQuestionAsk(getActivity()))
		{
			txtViewMenstrual.setVisibility(View.GONE);
			deviderMenstrual.setVisibility(View.GONE);
			menstrualButtonPanel.setVisibility(View.GONE);

		}
		else
		{

			if (event.getMenstruating() == null)
			{

				menstruate = new Menstruate();
				menstruate.setMenstrate(Menstruate.NO);
				event.setMenstruating(menstruate);
				migraineEventChangedListener.onEventChanged(event);
			}
			else
			{
				menstruate = event.getMenstruating();
			}
			setMenstrate(menstruate);
		}

		return view;
	}

	@Override
	public void onAttach(Activity activity)
	{

		super.onAttach(activity);
		if (activity instanceof IMigraineEventChangedListener)
		{
			migraineEventChangedListener = (IMigraineEventChangedListener) activity;
		}
		else
		{
			throw new ClassCastException(activity.toString() + " must implemenet IMigraineEventChangedListener");
		}
	}

	private void createEnvironmentList()
	{
		environmentPanel = (LinearLayout) view.findViewById(R.id.environmentPanel);
		if (event.getEnvironments() == null)
			environmentList = dbhelper.getEnvironment();
		else
			environmentList = event.getEnvironments();

		int size = environmentList.size();
		for (int i = 0; i < size; i++)
		{
			final Environment environment = environmentList.get(i);
			View checkedView = mInflater.inflate(R.layout.checked_list_row, null, false);
			environmentChkBox = (CheckBox) checkedView.findViewById(R.id.chkbox);
			environmentChkBox.setTag(i);
			environmentChkBox.setOnCheckedChangeListener(environmentCheckListener);
			environmentText = (AutoScaleTextView) checkedView.findViewById(R.id.chkBoxTextView);
			environmentChkBox.setChecked(environment.isChecked());
			environmentText.setText(environment.getName());
			environmentTag = (ImageView) checkedView.findViewById(R.id.imgViewTag);
			environmentTag.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					Utility.showAlert(getActivity(), "", environment.getTag());
				}
			});

			if (environment.getTag() != null && environment.getTag().trim().length() > 0)
				environmentTag.setVisibility(View.VISIBLE);

			View devider = new View(getActivity());
			devider.setLayoutParams(new LinearLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, deviderHeight));
			devider.setBackgroundColor(getResources().getColor(R.color.list_devider_color));
			environmentPanel.addView(checkedView);
			environmentPanel.addView(devider);

		}
		environmentPanel.removeViewAt(environmentPanel.getChildCount() - 1);
		btnEnvironmentAddYourAns = new Button(getActivity());
		btnEnvironmentAddYourAns.setTextAppearance(getActivity(), R.style.black_bold_font_medium);
		btnEnvironmentAddYourAns.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_add_ur_ans));

		LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		// btnLp.setMargins(btnMargin, 0, btnMargin, 0);
		btnEnvironmentAddYourAns.setLayoutParams(btnLp);
		btnEnvironmentAddYourAns.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		btnEnvironmentAddYourAns.setText(getActivity().getResources().getString(R.string.add_your_own_answer));
		Drawable drawableArrow = getActivity().getResources().getDrawable(R.drawable.ic_button_dark_arrow);
		btnEnvironmentAddYourAns.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableArrow, null);
		btnEnvironmentAddYourAns.setOnClickListener(this);
		environmentPanel.addView(btnEnvironmentAddYourAns);
	}

	private void setMenstrate(Menstruate menstruate)
	{
		switch (menstruate.getMenstrate())
		{
		case Menstruate.YES:
			btnMenstrualNo.setSelected(false);
			btnMenstrualYes.setSelected(true);
			btnMenstrualDontAskMeAgain.setSelected(false);
			break;
		case Menstruate.NO:
			btnMenstrualNo.setSelected(true);
			btnMenstrualYes.setSelected(false);
			btnMenstrualDontAskMeAgain.setSelected(false);
			break;
		case Menstruate.DONT_ASK_AGAIN:
			btnMenstrualNo.setSelected(false);
			btnMenstrualYes.setSelected(false);
			btnMenstrualDontAskMeAgain.setSelected(true);
			break;

		}
	}

	@Override
	public void onClick(View v)
	{

		if (v == btnMenstrualNo)
		{

			menstruate.setMenstrate(Menstruate.NO);
			event.setMenstruating(menstruate);
			migraineEventChangedListener.onEventChanged(event);
			setMenstrate(menstruate);
		}
		else if (v == btnMenstrualYes)
		{

			menstruate.setMenstrate(Menstruate.YES);
			event.setMenstruating(menstruate);
			migraineEventChangedListener.onEventChanged(event);
			setMenstrate(menstruate);

		}
		else if (v == btnMenstrualDontAskMeAgain)
		{
			SharedPrefUtility.saveAskMenstruationQuestion(getActivity(), false);
			menstruate.setMenstrate(Menstruate.DONT_ASK_AGAIN);
			event.setMenstruating(menstruate);
			migraineEventChangedListener.onEventChanged(event);
			setMenstrate(menstruate);
		}
		else if (v == btnEnvironmentAddYourAns)
		{
			Intent i = new Intent(getActivity(), AddYourOwnAnsActivity.class);
			i.putExtra(Constant.KEY_QUESTION, getString(R.string.add_your_own_item));
			i.putExtra(Constant.KEY_QUESTION_ID, Constant.QUESTION_ENVIRONMENT_ID);
			startActivityForResult(i, REQ_CODE_ADD_YOUR_ANS_ENVIRONMENT);

		}
		else if (v == btnNotesAddYourAns)
		{
			Intent i = new Intent(getActivity(), AddYourOwnAnsActivity.class);
			i.putExtra(Constant.KEY_QUESTION, getString(R.string.notes_));
			i.putExtra(Constant.KEY_QUESTION_ID, Constant.QUESTION_NOTES);
			i.putExtra(Constant.KEY_ANSWER, notes);
			startActivityForResult(i, REQ_CODE_ADD_NOTES);

		}
		else if (v == btnSaveThisEntry)
		{

			boolean isSuccessfull = dbhelper.saveMigraineEventEntry(event);
			if (isSuccessfull)
			{

				Builder alertBuilder = new Builder(getActivity());
				alertBuilder.setTitle(R.string.your_entry_was_saved_);
				alertBuilder.setMessage(R.string.would_you_like_to_view_your_entry_);
				alertBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						getActivity().onBackPressed();
					}
				});
				alertBuilder.setNeutralButton("View now", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						getActivity().onBackPressed();
						Intent i = new Intent(getActivity(), ViewSavedEntryActivity.class);
						i.putExtra(Constant.KEY_EVENT, event);
						startActivity(i);

					}
				});
				alertBuilder.setCancelable(false);
				alertBuilder.create().show();

				// getActivity().onBackPressed();

			}
			else
			{

				Utility.showAlert(getActivity(), "Error", "Saving entry failed.");

			}
		}

	}

	private OnCheckedChangeListener environmentCheckListener = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			int index = Integer.parseInt(buttonView.getTag().toString());
			environmentList.get(index).setChecked(isChecked);
			event.setEnvironments(environmentList);
			migraineEventChangedListener.onEventChanged(event);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQ_CODE_ADD_YOUR_ANS_ENVIRONMENT)
		{
			if (resultCode == getActivity().RESULT_OK)
			{
				if (data.getExtras().getString(Constant.KEY_ANSWER).length() > 0)
				{
					Environment environment = new Environment();
					environment.setChecked(true);
					environment.setName(data.getExtras().getString(Constant.KEY_ANSWER));
					environment.setNewAnswer(true);
					environment.setOrderNo(environmentList.size());
					long id = dbhelper.insertYourAns(environment);
					environment.setId(id);
					environmentPanel.removeAllViews();
					environmentList.add(environment);
					event.setEnvironments(environmentList);
					migraineEventChangedListener.onEventChanged(event);
				}
				else
				{
					event.setEnvironments(null);
					environmentPanel.removeAllViews();
				}
				createEnvironmentList();

			}

		}
		else if (requestCode == REQ_CODE_ADD_NOTES)
		{
			if (resultCode == getActivity().RESULT_OK)
			{
				notes = data.getExtras().getString(Constant.KEY_ANSWER);

				if (notes.length() > 0)
				{
					btnNotesAddYourAns.setText(getString(R.string.view_your_own_notes));
					event.setNotes(notes);
					migraineEventChangedListener.onEventChanged(event);
				}
				else
				{
					btnNotesAddYourAns.setText(getString(R.string.add_your_own_answer));
					event.setNotes("");
					migraineEventChangedListener.onEventChanged(event);
				}

			}
		}
	}

	public Button getSavedEntryButton()
	{
		return btnSaveThisEntry;
	}

	@Override
	public void onResume()
	{
		// Log.d(TAG, "On Resume call.......");
		super.onResume();
	}

}