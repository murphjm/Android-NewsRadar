package com.skeletonapp.android;

import java.util.ArrayList;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.skeletonapp.android.util.Utilities;
import com.viewpagerindicator.CirclePageIndicator;

public class PagedRSSActivity extends BaseActivity {
    private ViewPager _viewPager; 
    private Context _context;
    private RssPagerAdapter _rssAdapter;
    private RssFeed _rssFeed = null;
    private ArrayList<RssItem> _rssItems = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paged_activity);
        setHeader("Public Static Droid Main");
        
        _context = this;
        
        Bundle b = getIntent().getExtras(); 
        
        if(b != null) {
        	_rssFeed = (RssFeed) b.getSerializable("rss");
        	_rssItems = _rssFeed.getRssItems();
        }
        		
        _rssAdapter = new RssPagerAdapter();
        _viewPager = (ViewPager) findViewById(R.id.viewPager);
        _viewPager.setAdapter(_rssAdapter);

        //Bind the title indicator to the adapter
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(_viewPager);
		
//		final float density = getResources().getDisplayMetrics().density;
//		indicator.setBackgroundColor(Color.WHITE);
//		indicator.setRadius(4 * density);
//        indicator.setPageColor(0x880000FF);
//		indicator.setFillColor(Color.parseColor("#03a6ea"));
//		indicator.setStrokeColor(Color.BLACK);
//		indicator.setStrokeWidth((float) (1.5 * density));
    }
    
    private class RssPagerAdapter extends PagerAdapter {

        
        @Override
        public int getCount() {
        	// TODO: FIX!
            return _rssFeed.getRssItems().size();
        }

    /**
     * Create the page for the given position.  The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * {@link #finishUpdate()}.
     *
     * @param container The containing View in which the page will be shown.
     * @param position The page position to be instantiated.
     * @return Returns an Object representing the new page.  This does not
     * need to be a View, but can be some other container of the page.
     */
        @Override
        public Object instantiateItem(View collection, int position) {
        	    RssItem rssItem = _rssItems.get(position);
        	
                WebView wv = new WebView(_context);
                String pagedContent = "<h2>" + Utilities.encodeHTML(rssItem.getTitle()) + "</h2>";
                pagedContent += rssItem.getContent().replace("%", "&#37;"); //(!rssItem.getContent().equals("")) ? rssItem.getContent() : rssItem.getDescription();
                //pagedContent += "<iframe class=\"youtube-player\" type=\"text/html\" width=\"640\" height=\"385\" src=\"http://www.youtube.com/embed/8xgkw67o0Gc\" frameborder=\"0\">";
                wv.loadData(pagedContent, "text/html", "UTF-8");
                wv.setHorizontalScrollBarEnabled(false);
                
                WebSettings webSettings = wv.getSettings();
                webSettings.setSavePassword(false);
                webSettings.setSaveFormData(false);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setSupportZoom(false);
                webSettings.setBuiltInZoomControls(false);
                webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
                
                ((ViewPager) collection).addView(wv,0);
                return wv;
        }

    /**
     * Remove a page for the given position.  The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from {@link #finishUpdate()}.
     *
     * @param container The containing View from which the page will be removed.
     * @param position The page position to be removed.
     * @param object The same object that was returned by
     * {@link #instantiateItem(View, int)}.
     */
        @Override
        public void destroyItem(View collection, int position, Object view) {
                ((ViewPager) collection).removeView((WebView) view);
        }
        
        @Override
        public boolean isViewFromObject(View view, Object object) {
                return view==((WebView)object);
        }

        
    /**
     * Called when the a change in the shown pages has been completed.  At this
     * point you must ensure that all of the pages have actually been added or
     * removed from the container as appropriate.
     * @param container The containing View which is displaying this adapter's
     * page views.
     */
        @Override
        public void finishUpdate(View arg0) {}
        

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {}

        @Override
        public Parcelable saveState() {
                return null;
        }

        @Override
        public void startUpdate(View arg0) {}

    }
}


