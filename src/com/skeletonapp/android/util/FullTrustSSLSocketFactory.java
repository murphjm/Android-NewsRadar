package com.skeletonapp.android.util;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FullTrustSSLSocketFactory extends org.apache.http.conn.ssl.SSLSocketFactory
{
	private SSLSocketFactory FACTORY = HttpsURLConnection.getDefaultSSLSocketFactory ();

	public FullTrustSSLSocketFactory () throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException
	    {
	    super(null);
	    try
	        {
	        SSLContext context = SSLContext.getInstance ("TLS");
	        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[] {};
                }

                public void checkClientTrusted(X509Certificate[] chain,
                                String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                String authType) throws CertificateException {
                }
	        } };
	        context.init (null, trustAllCerts, new SecureRandom ());

	        FACTORY = context.getSocketFactory ();
	        }
	    catch (Exception e)
	        {
	        e.printStackTrace();
	        }
	    }

	@Override
	public Socket createSocket() throws IOException
	{
	    return FACTORY.createSocket();
	}
	
	@Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                    throws IOException, UnknownHostException {
            return FACTORY.createSocket(socket, host, port, autoClose);
    }

	 // TODO: add other methods like createSocket() and getDefaultCipherSuites().
	 // Hint: they all just make a call to member FACTORY 
}
