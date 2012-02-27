package com.skeletonapp.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SimpleRSSSQLiteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "simplerss.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_FEEDS = "feeds";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_FEED_NAME = "feed_name";
	public static final String COLUMN_FEED_URL = "feed_url";
	public static final String COLUMN_FEED_DATE = "feed_date";
	public static final String COLUMN_FEED_DATA = "feed_data";
	
	//	CREATE TABLE feeds 
	//	(id integer primary key autoincrement,
	//	feed_name text not null,
	//	feed_url text not null,
	//	feed_date text not null,
	//	feed_data blob not null);
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = String.format("CREATE TABLE %s (%s integer primary key autoincrement, %s text not null, %s text not null, %s text not null, %s blob not null);", TABLE_FEEDS, COLUMN_ID, COLUMN_FEED_NAME, COLUMN_FEED_URL, COLUMN_FEED_DATE, COLUMN_FEED_DATA);

	public SimpleRSSSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	public SimpleRSSSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SimpleRSSSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data.");
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_FEEDS);
		onCreate(db);
	}
}
