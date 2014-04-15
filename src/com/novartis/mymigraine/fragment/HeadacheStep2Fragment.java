package com.novartis.mymigraine.fragment;

import java.util.ArrayList;

import android.app.Activity;
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

import com.novartis.mymigraine.AddYourOwnAnsActivity;
import com.novartis.mymigraine.MyMigraineApp;
import com.novartis.mymigraine.R;
import com.novartis.mymigraine.common.AutoScaleTextView;
import com.novartis.mymigraine.common.Constant;
import com.novartis.mymigraine.common.IMigraineEvent;
import com.novartis.mymigraine.common.IMigraineEventChangedListener;
import com.novartis.mymigraine.common.Utility;
import com.novartis.mymigraine.database.DataBaseHelper;
import com.novartis.mymigraine.model.MigraineEvent;
import com.novartis.mymigraine.model.Symptom;
import com.novartis.mymigraine.model.Warning;

public class HeadacheStep2Fragment extends Fragment implements OnClickListener
{
	public static final String TAG = "HeadachePositiveStep2Fragment";
	private IMigraineEventChangedListener migraineEventChangedListener;
	private LayoutInflater mInflater;
	private ArrayList<Warning> warningList;
	private ArrayList<Symptom> symptomList;
	private LinearLayout warningSignPanel, symptomsPanel;
	int deviderHeight;
	CheckBox warningChkBox, symptomsChkBox;
	AutoScaleTextView warningText, symptomsText;
	ImageView warningTag, symptomsTag;
	private Button btnWarningAddYourAns, btnSymptomsAddYourAns;
	View view;
	private MigraineEvent event;
	private static final int REQ_CODE_ADD_YOUR_ANS_WARNING = 2;
	private static final int REQ_CODE_ADD_YOUR_ANS_SYMPTOMS = 3;
	/**
	 * Database Helper class to Access the database.
	 */
	private DataBaseHelper dbhelper;
	private MyMigraineApp mymigraineapp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Log.d(TAG, "onCreateView call.......");
		mInflater = inflater;
		view = inflater.inflate(R.layout.fragment_headache_positive_step_two, container, false);
		mymigraineapp = (MyMigraineApp) getActivity().getApplicationContext();
		dbhelper = mymigraineapp.getDbhelper();
		event = ((IMigraineEvent) getActivity()).getMigraineEvent();

		deviderHeight = (int) Utility.getPixelValue(getActivity(), 1);
		createWarningSignList();
		createSymptomsList();

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

	private void createWarningSignList()
	{
		warningSignPanel = (LinearLayout) view.findViewById(R.id.warningSignPanel);
		if (event.getWarnings() == null)
		{
			warningList = dbhelper.getWarningSigns();
		}
		else
		{
			warningList = event.getWarnings();
		}
		int size = warningList.size();
		for (int i = 0; i < size; i++)
		{
			final Warning warning = warningList.get(i);
			View checkedView = mInflater.inflate(R.layout.checked_list_row, null, false);
			warningChkBox = (CheckBox) checkedView.findViewById(R.id.chkbox);
			warningChkBox.setTag(i);
			warningChkBox.setOnCheckedChangeListener(warningSignCheckedListener);
			warningText = (AutoScaleTextView) checkedView.findViewById(R.id.chkBoxTextView);
			warningChkBox.setChecked(warning.isChecked());
			warningText.setText(warning.getName());
			warningTag = (ImageView) checkedView.findViewById(R.id.imgViewTag);
			warningTag.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					Utility.showAlert(getActivity(), "", warning.getTag());
				}
			});
			if (warning.getTag() != null && warning.getTag().trim().length() > 0)
			{
				warningTag.setVisibility(View.VISIBLE);
			}
			View devider = new View(getActivity());
			devider.setLayoutParams(new ViewGroup.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, deviderHeight));
			devider.setBackgroundColor(getResources().getColor(R.color.list_devider_color));
			warningSignPanel.addView(checkedView);
			warningSignPanel.addView(devider);

		}
		warningSignPanel.removeViewAt(warningSignPanel.getChildCount() - 1);
		btnWarningAddYourAns = new Button(getActivity());
		btnWarningAddYourAns.setTextAppearance(getActivity(), R.style.black_bold_font_medium);
		btnWarningAddYourAns.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_add_ur_ans));

		LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		// btnLp.setMargins(btnMargin, 0, btnMargin, 0);
		btnWarningAddYourAns.setLayoutParams(btnLp);
		btnWarningAddYourAns.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		btnWarningAddYourAns.setText(getActivity().getResources().getString(R.string.add_your_own_answer));
		Drawable drawableArrow = getActivity().getResources().getDrawable(R.drawable.ic_button_dark_arrow);
		btnWarningAddYourAns.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableArrow, null);
		btnWarningAddYourAns.setOnClickListener(this);
		warningSignPanel.addView(btnWarningAddYourAns);

	}

	private void createSymptomsList()
	{
		symptomsPanel = (LinearLayout) view.findViewById(R.id.symptomsPanel);
		if (event.getSymptoms() == null)
			symptomList = dbhelper.getSymptoms();
		else
			symptomList = event.getSymptoms();

		int size = symptomList.size();
		for (int i = 0; i < size; i++)
		{
			final Symptom symptom = symptomList.get(i);
			View checkedView = mInflater.inflate(R.layout.checked_list_row, null, false);
			symptomsChkBox = (CheckBox) checkedView.findViewById(R.id.chkbox);
			symptomsChkBox.setTag(i);
			symptomsChkBox.setOnCheckedChangeListener(symptomCheckedListener);
			symptomsText = (AutoScaleTextView) checkedView.findViewById(R.id.chkBoxTextView);
			symptomsChkBox.setChecked(symptom.isChecked());
			symptomsText.setText(symptom.getName());
			symptomsTag = (ImageView) checkedView.findViewById(R.id.imgViewTag);
			symptomsTag.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					Utility.showAlert(getActivity(), "", symptom.getTag());

				}
			});
			if (symptom.getTag() != null && symptom.getTag().trim().length() > 0)
				symptomsTag.setVisibility(View.VISIBLE);

			View devider = new View(getActivity());
			devider.setLayoutParams(new ViewGroup.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, deviderHeight));
			devider.setBackgroundColor(getResources().getColor(R.color.list_devider_color));
			symptomsPanel.addView(checkedView);
			symptomsPanel.addView(devider);

		}
		symptomsPanel.removeViewAt(symptomsPanel.getChildCount() - 1);
		btnSymptomsAddYourAns = new Button(getActivity());
		btnSymptomsAddYourAns.setTextAppearance(getActivity(), R.style.black_bold_font_medium);
		btnSymptomsAddYourAns.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_add_ur_ans));

		LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		btnSymptomsAddYourAns.setLayoutParams(btnLp);
		btnSymptomsAddYourAns.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		btnSymptomsAddYourAns.setText(getActivity().getResources().getString(R.string.add_your_own_answer));
		Drawable drawableArrow = getActivity().getResources().getDrawable(R.drawable.ic_button_dark_arrow);
		btnSymptomsAddYourAns.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableArrow, null);
		btnSymptomsAddYourAns.setOnClickListener(this);
		symptomsPanel.addView(btnSymptomsAddYourAns);

	}

	private OnCheckedChangeListener warningSignCheckedListener = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			int index = Integer.parseInt(buttonView.getTag().toString());
			warningList.get(index).setChecked(isChecked);
			event.setWarnings(warningList);
			migraineEventChangedListener.onEventChanged(event);

		}
	};
	private OnCheckedChangeListener symptomCheckedListener = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			int index = Integer.parseInt(buttonView.getTag().toString());
			symptomList.get(index).setChecked(isChecked);
			event.setSymptoms(symptomList);
			migraineEventChangedListener.onEventChanged(event);
		}
	};

	@Override
	public void onResume()
	{
		// Log.d(TAG, "On Resume call.......");
		super.onResume();
	}

	@Override
	public void onClick(View v)
	{
		if (v == btnWarningAddYourAns)
		{
			Intent i = new Intent(getActivity(), AddYourOwnAnsActivity.class);
			i.putExtra(Constant.KEY_QUESTION, getString(R.string.add_your_own_warning_sign));
			i.putExtra(Constant.KEY_QUESTION_ID, Constant.QUESTION_WARNING_SIGN_ID);
			startActivityForResult(i, REQ_CODE_ADD_YOUR_ANS_WARNING);
		}
		else if (v == btnSymptomsAddYourAns)
		{
			Intent i = new Intent(getActivity(), AddYourOwnAnsActivity.class);
			i.putExtra(Constant.KEY_QUESTION, getString(R.string.add_your_own_symptom));
			i.putExtra(Constant.KEY_QUESTION_ID, Constant.QUESTION_SYMPTOMS_ID);
			startActivityForResult(i, REQ_CODE_ADD_YOUR_ANS_SYMPTOMS);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQ_CODE_ADD_YOUR_ANS_WARNING)
		{
			if (resultCode == getActivity().RESULT_OK)
			{
				if (data.getExtras().getString(Constant.KEY_ANSWER).length() > 0)
				{
					Warning warning = new Warning();
					warning.setChecked(true);
					warning.setName(data.getExtras().getString(Constant.KEY_ANSWER));
					warning.setNewAnswer(true);

					warning.setOrderNo(warningList.size());
					long id = dbhelper.insertYourAns(warning);
					warning.setId(id);
					warningSignPanel.removeAllViews();
					warningList.add(warning);

					// set last checked item selected

					event.setWarnings(warningList);
					migraineEventChangedListener.onEventChanged(event);
				}
				else
				{
					event.setWarnings(null);

					warningSignPanel.removeAllViews();
				}
				createWarningSignList();

			}
		}
		else if (requestCode == REQ_CODE_ADD_YOUR_ANS_SYMPTOMS)
		{
			if (resultCode == getActivity().RESULT_OK)
			{
				if (data.getExtras().getString(Constant.KEY_ANSWER).length() > 0)
				{
					Symptom symptom = new Symptom();
					symptom.setChecked(true);
					symptom.setName(data.getExtras().getString(Constant.KEY_ANSWER));
					symptom.setNewAnswer(true);
					symptom.setOrderNo(symptomList.size());
					long id = dbhelper.insertYourAns(symptom);
					symptom.setId(id);
					symptomsPanel.removeAllViews();
					symptomList.add(symptom);

					// set last checked item selected

					event.setSymptoms(symptomList);
					migraineEventChangedListener.onEventChanged(event);
				}
				else
				{
					event.setSymptoms(null);
					symptomsPanel.removeAllViews();
				}
				createSymptomsList();

			}
		}
	}
}
