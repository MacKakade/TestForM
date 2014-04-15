package com.novartis.mymigraine;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.flurry.android.FlurryAgent;

public class PrintPDFActivity extends SherlockActivity {



	 private static final String PRINT_DIALOG_URL = "https://www.google.com/cloudprint/dialog.html";
	  private static final String JS_INTERFACE = "AndroidPrintDialog";
	  private static final String CONTENT_TRANSFER_ENCODING = "base64";

	  private static final String ZXING_URL = "http://zxing.appspot.com";
	  private static final int ZXING_SCAN_REQUEST = 65743;

	  /**
	   * Post message that is sent by Print Dialog web page when the printing dialog
	   * needs to be closed.
	   */
	  private static final String CLOSE_POST_MESSAGE_NAME = "cp-dialog-on-close";

	  /**
	   * Web view element to show the printing dialog in.
	   */
	  private WebView dialogWebView;

	  /**
	   * Intent that started the action.
	   */
	  Intent cloudPrintIntent;

	  @SuppressLint("JavascriptInterface")
	@Override
	  public void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    
	    setContentView(R.layout.print_dialog);
	    
	    ActionBar ab =getSupportActionBar();
	    ab.hide();
	    dialogWebView = (WebView) findViewById(R.id.webview);
	    cloudPrintIntent = this.getIntent();

	    WebSettings settings = dialogWebView.getSettings();
	    settings.setJavaScriptEnabled(true);

	    dialogWebView.setWebViewClient(new PrintDialogWebClient());
	    dialogWebView.addJavascriptInterface(
	      new PrintDialogJavaScriptInterface(), JS_INTERFACE);

	    dialogWebView.loadUrl(PRINT_DIALOG_URL);
	  }

	  @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == ZXING_SCAN_REQUEST && resultCode == RESULT_OK) {
	      dialogWebView.loadUrl(intent.getStringExtra("SCAN_RESULT"));
	     
	    }
	  }

	  final class PrintDialogJavaScriptInterface {
	    public String getType() {
	      return cloudPrintIntent.getType();
	    }

	    public String getTitle() {
	      return cloudPrintIntent.getExtras().getString(getString(R.string.title));
	    }

	    public String getContent() {
	      try {
	        ContentResolver contentResolver = getContentResolver();
	        InputStream is = contentResolver.openInputStream(cloudPrintIntent.getData());
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();

	        byte[] buffer = new byte[4096];
	        int n = is.read(buffer);
	        while (n >= 0) {
	          baos.write(buffer, 0, n);
	          n = is.read(buffer);
	        }
	        is.close();
	        baos.flush();

	        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
	      } catch (FileNotFoundException e) {
	        e.printStackTrace();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	      return "";
	    }

	    public String getEncoding() {
	      return CONTENT_TRANSFER_ENCODING;
	    }

	    public void onPostMessage(String message) {
	      if (message.startsWith(CLOSE_POST_MESSAGE_NAME)) {
	    	  
	    	  showEditTextMessageDialog(PrintPDFActivity.this,getString(R.string.printDocument));
	        
	      }
	    }
	  }

	  private final class PrintDialogWebClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	      if (url.startsWith(ZXING_URL)) {
	        Intent intentScan = new Intent("com.google.zxing.client.android.SCAN");
	        intentScan.putExtra("SCAN_MODE", "QR_CODE_MODE");
	        try {
	          startActivityForResult(intentScan, ZXING_SCAN_REQUEST);
	        } catch (ActivityNotFoundException error) {
	          view.loadUrl(url);
	        }
	      } else {
	        view.loadUrl(url);
	      }
	      return false;
	    }

	    @Override
	    public void onPageFinished(WebView view, String url) {
	      if (PRINT_DIALOG_URL.equals(url)) {
	        // Submit print document.
	        view.loadUrl("javascript:printDialog.setPrintDocument(printDialog.createPrintDocument("
	          + "window." + JS_INTERFACE + ".getType(),window." + JS_INTERFACE + ".getTitle(),"
	          + "window." + JS_INTERFACE + ".getContent(),window." + JS_INTERFACE + ".getEncoding()))");

	        // Add post messages listener.
	        view.loadUrl("javascript:window.addEventListener('message',"
	            + "function(evt){window." + JS_INTERFACE + ".onPostMessage(evt.data)}, false)");
	      }
	    }
	  }
	  
	  /**
		 * 
		 * This method is used to show message to user.
		 * */
		public static void showEditTextMessageDialog(final Context context,
				final String message) {

			AlertDialog.Builder builder = new Builder(context);
			builder.setTitle(context.getString(R.string.app_name));
			builder.setMessage(message);
			builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					((Activity) context).finish();
				}
			});
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
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
