package com.novartis.mymigraine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.Constant;

@SuppressLint("SetJavaScriptEnabled")
public class LearnDetailActivity extends SherlockActivity
{
	private RelativeLayout learnDetailPanel;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_detail);
		learnDetailPanel = (RelativeLayout) findViewById(R.id.learnDetailPanel);
		actionBar = getSupportActionBar();
		createActionBar();
		String url = getIntent().getExtras().getString(Constant.Url);
		WebView mWebView = new WebView(LearnDetailActivity.this);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.loadUrl(url);
		learnDetailPanel.addView(mWebView);
	}

	private void createActionBar()
	{

		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_title));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return true;
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, getString(R.string.flurry_analytics_api_key));
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}
