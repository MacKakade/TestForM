package com.novartis.mymigraine.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TimePicker;

import com.novartis.mymigraine.AddYourOwnAnsActivity;
import com.novartis.mymigraine.MyMigraineApp;
import com.novartis.mymigraine.R;
import com.novartis.mymigraine.common.AutoScaleTextView;
import com.novartis.mymigraine.common.Constant;
import com.novartis.mymigraine.common.IMigraineEvent;
import com.novartis.mymigraine.common.IMigraineEventChangedListener;
import com.novartis.mymigraine.common.NumberPicker;
import com.novartis.mymigraine.common.PainIntenseButton;
import com.novartis.mymigraine.common.Utility;
import com.novartis.mymigraine.database.DataBaseHelper;
import com.novartis.mymigraine.model.Intensity;
import com.novartis.mymigraine.model.Location;
import com.novartis.mymigraine.model.MigraineEvent;

public class HeadacheStep1Fragment extends Fragment implements
		View.OnClickListener, OnCheckedChangeListener {
	public static final String TAG = "HeadachePositiveStep1Fragment";

	private Button btnStartHourPicker, btnHowLongLastPicker;
	
	private LayoutInflater mInflater;
	
	private ArrayList<Location> dataList;
	private LinearLayout locationOfPainPanel;

	private CheckBox chkItem;
	private AutoScaleTextView chkText;
	private Button btnAddUrAnsLocation;
	private PainIntenseButton btnIntensePainMild, btnIntensePainModerate,
			btnIntensePainSevere;
	private ImageView imgViewTag;
	
	private Button btnStartHourSelectedDone;
	Calendar cal = Calendar.getInstance();

	String startHourFormat = "'Started' h a";
	TimePickerDialog timePicker;
	AlertDialog.Builder durationHourPickerDialogBuilder;
	Dialog durationHourPickerDialog;
	NumberPicker hourPicker;
	private Button btnNumberPickDone, btnNumberPickCancel;
	SimpleDateFormat format = new SimpleDateFormat(startHourFormat);
	private MigraineEvent event;
	String startHourText;
	Intensity intensity;
	View checkedView;
	private int durationHour = 3;
	private IMigraineEventChangedListener migraineEventChangedListener;
	private static final int REQ_CODE_ADD_YOUR_ANS_LOCATION = 1;

	/**
	 * Database Helper class to Access the database.
	 */
	private DataBaseHelper dbhelper;
	private MyMigraineApp mymigraineapp;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//Log.d(TAG, "onCreateView call.......");
		mInflater = inflater;
		View view = inflater.inflate(
				R.layout.fragment_headache_positive_step_one, container, false);
		mymigraineapp = (MyMigraineApp) getActivity().getApplicationContext();
		dbhelper = mymigraineapp.getDbhelper();
		
		event = ((IMigraineEvent) getActivity()).getMigraineEvent();

		btnStartHourPicker = (Button) view
				.findViewById(R.id.btnStartHourPicker);

		btnStartHourPicker.setOnClickListener(this);
		btnHowLongLastPicker = (Button) view
				.findViewById(R.id.btnHowLongLastPicker);
		btnHowLongLastPicker.setOnClickListener(this);
		locationOfPainPanel = (LinearLayout) view
				.findViewById(R.id.locationOfPainPanel);

		btnIntensePainMild = (PainIntenseButton) view
				.findViewById(R.id.btnIntensePainMild);
		btnIntensePainMild.setOnClickListener(this);
		btnIntensePainMild.getChildAt(0).setOnClickListener(this);
		btnIntensePainModerate = (PainIntenseButton) view
				.findViewById(R.id.btnIntensePainModerate);
		btnIntensePainModerate.setOnClickListener(this);
		btnIntensePainModerate.getChildAt(0).setOnClickListener(this);
		btnIntensePainSevere = (PainIntenseButton) view
				.findViewById(R.id.btnIntensePainSevere);
		btnIntensePainSevere.setOnClickListener(this);
		btnIntensePainSevere.getChildAt(0).setOnClickListener(this);

		if (event.getIntensity() == null) {
			intensity = new Intensity();
			intensity.setId(Intensity.MODERATE_ANSWER_REF_ID);
			selectPainIntensity(Intensity.MODERATE_ANSWER_REF_ID);
		} else {
			intensity = event.getIntensity();
			selectPainIntensity((int) intensity.getId());
		}
		event.setIntensity(intensity);

		if (event.getStartHour() == 0) {
			startHourText = format.format(cal.getTime());

		} else {
			cal.set(Calendar.HOUR_OF_DAY, event.getStartHour());
			cal.set(Calendar.MINUTE, event.getStartMinute());
			startHourText = format.format(cal.getTime());
		}
		btnStartHourPicker.setText(startHourText);
		event.setStartHour(cal.get(Calendar.HOUR_OF_DAY));
		event.setStartMinute(cal.get(Calendar.MINUTE));
		// need to implement logic here
		if (event.getDuration() == 0) {
			event.setDuration(180);
			durationHour = 3;
			setHowLongLastButtonText(durationHour);
		} else {
			durationHour = event.getDuration() / 60;
			setHowLongLastButtonText(durationHour);

		}

		createLocationList();

		return view;
	}
	
	private void setHowLongLastButtonText(int durationHour)
	{
		if (durationHour == 1) {
			btnHowLongLastPicker.setText(durationHour + ":00 hour");
		} else {
			btnHowLongLastPicker.setText(durationHour + ":00 hours");
		}
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		if (activity instanceof IMigraineEventChangedListener) {
			migraineEventChangedListener = (IMigraineEventChangedListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet IMigraineEventChangedListener");
		}
	}

	private void createLocationList() {
		if (event.getLocations() == null) {
			dataList = dbhelper.getLocationOfPain();

		} else {
			dataList = event.getLocations();
		}
		int size = dataList.size();
		int deviderHeight = (int) Utility.getPixelValue(getActivity(), 1);

		for (int i = 0; i < size; i++) {
			final Location location = dataList.get(i);
			checkedView = mInflater.inflate(R.layout.checked_list_row, null,
					false);
			chkItem = (CheckBox) checkedView.findViewById(R.id.chkbox);
			chkItem.setTag(i);
			chkItem.setOnCheckedChangeListener(this);
			chkText = (AutoScaleTextView) checkedView
					.findViewById(R.id.chkBoxTextView);
			chkItem.setChecked(location.isChecked());
			chkText.setText(location.getName());
			imgViewTag = (ImageView) checkedView.findViewById(R.id.imgViewTag);
			if (location.getTag() != null
					&& location.getTag().trim().length() > 0) {
				imgViewTag.setVisibility(View.VISIBLE);
			}

			View devider = new View(getActivity());
			devider.setLayoutParams(new ViewGroup.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT, deviderHeight));
			devider.setBackgroundColor(getResources().getColor(
					R.color.list_devider_color));
			locationOfPainPanel.addView(checkedView);
			locationOfPainPanel.addView(devider);

		}
		locationOfPainPanel
				.removeViewAt(locationOfPainPanel.getChildCount() - 1);

		btnAddUrAnsLocation = new Button(getActivity());
		btnAddUrAnsLocation.setTextAppearance(getActivity(),
				R.style.black_bold_font_medium);
		btnAddUrAnsLocation.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_add_ur_ans));
		LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		// btnLp.setMargins(btnMargin, 0, btnMargin, 0);
		btnAddUrAnsLocation.setLayoutParams(btnLp);
		btnAddUrAnsLocation.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		btnAddUrAnsLocation.setText(getActivity().getResources().getString(
				R.string.add_your_own_answer));
		Drawable drawableArrow = getActivity().getResources().getDrawable(
				R.drawable.ic_button_dark_arrow);
		btnAddUrAnsLocation.setCompoundDrawablesWithIntrinsicBounds(null, null,
				drawableArrow, null);
		btnAddUrAnsLocation.setOnClickListener(this);
		locationOfPainPanel.addView(btnAddUrAnsLocation);
	}

	@Override
	public void onClick(View v) {

		if (v == btnIntensePainMild || v == btnIntensePainMild.getChildAt(0)) {
			selectPainIntenseButton(PainIntenseButton.PAIN_INTENTSITY_MILD);
			intensity.setId(Intensity.MILD_ANSWER_REF_ID);
			event.setIntensity(intensity);
			migraineEventChangedListener.onEventChanged(event);

		} else if (v == btnIntensePainModerate
				|| v == btnIntensePainModerate.getChildAt(0)) {
			selectPainIntenseButton(PainIntenseButton.PAIN_INTENTSITY_MODERATE);

			intensity.setId(Intensity.MODERATE_ANSWER_REF_ID);
			event.setIntensity(intensity);
			migraineEventChangedListener.onEventChanged(event);

		} else if (v == btnIntensePainSevere
				|| v == btnIntensePainSevere.getChildAt(0)) {
			selectPainIntenseButton(PainIntenseButton.PAIN_INTENTSITY_SEVERE);

			intensity.setId(Intensity.SEVERE_ANSWER_REF_ID);
			event.setIntensity(intensity);
			migraineEventChangedListener.onEventChanged(event);

		} else if (v == btnStartHourPicker) {
			showStartHourDialog();
		} else if (v == btnHowLongLastPicker) {
			showDurationHourDialog();

		} else if (v == btnAddUrAnsLocation) {

			Intent i = new Intent(getActivity(), AddYourOwnAnsActivity.class);
			i.putExtra(Constant.KEY_QUESTION,
					getString(R.string.add_your_own_location));
			i.putExtra(Constant.KEY_QUESTION_ID,
					Constant.QUESTION_LOCATION_OF_PAIN_ID);
			startActivityForResult(i, REQ_CODE_ADD_YOUR_ANS_LOCATION);

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_CODE_ADD_YOUR_ANS_LOCATION) {
			if (resultCode == getActivity().RESULT_OK) {

				if (data.getExtras().getString(Constant.KEY_ANSWER).length() > 0) {
					Location location = new Location();
					location.setChecked(true);
					location.setName(data.getExtras().getString(
							Constant.KEY_ANSWER));
					location.setNewAnswer(true);
				
					location.setOrderNo(dataList.size());
					long id = dbhelper.insertYourAns(location);
					location.setId(id);
					locationOfPainPanel.removeAllViews();
					dataList.add(location);

					

					event.setLocations(dataList);
					migraineEventChangedListener.onEventChanged(event);
				} else {
					event.setLocations(null);
					// migraineEventChangedListener.onEventChanged(event);
					locationOfPainPanel.removeAllViews();
				}
				createLocationList();

//				Log.d(TAG,
//						"Result received"
//								+ data.getExtras().getString(
//										Constant.KEY_ANSWER));
			}

		}
	}

	

	private void showStartHourDialog() {

		timePicker = new TimePickerDialog(getActivity(), timeSetListener,
				cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
		timePicker.show();
	}

	private void showDurationHourDialog() {

		durationHourPickerDialogBuilder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View myView = inflater.inflate(
				R.layout.layout_hour_picker_diolog, null);
		hourPicker = (NumberPicker) myView.findViewById(R.id.hourPicker);
		hourPicker.setCurrent(durationHour);
		btnNumberPickDone = (Button) myView
				.findViewById(R.id.btnNumberPickDone);
		btnNumberPickDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				event.setDuration(hourPicker.getCurrent() * 60);
				durationHour = hourPicker.getCurrent();
				setHowLongLastButtonText(durationHour);
				migraineEventChangedListener.onEventChanged(event);
				durationHourPickerDialog.dismiss();
			}
		});
		btnNumberPickCancel = (Button) myView
				.findViewById(R.id.btnNumberPickCancel);
		btnNumberPickCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				durationHourPickerDialog.dismiss();
			}
		});

		durationHourPickerDialogBuilder.setTitle("How long did it last?");
		durationHourPickerDialogBuilder.setView(myView);
		durationHourPickerDialog = durationHourPickerDialogBuilder.create();

		durationHourPickerDialog.show();

	}

	TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
			cal.set(Calendar.MINUTE, minute);
			startHourText = format.format(cal.getTime());
			btnStartHourPicker.setText(startHourText);
			event.setStartHour(cal.get(Calendar.HOUR_OF_DAY));
			event.setStartMinute(cal.get(Calendar.MINUTE));
			migraineEventChangedListener.onEventChanged(event);

		}
	};

	private void selectPainIntensity(int intensity) {
		switch (intensity) {
		case Intensity.MILD_ANSWER_REF_ID:
			selectPainIntenseButton(PainIntenseButton.PAIN_INTENTSITY_MILD);
			break;
		case Intensity.MODERATE_ANSWER_REF_ID:
			selectPainIntenseButton(PainIntenseButton.PAIN_INTENTSITY_MODERATE);
			break;
		case Intensity.SEVERE_ANSWER_REF_ID:
			selectPainIntenseButton(PainIntenseButton.PAIN_INTENTSITY_SEVERE);
			break;
		default:
			selectPainIntenseButton(PainIntenseButton.PAIN_INTENTSITY_MODERATE);
			break;
		}

	}

	private void selectPainIntenseButton(int type) {
		switch (type) {
		case PainIntenseButton.PAIN_INTENTSITY_MILD:
			btnIntensePainMild.setSelected(true);
			btnIntensePainModerate.setSelected(false);
			btnIntensePainSevere.setSelected(false);

			break;
		case PainIntenseButton.PAIN_INTENTSITY_MODERATE:
			btnIntensePainMild.setSelected(false);
			btnIntensePainModerate.setSelected(true);
			btnIntensePainSevere.setSelected(false);
			break;
		case PainIntenseButton.PAIN_INTENTSITY_SEVERE:
			btnIntensePainMild.setSelected(false);
			btnIntensePainModerate.setSelected(false);
			btnIntensePainSevere.setSelected(true);
			break;

		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		// Log.d("checked order",
		// ""+Integer.parseInt(buttonView.getTag().toString()));
		int index = Integer.parseInt(buttonView.getTag().toString());
		dataList.get(index).setChecked(isChecked);
		event.setLocations(dataList);
		migraineEventChangedListener.onEventChanged(event);

	}

}
