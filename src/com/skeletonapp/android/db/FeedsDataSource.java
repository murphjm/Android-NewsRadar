package com.skeletonapp.android.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.matshofman.saxrssreader.RssFeed;

import com.skeletonapp.android.models.Feed;
import com.skeletonapp.android.util.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class FeedsDataSource {
		private SQLiteDatabase database;
		private SimpleRSSSQLiteHelper dbHelper;
		
		private String[] allColumns = { 
				SimpleRSSSQLiteHelper.COLUMN_ID,
				SimpleRSSSQLiteHelper.COLUMN_FEED_NAME,
				SimpleRSSSQLiteHelper.COLUMN_FEED_URL,
				SimpleRSSSQLiteHelper.COLUMN_FEED_DATE,
				SimpleRSSSQLiteHelper.COLUMN_FEED_DATA };
		
		public FeedsDataSource(Context context) {
			dbHelper = new SimpleRSSSQLiteHelper(context);
		}

		public void open() throws SQLException {
			database = dbHelper.getWritableDatabase();
		}

		public void close() {
			dbHelper.close();
		}
		
		public Feed createFeed(String name, String url, Date date, RssFeed feedData) {
			ContentValues values = new ContentValues();
			values.put(SimpleRSSSQLiteHelper.COLUMN_FEED_NAME, name);
			values.put(SimpleRSSSQLiteHelper.COLUMN_FEED_URL, url);
			values.put(SimpleRSSSQLiteHelper.COLUMN_FEED_DATE, date.toString());
			values.put(SimpleRSSSQLiteHelper.COLUMN_FEED_DATA , Utilities.convertRssFeedToByteStream(feedData));
			
			long insertId = database.insert(SimpleRSSSQLiteHelper.TABLE_FEEDS, null, values);
			
			// To show how to query
			Cursor cursor = database.query(SimpleRSSSQLiteHelper.TABLE_FEEDS,
					allColumns, SimpleRSSSQLiteHelper.COLUMN_ID + " = " + insertId, null,
					null, null, null);
			cursor.moveToFirst();

			return cursorToFeed(cursor);
		}

		public void deleteFeed(Feed feed) {
			long id = feed.getId();
			System.out.println("Comment deleted with id: " + id);
			database.delete(SimpleRSSSQLiteHelper.TABLE_FEEDS, SimpleRSSSQLiteHelper.COLUMN_ID + " = " + id, null);
		}
		
		public List<Feed> getAllComments() {
			List<Feed> feeds = new ArrayList<Feed>();
			Cursor cursor = database.query(SimpleRSSSQLiteHelper.TABLE_FEEDS, allColumns, null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Feed feed = cursorToFeed(cursor);
				feeds.add(feed);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			return feeds;
		}
		
		private Feed cursorToFeed(Cursor cursor) {
			Feed feed = new Feed();
			feed.setId(cursor.getLong(0));
			feed.setFeedName(cursor.getString(1));
			feed.setFeedUrl(cursor.getString(2));
//			feed.setFeedDate(cursor.getString(3));
//			feed.setComment(cursor.getString(4));
			return feed;
		}
}
