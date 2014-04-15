package com.novartis.mymigraine.model;

import com.novartis.mymigraine.common.Constant;

public class Intensity extends ChoiseItem {
	public static final int INTENSITY_VALUE_MILD = 10;
	public static final int INTENSITY_VALUE_MODERATE = 20;
	public static final int INTENSITY_VALUE_SEVERE = 30;

	public static final int MILD_ANSWER_REF_ID = 1;
	public static final int MODERATE_ANSWER_REF_ID = 2;
	public static final int SEVERE_ANSWER_REF_ID = 3;


	private int intensity;

	public int getQuestionId() {
		return Constant.QUESTION_INTENSE_PAIN_ID;
	}

//	public int getIntensityValue() {
//		return intensity;
//	}
//
//	public void setIntensityValue(int intensity) {
//		this.intensity = intensity;
//		
//		setId(intensity);
		
//		switch (intensity) {
//		case INTENSITY_VALUE_MILD:
//			setId(MILD_ANSWER_REF_ID);
//			break;
//		case INTENSITY_VALUE_MODERATE:
//			setId(MODERATE_ANSWER_REF_ID);
//			break;
//		case INTENSITY_VALUE_SEVERE:
//			setId(SEVERE_ANSWER_REF_ID);
//			break;
//		default:
//			setId(MODERATE_ANSWER_REF_ID);
//			break;
//		}		
	//}

}
