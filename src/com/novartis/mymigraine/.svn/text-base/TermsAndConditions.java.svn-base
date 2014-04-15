package com.novartis.mymigraine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.SharedPrefUtility;

public class TermsAndConditions extends Activity {

	private CheckBox chkTerms;
	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terms_and_conditions);
		
		chkTerms = (CheckBox) findViewById(R.id.terms_and_conditions_checkbox);
		chkTerms.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked){
					
					SharedPrefUtility.saveAppLoadFirstTimeValue(TermsAndConditions.this, false);
					Intent i = new Intent(TermsAndConditions.this,SettingActivity.class);
					i.putExtra("from", "TCScreen");
					startActivity(i);
					finish();
				}
			}
		});
		
		webView = (WebView) findViewById(R.id.terms_and_condition_webview);

//		webView.setWebViewClient(new WebViewClient() {
//			ProgressDialog dialog;
//
//			public void onPageStarted(WebView view, String url, Bitmap favicon) {
//
//				dialog = ProgressDialog.show(TermsAndConditions.this,
//						getString(R.string.app_name), getString(R.string.loading));
//				super.onPageStarted(view, url, favicon);
//			}
//
//			public void onPageFinished(WebView view, String url) {
//				dialog.dismiss();
//				super.onPageFinished(view, url);
//			}
//
//		});
		
		// load the URL into web view.
		webView.loadUrl(getString(R.string.terms_url));
		
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
