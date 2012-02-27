package com.skeletonapp.android.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skeletonapp.android.R;

public class Header extends LinearLayout {
	private TextView headerTextView;
	// private ImageView logoImageView;

	public Header(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.header_layout, this, true);
	}
	
	@Override
	public void onFinishInflate() {
		headerTextView = (TextView) this.findViewById(R.id.headerTextView);
		//logoImageView = (ImageView) this.findViewById(R.id.logoImageView);
	}
	
	public void setHeader(String headerText) {
//		if(!Utilities.isNullOrEmpty(headerText)) {
//			this.logoImageView.setVisibility(View.INVISIBLE);
//		}
//		else {
//			this.logoImageView.setVisibility(View.VISIBLE);
//		}
		
		this.headerTextView.setText(headerText);
	}
	
	public void setHeader(int resourceId) {
		String headerText = this.getContext().getResources().getString(resourceId);
		setHeader(headerText);
	}

}
