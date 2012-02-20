package com.skeletonapp.android;

import java.io.File;

import com.flurry.android.FlurryAgent;
import com.skeletonapp.android.controls.Header;
import com.skeletonapp.android.util.OperationCallback;
import com.skeletonapp.android.util.Utilities;
import com.skeletonapp.android.util.VoidOperationCallback;
import com.skeletonapp.views.IBaseView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class BaseActivity extends Activity implements IBaseView
{
	private Header _header;
	private ProgressDialog _busyDialog;
	private AlertDialog _alertDialog;
	
	protected boolean isBusyDialogVisible, isNotifyUserVisible, isDisplayErrorVisible;
	private String busyDialogMessage, notifyUserTitle, notifyUserMessage;
	protected Exception displayErrorException;
	
	private static final String BUSY_DIALOG_VISIBLE_INSTANCE_STATE_KEY = "isBusyDialogVisible";
	private static final String BUSY_DIALOG_MESSAGE_INSTANCE_STATE_KEY = "busyDialogMessage";
	private static final String NOTIFY_USER_VISIBLE_INSTANCE_STATE_KEY = "isNotifyUserVisible";
	private static final String NOTIFY_USER_TITLE_INSTANCE_STATE_KEY = "notifyUserTitle";
	private static final String NOTIFY_USER_MESSAGE_INSTANCE_STATE_KEY = "notifyUserMessage";
	private static final String DISPLAY_ERROR_VISIBLE_INSTANCE_STATE_KEY = "isDisplayErrorVisible";
	private static final String DISPLAY_ERROR_EXCEPTION_INSTANCE_STATE_KEY = "displayErrorException";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null) {
			restoreInstanceState(savedInstanceState);
		}
	}
	
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		this._header = (Header) this.findViewById(R.id.header);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if(_alertDialog != null)
			_alertDialog.dismiss();
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putBoolean(BUSY_DIALOG_VISIBLE_INSTANCE_STATE_KEY, isBusyDialogVisible);
		state.putString(BUSY_DIALOG_MESSAGE_INSTANCE_STATE_KEY, busyDialogMessage);
		state.putBoolean(NOTIFY_USER_VISIBLE_INSTANCE_STATE_KEY, isNotifyUserVisible);
		state.putString(NOTIFY_USER_TITLE_INSTANCE_STATE_KEY, notifyUserTitle);
		state.putString(NOTIFY_USER_MESSAGE_INSTANCE_STATE_KEY, notifyUserMessage);
		state.putBoolean(DISPLAY_ERROR_VISIBLE_INSTANCE_STATE_KEY, isDisplayErrorVisible);
		state.putSerializable(DISPLAY_ERROR_EXCEPTION_INSTANCE_STATE_KEY, displayErrorException);
	}
	
	private void restoreInstanceState(Bundle state) {
		isBusyDialogVisible = state.getBoolean(BUSY_DIALOG_VISIBLE_INSTANCE_STATE_KEY);
		busyDialogMessage = state.getString(BUSY_DIALOG_MESSAGE_INSTANCE_STATE_KEY);
		isNotifyUserVisible = state.getBoolean(NOTIFY_USER_VISIBLE_INSTANCE_STATE_KEY);
		notifyUserTitle = state.getString(NOTIFY_USER_TITLE_INSTANCE_STATE_KEY);
		notifyUserMessage = state.getString(NOTIFY_USER_MESSAGE_INSTANCE_STATE_KEY);
		isDisplayErrorVisible = state.getBoolean(DISPLAY_ERROR_VISIBLE_INSTANCE_STATE_KEY);
		displayErrorException = (Exception)state.getSerializable(DISPLAY_ERROR_EXCEPTION_INSTANCE_STATE_KEY);
		
		if(isBusyDialogVisible) {
			showBusyDialog(busyDialogMessage);
		}
		
		if(isNotifyUserVisible) {
			notifyUser(notifyUserTitle, notifyUserMessage);
		}
		
		if(isDisplayErrorVisible) {
			onError(displayErrorException);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		// TODO
		//inflater.inflate(R.menu.global_menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return true;
	}
	
	protected void setHeader(int resourceId) {
		if(_header != null) {
			_header.setHeader(resourceId);
		}
	}
	
	protected void setHeader(String header) {
		if(_header != null) {
			_header.setHeader(header);
		}
	}
	
	protected void transitionToActivity(Class<?> c) {
		Intent intent = new Intent(this, c);
		transitionToActivity(intent, false);
	}
	
	protected void transitionToActivity(Class<?> c, Bundle extras) {
		transitionToActivity(c, extras, false);
	}
	
	protected void transitionToActivity(Class<?> c, Bundle extras, boolean reverseAnimations) {
		Intent intent = new Intent(this, c);
		intent.putExtras(extras);
		transitionToActivity(intent, reverseAnimations);
	}
	
	protected void transitionToActivity(Intent intent, boolean reverseAnimations) {
		startActivity(intent);
		if(!reverseAnimations) {
			overridePendingTransition(R.anim.view_transition_in_left, R.anim.view_transition_out_left);
		}
		else {
			overridePendingTransition(R.anim.view_transition_in_right, R.anim.view_transition_out_right);
		}
	}
	
	public void navigateToAddFeed(View v) {
		transitionToActivity(AddFeedActivity.class);
	}
	
	public void navigateToFeedList(View v) {
		transitionToActivity(FeedListActivity.class);
	}
		
	public void onError(Exception e)
	{
		onError( e, new VoidOperationCallback() {

			@Override
			protected void onCompleted()
			{
				isDisplayErrorVisible = false;
				displayErrorException = null;
			}

			@Override
			protected void onError(Exception error)
			{
				isDisplayErrorVisible = false;
				displayErrorException = null;
			}
			
		});
	}
	
	protected void onError(Exception e, VoidOperationCallback cb)
	{
		displayErrorException = e;
		// TODO
		// Utilities.displayError(this, e, cb);
		isDisplayErrorVisible = true;
	}
	
	public void notifyUser(int titleId, int messageId)
	{
		notifyUserTitle = getStringResource(titleId);
		notifyUserMessage = getStringResource(messageId);
		notifyUser(notifyUserTitle, notifyUserMessage);
	}

	public void notifyUser(String title, String message)
	{
		notifyUserTitle = title;
		notifyUserMessage = message;
		_alertDialog = Utilities.notifyUser(this, notifyUserTitle, notifyUserMessage, new VoidOperationCallback() {

			@Override
			protected void onCompleted()
			{
				isNotifyUserVisible = false;
				notifyUserTitle = null;
				notifyUserMessage = null;
			}

			@Override
			protected void onError(Exception error)
			{
				isNotifyUserVisible = false;
				notifyUserTitle = null;
				notifyUserMessage = null;
			}
			
		});
		
		isNotifyUserVisible = true;
	}

	public void showBusyDialog()
	{
		showBusyDialog(R.string.busy_dialog_default_message);
	}

	public void showBusyDialog(int resourceId)
	{
		showBusyDialog(getResources().getString(resourceId));
	}
	
	public void showBusyDialog(String message)
	{
		busyDialogMessage =  message;
		
		if(_busyDialog == null) {
			_busyDialog = ProgressDialog.show(this, "", busyDialogMessage, true);			
		}
		else {
			_busyDialog.setMessage(message);
			_busyDialog.show();
		}
		
		isBusyDialogVisible = true;
	}

	public void dismissBusyDialog()
	{
		if (_busyDialog != null)
		{
			_busyDialog.dismiss();
		}
		isBusyDialogVisible = false;
		busyDialogMessage = null;
	}

	public void promptUser(String title, String message, final String positiveButton, final String negativeButton, final OperationCallback<String> callback)
	{
		Utilities.promptUser(this, title, message, positiveButton, negativeButton, callback);
	}

	public String getStringResource(int resourceId)
	{
		return getResources().getString(resourceId);
	}
	
	public void cleanupTemporaryDocuments() {
		try {
			File[] cacheFiles = getCacheDir().listFiles();
			
			for(File file : cacheFiles) {
				file.delete();
			}
		}
		catch(Exception err) {}
	}

	public Activity getContext()
	{
		return this;
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		// TODO
		// FlurryAgent.onStartSession(this, "BBA7IBMCUSGPZINYN2JA");
	}
	
	@Override
	public void onStop()
	{
	   super.onStop();
	   // TODO
	   // FlurryAgent.onEndSession(this);
	}

	public int getIntResource(int resourceId) {
		// TODO Auto-generated method stub
		return 0;
	}
}