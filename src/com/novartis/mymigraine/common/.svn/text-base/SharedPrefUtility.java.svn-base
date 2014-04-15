package com.novartis.mymigraine.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtility
{
	public static final String IS_LOAD_FIRST_TIME="isLoadFirstTime";
	public static final String ASK_MENSTRUATION_QUESTION="Ask menstruatuion question";
	public static final String COUPON_EMAIL_ID="CouponEmailId";
	
	public static boolean isMenstruationQuestionAsk(Context context)
	{
		SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(com.novartis.mymigraine.R.string.app_name), Context.MODE_PRIVATE);
		if (preferences.contains(ASK_MENSTRUATION_QUESTION))
			return preferences.getBoolean(ASK_MENSTRUATION_QUESTION, true);
		else
			return true;
	}

	public static boolean saveAskMenstruationQuestion(Context context, boolean ask)
	{
		SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(com.novartis.mymigraine.R.string.app_name), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(ASK_MENSTRUATION_QUESTION, ask);
		return editor.commit();
	}

	
	public static boolean isAppLoadFirstTime(Context context)
	{
		SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(com.novartis.mymigraine.R.string.app_name), Context.MODE_PRIVATE);
		if (preferences.contains(IS_LOAD_FIRST_TIME))
			return preferences.getBoolean(IS_LOAD_FIRST_TIME, true);
		else
			return true;
	}
	public static boolean saveAppLoadFirstTimeValue(Context context, boolean isFirstTime)
	{
		SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(com.novartis.mymigraine.R.string.app_name), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(IS_LOAD_FIRST_TIME, isFirstTime);
		return editor.commit();
	}
	public static String getCouponEmailId(Context context)
	{
		SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(com.novartis.mymigraine.R.string.app_name), Context.MODE_PRIVATE);
		if (preferences.contains(COUPON_EMAIL_ID))
			return preferences.getString(COUPON_EMAIL_ID, "");
		else
			return "";
	}
	public static boolean saveCouponEmailId(Context context, String emailId)
	{
		SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(com.novartis.mymigraine.R.string.app_name), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(COUPON_EMAIL_ID, emailId);
		return editor.commit();
	}
}
