package com.weihan.treatmill;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBForTreatmill extends SQLiteOpenHelper
{
	private final static String DATABASE_NAME="db_for_treatmill";
	private final static int DATABASE_VERSION=18;
	public final static String TABLE_NAME_USER="user";
	public final static String USER_NAME="user_name";
	public final static String USER_AGE="user_age";
	public final static String USER_GENDER="user_gender";
	public final static String USER_HEIGHT="user_height";
	public final static String USER_WEIGHT="user_weight";
	public final static String USER_HEIGHT_SCALE="user_height_scale";
	public final static String USER_WEIGHT_SCALE="user_weight_scale";

	
	public final static String TABLE_NAME_USER_FITNESS="fitness";
	public final static String ID="_id"; 
	public final static String USER_ID="user_id";
	public final static String CALORIE="calorie";
	public final static String DISTANCE="distance";
	public final static String TREAT_DATE="treat_data";
	public final static String TIME="time";
	public final static String HEART_BEAT = "heart_beat";
	public final static String USER_FITNESS_MODE ="fitness_mode";
    public final static String YEAR ="fitness_year";
    public final static String MONTH = "fitness_month";
    public final static String WEEK = "fitness_week";
    public final static String DAY = "fitness_day";
    public final static String HOUR = "fitness_hour";
    public final static String MINUTES = "fitness_minutes";
    
	public final static String TABLE_HISTORY_GOAL="history_goal";
	public final static String HISTORY_GOAL_DATE="history_goal_date";
	public final static String HISTORY_GOAL_VALUE="history_goal_value";
	public final static String HISTORY_GOAL_FINISH_PROCESS="history_goal_finish_process";
	public final static String HISTORY_CALORIE_OR_DISTANCE="history_calorie_or_distance";
	public final static int HISTORY_CALORIE=1;
	public final static int HISTORY_DISTANCE=2;
    public final static String HISTORY_MONTH = "history_month";
    public final static String HISTORY_YEAR = "history_year";
    public final static String HISTORY_DAY = "history_day";
	//id sceneID string cmd
	public DBForTreatmill(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	 @Override
	 public void onCreate(SQLiteDatabase db) 
	 {
	// TODO Auto-generated method stub
		 String sql1="Create table "+TABLE_NAME_USER +
				 "(" + ID + " integer primary key autoincrement, "+ USER_NAME +" text, " + USER_AGE +" integer, "+USER_GENDER+" integer, "+ USER_HEIGHT +" float, "
				 +USER_WEIGHT+" float, "+ USER_HEIGHT_SCALE +" integer, "+ USER_WEIGHT_SCALE+ " integer);";
		 
		 db.execSQL(sql1);
		 
		 
		 String sql2="Create table "+TABLE_NAME_USER_FITNESS+
				 "(" + ID + " integer primary key autoincrement, "+ USER_ID +" integer, "+ USER_FITNESS_MODE +" integer, "+ TREAT_DATE +" text, "+
				 CALORIE +" float,"+ DISTANCE +" float," + TIME +" integer, "  + HEART_BEAT+" integer,"+ DAY+" integer,"+WEEK +" integer,"+MONTH+" integer,"+YEAR+" integer," +
                HOUR +" integer,"+MINUTES + " integer);";
		 db.execSQL(sql2);
		 
		
		 
		 String sql3="Create table "+TABLE_HISTORY_GOAL +
				 "(" + ID + " integer primary key autoincrement, "+ USER_ID +" integer, "+ HISTORY_YEAR +" integer, "
				 + HISTORY_MONTH +" integer, "+ HISTORY_DAY +" integer, "
				 +HISTORY_CALORIE_OR_DISTANCE+" integer, " + HISTORY_GOAL_VALUE +" integer, "
				 +HISTORY_GOAL_FINISH_PROCESS+" integer);";
		 Log.v("db for history create","db for history create "+ sql3);
		 db.execSQL(sql3); 
	 }
	
	 @Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
	// TODO Auto-generated method stub
		 String sql=" DROP TABLE IF EXISTS "+TABLE_NAME_USER;
		 db.execSQL(sql);
		 String sql2=" DROP TABLE IF EXISTS "+TABLE_NAME_USER_FITNESS;
		 db.execSQL(sql2);
		 String sql3=" DROP TABLE IF EXISTS "+TABLE_HISTORY_GOAL;
		 db.execSQL(sql3);
		 onCreate(db);
	}
	
	public Cursor select(String table, String[] columns, String selection, String[] selectionArgs,SQLiteDatabase dbi)
	{   //SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=dbi.query(true,table, columns, selection, selectionArgs, null, null, " _id desc",null);
       // db.close();
		return cursor;
	}


    public Cursor selectInAsce(String table, String[] columns, String selection, String[] selectionArgs,SQLiteDatabase dbi)
    {   //SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=dbi.query(true,table, columns, selection, selectionArgs, null, null, " _id asc",null);
        // db.close();
        return cursor;
    }


	public void insert(String table, ContentValues cv)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		long row=db.insert(table, null, cv);
		db.close();
	}
	
	public void delete(String table, String where, String[] args)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(table, where, args);
		db.close();
	}
	
	public void update(String table, ContentValues cv, String where, String[] whereArgs)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		db.update(table, cv, where, whereArgs);
		db.close();
	}
}
