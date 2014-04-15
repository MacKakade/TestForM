package com.novartis.mymigraine.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.TextView;

public class GalleryAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> chartNameArray;

	public GalleryAdapter(Context context, ArrayList<String> reportImagesPath) {
		this.mContext = context;
		this.chartNameArray = reportImagesPath;
	}

	@Override
	public int getCount() {
		return chartNameArray.size();
	}

	@Override
	public Object getItem(int position) {
		return chartNameArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		return chartNameArray.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textview;
		if (convertView == null) {
			textview = new TextView(mContext);
			textview.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			textview.setTextColor(Color.WHITE);
			textview.setTextSize(15);
			textview.setPadding(5, 10, 5, 10);
		} else {
			textview = (TextView) convertView;
		}
		
		if(chartNameArray.get(position) != null && !chartNameArray.get(position).equals(""))
		{
			textview.setText(chartNameArray.get(position).toString());
		}
		
		return textview;
	}
}
