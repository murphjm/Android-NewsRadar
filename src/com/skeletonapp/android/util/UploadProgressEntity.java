package com.skeletonapp.android.util;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

public class UploadProgressEntity extends HttpEntityWrapper {
	private UploadProgressCallback listener;
	private long transferSizeTotal;

	public UploadProgressEntity(HttpEntity wrapped, UploadProgressCallback listener, long transferSizeTotal) {
		super(wrapped);
		this.listener = listener;
		this.transferSizeTotal = transferSizeTotal;
	}

	@Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(new UploadProgressOutputStream(outstream, this.transferSizeTotal, this.listener));
    }
}
