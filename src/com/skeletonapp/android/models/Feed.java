package com.skeletonapp.android.models;

import java.util.Date;

import com.skeletonapp.android.rss.RssFeed;

public class Feed {
	private long _id;
	private String _feedName;
	private String _feedUrl;
	private Date _feedDate;
	private String _feedData;
	
	public long getId() {
		return _id;
	}
	
	public String getFeedName() {
		return _feedName;
	}
	
	public String getFeedUrl() {
		return _feedUrl;
	}
	
	public Date getFeedDate() {
		return _feedDate;
	}
	
	public String getFeedData() {
		return _feedData;
	}
	
	public void setId(long _id) {
		this._id = _id;
	}
	
	public void setFeedName(String _feedName) {
		this._feedName = _feedName;
	}
	
	public void setFeedUrl(String _feedUrl) {
		this._feedUrl = _feedUrl;
	}
	
	public void setFeedDate(Date _feedDate) {
		this._feedDate = _feedDate;
	}
	
	public void setFeedData(String _feedData) {
		this._feedData = _feedData;
	}
	
	@Override
	public String toString() {
		return getFeedName();
	}
}
