package com.skeletonapp.android;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssReader;

import org.xml.sax.SAXException;

import com.skeletonapp.android.fragments.AddFeedFragment;
import com.skeletonapp.android.fragments.FeedListFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

public class MainActivity extends BaseActivity {
	EditText _txtRSSUrl = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        transitionToFragment(getListFeedsFragment());
        
        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
        	transitionToFragment(getAddFeedFragment());
            String uri = intent.getData().toString();
            try {
				fetchRSS(new URL(uri));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
    	setHeader("News Radar");
    }
    
    public void fetchRSS(View v) {
    	_txtRSSUrl = (EditText) findViewById(R.id.txtRSSUrl);
    	
    	URL url = null;
		try {
			url = new URL(_txtRSSUrl.getText().toString());
			fetchRSS(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
		}
    }
    
    public void fetchRSS(URL url) {
    	showBusyDialog("Fetching feed...");
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
