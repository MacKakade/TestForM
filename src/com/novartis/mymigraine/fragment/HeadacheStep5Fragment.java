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
import com.novartis.mymigraine.model.LifeStyle;
import com.novartis.mymigraine.model.MigraineEvent;

public class HeadacheStep5Fragment extends Fragment implements OnClickListener {
	public static final String TAG = "HeadachePositiveStep5Fragment";
	private IMigraineEventChangedListener migraineEventChangedListener;
	private LayoutInflater mInflater;
	private LinearLayout lifeStylePanel;
	private ArrayList<LifeStyle> lifeStyleList;
	int deviderHeight;
	CheckBox lifeStyleChkBox;
	AutoScaleTextView lifeStyleText;
	ImageView lifeStyleTag;
	private Button btnLifeStyleAddYourAns;
	View view;
	private MigraineEvent event;
	private static final int REQ_CODE_ADD_YOUR_ANS_LIFESTYLE = 7;
	/**
	 * Database Helper class to Access the database.
	 */
	private DataBaseHelper dbhelper;
	private MyMigraineApp mymigraineapp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		mymigraineapp = (MyMigraineApp) getActivity().getApplicationContext();
		dbhelper = mymigraineapp.getDbhelper();

		deviderHeight = (int) Utility.getPixelValue(getActivity(), 1);
		view = inflater.inflate(R.layout.fragment_headache_positive_step_five,
				container, false);
		event = ((IMigraineEvent) getActivity()).getMigraineEvent();
		createLifeStyleList();
		return view;
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

	private void createLifeStyleList() {
		lifeStylePanel = (LinearLayout) view.findViewById(R.id.lifeStylePanel);
		if (event.getLifeStyles() == null)
			lifeStyleList = dbhelper.getLifeStyle();
		else
			lifeStyleList = event.getLifeStyles();

		int size = lifeStyleList.size();
		for (int i = 0; i < size; i++) {
			final LifeStyle lifeStyle = lifeStyleList.get(i);
			View checkedView = mInflater.inflate(R.layout.checked_list_row,
					null, false);
			lifeStyleChkBox = (CheckBox) checkedView.findViewById(R.id.chkbox);
			lifeStyleChkBox.setTag(i);
			lifeStyleChkBox.setOnCheckedChangeListener(lifeStyleCheckListener);
			lifeStyleText = (AutoScaleTextView) checkedView
					.findViewById(R.id.chkBoxTextView);
			lifeStyleChkBox.setChecked(lifeStyle.isChecked());
			lifeStyleText.setText(lifeStyle.getName());
			lifeStyleTag = (ImageView) checkedView
					.findViewById(R.id.imgViewTag);
			lifeStyleTag.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Utility.showAlert(getActivity(), "", lifeStyle.getTag());
				}
			});
			if (lifeStyle.getTag() != null
					&& lifeStyle.getTag().trim().length() > 0)
				lifeStyleTag.setVisibility(View.VISIBLE);
			View devider = new View(getActivity());
			devider.setLayoutParams(new LinearLayout.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT, deviderHeight));
			devider.setBackgroundColor(getResources().getColor(
					R.color.list_devider_color));

			lifeStylePanel.addView(checkedView);
			lifeStylePanel.addView(devider);

		}
		lifeStylePanel.removeViewAt(lifeStylePanel.getChildCount() - 1);
		btnLifeStyleAddYourAns = new Button(getActivity());
		btnLifeStyleAddYourAns.setTextAppearance(getActivity(),
				R.style.black_bold_font_medium);
		btnLifeStyleAddYourAns.setBackgroundDrawable(getResources()
				.getDrawable(R.drawable.button_add_ur_ans));

		LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		// btnLp.setMargins(btnMargin, 0, btnMargin, 0);
		btnLifeStyleAddYourAns.setLayoutParams(btnLp);
		btnLifeStyleAddYourAns.setGravity(Gravity.LEFT
				| Gravity.CENTER_VERTICAL);
		btnLifeStyleAddYourAns.setText(getActivity().getResources().getString(
				R.string.add_your_own_answer));
		Drawable drawableArrow = getActivity().getResources().getDrawable(
				R.drawable.ic_button_dark_arrow);
		btnLifeStyleAddYourAns.setCompoundDrawablesWithIntrinsicBounds(null,
				null, drawableArrow, null);
		btnLifeStyleAddYourAns.setOnClickListener(this);
		lifeStylePanel.addView(btnLifeStyleAddYourAns);
	}

	private OnCheckedChangeListener lifeStyleCheckListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			int index = Integer.parseInt(buttonView.getTag().toString());
			lifeStyleList.get(index).setChecked(isChecked);
			event.setLifeStyles(lifeStyleList);
			migraineEventChangedListener.onEventChanged(event);
		}
	};

	@Override
	public void onResume() {
		//Log.d(TAG, "On Resume call.......");
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		if (v == btnLifeStyleAddYourAns) {
			Intent i = new Intent(getActivity(), AddYourOwnAnsActivity.class);
			i.putExtra(Constant.KEY_QUESTION,
					getString(R.string.add_your_own_item));
			i.putExtra(Constant.KEY_QUESTION_ID,
					Constant.QUESTION_LIFE_STYLE_ID);
			startActivityForResult(i, REQ_CODE_ADD_YOUR_ANS_LIFESTYLE);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_CODE_ADD_YOUR_ANS_LIFESTYLE) {
			if (resultCode == getActivity().RESULT_OK) {
				if (data.getExtras().getString(Constant.KEY_ANSWER).length() > 0) {
					LifeStyle lifestyle = new LifeStyle();
					lifestyle.setChecked(true);
					lifestyle.setName(data.getExtras().getString(
							Constant.KEY_ANSWER));
					lifestyle.setNewAnswer(true);
					lifestyle.setOrderNo(lifeStyleList.size());
					long id = dbhelper.insertYourAns(lifestyle);
					lifestyle.setId(id);
					lifeStylePanel.removeAllViews();
					lifeStyleList.add(lifestyle);
					event.setLifeStyles(lifeStyleList);
					migraineEventChangedListener.onEventChanged(event);
				} else {
					event.setLifeStyles(null);
					lifeStylePanel.removeAllViews();
				}
				createLifeStyleList();

			}

		}
	}
}