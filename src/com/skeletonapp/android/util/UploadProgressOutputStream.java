package com.skeletonapp.android.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UploadProgressOutputStream extends FilterOutputStream {
	private UploadProgressCallback listener;
	private long transferSizeTotal, transferred = 0;
	private int progressByteCount = 1024;
	

	public UploadProgressOutputStream(OutputStream out, long transferSizeTotal, UploadProgressCallback listener) {
		super(out);		
		this.progressByteCount = (int)((float)transferSizeTotal/15);
		this.transferSizeTotal = transferSizeTotal;
		this.listener = listener;
	}
	
	public void write(byte[] b, int position, int bytesToSend) throws IOException {
		int bytesToWrite = 0, bytesWritten = 0;
		
		while(bytesWritten != bytesToSend) {
			bytesToWrite = Math.min(progressByteCount, bytesToSend - bytesWritten);
			out.write(b, position + bytesWritten, bytesToWrite);
			
			bytesWritten += bytesToWrite;
			this.transferred += bytesToWrite;
			notifyProgress();
			Thread.yield();
		}
    }

    public void write(int b) throws IOException {
        out.write(b);
        this.transferred++;
        notifyProgress();
    }
    
    private void notifyProgress() {
    	if(this.listener != null) {
    		this.listener.notifyUploadProgressChanged(this.transferred, this.transferSizeTotal);
    	}
    }

}
