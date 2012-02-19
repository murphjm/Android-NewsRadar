package com.skeletonapp.views;

import android.app.Activity;
import com.skeletonapp.android.util.OperationCallback;

public interface IBaseView
{
	void onError(Exception e);

	void notifyUser(int titleId, int messageId);
	
	void showBusyDialog();
	
	void showBusyDialog(int resourceId);
	
	void dismissBusyDialog();
	
	void promptUser(String title, String message, final String positiveButton, final String negativeButton, final OperationCallback<String> callback);
	
	String getStringResource(int resourceId);
	
	Activity getContext();
	
	int getIntResource(int resourceId);
}