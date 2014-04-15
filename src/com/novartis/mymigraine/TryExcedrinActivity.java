package com.novartis.mymigraine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.novartis.mymigraine.common.Constant;
import com.novartis.mymigraine.common.ISendEMailStatus;
import com.novartis.mymigraine.common.Utility;

public class TryExcedrinActivity extends SherlockActivity implements OnClickListener, ISendEMailStatus
{
	private RelativeLayout tryExcedrinPanel;
	private ImageButton btnGetYourCoupon;
	private Button btnCaplets;
	private EditText editTextEmailId;
	private ActionBar actionbar;
	private MenuItem menuItemMyDiary, menuItemChart, menuItemTryExcedrin;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_try_excedrin);
		actionbar = getSupportActionBar();
		createActionBar();
		// getSupportActionBar().setTitle("Home");
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		tryExcedrinPanel = (RelativeLayout) findViewById(R.id.tryExcedrinPanel);

		editTextEmailId = (EditText) findViewById(R.id.editTextEmailId);

		btnGetYourCoupon = (ImageButton) findViewById(R.id.btnGetYourCoupon);
		btnGetYourCoupon.setOnClickListener(this);
		btnGetYourCoupon.postDelayed(new Runnable()
		{

			@Override
			public void run()
			{
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				lp.topMargin = (int) (tryExcedrinPanel.getMeasuredHeight() / 2 + Utility.getPixelValue(
						TryExcedrinActivity.this, 65));
				lp.leftMargin = (tryExcedrinPanel.getMeasuredWidth() - btnGetYourCoupon.getMeasuredWidth()) / 2
						- (int) (Utility.getPixelValue(TryExcedrinActivity.this, 5));
				btnGetYourCoupon.setLayoutParams(lp);
				LayoutParams lp1 = (LayoutParams) editTextEmailId.getLayoutParams();
				lp1.topMargin = lp.topMargin - (int) (Utility.getPixelValue(TryExcedrinActivity.this, 45));

			}
		}, 10);
		btnCaplets = (Button) findViewById(R.id.btnCaplets);
		btnCaplets.setOnClickListener(this);
		// if (SharedPrefUtility.getCouponEmailId(TryExcedrinActivity.this)
		// .length() > 0) {
		// disableGetMyCoupenComponent();
		// }

	}

	// private void disableGetMyCoupenComponent() {
	// editTextEmailId.setText(SharedPrefUtility
	// .getCouponEmailId(TryExcedrinActivity.this));
	// editTextEmailId.setEnabled(false);
	// btnGetYourCoupon.setEnabled(false);
	//
	// }

	private void createActionBar()
	{
		actionbar = getSupportActionBar();
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_title));
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		getSupportMenuInflater().inflate(R.menu.activity_try_excedrin, menu);
		menuItemMyDiary = menu.findItem(R.id.menu_mydiary);
		menuItemChart = menu.findItem(R.id.menu_charts);
		menuItemTryExcedrin = menu.findItem(R.id.menu_try_excedrin);
		activateTryExcedrin();
		return true;

	}

	// private void activateMyDiary() {
	// menuItemMyDiary.setIcon(R.drawable.ic_tab_mydiary_selected);
	// menuItemChart.setIcon(R.drawable.ic_tab_charts_unselected);
	// menuItemTryExcedrin.setIcon(R.drawable.ic_tab_tryexcedrin_unselected);
	// }
	//
	// private void activateChart() {
	// menuItemMyDiary.setIcon(R.drawable.ic_tab_mydiary_unselected);
	// menuItemChart.setIcon(R.drawable.ic_tab_charts_selected);
	// menuItemTryExcedrin.setIcon(R.drawable.ic_tab_tryexcedrin_unselected);
	// }

	private void activateTryExcedrin()
	{
		menuItemMyDiary.setIcon(R.drawable.ic_tab_mydiary_unselected);
		menuItemChart.setIcon(R.drawable.ic_tab_charts_unselected);
		menuItemTryExcedrin.setIcon(R.drawable.ic_tab_tryexcedrin_selected);
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:

			onBackPressed();
			finish();
			// getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			// getSupportActionBar().setDisplayShowTitleEnabled(false);
			break;
		case R.id.menu_mydiary:

			// activateMyDiary();
			Intent intentMydiary = new Intent(this, MigraineTriggerMainActivity.class);
			// intentMydiary.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentMydiary);
			finish();

			break;
		case R.id.menu_charts:
			// activateChart();
			Intent intentChart = new Intent(this, ChartActivity.class);
			// intentChart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentChart);
			finish();
			break;
		case R.id.menu_diary_log:
			Intent intentDiaryLog = new Intent(this, DiaryLogDetailsActivity.class);
			startActivity(intentDiaryLog);
			finish();
			break;
		case R.id.menu_try_excedrin:
			// activateTryExcedrin();
			// Intent intentTryExcedrin = new Intent(this,
			// TryExcedrinActivity.class);
			// intentTryExcedrin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intentTryExcedrin);
			break;
		case R.id.menu_learn:
			Intent learnIntent = new Intent(this, LearnActivity.class);
			// learnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(learnIntent);
			finish();
			break;
		case R.id.menu_help:
			Intent usingThisAppIntent = new Intent(this, HelpActivity.class);
			usingThisAppIntent.putExtra(Constant.Url, Constant.VIEW_USING_THIS_APP_ASSET_URL);
			startActivity(usingThisAppIntent);
			finish();
			break;
		case R.id.menu_setting:
			Intent intentsetting = new Intent(this, SettingActivity.class);
			startActivity(intentsetting);
			break;
		}
		return true;
	}

	@Override
	public void onClick(View v)
	{
		if (v == btnGetYourCoupon)
		{

			// try {
			// Utility.sendEmail(this, null, null, null,
			// Constant.EMAIL_SUBJECT, Utility.getDataFromAsset(this,
			// Constant.EMAIL_BODY_HTML_FILE), null);
			//
			//
			// } catch (IOException e) {
			//
			// e.printStackTrace();
			// }

			if (Utility.isNetworkConnectionAvailable(this))
			{
				if (editTextEmailId.getText().toString().trim().length() > 0)
				{
					if (Utility.isEmailAddressValid(editTextEmailId.getText().toString()))
					{
						Utility.hideSoftKeyboard(this);
						// "https://www.novartis-otc.com/webservices/sendNonTemplateEmail?recipientEmail="+editTextEmailId.getText().toString()
						/*
						 * new SendMailAsyncTask(this, this)
						 * .execute(editTextEmailId.getText().toString());
						 */
						new CallEmailWS().execute();

					}
					else
					{
						Utility.showAlert(this, "Invalid email address", "Please enter valid email address.");
					}
				}
				else
					Utility.showAlert(this, "Warning", "Please enter email address.");
			}
			else
			{
				Utility.showAlert(this, "Network not available",
						getResources().getString(R.string.network_not_availbale));
			}
		}
		else if (v == btnCaplets)
		{
			// Intent i = new Intent(this, PdfViewerActivity.class);
			// startActivity(i);
			Intent i = new Intent(TryExcedrinActivity.this, PdfViewerActivity.class);
			i.putExtra(Constant.Url, Constant.VIEW_MIGRAINE_CAPLETS_ASSET_URL);
			startActivity(i);
		}

	}

	public class CallEmailWS extends AsyncTask<Void, Void, String>
	{

		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialog.show(TryExcedrinActivity.this, null,
					getResources().getString(R.string.progress_dialog_msg_wait), true, false);
		}

		@Override
		protected String doInBackground(Void... params)
		{
			String result = "";
			try
			{
				HttpClient mClient = new DefaultHttpClient();
				HttpGet get = new HttpGet(getString(R.string.email_url) + editTextEmailId.getText().toString()
						+ "&recipientName=testUser");
				HttpResponse response = mClient.execute(get);
				result = convertInputStreamToString(response);
				// Log.e("Mihir", result);
			}
			catch (ClientProtocolException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(final String result)
		{
			super.onPostExecute(result);

			if (progressDialog != null)
				progressDialog.dismiss();
			runOnUiThread(new Runnable()
			{

				@Override
				public void run()
				{

					if (result != null && result.length() > 0)
					{
						try
						{
							JSONObject jsonObject = new JSONObject(result);
							if (jsonObject.getBoolean("success"))
							{
								// Utility.showAlert(TryExcedrinActivity.this,
								// "",
								// jsonObject.getString("data"));

								editTextEmailId.setText("");
								Utility.showAlert(TryExcedrinActivity.this, "", "Email successfully sent.");

							}
							else
							{
								/*
								 * JSONArray jsonArray =
								 * jsonObject.getJSONArray("messages");
								 * Utility.showAlert(TryExcedrinActivity.this,
								 * "",
								 * jsonArray.getJSONObject(0).getString("message"
								 * ));
								 */
								Utility.showAlert(TryExcedrinActivity.this, "",
										"Email can't be sent. Please try again later!");
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							Utility.showAlert(TryExcedrinActivity.this, "",
									"Email can't be sent. Please try again later!");
						}
					}
					else
					{
						Utility.showAlert(TryExcedrinActivity.this, "", "Email can't be sent. Please try again later!");
					}
				}
			});

		}

	}

	public String convertInputStreamToString(HttpResponse response)
	{
		BufferedReader bufferedReader = null;
		try
		{
			bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 8192);
			final StringBuffer stringBuffer = new StringBuffer("");
			String line = "";
			final String LineSeparator = System.getProperty("line.separator");

			while ((line = bufferedReader.readLine()) != null)
			{
				stringBuffer.append(line + LineSeparator);
			}
			return stringBuffer.toString();
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (bufferedReader != null)
			{
				try
				{
					bufferedReader.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	@Override
	public void onEMailSent(int status)
	{
		if (status == EMAIL_SENT_SUCCESSFULLY)
		{
			//Log.d("Email sent status", "Email successfully sent");
			runOnUiThread(new Runnable()
			{

				@Override
				public void run()
				{
					editTextEmailId.setText("");
					Utility.showAlert(TryExcedrinActivity.this, "Status", "Email sent successfully.");

				}
			});

			// SharedPrefUtility.saveCouponEmailId(this,
			// editTextEmailId.getText()
			// .toString());

			// disableGetMyCoupenComponent();

		}

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
