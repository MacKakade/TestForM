package com.novartis.mymigraine.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.ToggleButton;

import com.novartis.mymigraine.AddYourOwnAnsActivity;
import com.novartis.mymigraine.MyMigraineApp;
import com.novartis.mymigraine.R;
import com.novartis.mymigraine.common.AutoScaleTextView;
import com.novartis.mymigraine.common.Constant;
import com.novartis.mymigraine.common.IMigraineEvent;
import com.novartis.mymigraine.common.IMigraineEventChangedListener;
import com.novartis.mymigraine.common.Utility;
import com.novartis.mymigraine.database.DataBaseHelper;
import com.novartis.mymigraine.model.Fasting;
import com.novartis.mymigraine.model.Food;
import com.novartis.mymigraine.model.MigraineEvent;
import com.novartis.mymigraine.model.SkippedMeal;

public class HeadacheStep4Fragment extends Fragment implements View.OnClickListener
{
	public static final String TAG = "HeadachePositiveStep4Fragment";
	private IMigraineEventChangedListener migraineEventChangedListener;
	private LayoutInflater mInflater;
	private Button btnFastingNo, btnFastingYes;
	private ToggleButton btnBreakFast, btnLunch, btnDinner;
	private ArrayList<Food> foodList;
	private LinearLayout foodsPanel;
	int deviderHeight;
	CheckBox foodChkBox;
	AutoScaleTextView foodText;
	ImageView foodTag;
	private Button btnFoodAddYourAns;
	View view;
	private MigraineEvent event;
	Fasting fasting;
	ArrayList<SkippedMeal> skippedmeals;
	private static final int REQ_CODE_ADD_YOUR_ANS_FOOD = 6;
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
		view = inflater.inflate(R.layout.fragment_headache_positive_step_four, container, false);
		btnFastingNo = (Button) view.findViewById(R.id.btnFastingNo);
		btnFastingNo.setSelected(true);
		btnFastingNo.setOnClickListener(this);
		btnFastingYes = (Button) view.findViewById(R.id.btnFastingYes);
		btnFastingYes.setOnClickListener(this);
		if (event.getFasting() == null)
		{
			fasting = new Fasting();
			fasting.setFasting(false);
			event.setFasting(fasting);
			migraineEventChangedListener.onEventChanged(event);
		}
		else
		{
			fasting = event.getFasting();

		}
		setFasting(fasting);

		btnBreakFast = (ToggleButton) view.findViewById(R.id.btnBreakFast);
		btnBreakFast.setOnCheckedChangeListener(mealSkippedButtonCheckListner);
		btnDinner = (ToggleButton) view.findViewById(R.id.btnDinner);
		btnDinner.setOnCheckedChangeListener(mealSkippedButtonCheckListner);
		btnLunch = (ToggleButton) view.findViewById(R.id.btnLunch);
		btnLunch.setOnCheckedChangeListener(mealSkippedButtonCheckListner);

		setSkippedMeals();
		createFoodList();
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

	private void setSkippedMeals()
	{

		if (event.getSkippedMeals() == null)
		{
			skippedmeals = dbhelper.getSkippedMeals();
		}
		else
		{
			skippedmeals = event.getSkippedMeals();
		}
		int size = skippedmeals.size();
		for (int i = 0; i < size; i++)
		{

			SkippedMeal meal = skippedmeals.get(i);
			switch (i)
			{
			case SkippedMeal.SKIPPED_MEAL_BREAKFAST:
				btnBreakFast.setTag(i);
				btnBreakFast.setChecked(meal.isChecked());

				break;
			case SkippedMeal.SKIPPED_MEAL_LUNCH:
				btnLunch.setTag(i);
				btnLunch.setChecked(meal.isChecked());

				break;
			case SkippedMeal.SKIPPED_MEAL_DINNER:
				btnDinner.setTag(i);
				btnDinner.setChecked(meal.isChecked());

				break;
			}

		}
	}

	private void setFasting(Fasting fasting)
	{
		if (fasting.isFasting())
		{
			btnFastingYes.setSelected(true);
			btnFastingNo.setSelected(false);
		}
		else
		{
			btnFastingYes.setSelected(false);
			btnFastingNo.setSelected(true);
		}
	}

	private void createFoodList()
	{
		foodsPanel = (LinearLayout) view.findViewById(R.id.foodsPanel);
		if (event.getFoods() == null)
			foodList = dbhelper.getFoods();
		else
			foodList = event.getFoods();

		int size = foodList.size();
		for (int i = 0; i < size; i++)
		{
			final Food food = foodList.get(i);
			View checkedView = mInflater.inflate(R.layout.checked_list_row, null, false);
			foodChkBox = (CheckBox) checkedView.findViewById(R.id.chkbox);
			foodChkBox.setTag(i);
			foodChkBox.setOnCheckedChangeListener(foodCheckListener);
			foodText = (AutoScaleTextView) checkedView.findViewById(R.id.chkBoxTextView);
			foodChkBox.setChecked(food.isChecked());
			foodText.setText(food.getName());
			foodTag = (ImageView) checkedView.findViewById(R.id.imgViewTag);
			foodTag.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					Utility.showAlert(getActivity(), "", food.getTag());
				}
			});

			if (food.getTag() != null && food.getTag().trim().length() > 0)
				foodTag.setVisibility(View.VISIBLE);

			View devider = new View(getActivity());
			devider.setLayoutParams(new LinearLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, deviderHeight));
			devider.setBackgroundColor(getResources().getColor(R.color.list_devider_color));

			foodsPanel.addView(checkedView);
			foodsPanel.addView(devider);

		}
		foodsPanel.removeViewAt(foodsPanel.getChildCount() - 1);
		btnFoodAddYourAns = new Button(getActivity());
		btnFoodAddYourAns.setTextAppearance(getActivity(), R.style.black_bold_font_medium);
		btnFoodAddYourAns.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_add_ur_ans));

		LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		// btnLp.setMargins(btnMargin, 0, btnMargin, 0);
		btnFoodAddYourAns.setLayoutParams(btnLp);
		btnFoodAddYourAns.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		btnFoodAddYourAns.setText(getActivity().getResources().getString(R.string.add_your_own_answer));
		Drawable drawableArrow = getActivity().getResources().getDrawable(R.drawable.ic_button_dark_arrow);
		btnFoodAddYourAns.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableArrow, null);
		btnFoodAddYourAns.setOnClickListener(this);
		foodsPanel.addView(btnFoodAddYourAns);
	}

	@Override
	public void onClick(View v)
	{

		if (v == btnFastingYes)
		{

			fasting.setFasting(true);
			event.setFasting(fasting);
			migraineEventChangedListener.onEventChanged(event);
			setFasting(fasting);
		}
		else if (v == btnFastingNo)
		{

			fasting.setFasting(false);
			event.setFasting(fasting);
			migraineEventChangedListener.onEventChanged(event);
			setFasting(fasting);
		}
		else if (v == btnFoodAddYourAns)
		{
			Intent i = new Intent(getActivity(), AddYourOwnAnsActivity.class);
			i.putExtra(Constant.KEY_QUESTION, getString(R.string.add_your_own_food_item));
			i.putExtra(Constant.KEY_QUESTION_ID, Constant.QUESTION_FOOD_ID);
			startActivityForResult(i, REQ_CODE_ADD_YOUR_ANS_FOOD);
		}

	}

	private OnCheckedChangeListener foodCheckListener = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			int index = Integer.parseInt(buttonView.getTag().toString());
			foodList.get(index).setChecked(isChecked);
			event.setFoods(foodList);
			migraineEventChangedListener.onEventChanged(event);
		}
	};

	private OnCheckedChangeListener mealSkippedButtonCheckListner = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{

			int index = Integer.parseInt(buttonView.getTag().toString());
			skippedmeals.get(index).setChecked(isChecked);
			event.setSkippedMeals(skippedmeals);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQ_CODE_ADD_YOUR_ANS_FOOD)
		{
			if (resultCode == getActivity().RESULT_OK)
			{
				if (data.getExtras().getString(Constant.KEY_ANSWER).length() > 0)
				{
					Food food = new Food();
					food.setChecked(true);
					food.setName(data.getExtras().getString(Constant.KEY_ANSWER));
					food.setNewAnswer(true);
					food.setOrderNo(foodList.size());
					long id = dbhelper.insertYourAns(food);
					food.setId(id);
					foodsPanel.removeAllViews();
					foodList.add(food);

					event.setFoods(foodList);
					migraineEventChangedListener.onEventChanged(event);
				}
				else
				{
					event.setFoods(null);
					foodsPanel.removeAllViews();
				}
				createFoodList();

			}

		}
	}
}
