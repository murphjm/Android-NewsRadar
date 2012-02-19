package com.skeletonapp.android.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class WebClient {
	private HttpClient httpClient;
	private int connectionTimeout = 3000;
	private int socketTimeout = 5000;
	
	public WebClient() {}
	
	public WebClient(int connectionTimeout, int socketTimeout)
	{
		this.connectionTimeout = connectionTimeout;
		this.socketTimeout = socketTimeout;
	}
	
	public HttpResponse get(String url) throws ClientProtocolException, IOException, WebError, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException
	{
		if(httpClient == null)
		{
			httpClient = initializeHttpClient(connectionTimeout, socketTimeout);
		}
		
		HttpGet request = new HttpGet(url);
		HttpResponse response = httpClient.execute(request);
		
		if(response.getStatusLine().getStatusCode() != 200)
		{
			WebError error = new WebError();
			error.setResponse(response);
			throw error;
		}
		
		return response;
	}
	
	public HttpResponse post(String url, String body) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException
	{
		if(httpClient == null)
		{
			httpClient = initializeHttpClient(connectionTimeout, socketTimeout);
		}
		
		StringEntity payload = new StringEntity(body);
		HttpPost post = new HttpPost(url);
		post.setEntity(payload);
		
		return httpClient.execute(post);
	}
	
	public HttpResponse postData(String url, byte[] data, String contentType, UploadProgressCallback listener) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException
	{
		if(httpClient == null)
		{
			httpClient = initializeHttpClient(connectionTimeout, socketTimeout);
		}
		
		ByteArrayEntity fileEntity = new ByteArrayEntity(data);
		fileEntity.setContentType(contentType);
		UploadProgressEntity payload = new UploadProgressEntity(fileEntity, listener, data.length);
		HttpPost post = new HttpPost(url);
		post.setEntity(payload);
		
		return httpClient.execute(post);
	}
	
	public HttpResponse put(String url, String body) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException
	{
		if(httpClient == null)
		{
			httpClient = initializeHttpClient(connectionTimeout, socketTimeout);
		}
		
		StringEntity payload = new StringEntity(body);
		HttpPut post = new HttpPut(url);
		post.setEntity(payload);
		
		return httpClient.execute(post);
	}
	
	public Bitmap downloadImage(String url) throws MalformedURLException, IOException, WebError
	{
		byte[] bytes = download(url);
		Bitmap result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		
		if(result == null)
		{
			WebError error = new WebError();
			throw error;
		}
		
		return result;
	}
	
	protected static byte[] download(String strurl) {
	    try {
	        URL url = new URL(strurl);
	        InputStream is = (InputStream) url.getContent();
	        byte[] buffer = new byte[8192];
	        int bytesRead;
	        ByteArrayOutputStream output = new ByteArrayOutputStream();
	        while ((bytesRead = is.read(buffer)) != -1) {
	            output.write(buffer, 0, bytesRead);
	        }
	        return output.toByteArray();
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    return null;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	private static HttpClient initializeHttpClient(int connectionTimeout, int socketTimeout) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException
	{
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
		HttpConnectionParams.setSoTimeout(params, socketTimeout);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		SSLSocketFactory sslSocketFactory = new FullTrustSSLSocketFactory();
		sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
		ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(params, schemeRegistry);
		
		return new DefaultHttpClient(connectionManager, params);
	}
}
