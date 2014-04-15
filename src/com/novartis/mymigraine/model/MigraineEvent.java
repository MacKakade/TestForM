package com.novartis.mymigraine.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;


public class MigraineEvent implements Serializable{
//
//	private int refQuestionId;
//	private int refAnswerId;
	private long eventId;
	private int startHour;
	private int duration;
	private Date datetime;
	private int startMinute;
	//	private boolean skipBreakfast;
//	private boolean skipLunch;
//	private boolean skipDinner;
	private boolean hasHeadache;
	private String notes;
	private Intensity intensity;
	private Fasting fasting;
	private Menstruate menstruating;
	private ArrayList<Food> foods;
	private ArrayList<Environment> environments;
	private ArrayList<LifeStyle> lifeStyles;
	
	private ArrayList<SkippedMeal> skippedMeals;



	/**
	 * @return the datetime
	 */
	public Date getDatetime() {
		return datetime;
	}

	/**
	 * @param datetime the datetime to set
	 */
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	/**
	 * @return the startMinute
	 */
	public int getStartMinute() {
		return startMinute;
	}

	/**
	 * @param startMinute the startMinute to set
	 */
	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}

	
	public ArrayList<SkippedMeal> getSkippedMeals() {
		return skippedMeals;
	}

	public void setSkippedMeals(ArrayList<SkippedMeal> skippedMeals) {
		this.skippedMeals = skippedMeals;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Date getDateTime() {
		return datetime;
	}

	public void setDateTime(Date datetime) {
		this.datetime = datetime;
	}



	public boolean isHasHeadache() {
		return hasHeadache;
	}

	public void setHasHeadache(boolean hasHeadache) {
		this.hasHeadache = hasHeadache;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Intensity getIntensity() {
		return intensity;
	}

	public void setIntensity(Intensity intensity) {
		this.intensity = intensity;
	}

	public Fasting getFasting() {
		return fasting;
	}

	public void setFasting(Fasting fasting) {
		this.fasting = fasting;
	}

	public Menstruate getMenstruating() {
		return menstruating;
	}

	public void setMenstruating(Menstruate menstruating) {
		this.menstruating = menstruating;
	}

	public ArrayList<Food> getFoods() {
		return foods;
	}

	public void setFoods(ArrayList<Food> foods) {
		this.foods = foods;
	}

	public ArrayList<Environment> getEnvironments() {
		return environments;
	}

	public void setEnvironments(ArrayList<Environment> environments) {
		this.environments = environments;
	}

	public ArrayList<LifeStyle> getLifeStyles() {
		return lifeStyles;
	}

	public void setLifeStyles(ArrayList<LifeStyle> lifeStyles) {
		this.lifeStyles = lifeStyles;
	}

	public ArrayList<Location> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<Location> locations) {
		this.locations = locations;
	}

	public ArrayList<Relief> getReliefs() {
		return reliefs;
	}

	public void setReliefs(ArrayList<Relief> reliefs) {
		this.reliefs = reliefs;
	}

	public ArrayList<Symptom> getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(ArrayList<Symptom> symptoms) {
		this.symptoms = symptoms;
	}

	public ArrayList<Treatment> getTreatments() {
		return treatments;
	}

	public void setTreatments(ArrayList<Treatment> treatments) {
		this.treatments = treatments;
	}

	public ArrayList<Warning> getWarnings() {
		return warnings;
	}

	public void setWarnings(ArrayList<Warning> warnings) {
		this.warnings = warnings;
	}

	private ArrayList<Location> locations;
	private ArrayList<Relief> reliefs;
	private ArrayList<Symptom> symptoms;
	private ArrayList<Treatment> treatments;
	private ArrayList<Warning> warnings;
}
