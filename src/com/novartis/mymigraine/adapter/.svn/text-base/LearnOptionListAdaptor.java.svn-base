package com.novartis.mymigraine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.novartis.mymigraine.R;

public class LearnOptionListAdaptor extends BaseAdapter
{
	private Context mContext;
	private String[] mDatalist;
	private LayoutInflater inflater = null;

	public LearnOptionListAdaptor(Context context, String[] datalist)
	{
		mContext = context;
		mDatalist = datalist;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount()
	{
		return mDatalist.length;
	}

	public Object getItem(int position)
	{

		return mDatalist[position];
	}

	public long getItemId(int position)
	{

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.learn_option_list_row, null);
		TextView learnOptionTextView = (TextView) vi.findViewById(R.id.lstTextViewLearnOption);
		learnOptionTextView.setText(mDatalist[position]);

		return vi;
	}

}
