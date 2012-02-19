package com.skeletonapp.android.util;

import org.apache.http.HttpResponse;

public class WebError extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1414435263725055095L;
	private HttpResponse response;

	public void setResponse(HttpResponse response) {
		this.response = response;
	}
	public HttpResponse getResponse() {
		return response;
	}
	
	
}
