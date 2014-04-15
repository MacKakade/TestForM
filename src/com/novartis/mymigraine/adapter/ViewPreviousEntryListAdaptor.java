package com.novartis.mymigraine.adapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.novartis.mymigraine.R;
import com.novartis.mymigraine.model.Intensity;
import com.novartis.mymigraine.model.MigraineEvent;

public class ViewPreviousEntryListAdaptor extends BaseAdapter {
	private static LayoutInflater inflater = null;
	Activity activity;
	private ArrayList<MigraineEvent> dataList;

	private Calendar cal;
	private DecimalFormat dayFormat=new DecimalFormat("00");
	private SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
	private SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
	private SimpleDateFormat hourformat = new SimpleDateFormat("hh", Locale.getDefault());
	private SimpleDateFormat ampmFormat = new SimpleDateFormat("a", Locale.getDefault());
	private MigraineEvent event;

	public ViewPreviousEntryListAdaptor(Activity activity,
			ArrayList<MigraineEvent> dataList) {
		this.activity = activity;
		this.dataList = dataList;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {
		return dataList.size();
	}

	public Object getItem(int position) {

		return dataList.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.view_previous_entry_list_row, null);
			holder = new ViewHolder();
			holder.lstTxtViewDayOfMonth = (TextView) convertView
					.findViewById(R.id.lstTxtViewDayOfMonth);
			holder.lstTxtViewMonth = (TextView) convertView
					.findViewById(R.id.lstTxtViewMonth);
			holder.lstTxtViewDayOfWeek = (TextView) convertView
					.findViewById(R.id.lstTxtViewDayOfWeek);
			holder.lstTxtHeadacheTime = (TextView) convertView
					.findViewById(R.id.lstTxtHeadacheTime);
			holder.lstIntenseStripe = (View) convertView
					.findViewById(R.id.lstIntenseStripe);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		event = dataList.get(position);
		cal = Calendar.getInstance(Locale.getDefault());
		
		cal.setTime(event.getDateTime());
		cal.set(Calendar.HOUR_OF_DAY, event.getStartHour());
		cal.set(Calendar.MINUTE, event.getStartMinute());
		holder.lstTxtViewDayOfMonth
				.setText("" + dayFormat.format(cal.get(Calendar.DAY_OF_MONTH)));
		holder.lstTxtViewMonth.setText("" + monthFormat.format(cal.getTime()));
		holder.lstTxtViewDayOfWeek.setText(""
				+ dayOfWeekFormat.format(cal.getTime()));
		if (event.isHasHeadache()) {
			final String hourslabel;
			final String minute;
			hourslabel = (event.getDuration() / 60) == 1 ? "hour" : "hours";
			if(dataList.get(position).getStartMinute()<10){
				minute = "0"+dataList.get(position).getStartMinute();
			}
			else{
				minute = String.valueOf(dataList.get(position).getStartMinute());
			}
			holder.lstTxtHeadacheTime.setText(""
					+ hourformat.format(cal.getTime()) +":"+minute+ " " +ampmFormat.format(cal.getTime()) + ", lasted for "
					+ event.getDuration() / 60 + " " + hourslabel);
			setIntensityColor(holder.lstIntenseStripe, (int) event
					.getIntensity().getId());
		} else {
			holder.lstTxtHeadacheTime.setText("No Headache");
			setIntensityColor(holder.lstIntenseStripe, 0);
		}

		return convertView;
	}

	static class ViewHolder {

		TextView lstTxtViewDayOfMonth, lstTxtViewMonth, lstTxtViewDayOfWeek,
				lstTxtHeadacheTime;
		View lstIntenseStripe;
		ImageView imgViewEdit;

	}

	private void setIntensityColor(View v, int intensity) {
		switch (intensity) {
		case Intensity.MILD_ANSWER_REF_ID:
			v.setBackgroundColor(activity.getResources().getColor(
					R.color.pain_intense_mild_color));

			break;
		case Intensity.MODERATE_ANSWER_REF_ID:
			v.setBackgroundColor(activity.getResources().getColor(
					R.color.pain_intense_moderate_color));

			break;
		case Intensity.SEVERE_ANSWER_REF_ID:
			v.setBackgroundColor(activity.getResources().getColor(
					R.color.pain_intense_severe_color));

			break;
		default:
			v.setBackgroundColor(Color.parseColor("#e9e9e9"));
			break;

		}
	}

}
