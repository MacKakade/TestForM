package com.novartis.mymigraine.model;

import com.novartis.mymigraine.common.Constant;

public class Menstruate extends ChoiseItem {
	private int menstrate;

	public int getMenstrate() {
		
		switch ((int)getId()) {
		case MENSTRUATION_YES_ID:
			menstrate=YES;
			break;
		case MENSTRUATION_NO_ID:
			menstrate=NO;
			break;
		case MENSTRUATION_DONT_ASK_AGAIN_ID:
			menstrate=DONT_ASK_AGAIN;
			break;
		
		}
		return menstrate;

	}

	public void setMenstrate(int menstrate) {
		this.menstrate = menstrate;
		switch (menstrate) {
		case YES:
			setId(MENSTRUATION_YES_ID);
			break;
		case NO:
			setId(MENSTRUATION_NO_ID);
			break;
		case DONT_ASK_AGAIN:
			setId(MENSTRUATION_DONT_ASK_AGAIN_ID);
			break;

		}
	}

	public static final int YES = 1;
	public static final int NO = 0;
	public static final int DONT_ASK_AGAIN = 2;

	public static final int MENSTRUATION_YES_ID = 62;
	public static final int MENSTRUATION_NO_ID = 63;
	public static final int MENSTRUATION_DONT_ASK_AGAIN_ID = 64;

	

	public int getQuestionId() {
		return  Constant.QUESTION_MESTRUAL_ID;
	}

}
