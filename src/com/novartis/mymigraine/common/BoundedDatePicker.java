package com.novartis.mymigraine.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.novartis.mymigraine.R;

public class BoundedDatePicker extends RelativeLayout
{
	// DatePicker reference
	private int startYear = 1900;
	private int endYear = 2100;

	private View myPickerView;

	private Button month_plus;
	private TextView month_display;
	private TextView month_display_next;
	private TextView month_display_previous;
	private Button month_minus;

	private Button date_plus;
	private TextView date_display;
	private TextView date_display_next;
	private TextView date_display_previous;
	private Button date_minus;
	Calendar prev2MonthCal, maxCal;

	private TextView dayOfWeek_display;

	private Calendar cal;
	private String[] days = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };

	int maxYear;

	public int getMaxYear()
	{
		return maxYear;
	}

	public void setMaxYear(int maxYear)
	{
		this.maxYear = maxYear;
	}

	public int getMaxMonth()
	{
		return maxMonth;
	}

	public void setMaxMonth(int maxMonth)
	{
		this.maxMonth = maxMonth;
	}

	public int getMaxDay()
	{
		return maxDay;
	}

	public void setMaxDay(int maxDay)
	{
		this.maxDay = maxDay;
	}

	public int getMinYear()
	{
		return minYear;
	}

	public void setMinYear(int minYear)
	{
		this.minYear = minYear;
	}

	public int getMinMonth()
	{
		return minMonth;
	}

	public void setMinMonth(int minMonth)
	{
		this.minMonth = minMonth;
	}

	public int getMinDay()
	{
		return minDay;
	}

	public void setMinDay(int minDay)
	{
		this.minDay = minDay;
	}

	int maxMonth;
	int maxDay;

	int minYear;
	int minMonth;
	int minDay;

	int mYear, mMonth, mDay;

	// Constructor start
	public BoundedDatePicker(Context context)
	{
		this(context, null);

		init(context);
	}

	public BoundedDatePicker(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public BoundedDatePicker(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		// // Get LayoutInflater instance

		LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myPickerView = inflator.inflate(R.layout.datetimepicker, null);
		this.addView(myPickerView);

		initializeReference();

	}

	private void init(Context mContext)
	{
		LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myPickerView = inflator.inflate(R.layout.datetimepicker, null);
		this.addView(myPickerView);

		initializeReference();
	}

	private void initializeReference()
	{

		month_plus = (Button) myPickerView.findViewById(R.id.month_plus);
		month_plus.setOnClickListener(month_plus_listener);
		month_display = (TextView) myPickerView.findViewById(R.id.month_display);
		month_display_next = (TextView) myPickerView.findViewById(R.id.month_display_next);
		month_display_previous = (TextView) myPickerView.findViewById(R.id.month_display_previous);
		month_minus = (Button) myPickerView.findViewById(R.id.month_minus);
		month_minus.setOnClickListener(month_minus_listener);

		date_plus = (Button) myPickerView.findViewById(R.id.date_plus);
		date_plus.setOnClickListener(date_plus_listener);
		date_display = (TextView) myPickerView.findViewById(R.id.date_display);
		date_display_next = (TextView) myPickerView.findViewById(R.id.date_display_next);
		date_display_previous = (TextView) myPickerView.findViewById(R.id.date_display_previous);
		date_display.addTextChangedListener(date_watcher);
		date_minus = (Button) myPickerView.findViewById(R.id.date_minus);
		date_minus.setOnClickListener(date_minus_listener);

		dayOfWeek_display = (TextView) myPickerView.findViewById(R.id.dayofweek_display);
		dayOfWeek_display.setOnFocusChangeListener(mLostFocusYear);

		initData();
		initFilterNumericDigit();

	}

	public void initData()
	{
		cal = Calendar.getInstance();
		month_display.setText(months[cal.get(Calendar.MONTH)]);
		month_display_previous.setText(months[cal.get(Calendar.MONTH) - 1 == -1 ? 0 : cal.get(Calendar.MONTH) - 1]);
		month_display_next.setText(months[cal.get(Calendar.MONTH) + 1 == 12 ? 11 : cal.get(Calendar.MONTH) + 1]);
		date_display.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		date_display_previous.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH) - 1));
		date_display_next.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH) + 1));
		dayOfWeek_display.setText(days[cal.get(Calendar.DAY_OF_WEEK) - 1]);

	}

	public void initData(Calendar cal)
	{
		this.cal = cal;
		maxCal = Calendar.getInstance();
		prev2MonthCal = Calendar.getInstance();
		prev2MonthCal.set(Calendar.MONTH, maxCal.get(Calendar.MONTH) - 2);

		minMonth = prev2MonthCal.get(Calendar.MONTH);
		minYear = prev2MonthCal.get(Calendar.YEAR);
		minDay = prev2MonthCal.get(Calendar.DAY_OF_MONTH);
		mYear = maxYear = maxCal.get(Calendar.YEAR);
		mMonth = maxMonth = maxCal.get(Calendar.MONTH);
		mDay = maxDay = maxCal.get(Calendar.DAY_OF_MONTH);

		month_display.setText(months[cal.get(Calendar.MONTH)]);
		month_display_previous.setText(months[cal.get(Calendar.MONTH) - 1 == -1 ? 0 : cal.get(Calendar.MONTH) - 1]);

		month_display_next.setText(months[cal.get(Calendar.MONTH) + 1 == 12 ? 11 : cal.get(Calendar.MONTH) + 1]);

		date_display.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		date_display_previous.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH) - 1));
		date_display_next.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH) + 1));
		dayOfWeek_display.setText(days[cal.get(Calendar.DAY_OF_WEEK) - 1]);
		initFilterNumericDigit();
	}

	private void initFilterNumericDigit()
	{

		try
		{
			date_display.setFilters(new InputFilter[] { new InputFilterMinMax(1, cal
					.getActualMaximum(Calendar.DAY_OF_MONTH)) });
			date_display_next.setFilters(new InputFilter[] { new InputFilterMinMax(1, cal
					.getActualMaximum(Calendar.DAY_OF_MONTH)) });
			date_display_previous.setFilters(new InputFilter[] { new InputFilterMinMax(1, cal
					.getActualMaximum(Calendar.DAY_OF_MONTH)) });

		}
		catch (Exception e)
		{

			e.printStackTrace();
		}
	}

	private void changeFilter()
	{
		try
		{

			date_display.setFilters(new InputFilter[] { new InputFilterMinMax(1, cal
					.getActualMaximum(Calendar.DAY_OF_MONTH)) });
			date_display_next.setFilters(new InputFilter[] { new InputFilterMinMax(1, cal
					.getActualMaximum(Calendar.DAY_OF_MONTH)) });
			date_display_previous.setFilters(new InputFilter[] { new InputFilterMinMax(1, cal
					.getActualMaximum(Calendar.DAY_OF_MONTH)) });

		}
		catch (Exception e)
		{
			date_display.setText("" + cal.get(Calendar.DAY_OF_MONTH));
			e.printStackTrace();
		}
	}

	String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	View.OnClickListener month_plus_listener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{

			if (cal.get(Calendar.MONTH) < maxCal.get(Calendar.MONTH)
					|| cal.get(Calendar.YEAR) < maxCal.get(Calendar.YEAR))
			{
				try
				{

					cal.add(Calendar.MONTH, 1);
					if (cal.getTime().after(maxCal.getTime()))
					{
						cal.set(Calendar.DAY_OF_MONTH, maxCal.get(Calendar.DAY_OF_MONTH));
					}
					// mMonth = cal.get(Calendar.MONTH);
					month_display.setText(months[cal.get(Calendar.MONTH)]);
					if (months[cal.get(Calendar.MONTH)].toString().trim().equals("Jan"))
					{
						month_display_previous.setText(months[11]);
						month_display_next.setText(months[1]);

					}
					else if (months[cal.get(Calendar.MONTH)].toString().trim().equals("Dec"))
					{
						month_display_next.setText(months[0]);
						month_display_previous.setText(months[10]);
					}
					else
					{
						month_display_previous.setText(months[cal.get(Calendar.MONTH) - 1]);
						month_display_next.setText(months[cal.get(Calendar.MONTH) + 1]);
					}
					dayOfWeek_display.setText(days[cal.get(Calendar.DAY_OF_WEEK) - 1]);
					// year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
					date_display.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));

					date_display_next.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH) + 1));
					date_display_previous.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH) - 1));

					changeFilter();
					sendToListener();
				}
				catch (Exception e)
				{
					// Log.e("", e.toString());
				}

			}
			// }
		}

	};
	View.OnClickListener month_minus_listener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{

			if (cal.get(Calendar.MONTH) > prev2MonthCal.get(Calendar.MONTH)
					|| cal.get(Calendar.YEAR) > prev2MonthCal.get(Calendar.YEAR))
			{

				try
				{
					cal.add(Calendar.MONTH, -1);
					if (cal.getTime().before(prev2MonthCal.getTime()))
					{
						cal.set(Calendar.DAY_OF_MONTH, prev2MonthCal.get(Calendar.DAY_OF_MONTH));
					}
					mMonth = cal.get(Calendar.MONTH);
					month_display.setText(months[cal.get(Calendar.MONTH)]);
					if (months[cal.get(Calendar.MONTH)].toString().trim().equals("Jan"))
					{
						month_display_previous.setText(months[11]);
						month_display_next.setText(months[1]);

					}
					else if (months[cal.get(Calendar.MONTH)].toString().trim().equals("Dec"))
					{
						month_display_next.setText(months[0]);
						month_display_previous.setText(months[10]);
					}
					else
					{
						month_display_previous.setText(months[cal.get(Calendar.MONTH) - 1]);
						month_display_next.setText(months[cal.get(Calendar.MONTH) + 1]);
					}
					// year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
					dayOfWeek_display.setText(days[cal.get(Calendar.DAY_OF_WEEK) - 1]);
					date_display.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
					date_display_next.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH) + 1));
					date_display_previous.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH) - 1));

					changeFilter();
					sendToListener();
				}
				catch (Exception e)
				{
					// Log.e("", e.toString());
				}
			}

		}

	};
	View.OnClickListener date_plus_listener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

			Date dt1 = null;
			Date dt2 = null;
			try
			{
				dt1 = format.parse(format.format(cal.getTime()));
				dt2 = format.parse(format.format(maxCal.getTime()));
			}
			catch (ParseException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (dt1.before(dt2))
			{

				try
				{
					date_display.requestFocus();
					cal.add(Calendar.DAY_OF_MONTH, 1);
					mDay = cal.get(Calendar.DAY_OF_MONTH);
					month_display.setText(months[cal.get(Calendar.MONTH)]);
					if (months[cal.get(Calendar.MONTH)].toString().trim().equals("Jan"))
					{
						month_display_previous.setText(months[11]);
						month_display_next.setText(months[1]);

					}
					else if (months[cal.get(Calendar.MONTH)].toString().trim().equals("Dec"))
					{
						month_display_next.setText(months[0]);
						month_display_previous.setText(months[10]);
					}
					else
					{
						month_display_previous.setText(months[cal.get(Calendar.MONTH) - 1]);
						month_display_next.setText(months[cal.get(Calendar.MONTH) + 1]);
					}
					dayOfWeek_display.setText(days[cal.get(Calendar.DAY_OF_WEEK) - 1]);

					date_display.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
					date_display_next.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH) + 1));
					date_display_previous.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH) - 1));

					sendToListener();
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	};
	View.OnClickListener date_minus_listener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

			Date dt1 = null;
			Date dt2 = null;
			try
			{
				dt1 = format.parse(format.format(cal.getTime()));
				dt2 = format.parse(format.format(prev2MonthCal.getTime()));
			}
			catch (ParseException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (dt1.after(dt2))
			{

				try
				{
					date_display.requestFocus();
					cal.add(Calendar.DAY_OF_MONTH, -1);
					mDay = cal.get(Calendar.DAY_OF_MONTH);
					month_display.setText(months[cal.get(Calendar.MONTH)]);
					if (months[cal.get(Calendar.MONTH)].toString().trim().equals("Jan"))
					{
						month_display_previous.setText(months[11]);
						month_display_next.setText(months[1]);

					}
					else if (months[cal.get(Calendar.MONTH)].toString().trim().equals("Dec"))
					{
						month_display_next.setText(months[0]);
						month_display_previous.setText(months[10]);
					}
					else
					{
						month_display_previous.setText(months[cal.get(Calendar.MONTH) - 1]);
						month_display_next.setText(months[cal.get(Calendar.MONTH) + 1]);
					}
					// year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
					dayOfWeek_display.setText(days[cal.get(Calendar.DAY_OF_WEEK) - 1]);
					date_display.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
					date_display_next.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH) + 1));
					date_display_previous.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH) - 1));

					sendToListener();
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	class InputFilterMinMax implements InputFilter
	{

		private int min, max;

		public InputFilterMinMax(int min, int max)
		{
			this.min = min;
			this.max = max;
		}

		public InputFilterMinMax(String min, String max)
		{
			this.min = Integer.parseInt(min);
			this.max = Integer.parseInt(max);
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
		{
			try
			{
				int input = Integer.parseInt(dest.toString() + source.toString());
				if (isInRange(min, max, input))
				{
					return null;
				}
			}
			catch (NumberFormatException nfe)
			{
			}
			return "";
		}

		private boolean isInRange(int a, int b, int c)
		{
			return b > a ? c >= a && c <= b : c >= b && c <= a;
		}
	}

	public void reset()
	{
		cal = Calendar.getInstance();
		initFilterNumericDigit();
		initData();
		// sendToDisplay();
	}

	synchronized private void sendToListener()
	{

		if (mDateWatcher != null)
		{
			mDateWatcher.onDateChanged(cal);
		}
	}

	public String getYear()
	{
		return dayOfWeek_display.getText().toString();
	}

	public int getDay()
	{
		return Integer.parseInt(date_display.getText().toString());
	}

	public String getMonth()
	{
		return month_display.getText().toString();
	}

	public int getMonthAsNumber()
	{
		return cal.get(Calendar.MONTH);
	}

	public int getYearAsNumber()
	{
		return cal.get(Calendar.YEAR);
	}

	public void setDateChangedListener(DateWatcher listener)
	{
		this.mDateWatcher = listener;
	}

	public void removeDateChangedListener()
	{
		this.mDateWatcher = null;
	}

	View.OnFocusChangeListener mLostFocusYear = new OnFocusChangeListener()
	{

		@Override
		public void onFocusChange(View v, boolean hasFocus)
		{
			if (!hasFocus)
			{
				dayOfWeek_display.setText(days[cal.get(Calendar.DAY_OF_WEEK)]);
				// year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));

			}
		}
	};

	TextWatcher date_watcher = new TextWatcher()
	{

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{

		}

		@Override
		public void afterTextChanged(Editable s)
		{

			try
			{
				if (s.toString().length() > 0)
				{
					// Log.e("", "afterTextChanged : " + s.toString());
					cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s.toString()));

					month_display.setText(months[cal.get(Calendar.MONTH)]);

					sendToListener();
				}
			}
			catch (NumberFormatException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	};

	DateWatcher mDateWatcher = null;

	public interface DateWatcher
	{
		void onDateChanged(Calendar c);
	}

}
