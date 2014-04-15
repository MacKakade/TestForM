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
import com.novartis.mymigraine.model.Relief;
import com.novartis.mymigraine.model.Treatment;

public class HeadacheStep3Fragment extends Fragment implements OnClickListener
{
	public static final String TAG = "HeadachePositiveStep3Fragment";
	private IMigraineEventChangedListener migraineEventChangedListener;
	int deviderHeight;
	private LayoutInflater mInflater;
	private LinearLayout treatmentPanel, reliefPanel;
	private CheckBox treatmentChkBox, reliefChkBox;
	private AutoScaleTextView treatmentText, reliefText;
	private Button btnTreatmentAddYourAns, btnReleifAddYourAns;
	private View view;
	private ArrayList<Relief> releifList;
	private ArrayList<Treatment> treatmentList;
	ImageView treatmentTag, reliefTag;
	private MigraineEvent event;
	private static final int REQ_CODE_ADD_YOUR_ANS_TREATMENT = 4;
	private static final int REQ_CODE_ADD_YOUR_ANS_RELIEF = 5;
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

		event = ((IMigraineEvent) getActivity()).getMigraineEvent();
		deviderHeight = (int) Utility.getPixelValue(getActivity(), 1);
		view = inflater.inflate(R.layout.fragment_headache_positive_step_three, container, false);
		createTreatmentList();
		createReliefList();
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

	private void createTreatmentList()
	{
		treatmentPanel = (LinearLayout) view.findViewById(R.id.medicationTherapyPanel);
		if (event.getTreatments() == null)
			treatmentList = dbhelper.getTreatments();
		else
			treatmentList = event.getTreatments();
		if (treatmentList != null && treatmentList.size() > 0)
		{
			int size = treatmentList.size();
			for (int i = 0; i < size; i++)
			{
				final Treatment treatment = treatmentList.get(i);
				View checkedView = mInflater.inflate(R.layout.checked_list_row, null, false);
				treatmentChkBox = (CheckBox) checkedView.findViewById(R.id.chkbox);
				treatmentChkBox.setTag(i);
				treatmentChkBox.setOnCheckedChangeListener(treatmentCheckListener);
				treatmentText = (AutoScaleTextView) checkedView.findViewById(R.id.chkBoxTextView);
				treatmentChkBox.setChecked(treatment.isChecked());
				treatmentText.setText(treatment.getName());
				treatmentTag = (ImageView) checkedView.findViewById(R.id.imgViewTag);
				treatmentTag.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						Utility.showAlert(getActivity(), "", treatment.getTag());
					}
				});
				if (treatment.getTag() != null && treatment.getTag().trim().length() > 0)
					treatmentTag.setVisibility(View.VISIBLE);

				View devider = new View(getActivity());
				devider.setLayoutParams(new ViewGroup.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, deviderHeight));
				devider.setBackgroundColor(getResources().getColor(R.color.list_devider_color));
				treatmentPanel.addView(checkedView);
				treatmentPanel.addView(devider);

			}
			treatmentPanel.removeViewAt(treatmentPanel.getChildCount() - 1);

		}
		btnTreatmentAddYourAns = new Button(getActivity());
		btnTreatmentAddYourAns.setTextAppearance(getActivity(), R.style.black_bold_font_medium);
		btnTreatmentAddYourAns.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_add_ur_ans));

		LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		// btnLp.setMargins(btnMargin, 0, btnMargin, 0);
		btnTreatmentAddYourAns.setLayoutParams(btnLp);
		btnTreatmentAddYourAns.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		btnTreatmentAddYourAns.setText(getActivity().getResources().getString(R.string.add_your_own_answer));
		Drawable drawableArrow = getActivity().getResources().getDrawable(R.drawable.ic_button_dark_arrow);
		btnTreatmentAddYourAns.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableArrow, null);
		btnTreatmentAddYourAns.setOnClickListener(this);
		treatmentPanel.addView(btnTreatmentAddYourAns);
	}

	private void createReliefList()
	{
		reliefPanel = (LinearLayout) view.findViewById(R.id.medicationTherapyReleifPanel);
		if (event.getReliefs() == null)
			releifList = dbhelper.getReliefs();
		else
			releifList = event.getReliefs();

		int size = releifList.size();
		for (int i = 0; i < size; i++)
		{
			final Relief relief = releifList.get(i);
			View checkedView = mInflater.inflate(R.layout.checked_list_row, null, false);
			reliefChkBox = (CheckBox) checkedView.findViewById(R.id.chkbox);
			reliefChkBox.setTag(i);
			reliefChkBox.setOnCheckedChangeListener(reliefCheckListener);
			reliefText = (AutoScaleTextView) checkedView.findViewById(R.id.chkBoxTextView);
			reliefChkBox.setChecked(relief.isChecked());
			reliefText.setText(relief.getName());
			reliefTag = (ImageView) checkedView.findViewById(R.id.imgViewTag);
			reliefTag.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					Utility.showAlert(getActivity(), "", relief.getTag());
				}
			});
			if (relief.getTag() != null && relief.getTag().trim().length() > 0)
				reliefTag.setVisibility(View.VISIBLE);

			View devider = new View(getActivity());
			devider.setLayoutParams(new ViewGroup.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, deviderHeight));
			devider.setBackgroundColor(getResources().getColor(R.color.list_devider_color));
			reliefPanel.addView(checkedView);
			reliefPanel.addView(devider);

		}
		reliefPanel.removeViewAt(reliefPanel.getChildCount() - 1);

		btnReleifAddYourAns = new Button(getActivity());
		btnReleifAddYourAns.setTextAppearance(getActivity(), R.style.black_bold_font_medium);
		btnReleifAddYourAns.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_add_ur_ans));

		LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		// btnLp.setMargins(btnMargin, 0, btnMargin, 0);
		btnReleifAddYourAns.setLayoutParams(btnLp);
		btnReleifAddYourAns.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		btnReleifAddYourAns.setText(getActivity().getResources().getString(R.string.add_your_own_answer));
		Drawable drawableArrow = getActivity().getResources().getDrawable(R.drawable.ic_button_dark_arrow);
		btnReleifAddYourAns.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableArrow, null);

		btnReleifAddYourAns.setOnClickListener(this);
		reliefPanel.addView(btnReleifAddYourAns);
	}

	private OnCheckedChangeListener reliefCheckListener = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{

			int index = Integer.parseInt(buttonView.getTag().toString());
			releifList.get(index).setChecked(isChecked);
			event.setReliefs(releifList);
			migraineEventChangedListener.onEventChanged(event);
		}
	};
	private OnCheckedChangeListener treatmentCheckListener = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{

			int index = Integer.parseInt(buttonView.getTag().toString());
			treatmentList.get(index).setChecked(isChecked);
			event.setTreatments(treatmentList);
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
		if (v == btnTreatmentAddYourAns)
		{
			Intent i = new Intent(getActivity(), AddYourOwnAnsActivity.class);
			i.putExtra(Constant.KEY_QUESTION, getString(R.string.add_your_own_migraine_treatment));
			i.putExtra(Constant.KEY_QUESTION_ID, Constant.QUESTION_TREATMENT_ID);
			startActivityForResult(i, REQ_CODE_ADD_YOUR_ANS_TREATMENT);
		}
		else if (v == btnReleifAddYourAns)
		{
			Intent i = new Intent(getActivity(), AddYourOwnAnsActivity.class);
			i.putExtra(Constant.KEY_QUESTION, getString(R.string.add_your_own_relief_desription));
			i.putExtra(Constant.KEY_QUESTION_ID, Constant.QUESTION_RELEIF_ID);
			startActivityForResult(i, REQ_CODE_ADD_YOUR_ANS_RELIEF);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQ_CODE_ADD_YOUR_ANS_TREATMENT)
		{
			if (resultCode == getActivity().RESULT_OK)
			{
				if (data.getExtras().getString(Constant.KEY_ANSWER).length() > 0)
				{
					Treatment treatment = new Treatment();
					treatment.setChecked(true);
					treatment.setName(data.getExtras().getString(Constant.KEY_ANSWER));
					treatment.setNewAnswer(true);

					treatment.setOrderNo(treatmentList.size());

					long id = dbhelper.insertYourAns(treatment);
					treatment.setId(id);
					treatmentPanel.removeAllViews();
					treatmentList.add(treatment);

					event.setTreatments(treatmentList);
					migraineEventChangedListener.onEventChanged(event);
				}
				else
				{
					event.setTreatments(null);
					treatmentPanel.removeAllViews();
				}
				createTreatmentList();

			}
		}
		else if (requestCode == REQ_CODE_ADD_YOUR_ANS_RELIEF)
		{
			if (resultCode == getActivity().RESULT_OK)
			{
				if (data.getExtras().getString(Constant.KEY_ANSWER).length() > 0)
				{
					Relief relief = new Relief();
					relief.setChecked(true);
					relief.setName(data.getExtras().getString(Constant.KEY_ANSWER));
					relief.setNewAnswer(true);
					relief.setOrderNo(releifList.size());
					long id = dbhelper.insertYourAns(relief);
					relief.setId(id);
					reliefPanel.removeAllViews();
					releifList.add(relief);
					event.setReliefs(releifList);
					migraineEventChangedListener.onEventChanged(event);
				}
				else
				{
					event.setReliefs(null);
					reliefPanel.removeAllViews();
				}
				createReliefList();

			}
		}
	}

}
