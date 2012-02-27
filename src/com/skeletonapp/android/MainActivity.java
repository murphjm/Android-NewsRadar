package com.skeletonapp.android;

import java.net.MalformedURLException;
import java.net.URL;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssReader;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends BaseActivity {
	EditText _txtRSSUrl = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            String uri = intent.getData().toString();
            
            try {
				fetchRSS(new URL(uri));
			} catch (MalformedURLException e) {
				// Swallow the exception
			}
        }
    }
    
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
    	setHeader("SimpleRSS");
    }
    
    public void fetchRSS(View v) {
    	_txtRSSUrl = (EditText) findViewById(R.id.txtRSSUrl);
    	
    	URL url = null;
		try {
			url = new URL(_txtRSSUrl.getText().toString());
			fetchRSS(url);
		} catch (MalformedURLException e) {
			// Swallow the exception here
		}
    }
    
    public void fetchRSS(URL url) {
    	showBusyDialog("Fetching feed...");
    	new RSSAsyncTask().execute(url);
    }
    
    public class RSSAsyncTask extends AsyncTask<URL, Void, RssFeed> { 
    	Throwable t = null;
    	// TODO: Database insert / lookup
    	@Override
    	protected RssFeed doInBackground(URL... url) {
    		RssFeed feed = null;
    		try {
				feed = RssReader.read(url[0]);
			} catch (Exception e) {
				t = e;
			}
    		
    		return feed;
    	}
    	
    	@Override
        protected void onPostExecute(RssFeed result) {
    		// TODO: Something with T
    		dismissBusyDialog();
    		
    		if(result != null) {
                Bundle b = new Bundle();
                b.putSerializable("rss", result);
                transitionToActivity(PagedRSSActivity.class, b);
    		}
        }
    }
}
