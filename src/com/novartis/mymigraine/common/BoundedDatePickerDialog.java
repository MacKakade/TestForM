package com.novartis.mymigraine.common;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

public class BoundedDatePickerDialog extends DatePickerDialog
{

	int maxYear;
	int maxMonth;
	int maxDay;

	int minYear;
	int minMonth;
	int minDay;

	public BoundedDatePickerDialog(Context context, OnDateSetListener callBack, int minYear, int minMonth, int minDay,
			int maxYear, int maxMonth, int maxDay)
	{
		super(context, callBack, maxYear, maxMonth, maxDay);
		this.minDay = minDay;
		this.minMonth = minMonth;
		this.minYear = minYear;
		this.maxDay = maxDay;
		this.maxMonth = maxMonth;
		this.maxYear = maxYear;
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
	{
		{
			super.onDateChanged(view, year, monthOfYear, dayOfMonth);
			
			 if (year < minYear)
                 view.updateDate(minYear, minMonth, minDay);

                 if (monthOfYear < minMonth && year == minYear  )
                 view.updateDate(minYear, minMonth, minDay );



                 if (dayOfMonth < minDay && year == minYear && monthOfYear == minMonth)
                 view.updateDate(minYear, minMonth, minDay);


                 if (year > maxYear)
                 view.updateDate(maxYear, maxMonth, maxDay);

                 if (monthOfYear > maxMonth && year == maxYear)
                 view.updateDate(maxYear, maxMonth, maxDay);

                 if (dayOfMonth > maxDay && year == maxYear && monthOfYear == maxMonth)
                 view.updateDate(maxYear, maxMonth, maxDay);
		}

	}
}
