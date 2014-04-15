package com.novartis.mymigraine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.Constant;

public class PdfViewerActivity extends SherlockActivity {
	private ActionBar actionBar;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		actionBar = getSupportActionBar();
		createActionBar();

		// WebView mWebView=new WebView(PdfViewerActivity.this);
		// mWebView.getSettings().setJavaScriptEnabled(true);
		//
		// mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+Constant.PDF_FILE_URL);
		// setContentView(mWebView);

		String url = getIntent().getExtras().getString(Constant.Url);
		WebView mWebView = new WebView(this);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
			
	    
		// mWebView.getSettings().setPluginsEnabled(true);
		mWebView.loadUrl(url);
		setContentView(mWebView);

	}

	private void createActionBar() {
		
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_title));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			onBackPressed();
			break;
		}
		return true;
	}
	@Override
	protected void onStart() {
		
		super.onStart();
		FlurryAgent.onStartSession(this, getString(R.string.flurry_analytics_api_key));
	}
	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}
