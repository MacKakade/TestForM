package com.novartis.mymigraine.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.novartis.mymigraine.AddYourOwnAnsActivity;
import com.novartis.mymigraine.MyMigraineApp;
import com.novartis.mymigraine.R;
import com.novartis.mymigraine.common.AutoScaleTextView;
import com.novartis.mymigraine.database.DataBaseHelper;
import com.novartis.mymigraine.model.ChoiseItem;

public class AddYourAnsListAdaptor extends BaseAdapter implements
		OnClickListener {
	private static LayoutInflater inflater = null;
	Activity activity;
	JSONArray deletedEntry=new JSONArray();
	private ArrayList<ChoiseItem> dataList;
	/**
	 * Database Helper class to Access the database.
	 */
	private DataBaseHelper dbhelper;
	private MyMigraineApp mymigraineapp;

	public AddYourAnsListAdaptor(Activity activity, ArrayList<ChoiseItem> dataList) {
		this.activity = activity;
		this.dataList = dataList;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mymigraineapp = (MyMigraineApp) this.activity.getApplicationContext();
		dbhelper = mymigraineapp.getDbhelper();
		

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
			convertView = inflater
					.inflate(R.layout.add_your_ans_list_row, null);
			holder = new ViewHolder();
			holder.btnAnsDelete = (ImageButton) convertView
					.findViewById(R.id.btnAnsDelete);
			holder.btnAnsDelete.setOnClickListener(this);
		
			holder.listTextViewAns = (AutoScaleTextView) convertView
					.findViewById(R.id.listTextViewAns);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.btnAnsDelete.setTag(position);
		holder.listTextViewAns.setText(dataList.get(position).getName());

		return convertView;
	}

	static class ViewHolder {

		AutoScaleTextView listTextViewAns;
		ImageButton btnAnsDelete;
	}

	@Override
	public void onClick(View v) {
		int index = Integer.parseInt(v.getTag().toString());
		//dbhelper.deleteAnswer(String.valueOf(dataList.get(index).getId()));
		deletedEntry.put(dataList.get(index).getId());
		dataList.remove(index);
		notifyDataSetChanged();
		((AddYourOwnAnsActivity)activity).setEntryDeleted(true);
		

	}
	
	public String getDeletedItemIds()
	{
		String answerIds;
		answerIds=deletedEntry.toString();
		answerIds=answerIds.replace("[", " ");
		answerIds=answerIds.replace("]", " ");
		return answerIds.trim();
	}
}
