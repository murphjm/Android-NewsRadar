package com.skeletonapp.android.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import org.apache.http.client.ClientProtocolException;

public interface ICallable<T> {
	T call() throws ClientProtocolException, IOException, WebError, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, IllegalStateException;
}
