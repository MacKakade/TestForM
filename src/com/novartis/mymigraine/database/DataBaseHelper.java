package com.novartis.mymigraine.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.novartis.mymigraine.R;
import com.novartis.mymigraine.common.Constant;
import com.novartis.mymigraine.model.ChoiseItem;
import com.novartis.mymigraine.model.DiaryLogDetailsModel;
import com.novartis.mymigraine.model.Environment;
import com.novartis.mymigraine.model.Fasting;
import com.novartis.mymigraine.model.Food;
import com.novartis.mymigraine.model.Intensity;
import com.novartis.mymigraine.model.LifeStyle;
import com.novartis.mymigraine.model.Location;
import com.novartis.mymigraine.model.Menstruate;
import com.novartis.mymigraine.model.MigraineEvent;
import com.novartis.mymigraine.model.PieChartDetailsModel;
import com.novartis.mymigraine.model.Relief;
import com.novartis.mymigraine.model.SkippedMeal;
import com.novartis.mymigraine.model.Symptom;
import com.novartis.mymigraine.model.Treatment;
import com.novartis.mymigraine.model.Warning;

public class DataBaseHelper extends SQLiteOpenHelper
{
	/**
	 * SQLiteDatabase class
	 */
	// private SQLiteDatabase myDataBase;
	/**
	 * Take Context for Given Class.
	 */
	private Context myContext;

	/**
	 * Constructor for DataBaseHelper
	 * 
	 * @param context
	 */

	int colors[] = { 0xff4879c2, 0xffa9c450, 0xffbf6b44, 0xffcab252, 0xff7b5aa4, 0xffc55245, 0xff435188, 0xff748a4c,
			0xff883d28, 0xff8b7541 };
	public static final String COLUMN_ZORDERNO = "ZORDERNO";
	public static final String COLUMN_ZNAME = "ZNAME";

	private static SQLiteDatabase db;
	public static final String LOCATION_OF_PAIN_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_LOCATION_OF_PAIN_ID + " order by order_no";
	public static final String WARNING_SIGN_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_WARNING_SIGN_ID + " order by order_no";
	public static final String SYMPTOMS_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_SYMPTOMS_ID + " order by order_no";
	public static final String FOODS_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_FOOD_ID + " order by order_no";
	public static final String LIFE_STYLE_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_LIFE_STYLE_ID + " order by order_no";
	public static final String ENVIRONMENT_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_ENVIRONMENT_ID + " order by order_no";
	public static final String RELIEF_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_RELEIF_ID + " order by order_no";

	public static final String TREATMENT_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_TREATMENT_ID + " AND isNewAnswer=1 order by order_no";

	public static final String CUSTOM_TREATMENT_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_TREATMENT_ID + " order by order_no";

	public static final String SKIPPED_MEAL_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_SKIP_MEAL_ID + " order by order_no";

	public static final String CUSTOM_LOCATION_OF_PAIN_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_LOCATION_OF_PAIN_ID + " AND isNewAnswer=1 order by order_no";

	public static final String CUSTOM_WARNING_SIGN_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_WARNING_SIGN_ID + " AND isNewAnswer=1 order by order_no";

	public static final String CUSTOM_SYMPTOMS_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_SYMPTOMS_ID + " AND isNewAnswer=1 order by order_no";

	public static final String CUSTOM_RELIEF_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_RELEIF_ID + " AND isNewAnswer=1 order by order_no";

	public static final String CUSTOM_FOODS_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_FOOD_ID + " AND isNewAnswer=1 order by order_no";

	public static final String CUSTOM_LIFE_STYLE_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_LIFE_STYLE_ID + " AND isNewAnswer=1 order by order_no";

	public static final String CUSTOM_ENVIRONMENT_QUERY = "SELECT * FROM MST_Answer WHERE ref_questionId="
			+ Constant.QUESTION_ENVIRONMENT_ID + " AND isNewAnswer=1 order by order_no";

	public static final String GET_MAX_EVENT_ID = "SELECT MAX(event_id) FROM MST_UserAnswer";

	public static final String GET_MAX_ORDER_NO = "SELECT MAX(order_no) FROM MST_Answer WHERE ref_questionId=";

	public static final String GET_MIRGAINE_EVENTS = "SELECT * FROM MST_MigraineEvent ORDER BY date_time DESC";

	public static final String GET_INTENSITY_QUERY = "SELECT * FROM MST_Answer WHERE  id IN (SELECT ref_answer_id FROM MigraineEvent_Answer WHERE ref_question_id="
			+ Constant.QUESTION_INTENSE_PAIN_ID + " AND ref_event_id=";

	public static final String GET_FASTING_QUERY = "SELECT * FROM MST_Answer WHERE  id IN (SELECT ref_answer_id FROM MigraineEvent_Answer WHERE ref_question_id="
			+ Constant.QUESTION_FASTING_ID + " AND ref_event_id=";

	public static final String GET_LOCATION_OF_PAIN_QUERY = "SELECT * FROM MST_Answer WHERE  id IN (SELECT ref_answer_id FROM MigraineEvent_Answer WHERE ref_question_id="
			+ Constant.QUESTION_LOCATION_OF_PAIN_ID + " AND ref_event_id=";

	public static final String GET_WARNING_SING_QUERY = "SELECT * FROM MST_Answer WHERE  id IN (SELECT ref_answer_id FROM MigraineEvent_Answer WHERE ref_question_id="
			+ Constant.QUESTION_WARNING_SIGN_ID + " AND ref_event_id=";

	public static final String GET_SYMPTOMS_QUERY = "SELECT * FROM MST_Answer WHERE  id IN (SELECT ref_answer_id FROM MigraineEvent_Answer WHERE ref_question_id="
			+ Constant.QUESTION_SYMPTOMS_ID + " AND ref_event_id=";

	public static final String GET_FOODS_QUERY = "SELECT * FROM MST_Answer WHERE  id IN (SELECT ref_answer_id FROM MigraineEvent_Answer WHERE ref_question_id="
			+ Constant.QUESTION_FOOD_ID + " AND ref_event_id=";

	public static final String GET_LIFE_STYLE_QUERY = "SELECT * FROM MST_Answer WHERE  id IN (SELECT ref_answer_id FROM MigraineEvent_Answer WHERE ref_question_id="
			+ Constant.QUESTION_LIFE_STYLE_ID + " AND ref_event_id=";

	public static final String GET_ENVIRONMENT_QUERY = "SELECT * FROM MST_Answer WHERE  id IN (SELECT ref_answer_id FROM MigraineEvent_Answer WHERE ref_question_id="
			+ Constant.QUESTION_ENVIRONMENT_ID + " AND ref_event_id=";

	public static final String GET_RELIEF_QUERY = "SELECT * FROM MST_Answer WHERE  id IN (SELECT ref_answer_id FROM MigraineEvent_Answer WHERE ref_question_id="
			+ Constant.QUESTION_RELEIF_ID + " AND ref_event_id=";

	public static final String GET_TREATMENT_QUERY = "SELECT * FROM MST_Answer WHERE  id IN (SELECT ref_answer_id FROM MigraineEvent_Answer WHERE ref_question_id="
			+ Constant.QUESTION_TREATMENT_ID + " AND ref_event_id=";

	public static final String GET_SKIPPED_MEAL_QUERY = "SELECT * FROM MST_Answer WHERE  id IN (SELECT ref_answer_id FROM MigraineEvent_Answer WHERE ref_question_id="
			+ Constant.QUESTION_SKIP_MEAL_ID + " AND ref_event_id=";

	public static final String GET_MESTRUATE_QUERY = "SELECT * FROM MST_Answer WHERE  id IN (SELECT ref_answer_id FROM MigraineEvent_Answer WHERE ref_question_id="
			+ Constant.QUESTION_MESTRUAL_ID + " AND ref_event_id=";

	static class MST_Answer
	{

		public static final String COLUMN_ID = "id";
		public static final String COLUMN_REF_QUESTION = "ref_questionId";
		public static final String COLUMN_ANSWER = "answer";
		public static final String COLUMN_ORDER_NO = "order_no";
		public static final String COLUMN_TAG = "tag";
		public static final String COLUMN_IS_NEW_ANS = "isNewAnswer";

	}

	public static SimpleDateFormat sqliteDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public DataBaseHelper(Context context)
	{
		super(context, context.getResources().getString(R.string.DB_NAME), null, 1);
		this.myContext = context;
	}

	/**
	 * called when create DataBaseHelper class.
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
	}

	/**
	 * called when upgrade the database.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * Function is used to create a Database.
	 * 
	 * @throws IOException
	 */
	public void createDataBase() throws IOException
	{
		// ---Check whether database is already created or not---
		boolean dbExist = checkDataBase();

		if (!dbExist)
		{
			this.getReadableDatabase();
			try
			{
				// ---If not created then copy the database---
				copyDataBase();
				this.close();
			}
			catch (IOException e)
			{
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Copy the database to the output stream
	 * 
	 * @throws IOException
	 */
	private void copyDataBase() throws IOException
	{
		InputStream myInput = myContext.getAssets().open(myContext.getString(R.string.DB_NAME));
		String outFileName = myContext.getString(R.string.DB_PATH) + myContext.getString(R.string.DB_NAME);
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0)
		{
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	/**
	 * Open the database
	 * 
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException
	{
		// --- Open the database---
		String myPath = myContext.getString(R.string.DB_PATH) + myContext.getString(R.string.DB_NAME);
		db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}

	/**
	 * Check whether database already created or not
	 * 
	 * @return boolean
	 */
	private boolean checkDataBase()
	{
		try
		{
			String myPath = myContext.getString(R.string.DB_PATH) + myContext.getString(R.string.DB_NAME);
			File f = new File(myPath);
			if (f.exists())
				return true;
			else
				return false;
		}
		catch (SQLiteException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<PieChartDetailsModel> getpotentialTriggerChartData(String startTime, String endTime)
	{

		ArrayList<PieChartDetailsModel> list = new ArrayList<PieChartDetailsModel>();
		PieChartDetailsModel model = null;

		Cursor cursor = null;
		int otherCount = 0;
		int totalCount = 0;

		// String query =
		// "select ref_question_id,ref_answer_id,answer,count(ref_answer_id) as ans_id,(select sum(ans_id) from (select count(ref_answer_id) as ans_id from MigraineEvent_Answer, MST_Answer where ref_answer_id=MST_Answer.id and ref_question_id in (7,8,9,10,11) group by ref_question_id,ref_answer_id order by ans_id desc)) as total_count from MigraineEvent_Answer, MST_Answer where ref_answer_id=MST_Answer.id and ref_question_id in (7,8,9,10,11) group by ref_question_id,ref_answer_id order by ans_id desc";

		String query = "select ref_question_id,ref_answer_id,answer,count(ref_answer_id) as ans_id,(select sum(ans_id) from (select count(ref_answer_id) as ans_id from MigraineEvent_Answer, MST_Answer,  MST_MigraineEvent  where ref_answer_id=MST_Answer.id and ref_question_id in (7,8,9,10,11) and MST_MigraineEvent.event_id = MigraineEvent_Answer.ref_event_id and MST_MigraineEvent.hasHeadache in (0,1) and MST_MigraineEvent.date_time >='"
				+ startTime
				+ "' and MST_MigraineEvent.date_time <='"
				+ endTime
				+ "' and MigraineEvent_Answer.ref_answer_id!='32' group by ref_question_id,ref_answer_id order by ans_id desc)) as total_count from MigraineEvent_Answer, MST_Answer, MST_MigraineEvent where ref_answer_id=MST_Answer.id and ref_question_id in (7,8,9,10,11) and MST_MigraineEvent.event_id = MigraineEvent_Answer.ref_event_id and MST_MigraineEvent.hasHeadache in (0,1) and MST_MigraineEvent.date_time >='"
				+ startTime
				+ "' and MST_MigraineEvent.date_time <='"
				+ endTime
				+ "' and MigraineEvent_Answer.ref_answer_id!='32' group by ref_question_id,ref_answer_id order by ans_id desc";

		try
		{
			cursor = db.rawQuery(query, null);

			if (cursor != null && cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				for (int i = 0; i < cursor.getCount(); i++)
				{

					totalCount = cursor.getInt(cursor.getColumnIndex("total_count"));

					if (i < 10)
					{
						model = new PieChartDetailsModel();
						model.setRefQuestionId(cursor.getInt(cursor.getColumnIndex("ref_question_id")));
						model.setRefAnswerId(cursor.getInt(cursor.getColumnIndex("ref_answer_id")));
						model.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
						model.setColor(colors[i]);

						final int actualCount = (cursor.getInt(cursor.getColumnIndex("ans_id")) * 100) / totalCount;

						model.setCount(actualCount);
						list.add(model);
					}
					else
					{
						otherCount += cursor.getInt(cursor.getColumnIndex("ans_id"));
					}

					cursor.moveToNext();
				}

				if (otherCount != 0)
				{
					model = new PieChartDetailsModel();
					model.setRefQuestionId(100);
					model.setRefAnswerId(101);
					model.setAnswer("Other");
					model.setColor(0xffac453d);

					final int actualCount = (otherCount * 100) / totalCount;

					model.setCount(actualCount);

					list.add(model);
				}
			}
			cursor.close();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}

		return list;
	}

	public ArrayList<PieChartDetailsModel> getTriggerExposureChartData(String startTime, String endTime)
	{

		ArrayList<PieChartDetailsModel> list = new ArrayList<PieChartDetailsModel>();
		PieChartDetailsModel model = null;

		Cursor cursor = null;
		int otherCount = 0;
		int totalCount = 0;

		final String headacheQuery = "select event_id from MST_MigraineEvent where date_time >= '" + startTime
				+ "' and date_time <= '" + endTime + "' and hasHeadache=1";
		Cursor headacheTotalCount = db.rawQuery(headacheQuery, null);

		String query = "select ref_question_id,ref_answer_id,answer,count(ref_answer_id) as ans_id,(select sum(ans_id) from (select count(ref_answer_id) as ans_id from MigraineEvent_Answer, MST_Answer,  MST_MigraineEvent  where ref_answer_id=MST_Answer.id and ref_question_id in (7,8,9,10,11) and MST_MigraineEvent.event_id = MigraineEvent_Answer.ref_event_id and MST_MigraineEvent.hasHeadache in (1) and MST_MigraineEvent.date_time >='"
				+ startTime
				+ "' and MST_MigraineEvent.date_time <='"
				+ endTime
				+ "' and MigraineEvent_Answer.ref_answer_id!='32' group by ref_question_id,ref_answer_id order by ans_id desc)) as total_count from MigraineEvent_Answer, MST_Answer, MST_MigraineEvent where ref_answer_id=MST_Answer.id and ref_question_id in (7,8,9,10,11) and MST_MigraineEvent.event_id = MigraineEvent_Answer.ref_event_id and MST_MigraineEvent.hasHeadache in (1) and MST_MigraineEvent.date_time >='"
				+ startTime
				+ "' and MST_MigraineEvent.date_time <='"
				+ endTime
				+ "' and MigraineEvent_Answer.ref_answer_id!='32' group by ref_question_id,ref_answer_id order by ans_id desc";

		try
		{
			cursor = db.rawQuery(query, null);

			if (cursor != null && cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				for (int i = 0; i < cursor.getCount(); i++)
				{

					totalCount = cursor.getInt(cursor.getColumnIndex("total_count"));

					if (i < 10)
					{
						model = new PieChartDetailsModel();
						model.setRefQuestionId(cursor.getInt(cursor.getColumnIndex("ref_question_id")));
						model.setRefAnswerId(cursor.getInt(cursor.getColumnIndex("ref_answer_id")));
						model.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
						model.setColor(colors[i]);

						// final int actualCount = (cursor.getInt(cursor
						// .getColumnIndex("ans_id")) * 100) / totalCount;

						final int actualCount = (cursor.getInt(cursor.getColumnIndex("ans_id")) * 100)
								/ headacheTotalCount.getCount();
						model.setCount(actualCount);
						list.add(model);
					}
					else
					{
						otherCount += cursor.getInt(cursor.getColumnIndex("ans_id"));
					}

					cursor.moveToNext();
				}

				if (otherCount != 0)
				{
					model = new PieChartDetailsModel();
					model.setRefQuestionId(100);
					model.setRefAnswerId(101);
					model.setAnswer("Other");
					model.setColor(0xffac453d);

					final int actualCount = (otherCount * 100) / totalCount;

					model.setCount(actualCount);

					list.add(model);
				}
			}
			cursor.close();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}

		return list;
	}

	public ArrayList<PieChartDetailsModel> getPainLevelChartData(String startTime, String endTime)
	{
		ArrayList<PieChartDetailsModel> list = new ArrayList<PieChartDetailsModel>();
		PieChartDetailsModel model = null;

		Cursor cursor = null;
		int totalCount = 0;

		String query = "select ref_question_id,ref_answer_id,answer,count(ref_answer_id) as ans_id,(select sum(ans_id) from (select count(ref_answer_id) as ans_id from MigraineEvent_Answer, MST_Answer,  MST_MigraineEvent  where ref_answer_id=MST_Answer.id and ref_question_id in (1) and MST_MigraineEvent.event_id = MigraineEvent_Answer.ref_event_id and MST_MigraineEvent.hasHeadache in (1) and MST_MigraineEvent.date_time >='"
				+ startTime
				+ "' and MST_MigraineEvent.date_time <='"
				+ endTime
				+ "' group by ref_question_id,ref_answer_id order by ans_id desc)) as total_count from MigraineEvent_Answer, MST_Answer, MST_MigraineEvent where ref_answer_id=MST_Answer.id and ref_question_id in (1) and MST_MigraineEvent.event_id = MigraineEvent_Answer.ref_event_id and MST_MigraineEvent.hasHeadache in (1) and MST_MigraineEvent.date_time >='"
				+ startTime
				+ "' and MST_MigraineEvent.date_time <='"
				+ endTime
				+ "' group by ref_question_id,ref_answer_id order by ans_id desc";
		try
		{
			cursor = db.rawQuery(query, null);
			if (cursor != null && cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				for (int i = 0; i < cursor.getCount(); i++)
				{

					totalCount = cursor.getInt(cursor.getColumnIndex("total_count"));

					model = new PieChartDetailsModel();
					model.setRefQuestionId(cursor.getInt(cursor.getColumnIndex("ref_question_id")));
					model.setRefAnswerId(cursor.getInt(cursor.getColumnIndex("ref_answer_id")));
					model.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
					if (cursor.getString(cursor.getColumnIndex("answer")).equalsIgnoreCase("Mild"))
						model.setColor(0xffDBB311);
					else if (cursor.getString(cursor.getColumnIndex("answer")).equalsIgnoreCase("Moderate"))
						model.setColor(0xffE86C00);
					else if (cursor.getString(cursor.getColumnIndex("answer")).equalsIgnoreCase("Severe"))
						model.setColor(0xffBA2E2B);

					final int actualCount = (cursor.getInt(cursor.getColumnIndex("ans_id")) * 100) / totalCount;

					model.setCount(actualCount);
					list.add(model);
					cursor.moveToNext();
				}

			}
			cursor.close();
		}
		catch (Exception e)
		{
			if (cursor != null)
			{
				cursor.close();
			}
			e.printStackTrace();
			// TODO: handle exception
		}

		return list;

	}

	public ArrayList<PieChartDetailsModel> getPainLocationChartData(String startTime, String endTime)
	{
		ArrayList<PieChartDetailsModel> list = new ArrayList<PieChartDetailsModel>();
		PieChartDetailsModel model = null;

		Cursor cursor = null;
		int totalCount = 0;
		int otherCount = 0;

		final String headacheQuery = "select event_id from MST_MigraineEvent where date_time >= '" + startTime
				+ "' and date_time <= '" + endTime + "' and hasHeadache=1";
		Cursor headacheTotalCount = db.rawQuery(headacheQuery, null);

		String query = "select ref_question_id,ref_answer_id,answer,count(ref_answer_id) as ans_id,(select sum(ans_id) from (select count(ref_answer_id) as ans_id from MigraineEvent_Answer, MST_Answer,  MST_MigraineEvent  where ref_answer_id=MST_Answer.id and ref_question_id in (2) and MST_MigraineEvent.event_id = MigraineEvent_Answer.ref_event_id and MST_MigraineEvent.hasHeadache in (1) and MST_MigraineEvent.date_time >='"
				+ startTime
				+ "' and MST_MigraineEvent.date_time <='"
				+ endTime
				+ "' group by ref_question_id,ref_answer_id order by ans_id desc)) as total_count from MigraineEvent_Answer, MST_Answer, MST_MigraineEvent where ref_answer_id=MST_Answer.id and ref_question_id in (2) and MST_MigraineEvent.event_id = MigraineEvent_Answer.ref_event_id and MST_MigraineEvent.hasHeadache in (1) and MST_MigraineEvent.date_time >='"
				+ startTime
				+ "' and MST_MigraineEvent.date_time <='"
				+ endTime
				+ "' group by ref_question_id,ref_answer_id order by ans_id desc";

		try
		{
			cursor = db.rawQuery(query, null);

			if (cursor != null && cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				for (int i = 0; i < cursor.getCount(); i++)
				{

					totalCount = cursor.getInt(cursor.getColumnIndex("total_count"));

					if (i < 10)
					{
						model = new PieChartDetailsModel();
						model.setRefQuestionId(cursor.getInt(cursor.getColumnIndex("ref_question_id")));
						model.setRefAnswerId(cursor.getInt(cursor.getColumnIndex("ref_answer_id")));
						model.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
						model.setColor(colors[i]);

						final int actualCount = ((cursor.getInt(cursor.getColumnIndex("ans_id")) * 100) / headacheTotalCount
								.getCount());

						model.setCount(actualCount);
						list.add(model);
					}
					else
					{
						otherCount += cursor.getInt(cursor.getColumnIndex("ans_id"));
					}

					cursor.moveToNext();
				}

				if (otherCount != 0)
				{
					model = new PieChartDetailsModel();
					model.setRefQuestionId(100);
					model.setRefAnswerId(101);
					model.setAnswer("Other");
					model.setColor(0xffac453d);

					final int actualCount = (otherCount * 100) / totalCount;

					model.setCount(actualCount);

					list.add(model);
				}
			}
			cursor.close();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}

		return list;
	}

	public ArrayList<PieChartDetailsModel> getSymptomsFrequencyChartData(String startTime, String endTime)
	{
		ArrayList<PieChartDetailsModel> list = new ArrayList<PieChartDetailsModel>();
		PieChartDetailsModel model = null;

		Cursor cursor = null;
		int totalCount = 0;
		int otherCount = 0;

		final String headacheQuery = "select event_id from MST_MigraineEvent where date_time >= '" + startTime
				+ "' and date_time <= '" + endTime + "' and hasHeadache=1";
		Cursor headacheTotalCount = db.rawQuery(headacheQuery, null);

		String query = "select ref_question_id,ref_answer_id,answer,count(ref_answer_id) as ans_id,(select sum(ans_id) from (select count(ref_answer_id) as ans_id from MigraineEvent_Answer, MST_Answer,  MST_MigraineEvent  where ref_answer_id=MST_Answer.id and ref_question_id in (4) and MST_MigraineEvent.event_id = MigraineEvent_Answer.ref_event_id and MST_MigraineEvent.hasHeadache in (1) and MST_MigraineEvent.date_time >='"
				+ startTime
				+ "' and MST_MigraineEvent.date_time <='"
				+ endTime
				+ "' group by ref_question_id,ref_answer_id order by ans_id desc)) as total_count from MigraineEvent_Answer, MST_Answer, MST_MigraineEvent where ref_answer_id=MST_Answer.id and ref_question_id in (4) and MST_MigraineEvent.event_id = MigraineEvent_Answer.ref_event_id and MST_MigraineEvent.hasHeadache in (1) and MST_MigraineEvent.date_time >='"
				+ startTime
				+ "' and MST_MigraineEvent.date_time <='"
				+ endTime
				+ "' group by ref_question_id,ref_answer_id order by ans_id desc";

		try
		{
			cursor = db.rawQuery(query, null);

			if (cursor != null && cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				for (int i = 0; i < cursor.getCount(); i++)
				{

					totalCount = cursor.getInt(cursor.getColumnIndex("total_count"));

					if (i < 10)
					{
						model = new PieChartDetailsModel();
						model.setRefQuestionId(cursor.getInt(cursor.getColumnIndex("ref_question_id")));
						model.setRefAnswerId(cursor.getInt(cursor.getColumnIndex("ref_answer_id")));
						model.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
						model.setColor(colors[i]);

						final int actualCount = (cursor.getInt(cursor.getColumnIndex("ans_id")) * 100)
								/ headacheTotalCount.getCount();

						model.setCount(actualCount);
						list.add(model);
					}
					else
					{
						otherCount += cursor.getInt(cursor.getColumnIndex("ans_id"));
					}

					cursor.moveToNext();
				}

				if (otherCount != 0)
				{
					model = new PieChartDetailsModel();
					model.setRefQuestionId(100);
					model.setRefAnswerId(101);
					model.setAnswer("Other");
					model.setColor(0xffac453d);

					final int actualCount = (otherCount * 100) / totalCount;

					model.setCount(actualCount);

					list.add(model);
				}
			}
			cursor.close();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}

		return list;
	}

	public LinkedHashMap<String, LinkedHashMap<Integer, ArrayList<DiaryLogDetailsModel>>> getDiaryLogDetails(
			String filterId, String startTime)
	{

		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String endTime = s.format(new Date());

		ArrayList<DiaryLogDetailsModel> list = new ArrayList<DiaryLogDetailsModel>();
		DiaryLogDetailsModel model;
		Cursor cursor = null;

		String query = "";
		
		query = "SELECT me.event_id,mea.ref_question_id,me.date_time,mea.ref_answer_id,ma.answer,me.duration,me.notes,me.start_hour,me.start_minute,me.hasHeadache from MigraineEvent_Answer mea, MST_MigraineEvent me , MST_Answer ma where me.event_id = mea.ref_event_id and mea.ref_question_id in ("
				+ filterId
				+ ") and mea.ref_answer_id!=32 AND mea.ref_answer_id!=64 AND me.hasHeadache in (1,0) and date_time between '"
				+ startTime
				+ "' and '"
				+ endTime
				+ "' and ma.id = mea.ref_answer_id order by me.date_time desc, mea.ref_question_id";

//		query = "SELECT me.event_id,mea.ref_question_id,me.date_time,mea.ref_answer_id,ma.answer,me.duration,me.notes,me.start_hour,me.start_minute,me.hasHeadache from MigraineEvent_Answer mea, MST_MigraineEvent me , MST_Answer ma where me.event_id = mea.ref_event_id and (( mea.ref_question_id in ("
//				+ filterId
//				+ ") and mea.ref_answer_id!=32 AND me.hasHeadache in (1,0)) or ( mea.ref_question_id in (8) and me.hasHeadache in (0)  and mea .ref_answer_id!=32)) and date_time between '"
//				+ startTime
//				+ "' and '"
//				+ endTime
//				+ "' and ma.id = mea.ref_answer_id order by me.date_time desc, mea.ref_question_id";

	

		final LinkedHashMap<String, LinkedHashMap<Integer, ArrayList<DiaryLogDetailsModel>>> hashMap = new LinkedHashMap<String, LinkedHashMap<Integer, ArrayList<DiaryLogDetailsModel>>>();
		String monthName = "";
		LinkedHashMap<Integer, ArrayList<DiaryLogDetailsModel>> map;

		try
		{
			cursor = db.rawQuery(query, null);

			if (cursor != null && cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				for (int i = 0; i < cursor.getCount(); i++)
				{

					model = new DiaryLogDetailsModel();

					model.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
					model.setDate_time(cursor.getString(cursor.getColumnIndex("date_time")));
					model.setEvent_id(cursor.getInt(cursor.getColumnIndex("event_id")));
					model.setRef_answer_id(cursor.getInt(cursor.getColumnIndex("ref_answer_id")));
					model.setRef_question_id(cursor.getInt(cursor.getColumnIndex("ref_question_id")));
					model.setDuration(cursor.getInt(cursor.getColumnIndex("duration")));
					model.setNotes(cursor.getString(cursor.getColumnIndex("notes")));
					model.setStart_hour(cursor.getInt(cursor.getColumnIndex("start_hour")));
					model.setStart_minute(cursor.getInt(cursor.getColumnIndex("start_minute")));
					model.setHasHedache(cursor.getInt(cursor.getColumnIndex("hasHeadache")));

					// Log.e("Date = ", "date in DB = "
					// +cursor.getString(cursor
					// .getColumnIndex("date_time")));

					// list.add(model);
					monthName = new SimpleDateFormat("MMM yyyy").format(s.parse(model.getDate_time()));

					if (hashMap.containsKey(monthName))
					{
						map = hashMap.get(monthName);
					}
					else
					{
						map = new LinkedHashMap<Integer, ArrayList<DiaryLogDetailsModel>>();
					}

					if (map.containsKey(model.getEvent_id()))
					{
						list = map.get(model.getEvent_id());
					}
					else
					{
						list = new ArrayList<DiaryLogDetailsModel>();
					}

					list.add(model);
					Collections.sort(list, new DiaryLogEntryComparator());
					map.put(model.getEvent_id(), list);
					hashMap.put(monthName, map);

					cursor.moveToNext();
				}
			}

			cursor.close();

		}
		catch (Exception e)
		{
			if (cursor != null)
				cursor.close();
			// TODO: handle exception
		}

		return hashMap;
	}

	public class DiaryLogEntryComparator implements Comparator<DiaryLogDetailsModel>
	{

		int getDiaryLogEntryOrder(int question_id)
		{
			int val = -1;
			switch (question_id)
			{
			case Constant.QUESTION_WARNING_SIGN_ID:
				val = 0;
				break;
			case Constant.QUESTION_INTENSE_PAIN_ID:
				val = 1;
				break;
			case Constant.QUESTION_LOCATION_OF_PAIN_ID:
				val = 2;
				break;
			case Constant.QUESTION_SYMPTOMS_ID:
				val = 3;
				break;
			case Constant.QUESTION_SKIP_MEAL_ID:
				val = 4;
				break;
			case Constant.QUESTION_FASTING_ID:
				val = 5;
				break;
			case Constant.QUESTION_FOOD_ID:
				val = 6;
				break;
			case Constant.QUESTION_LIFE_STYLE_ID:
				val = 7;
				break;
			case Constant.QUESTION_MESTRUAL_ID:
				val = 8;
				break;
			case Constant.QUESTION_ENVIRONMENT_ID:
				val = 9;
				break;
			case Constant.QUESTION_NOTES:
				val = 10;
				break;

			}
			return val;
		}

		@Override
		public int compare(DiaryLogDetailsModel lhs, DiaryLogDetailsModel rhs)
		{
			return getDiaryLogEntryOrder(lhs.getRef_question_id()) < getDiaryLogEntryOrder(rhs.getRef_question_id()) ? -1
					: 1;
		}

	}

	/************************ Vinay ***************************/
	public boolean saveMigraineEventEntry(MigraineEvent event)
	{
		boolean isSavedSuccessful;
		db.beginTransaction();
		try
		{
			long eventId = createMigraineEvent(event);
			if (eventId != -1)
			{
				event.setEventId(eventId);
				saveIntensity(event);
				saveEventForLocation(event);
				saveEventForTreatment(event);
				saveEventForRelief(event);
				saveEventForSkippedMeal(event);
				saveFasting(event);
				saveEventForFood(event);
				saveEventForLifeStyle(event);
				saveEventForEnvironment(event);
				saveEventForSymptoms(event);
				saveEventForWarning(event);
				saveMestruate(event);
				db.setTransactionSuccessful();
				isSavedSuccessful = true;
			}
			else
			{
				isSavedSuccessful = false;
				return isSavedSuccessful;
			}
		}
		catch (SQLException e)
		{
			isSavedSuccessful = false;

		}
		catch (Exception e)
		{
			isSavedSuccessful = false;
		}
		finally
		{
			db.endTransaction();
		}

		return isSavedSuccessful;
	}

	private static long createMigraineEvent(MigraineEvent event) throws SQLException
	{
		ContentValues cv = new ContentValues();
		cv.put("start_hour", event.getStartHour());
		cv.put("start_minute", event.getStartMinute());
		cv.put("duration", event.getDuration());
		cv.put("date_time", sqliteDateTimeFormat.format(event.getDateTime()));
		cv.put("hasHeadache", event.isHasHeadache());
		cv.put("notes", event.getNotes());
		return db.insert("MST_MigraineEvent", "event_id", cv);
	}

	private static void saveEventForEnvironment(MigraineEvent event)
	{
		ArrayList<Environment> environments = event.getEnvironments();
		if (environments != null)
		{
			int size = environments.size();
			ContentValues cv = new ContentValues();
			for (int i = 0; i < size; i++)
			{
				Environment environment = environments.get(i);
				if (environment.isChecked())
				{
					cv.put("ref_event_id", event.getEventId());
					cv.put("ref_question_id", environment.getQuestionId());
					cv.put("ref_answer_id", environment.getId());
					db.insert("MigraineEvent_Answer", "id", cv);
				}
				cv.clear();

			}
		}

	}

	private static void saveEventForFood(MigraineEvent event)
	{
		ArrayList<Food> foods = event.getFoods();
		if (foods != null)
		{
			int size = foods.size();
			ContentValues cv = new ContentValues();
			for (int i = 0; i < size; i++)
			{
				Food food = foods.get(i);
				if (food.isChecked())
				{
					cv.put("ref_event_id", event.getEventId());
					cv.put("ref_question_id", food.getQuestionId());
					cv.put("ref_answer_id", food.getId());
					db.insert("MigraineEvent_Answer", "id", cv);
				}
				cv.clear();

			}
		}
	}

	private static void saveEventForLifeStyle(MigraineEvent event)
	{
		ArrayList<LifeStyle> lifestyles = event.getLifeStyles();
		if (lifestyles != null)
		{
			int size = lifestyles.size();
			ContentValues cv = new ContentValues();
			for (int i = 0; i < size; i++)
			{
				LifeStyle lifestyle = lifestyles.get(i);
				if (lifestyle.isChecked())
				{
					cv.put("ref_event_id", event.getEventId());
					cv.put("ref_question_id", lifestyle.getQuestionId());
					cv.put("ref_answer_id", lifestyle.getId());
					db.insert("MigraineEvent_Answer", "id", cv);
				}
				cv.clear();

			}
		}
	}

	private static void saveEventForLocation(MigraineEvent event)
	{
		ArrayList<Location> locations = event.getLocations();
		if (locations != null)
		{
			int size = locations.size();
			ContentValues cv = new ContentValues();
			for (int i = 0; i < size; i++)
			{
				Location location = locations.get(i);
				if (location.isChecked())
				{
					cv.put("ref_event_id", event.getEventId());
					cv.put("ref_question_id", location.getQuestionId());
					cv.put("ref_answer_id", location.getId());
					db.insert("MigraineEvent_Answer", "id", cv);
				}
				cv.clear();
			}
		}
	}

	private static void saveEventForRelief(MigraineEvent event)
	{
		ArrayList<Relief> reliefs = event.getReliefs();
		if (reliefs != null)
		{
			int size = reliefs.size();
			ContentValues cv = new ContentValues();
			for (int i = 0; i < size; i++)
			{
				Relief relief = reliefs.get(i);
				if (relief.isChecked())
				{
					cv.put("ref_event_id", event.getEventId());
					cv.put("ref_question_id", relief.getQuestionId());
					cv.put("ref_answer_id", relief.getId());
					db.insert("MigraineEvent_Answer", "id", cv);
				}
				cv.clear();
			}
		}
	}

	private static void saveEventForSymptoms(MigraineEvent event)
	{

		ArrayList<Symptom> symptoms = event.getSymptoms();
		if (symptoms != null)
		{
			int size = symptoms.size();
			ContentValues cv = new ContentValues();
			for (int i = 0; i < size; i++)
			{
				Symptom symptom = symptoms.get(i);
				if (symptom.isChecked())
				{
					cv.put("ref_event_id", event.getEventId());
					cv.put("ref_question_id", symptom.getQuestionId());
					cv.put("ref_answer_id", symptom.getId());
					db.insert("MigraineEvent_Answer", "id", cv);
				}
				cv.clear();
			}
		}

	}

	private static void saveEventForTreatment(MigraineEvent event)
	{
		ArrayList<Treatment> treatments = event.getTreatments();
		if (treatments != null)
		{
			int size = treatments.size();
			ContentValues cv = new ContentValues();
			for (int i = 0; i < size; i++)
			{
				Treatment treatment = treatments.get(i);
				if (treatment.isChecked())
				{
					cv.put("ref_event_id", event.getEventId());
					cv.put("ref_question_id", treatment.getQuestionId());
					cv.put("ref_answer_id", treatment.getId());
					db.insert("MigraineEvent_Answer", "id", cv);
				}
				cv.clear();
			}
		}
	}

	private static void saveEventForWarning(MigraineEvent event)
	{
		ArrayList<Warning> warnings = event.getWarnings();
		if (warnings != null)
		{
			int size = warnings.size();
			ContentValues cv = new ContentValues();
			for (int i = 0; i < size; i++)
			{
				Warning warning = warnings.get(i);
				if (warning.isChecked())
				{
					cv.put("ref_event_id", event.getEventId());
					cv.put("ref_question_id", warning.getQuestionId());
					cv.put("ref_answer_id", warning.getId());
					db.insert("MigraineEvent_Answer", "id", cv);
				}
				cv.clear();
			}
		}

	}

	private static void saveEventForSkippedMeal(MigraineEvent event)
	{
		ArrayList<SkippedMeal> skippedMeals = event.getSkippedMeals();
		if (skippedMeals != null)
		{
			int size = skippedMeals.size();
			ContentValues cv = new ContentValues();
			for (int i = 0; i < size; i++)
			{
				SkippedMeal meal = skippedMeals.get(i);
				if (meal.isChecked())
				{
					cv.put("ref_event_id", event.getEventId());
					cv.put("ref_question_id", meal.getQuestionId());
					cv.put("ref_answer_id", meal.getId());
					db.insert("MigraineEvent_Answer", "id", cv);
				}
				cv.clear();
			}
		}
	}

	private static void saveIntensity(MigraineEvent event)
	{
		Intensity intensity = event.getIntensity();
		if (intensity != null)
		{
			ContentValues cv = new ContentValues();
			cv.put("ref_event_id", event.getEventId());
			cv.put("ref_question_id", intensity.getQuestionId());
			cv.put("ref_answer_id", event.getIntensity().getId());
			db.insert("MigraineEvent_Answer", "id", cv);
		}
	}

	private static void saveFasting(MigraineEvent event)
	{
		Fasting fasting = event.getFasting();
		if (fasting != null)
		{
			ContentValues cv = new ContentValues();
			cv.put("ref_event_id", event.getEventId());
			cv.put("ref_question_id", fasting.getQuestionId());
			cv.put("ref_answer_id", event.getFasting().getId());
			db.insert("MigraineEvent_Answer", "id", cv);
		}
	}

	private static void saveMestruate(MigraineEvent event)
	{
		Menstruate menstruate = event.getMenstruating();
		if (menstruate != null)
		{
			ContentValues cv = new ContentValues();
			cv.put("ref_event_id", event.getEventId());
			cv.put("ref_question_id", menstruate.getQuestionId());
			cv.put("ref_answer_id", event.getMenstruating().getId());
			db.insert("MigraineEvent_Answer", "id", cv);
		}
	}

	public long insertYourAns(ChoiseItem item) throws SQLException
	{
		ContentValues cv = new ContentValues();
		cv.put("ref_questionId", item.getQuestionId());
		cv.put("answer", item.getName());
		cv.put("order_no", item.getOrderNo());
		cv.put("isNewAnswer", item.isNewAnswer());
		cv.put("tag", item.getTag());
		return db.insert("MST_Answer", "id", cv);

	}

	/***************** update *******************************************/
	public boolean updateMigraineEventEntry(MigraineEvent event)
	{
		boolean isUpdateSuccessful;
		db.beginTransaction();
		try
		{
			updateMigraineEvent(event);
			updateIntensity(event);
			updateEventForLocation(event);
			updateEventForTreatment(event);
			updateEventForRelief(event);
			updateEventForSkippedMeal(event);
			updateFasting(event);
			updateEventForFood(event);
			updateEventForLifeStyle(event);
			updateEventForEnvironment(event);
			updateEventForSymptoms(event);
			updateEventForWarning(event);
			updateMestruate(event);
			db.setTransactionSuccessful();
			isUpdateSuccessful = true;

		}
		catch (SQLException e)
		{
			isUpdateSuccessful = false;

		}
		catch (Exception e)
		{
			isUpdateSuccessful = false;
		}
		finally
		{
			db.endTransaction();
		}

		return isUpdateSuccessful;
	}

	private static void updateMigraineEvent(MigraineEvent event) throws SQLException
	{
		ContentValues cv = new ContentValues();
		cv.put("start_hour", event.getStartHour());
		cv.put("start_minute", event.getStartMinute());
		cv.put("duration", event.getDuration());
		cv.put("date_time", sqliteDateTimeFormat.format(event.getDateTime()));
		cv.put("hasHeadache", event.isHasHeadache());
		cv.put("notes", event.getNotes());
		db.update("MST_MigraineEvent", cv, "event_id=?", new String[] { "" + event.getEventId() });
	}

	private static void updateEventForEnvironment(MigraineEvent event)
	{
		deleteQuestionAnswerEntry(event.getEventId(), Constant.QUESTION_ENVIRONMENT_ID);
		saveEventForEnvironment(event);

	}

	private static void updateEventForFood(MigraineEvent event)
	{
		deleteQuestionAnswerEntry(event.getEventId(), Constant.QUESTION_FOOD_ID);
		saveEventForFood(event);

	}

	private static void updateEventForLifeStyle(MigraineEvent event)
	{
		deleteQuestionAnswerEntry(event.getEventId(), Constant.QUESTION_LIFE_STYLE_ID);
		saveEventForLifeStyle(event);
	}

	private static void updateEventForLocation(MigraineEvent event)
	{
		deleteQuestionAnswerEntry(event.getEventId(), Constant.QUESTION_LOCATION_OF_PAIN_ID);
		saveEventForLocation(event);
	}

	private static void updateEventForRelief(MigraineEvent event)
	{
		deleteQuestionAnswerEntry(event.getEventId(), Constant.QUESTION_RELEIF_ID);
		saveEventForRelief(event);
	}

	private static void updateEventForSymptoms(MigraineEvent event)
	{

		deleteQuestionAnswerEntry(event.getEventId(), Constant.QUESTION_SYMPTOMS_ID);
		saveEventForSymptoms(event);

	}

	private static void updateEventForTreatment(MigraineEvent event)
	{
		deleteQuestionAnswerEntry(event.getEventId(), Constant.QUESTION_TREATMENT_ID);
		saveEventForTreatment(event);
	}

	private static void updateEventForWarning(MigraineEvent event)
	{
		deleteQuestionAnswerEntry(event.getEventId(), Constant.QUESTION_WARNING_SIGN_ID);
		saveEventForWarning(event);

	}

	private static void deleteQuestionAnswerEntry(long eventId, int questionId)
	{
		db.delete("MigraineEvent_Answer", "ref_event_id=? AND ref_question_id=?", new String[] { "" + eventId,
				"" + questionId });
	}

	private static void updateEventForSkippedMeal(MigraineEvent event)
	{

		deleteQuestionAnswerEntry(event.getEventId(), Constant.QUESTION_SKIP_MEAL_ID);
		saveEventForSkippedMeal(event);
	}

	private static void updateIntensity(MigraineEvent event)
	{
		deleteQuestionAnswerEntry(event.getEventId(), Constant.QUESTION_INTENSE_PAIN_ID);
		saveIntensity(event);

	}

	private static void updateFasting(MigraineEvent event)
	{

		deleteQuestionAnswerEntry(event.getEventId(), Constant.QUESTION_FASTING_ID);
		saveFasting(event);

	}

	private static void updateMestruate(MigraineEvent event)
	{
		deleteQuestionAnswerEntry(event.getEventId(), Constant.QUESTION_MESTRUAL_ID);
		saveMestruate(event);
	}

	/***********************************************************************/

	/* Get Migraine Events */

	/**
	 * @throws ParseException
	 *********************************************************************/

	public ArrayList<MigraineEvent> getMigraineEvents() throws ParseException
	{
		ArrayList<MigraineEvent> events = new ArrayList<MigraineEvent>();
		Cursor cursor = db.rawQuery(GET_MIRGAINE_EVENTS, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{

					MigraineEvent event = new MigraineEvent();
					event.setEventId(cursor.getInt(cursor.getColumnIndex("event_id")));
					event.setStartHour(cursor.getInt(cursor.getColumnIndex("start_hour")));
					event.setStartMinute(cursor.getInt(cursor.getColumnIndex("start_minute")));
					event.setDuration(cursor.getInt(cursor.getColumnIndex("duration")));
					event.setDateTime(sqliteDateTimeFormat.parse(cursor.getString(cursor.getColumnIndex("date_time"))));
					event.setHasHeadache(cursor.getInt(cursor.getColumnIndex("hasHeadache")) > 0);
					event.setNotes(cursor.getString(cursor.getColumnIndex("notes")));
					event.setIntensity(getIntensity(event.getEventId()));
					events.add(event);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return events;

	}

	public ArrayList<ChoiseItem> getMyOwnLocationOfPain() throws SQLException
	{
		ArrayList<ChoiseItem> locations = new ArrayList<ChoiseItem>();
		Cursor cursor = db.rawQuery(CUSTOM_LOCATION_OF_PAIN_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Location location = new Location();
					location.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					location.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					location.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					location.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					location.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					locations.add(location);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return locations;
	}

	public ArrayList<Location> getLocationOfPain() throws SQLException
	{
		ArrayList<Location> locations = new ArrayList<Location>();
		Cursor cursor = db.rawQuery(LOCATION_OF_PAIN_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Location location = new Location();
					location.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					location.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					location.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					location.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					location.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					locations.add(location);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return locations;
	}

	public ArrayList<Warning> getWarningSigns() throws SQLException
	{
		ArrayList<Warning> warnings = new ArrayList<Warning>();
		Cursor cursor = db.rawQuery(WARNING_SIGN_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Warning warning = new Warning();
					warning.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					warning.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					warning.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					warning.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					warning.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					warnings.add(warning);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return warnings;
	}

	public ArrayList<ChoiseItem> getMyOwnWarningSigns() throws SQLException
	{
		ArrayList<ChoiseItem> warnings = new ArrayList<ChoiseItem>();
		Cursor cursor = db.rawQuery(CUSTOM_WARNING_SIGN_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Warning warning = new Warning();
					warning.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					warning.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					warning.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					warning.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					warning.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					warnings.add(warning);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return warnings;

	}

	public ArrayList<Symptom> getSymptoms() throws SQLException
	{
		ArrayList<Symptom> symptoms = new ArrayList<Symptom>();
		Cursor cursor = db.rawQuery(SYMPTOMS_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Symptom symptom = new Symptom();
					symptom.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					symptom.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					symptom.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					symptom.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					symptom.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					symptoms.add(symptom);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return symptoms;
	}

	public ArrayList<ChoiseItem> getMyOwnSymptoms() throws SQLException
	{
		ArrayList<ChoiseItem> symptoms = new ArrayList<ChoiseItem>();
		Cursor cursor = db.rawQuery(CUSTOM_SYMPTOMS_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Symptom symptom = new Symptom();
					symptom.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					symptom.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					symptom.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					symptom.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					symptom.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					symptoms.add(symptom);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return symptoms;
	}

	public ArrayList<Food> getFoods() throws SQLException
	{
		ArrayList<Food> foods = new ArrayList<Food>();
		Cursor cursor = db.rawQuery(FOODS_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Food food = new Food();
					food.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					food.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					food.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					food.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					food.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					foods.add(food);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return foods;
	}

	public ArrayList<ChoiseItem> getMyOwnFoods() throws SQLException
	{
		ArrayList<ChoiseItem> foods = new ArrayList<ChoiseItem>();
		Cursor cursor = db.rawQuery(CUSTOM_FOODS_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Food food = new Food();
					food.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					food.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					food.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					food.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					food.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					foods.add(food);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return foods;
	}

	public ArrayList<LifeStyle> getLifeStyle() throws SQLException
	{
		ArrayList<LifeStyle> lifestyles = new ArrayList<LifeStyle>();
		Cursor cursor = db.rawQuery(LIFE_STYLE_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					LifeStyle lifestyle = new LifeStyle();
					lifestyle.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					lifestyle.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					lifestyle.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					lifestyle.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					lifestyle.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					lifestyles.add(lifestyle);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return lifestyles;
	}

	public ArrayList<ChoiseItem> getMyOwnLifeStyle() throws SQLException
	{
		ArrayList<ChoiseItem> lifestyles = new ArrayList<ChoiseItem>();
		Cursor cursor = db.rawQuery(CUSTOM_LIFE_STYLE_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					LifeStyle lifestyle = new LifeStyle();
					lifestyle.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					lifestyle.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					lifestyle.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					lifestyle.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					lifestyle.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					lifestyles.add(lifestyle);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return lifestyles;
	}

	public ArrayList<Environment> getEnvironment() throws SQLException
	{
		ArrayList<Environment> environments = new ArrayList<Environment>();
		Cursor cursor = db.rawQuery(ENVIRONMENT_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Environment environment = new Environment();
					environment.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					environment.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					environment.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					environment.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					environment.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					environments.add(environment);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return environments;
	}

	public ArrayList<ChoiseItem> getMyOwnEnvironment() throws SQLException
	{
		ArrayList<ChoiseItem> environments = new ArrayList<ChoiseItem>();
		Cursor cursor = db.rawQuery(CUSTOM_ENVIRONMENT_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Environment environment = new Environment();
					environment.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					environment.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					environment.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					environment.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					environment.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					environments.add(environment);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return environments;
	}

	public ArrayList<Relief> getReliefs() throws SQLException
	{
		ArrayList<Relief> environments = new ArrayList<Relief>();
		Cursor cursor = db.rawQuery(RELIEF_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Relief relief = new Relief();
					relief.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					relief.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					relief.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					relief.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					relief.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					environments.add(relief);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return environments;
	}

	public ArrayList<ChoiseItem> getMyOwnReliefs() throws SQLException
	{
		ArrayList<ChoiseItem> environments = new ArrayList<ChoiseItem>();
		Cursor cursor = db.rawQuery(CUSTOM_RELIEF_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Relief relief = new Relief();
					relief.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					relief.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					relief.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					relief.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					relief.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					environments.add(relief);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return environments;
	}

	public ArrayList<ChoiseItem> getMyOwnTreatments() throws SQLException
	{
		ArrayList<ChoiseItem> treatments = new ArrayList<ChoiseItem>();
		Cursor cursor = db.rawQuery(TREATMENT_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Treatment treatment = new Treatment();
					treatment.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					treatment.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					treatment.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					treatment.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					treatment.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					treatments.add(treatment);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return treatments;
	}

	public ArrayList<Treatment> getTreatments() throws SQLException
	{
		ArrayList<Treatment> treatments = new ArrayList<Treatment>();
		Cursor cursor = db.rawQuery(TREATMENT_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Treatment treatment = new Treatment();
					treatment.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					treatment.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					treatment.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					treatment.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					treatment.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					treatments.add(treatment);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return treatments;
	}

	public ArrayList<SkippedMeal> getSkippedMeals() throws SQLException
	{
		ArrayList<SkippedMeal> skippedMeals = new ArrayList<SkippedMeal>();
		Cursor cursor = db.rawQuery(SKIPPED_MEAL_QUERY, new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					SkippedMeal meal = new SkippedMeal();
					meal.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					meal.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					meal.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					meal.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					meal.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					skippedMeals.add(meal);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return skippedMeals;
	}

	/********************* get by event id ***************/

	public ArrayList<Location> getLocationOfPain(long eventId) throws SQLException
	{
		ArrayList<Location> locations = new ArrayList<Location>();
		Cursor cursor = db.rawQuery(GET_LOCATION_OF_PAIN_QUERY + eventId + ")", new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Location location = new Location();
					location.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					location.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					location.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					location.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					location.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					locations.add(location);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return locations;
	}

	public ArrayList<Warning> getWarningSigns(long eventId) throws SQLException
	{
		ArrayList<Warning> warnings = new ArrayList<Warning>();
		Cursor cursor = db.rawQuery(GET_WARNING_SING_QUERY + eventId + ")", new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Warning warning = new Warning();
					warning.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					warning.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					warning.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					warning.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					warning.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					warnings.add(warning);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return warnings;
	}

	public Intensity getIntensity(long eventId) throws SQLException
	{
		Intensity intensity = null;
		Cursor cursor = db.rawQuery(GET_INTENSITY_QUERY + eventId + ")", new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				intensity = new Intensity();
				intensity.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
				intensity.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
				intensity.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
				intensity.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
				intensity.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);
			}
		}

		cursor.close();
		return intensity;
	}

	public Fasting getFasting(long eventId) throws SQLException
	{
		Fasting fasting = null;
		Cursor cursor = db.rawQuery(GET_FASTING_QUERY + eventId + ")", new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				fasting = new Fasting();
				fasting.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
				fasting.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
				fasting.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
				fasting.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
				fasting.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);
			}
		}

		cursor.close();
		return fasting;
	}

	public Menstruate getMenstruate(long eventId) throws SQLException
	{
		Menstruate menstruate = null;
		Cursor cursor = db.rawQuery(GET_MESTRUATE_QUERY + eventId + ")", new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				menstruate = new Menstruate();
				menstruate.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
				menstruate.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
				menstruate.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
				menstruate.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
				menstruate.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);
			}
		}

		cursor.close();
		return menstruate;
	}

	public ArrayList<Symptom> getSymptoms(long eventId) throws SQLException
	{
		ArrayList<Symptom> symptoms = new ArrayList<Symptom>();
		Cursor cursor = db.rawQuery(GET_SYMPTOMS_QUERY + eventId + ")", new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Symptom symptom = new Symptom();
					symptom.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					symptom.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					symptom.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					symptom.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					symptom.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					symptoms.add(symptom);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return symptoms;
	}

	public ArrayList<Food> getFoods(long eventId) throws SQLException
	{
		ArrayList<Food> foods = new ArrayList<Food>();
		Cursor cursor = db.rawQuery(GET_FOODS_QUERY + eventId + ")", new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Food food = new Food();
					food.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					food.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					food.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					food.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					food.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					foods.add(food);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return foods;
	}

	public ArrayList<LifeStyle> getLifeStyle(long eventId) throws SQLException
	{
		ArrayList<LifeStyle> lifestyles = new ArrayList<LifeStyle>();
		Cursor cursor = db.rawQuery(GET_LIFE_STYLE_QUERY + eventId + ")", new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					LifeStyle lifestyle = new LifeStyle();
					lifestyle.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					lifestyle.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					lifestyle.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					lifestyle.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					lifestyle.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					lifestyles.add(lifestyle);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return lifestyles;
	}

	public ArrayList<Environment> getEnvironment(long eventId) throws SQLException
	{
		ArrayList<Environment> environments = new ArrayList<Environment>();
		Cursor cursor = db.rawQuery(GET_ENVIRONMENT_QUERY + eventId + ")", new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Environment environment = new Environment();
					environment.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					environment.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					environment.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					environment.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					environment.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					environments.add(environment);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return environments;
	}

	public ArrayList<Relief> getReliefs(long eventId) throws SQLException
	{
		ArrayList<Relief> environments = new ArrayList<Relief>();
		Cursor cursor = db.rawQuery(GET_RELIEF_QUERY + eventId + ")", new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Relief relief = new Relief();
					relief.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					relief.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					relief.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					relief.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					relief.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					environments.add(relief);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return environments;
	}

	public ArrayList<Treatment> getTreatments(long eventId) throws SQLException
	{
		ArrayList<Treatment> treatments = new ArrayList<Treatment>();
		Cursor cursor = db.rawQuery(GET_TREATMENT_QUERY + eventId + ")", new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Treatment treatment = new Treatment();
					treatment.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					treatment.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					treatment.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					treatment.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					treatment.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					treatments.add(treatment);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return treatments;
	}

	public ArrayList<SkippedMeal> getSkippedMeals(long eventId) throws SQLException
	{
		ArrayList<SkippedMeal> skippedMeals = new ArrayList<SkippedMeal>();
		Cursor cursor = db.rawQuery(GET_SKIPPED_MEAL_QUERY + eventId + ")", new String[] {});
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					SkippedMeal meal = new SkippedMeal();
					meal.setId(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ID)));
					meal.setName(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_ANSWER)));
					meal.setOrderNo(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_ORDER_NO)));
					meal.setTag(cursor.getString(cursor.getColumnIndex(MST_Answer.COLUMN_TAG)));
					meal.setNewAnswer(cursor.getInt(cursor.getColumnIndex(MST_Answer.COLUMN_IS_NEW_ANS)) > 0);

					skippedMeals.add(meal);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return skippedMeals;
	}

	public void deleteAnswer(String answerIds)
	{

		db.execSQL("DELETE FROM MST_Answer WHERE id IN(" + answerIds + ")");

		// db.delete("MST_Answer", "id=?", new String[] { answerId });
	}

	public int getdata(String startTime)
	{

		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String endTime = s.format(new Date());

		Cursor cursor = db.query("MST_MigraineEvent", null, "date_time between '" + startTime + "' and '" + endTime
				+ "'", null, null, null, null);

		int count = 0;

		if (cursor != null && cursor.getCount() > 0)
		{
			count = cursor.getCount();
			cursor.close();
			return count;
		}
		cursor.close();
		return 0;
	}

	public int getMigraineCount()
	{
		Cursor cursor = db.query("MST_MigraineEvent", null, null, null, null, null, null);

		int count = 0;

		//Log.e("TAG", "Count = " + cursor.getCount());

		if (cursor != null && cursor.getCount() > 0)
		{
			count = cursor.getCount();
			cursor.close();
			return count;
		}
		cursor.close();
		return 0;
	}

}