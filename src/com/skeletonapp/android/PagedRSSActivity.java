package com.skeletonapp.android;

import com.skeletonapp.rss.RSSFeed;
import com.skeletonapp.rss.RSSItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;

public class PagedRSSActivity extends BaseActivity {
    private ViewPager _viewPager; 
    private Context _context;
    private RSSPagerAdapter _rssAdapter;
    private RSSFeed _rssFeed;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paged_activity);
        setHeader("Public Static Droid Main");
        
        _context = this;
        
        Bundle b = getIntent().getExtras(); 
        _rssFeed = (RSSFeed) b.getSerializable("rss");
        		
        _rssAdapter = new RSSPagerAdapter();
        _viewPager = (ViewPager) findViewById(R.id.viewPager);
        _viewPager.setAdapter(_rssAdapter);
    }
    
    private class RSSPagerAdapter extends PagerAdapter {

        
        @Override
        public int getCount() {
                return _rssFeed.getItemCount();
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
        	    RSSItem rssItem = _rssFeed.getItem(position);
        	
                WebView wv = new WebView(_context);
                String pagedContent = "<h1>" + rssItem.getTitle() + "</h1>";
                pagedContent += rssItem.getContent().replace("%", "&#37;"); //(!rssItem.getContent().equals("")) ? rssItem.getContent() : rssItem.getDescription();
                wv.loadData(pagedContent, "text/html", "UTF-8");
                wv.setHorizontalScrollBarEnabled(false);
                
                WebSettings webSettings = wv.getSettings();
                webSettings.setSavePassword(false);
                webSettings.setSaveFormData(false);
                webSettings.setJavaScriptEnabled(false);
                webSettings.setSupportZoom(false);
                webSettings.setBuiltInZoomControls(false);
                webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
                
                ((ViewPager) collection).addView((View)wv,0);
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


