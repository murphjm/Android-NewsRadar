package com.skeletonapp.android;

import java.net.URL;
import com.skeletonapp.android.db.FeedsDataSource;
import com.skeletonapp.android.rss.RssFeed;
import com.skeletonapp.android.rss.RssReader;
import com.skeletonapp.android.util.Utilities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends BaseActivity {
	private EditText _txtRSSUrl = null;
	private FeedsDataSource _feedDataSource = null;
	private String _feedUrl = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        _txtRSSUrl = (EditText) findViewById(R.id.txtRSSUrl);
        
//        _feedDataSource = new FeedsDataSource(this);
//        _feedDataSource.open();
//        
//        List<Feed> values = _feedDataSource.getAllFeeds();
        
        final Intent intent = getIntent();
        final String action = intent.getAction();

        // Handle browser intent
        if (Intent.ACTION_VIEW.equals(action)) {
            fetchRSS(intent.getData().toString());
        }
    }
    
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
    	setHeader("SimpleRSS");
    }
    
    public void fetchRSSClick(View v) {
    	fetchRSS(_txtRSSUrl.getText().toString());
    }
    
    public void fetchRSS(String url) {
    	showBusyDialog("Fetching feed...");
    	
    	try {
    		String xml = Utilities.readStreamFully(new URL(url).openConnection().getInputStream());
    		new RSSAsyncTask().execute(xml);
		} catch (Exception e) {
			// TODO: Swallow the exception here
		}
    }
    
    private class RSSAsyncTask extends AsyncTask<String, Void, RssFeed> { 
    	Throwable t = null;
    	
    	@Override
    	protected RssFeed doInBackground(String... xml) {
    		RssFeed rssFeed = null;
    		
    		try {
				rssFeed = RssReader.read(xml[0]);
			} catch (Exception e) {
				t = e;
			}
    		
    		return rssFeed;
    	}
    	
    	@Override
        protected void onPostExecute(RssFeed result) {
    		// TODO: Something with T
    		dismissBusyDialog();
    		
    		if(t == null && result != null) {
                Bundle b = new Bundle();
                b.putSerializable("rss", result);
                transitionToActivity(PagedRSSActivity.class, b);
    		}
        }
    }
}
