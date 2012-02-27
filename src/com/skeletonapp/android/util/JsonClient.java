package com.skeletonapp.android.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import android.util.Log;

import com.google.gson.Gson;

public class JsonClient
{
	private WebClient webClient;
	private Gson gson;

	public JsonClient(WebClient webClient)
	{
		this.webClient = webClient;
		gson = new Gson();

		//gson = new GsonBuilder().registerTypeAdapter(Endpoint.class, new EndpointAdapter()).create();
	}

	@SuppressWarnings({ "rawtypes" })
	public <T> T get(String url, Class responseType) throws ClientProtocolException, IOException, WebError, KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, UnrecoverableKeyException, IllegalStateException
	{
		HttpResponse response = webClient.get(url);
		return parseResponse(response, responseType);
	}

	@SuppressWarnings({ "rawtypes" })
	public <T, U> T post(String url, U requestData, Class responseType) throws ClientProtocolException, IOException, WebError, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, IllegalStateException
	{
		String body = gson.toJson(requestData);

		Log.d(getClass().getName(), body);

		HttpResponse response = webClient.post(url, body);
		return parseResponse(response, responseType);
	}

	public <T> void post(String url, T requestData) throws ClientProtocolException, IOException, WebError, KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, UnrecoverableKeyException, IllegalStateException
	{
		String body = gson.toJson(requestData);
		HttpResponse response = webClient.post(url, body);
		parseError(response);
	}

	@SuppressWarnings({ "rawtypes" })
	public <T, U> T put(String url, U requestData, Class responseType) throws ClientProtocolException, IOException, WebError, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, IllegalStateException
	{
		String body = gson.toJson(requestData);
		HttpResponse response = webClient.put(url, body);
		return parseResponse(response, responseType);
	}

	public <T> void put(String url, T requestData) throws ClientProtocolException, IOException, WebError, KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, UnrecoverableKeyException, IllegalStateException
	{
		String body = gson.toJson(requestData);
		HttpResponse response = webClient.put(url, body);
		parseError(response);
	}

	public void parseError(HttpResponse response) throws IllegalStateException, IOException
	{
		InputStream inputStream = response.getEntity().getContent();

		try
		{
			String data = Utilities.readStreamFully(inputStream);

			if (response.getStatusLine().getStatusCode() != 200)
			{
				// TODO
//				BreezyApiException error = (BreezyApiException) gson.fromJson(data, BreezyApiException.class);
//				error.setStatusCode(response.getStatusLine().getStatusCode());
//				throw error;
			}
		}
		finally
		{
			inputStream.close();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T parseResponse(HttpResponse response, Class responseType) throws IllegalStateException, IOException
	{
		T result;
		InputStream inputStream = response.getEntity().getContent();

		try
		{
			String data = Utilities.readStreamFully(inputStream);

			if (response.getStatusLine().getStatusCode() == 200)
			{
				result = (T) gson.fromJson(data, responseType);
			}
			else
			{
				// TODO: JS
			}
		}
		finally
		{
			inputStream.close();
		}

		// TODO: JS
		return null;
	}
}
