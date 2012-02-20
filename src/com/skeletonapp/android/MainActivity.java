package com.skeletonapp.android;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends BaseActivity {
	EditText txtRSSUrl = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        txtRSSUrl = (EditText) this.findViewById(R.id.txtRSSUrl);
        
        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            String uri = intent.getData().toString();
            txtRSSUrl.setText(uri);
        }
    }
    
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
    	setHeader("News Radar");
    }
    
    public void fetchRSS(View v) {
    	showBusyDialog("Fetching feed...");
    	URL url = null;
		try {
			url = new URL(txtRSSUrl.getText().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
		}
		
    	new RSSAsyncTask().execute(url);
    }
    
    public class RSSAsyncTask extends AsyncTask<URL, Void, RssFeed> { 

    	@Override
    	protected RssFeed doInBackground(URL... url) {
    		RssFeed feed = null;
    		try {
				feed = RssReader.read(url[0]);
			} catch (SAXException e) {
				
			} catch (IOException e) {
				
			}
    		
    		return feed;
    	}
    	
    	@Override
        protected void onPostExecute(RssFeed result) {
    		if(result != null) {
    			dismissBusyDialog();
                Bundle b = new Bundle();
                b.putSerializable("rss", (Serializable) result);
                transitionToActivity(PagedRSSActivity.class, b);
    		}
        }
    }
}