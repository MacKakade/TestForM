package com.novartis.mymigraine.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.novartis.mymigraine.ChartActivity;
import com.novartis.mymigraine.R;

public class Utility {
	public static final String ASSET_ROOT_PATH = "file:///android_asset/";

	public static float getPixelValue(Context context, int valueIdDP) {
		float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				valueIdDP, context.getResources().getDisplayMetrics());
		return pixels;
	}

	public static void showDPIOfDevice(Context context) {
		int density = context.getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_LOW:
			Toast.makeText(context, "LDPI", Toast.LENGTH_LONG).show();
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			Toast.makeText(context, "MDPI", Toast.LENGTH_LONG).show();
			break;
		case DisplayMetrics.DENSITY_HIGH:
			Toast.makeText(context, "HDPI", Toast.LENGTH_LONG).show();
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			Toast.makeText(context, "XHDPI", Toast.LENGTH_LONG).show();
			break;
		}
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return metrics.widthPixels;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return metrics.heightPixels;
	}

	public static void sendEmail(Context context, String[] to, String[] cc,
			String[] bcc, String subject, String text, Uri attachmentUri) {
		try {
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			// emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
			// emailIntent.setType("message/rfc822");
			emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
			if (cc != null)
				emailIntent.putExtra(Intent.EXTRA_CC, cc);
			if (bcc != null)
				emailIntent.putExtra(Intent.EXTRA_BCC, bcc);
			if (subject != null)
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

			emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(text));
			if (attachmentUri != null)
				// emailIntent.putExtra(Intent.EXTRA_STREAM, "content://" +
				// attachmentPath);
				emailIntent.putExtra(Intent.EXTRA_STREAM, attachmentUri);
			emailIntent.setType("text/html");

			if (isIntentSafe(context, emailIntent)) {
				Intent chooser = Intent.createChooser(emailIntent, context
						.getResources().getString(R.string.choose_mail_client));
				context.startActivity(chooser);

			} else {
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.no_email_client_installed_),
						Toast.LENGTH_SHORT).show();
			}

		} catch (android.content.ActivityNotFoundException e) {
			throw new ActivityNotFoundException(context.getResources()
					.getString(R.string.no_email_client_installed_));
		}

	}

	public static boolean isIntentSafe(Context context, Intent intent) {
		boolean isSafe = false;
		PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(
				intent, 0);
		isSafe = activities.size() > 0;
		return isSafe;

	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null)
			inputMethodManager.hideSoftInputFromWindow(activity
					.getCurrentFocus().getWindowToken(), 0);
		// inputMethodManager.toggleSoftInput(0,
		// InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public static void showKeyBoard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(activity.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {

			inputMethodManager.toggleSoftInput(0,
					InputMethodManager.SHOW_IMPLICIT);
		}
	}

	public static String getDataFromAsset(Context context, String filename)
			throws IOException {

		// Open your local db as the input stream
		InputStream is = context.getAssets().open(filename);

		StringBuffer strBuffer = new StringBuffer();

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) > 0) {
			strBuffer.append(new String(buffer, "UTF-8"));
		}
		return strBuffer.toString();
	}

	public static boolean isNetworkConnectionAvailable(Context context) {

		boolean connectedWifi = false;
		boolean connectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networks = cm.getAllNetworkInfo();
		for (NetworkInfo ni : networks) {
			if ("WIFI".equalsIgnoreCase(ni.getTypeName()))
				if (ni.isConnected())
					connectedWifi = true;
			if ("MOBILE".equalsIgnoreCase(ni.getTypeName()))
				if (ni.isConnected())
					connectedMobile = true;
		}
		return connectedWifi || connectedMobile;

	}

	public static void showAlert(Context context, String title, String text) {
		Builder alertBuilder = new Builder(context);
		alertBuilder.setTitle(title);
		alertBuilder.setMessage(text);
		alertBuilder.setPositiveButton("Ok", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertBuilder.create().show();
	}

	

	public static boolean isEmailAddressValid(String emailAdderss) {
		if (emailAdderss == null || emailAdderss.length() == 0|| emailAdderss.startsWith("."))
			return false;
		else
			return Patterns.EMAIL_ADDRESS.matcher(emailAdderss).matches();
	}

	/**
	 * 
	 * This method is used to show message to user.
	 * */
	public static void showLearnDialog(final Context context,
			final String titleofdialog) {

		final SharedPreferences preferences = context.getSharedPreferences(
				context.getString(R.string.app_name), Context.MODE_PRIVATE);
		final Editor editor = preferences.edit();

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.learn_dialog);
		dialog.setCancelable(false);

		TextView title = (TextView) dialog
				.findViewById(R.id.learn_dialog_txt_title);
		title.setText(titleofdialog);

		Button b = (Button) dialog.findViewById(R.id.learn_dialog_btnDone);
		b.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (context instanceof ChartActivity)
					editor.putBoolean(context.getString(R.string.learnChart),
							true);
				else
					editor.putBoolean(
							context.getString(R.string.learnDiaryLog), true);
				
				editor.commit();
				
				dialog.dismiss();

			}
		});
		dialog.show();
	}

}
