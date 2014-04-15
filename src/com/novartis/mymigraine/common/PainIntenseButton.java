package com.novartis.mymigraine.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.novartis.mymigraine.R;

public class PainIntenseButton extends LinearLayout
{
	public static final int PAIN_INTENTSITY_MILD = 0;
	public static final int PAIN_INTENTSITY_MODERATE = 1;
	public static final int PAIN_INTENTSITY_SEVERE = 2;

	private int level;
	private boolean isSelected;
	TypedArray a;
	private TextView txtViewIntensePain;
	private View viewStripe;

	public PainIntenseButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		if (attrs != null)
		{
			a = context.obtainStyledAttributes(attrs, R.styleable.PainIntenseButton);
			level = a.getInt(R.styleable.PainIntenseButton_intenseLevel, 1);
			isSelected = a.getBoolean(R.styleable.PainIntenseButton_isSelected, false);
			// this.setSelected(isSelected);
			a.recycle();
		}
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.intense_pain_button_layout, this, true);
		this.setClickable(true);
		txtViewIntensePain = (TextView) view.findViewById(R.id.txtViewIntensePain);
		viewStripe = view.findViewById(R.id.viewStripe);

		setValue();

	}

	private void setValue()
	{

		if (isSelected)
		{
			this.setSelected(true);

		}

		switch (level)
		{
		case PAIN_INTENTSITY_MILD:
			viewStripe.setBackgroundColor(getResources().getColor(R.color.pain_intense_mild_color));
			txtViewIntensePain.setText(getResources().getString(R.string.mild));
			break;
		case PAIN_INTENTSITY_MODERATE:
			viewStripe.setBackgroundColor(getResources().getColor(R.color.pain_intense_moderate_color));
			txtViewIntensePain.setText(getResources().getString(R.string.moderate));
			break;
		case PAIN_INTENTSITY_SEVERE:

			viewStripe.setBackgroundColor(getResources().getColor(R.color.pain_intense_severe_color));
			txtViewIntensePain.setText(getResources().getString(R.string.severe));
			break;
		}
	}

	public boolean isSelected()
	{
		return isSelected;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

}
