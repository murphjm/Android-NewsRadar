package com.skeletonapp.android.util;

public abstract class OperationCallbackBase implements Runnable {
	public enum DispatchType { CurrentThread, MainThread, NewThread }
	
	protected void DispatchToNewThread()
	{
		Thread thread = new Thread(this);
		thread.start();
	}
	
	protected void DispatchToMainThread()
	{
		Utilities.dispatchToMainThread(this);
	}
}
