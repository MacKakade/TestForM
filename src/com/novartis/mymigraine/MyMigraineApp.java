package com.novartis.mymigraine;

import android.app.Application;

import com.novartis.mymigraine.database.DataBaseHelper;

public class MyMigraineApp extends Application {

	/**
	 *  Database Helper class to Access the database.
	 */
	private DataBaseHelper dbhelper;

	/**
	 * @return the dbhelper
	 */
	public DataBaseHelper getDbhelper() {
		return dbhelper;
	}

	/**
	 * @param dbhelper
	 *            the dbhelper to set
	 */
	public void setDbhelper(DataBaseHelper dbhelper) {
		this.dbhelper = dbhelper;
	}

	/**
	 * Called when the application class is called. and it create and open database.
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		dbhelper = new DataBaseHelper(this);

		try {

			// create and open database.
			dbhelper.createDataBase();
			dbhelper.openDataBase();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	/**
	 *  Method is called when terminate the Application class.
	 */
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		if (dbhelper != null)
			dbhelper.close();
		
	}
	
}
