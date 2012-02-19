package com.skeletonapp.android.util;

import java.io.Serializable;

import com.skeletonapp.android.util.OperationCallbackBase.DispatchType;

public abstract class UploadProgressCallback implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3981091442283835590L;
	private DispatchType dispatchType;
	
	public UploadProgressCallback()
	{
		this.dispatchType = DispatchType.CurrentThread;
	}
	
	public UploadProgressCallback(DispatchType dispatchType) {
		this.dispatchType = dispatchType;
	}
	
	protected abstract void onUploadProgressChanged(long progress, long total);

	public void notifyUploadProgressChanged(final long progress, final long total) {
		Runnable notifier = new Runnable() {

			// TODO: Add @override
			public void run() {
				UploadProgressCallback.this.onUploadProgressChanged(progress, total);
			}
			
		};
		
		if (dispatchType == DispatchType.MainThread)
		{
			Utilities.dispatchToMainThread(notifier);
		}
		else if (dispatchType == DispatchType.NewThread)
		{
			Thread thread = new Thread(notifier);
			thread.start();
		}
		else
		{
			notifier.run();
		}
	}
	
}
