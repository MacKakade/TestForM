package com.novartis.mymigraine.model;

import com.novartis.mymigraine.common.Constant;

public class Fasting extends ChoiseItem {
	public static final int FASTING_YES_ANSWER_REF_ID = 31;
	public static final int FASTING_NO_ANSWER_REF_ID = 32;
	
	private boolean fasting;

	public boolean isFasting() {
		return getId()==FASTING_YES_ANSWER_REF_ID;
	}

	public void setFasting(boolean fasting) {
		this.fasting = fasting;
		setId(fasting ? FASTING_YES_ANSWER_REF_ID : FASTING_NO_ANSWER_REF_ID);
	}

	public int getQuestionId() {
		return Constant.QUESTION_FASTING_ID;
	}

}
