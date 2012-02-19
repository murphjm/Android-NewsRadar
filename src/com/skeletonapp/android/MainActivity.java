package com.skeletonapp.android;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import com.skeletonapp.android.controls.Header;
import com.skeletonapp.rss.RSSFeed;
import com.skeletonapp.rss.RSSHandler;

import android.os.AsyncTask;
import android.os.Bundle;
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
    }
    
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
    	setHeader("News Radar");
    }
    
    public void fetchRSS(View v) {
    	showBusyDialog("Fetching News");
    	new RSSAsyncTask().execute(txtRSSUrl.getText().toString());
    }

    private RSSFeed getFeed(String urlToRssFeed)
    {
    	try
    	{
    		// setup the url
    	   URL url = new URL(urlToRssFeed);

           // create the factory
           SAXParserFactory factory = SAXParserFactory.newInstance();
           // create a parser
           SAXParser parser = factory.newSAXParser();

           // create the reader (scanner)
           XMLReader xmlreader = parser.getXMLReader();
           // instantiate our handler
           RSSHandler theRssHandler = new RSSHandler();
           // assign our handler
           xmlreader.setContentHandler(theRssHandler);
           // get our data via the url class
           InputSource is = new InputSource(url.openStream());
           // perform the synchronous parse           
           xmlreader.parse(is);
           // get the results - should be a fully populated RSSFeed instance, or null on error
           return theRssHandler.getFeed();
    	}
    	catch (Exception ee)
    	{
    		// if we have a problem, simply return null
    		return null;
    	}
    }
    
    public class RSSAsyncTask extends AsyncTask<String, Void, RSSFeed> { 

    	@Override
    	protected RSSFeed doInBackground(String... url) {
    		// TODO Auto-generated method stub
    		return getFeed(url[0]);
    	}
    	
    	@Override
        protected void onPostExecute(RSSFeed result) {
            
    		// Our long-running process is done, and we can safely access the UI thread
    		RSSFeed rssFeed = (RSSFeed)result;
    		String r = rssFeed.getTitle();
    		
    		if(result != null) {
                dismissBusyDialog();
                Bundle b = new Bundle();
                b.putSerializable("rss", rssFeed);
                transitionToActivity(PagedRSSActivity.class, b);
    		}
        }
    }
}