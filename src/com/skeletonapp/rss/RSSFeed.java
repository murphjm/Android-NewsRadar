package com.skeletonapp.rss;


import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import android.os.Parcel;
import android.os.Parcelable;

import com.skeletonapp.rss.RSSItem;

public class RSSFeed implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _title = null;
	private String _pubdate = null;
	private int _itemcount = 0;
	private List<RSSItem> _itemlist;
	
	
	RSSFeed()
	{
		_itemlist = new Vector(0); 
	}

	public int addItem(RSSItem item)
	{
		_itemlist.add(item);
		_itemcount++;
		
		return _itemcount;
	}
	
	public RSSItem getItem(int location)
	{
		return _itemlist.get(location);
	}
	
	public List getAllItems()
	{
		return _itemlist;
	}
	
	public int getItemCount()
	{
		return _itemcount;
	}
	
	public void setTitle(String title)
	{
		_title = title;
	}
	
	public void setPubDate(String pubdate)
	{
		_pubdate = pubdate;
	}
	
	public String getTitle()
	{
		return _title;
	}
	
	public String getPubDate()
	{
		return _pubdate;
	}
}
