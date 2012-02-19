package com.skeletonapp.android.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ViewFlipper;

public class FixedViewFlipper extends ViewFlipper {

	public FixedViewFlipper(Context context) {
		super(context);		
	}
	
	public FixedViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);		
	}

	@Override
	protected void onDetachedFromWindow() {
		try {
	        super.onDetachedFromWindow();
	    }
	    catch (IllegalArgumentException e) {
	    	Log.d("FixedViewFliper", "Crisis averted!");
	        stopFlipping();
	    }
	}
	
}