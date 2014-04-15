package com.novartis.mymigraine.common;

public interface ISendEMailStatus {

	public int EMAIL_SENT_SUCCESSFULLY = 1;
	public int EMAIL_SENT_FAIL = 0;
	
	public void onEMailSent(int status);
}
