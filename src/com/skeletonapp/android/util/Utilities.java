package com.skeletonapp.android.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import org.acra.util.Base64;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

public class Utilities
{
	private static final String EmailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
	private static Handler mainHandler;
	
	private static final SecureRandom random = new SecureRandom();

	public static void dispatchToMainThread(Runnable runnable)
	{
		if (mainHandler == null)
		{
			mainHandler = new Handler(Looper.getMainLooper());
		}

		mainHandler.post(runnable);
	}
	
	public static String encodeHTML(String s)
    {
        StringBuffer out = new StringBuffer();
        for(int i=0; i<s.length(); i++)
        {
            char c = s.charAt(i);
            if(c > 127 || c=='"' || c=='<' || c=='>')
            {
               out.append("&#"+(int)c+";");
            }
            else
            {
                out.append(c);
            }
        }
        return out.toString();
    }
	
	public static boolean isNetworkAvailable()
	{
//		ConnectivityManager connectivityManager = (ConnectivityManager)BreezyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//		
//		return networkInfo != null && networkInfo.isConnected();
		// TODO
		return true;
	}

	/**
	 * 
	 * @param stream
	 * @return
	 * @throws IOException
	 * @deprecated Use {@link Utilities#readStreamFully(InputStream)} instead
	 */
	@Deprecated
	public static String ReadStreamToEnd(InputStream stream) throws IOException
	{
		int read = 0;
		char[] buffer = new char[8000];
		StringBuffer stringBuffer = new StringBuffer();
		InputStreamReader reader = new InputStreamReader(stream);

		try
		{
			while (read != -1)
			{
				read = reader.read(buffer, 0, buffer.length);
				if (read > 0)
				{
					stringBuffer.append(buffer, 0, read);
				}
			}
		}
		finally
		{
			reader.close();
		}

		return stringBuffer.toString();
	}

	/**
	 * Reads the entire stream without double-buffering and returns its
	 * {@link String} representation. Note this method will not clean up the
	 * input stream when finished.
	 * 
	 * @param stream
	 *            The {@link InputStream} containing characters to read
	 * @return The fully read {@link String}
	 * @throws IOException
	 *             If the underlying {@link InputStreamReader} does
	 * @author Jeff Mixon
	 */
	public static String readStreamFully(InputStream stream) throws IOException
	{
		InputStreamReader reader = new InputStreamReader(stream);
		StringBuffer sb = new StringBuffer();
		try
		{
			int i = 0;

			while ((i = reader.read()) != -1)
			{
				sb.append((char) i);
			}
		}
		finally
		{
			reader.close();
		}

		return sb.toString();
	}
	
	public static byte[] readStreamDataFully(InputStream inputStream) throws IOException
	{
		// this dynamically extends to take the bytes you read
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

		// this is storage overwritten on each iteration with bytes
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		// we need to know how may bytes were read to write them to the byteBuffer
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}

		// and then we can return your byte array.
		return byteBuffer.toByteArray();
	}

	public static void notifyUser(Context context, String message)
	{
		// TODO: Fix. 
		String defaultTitle = "Alert";
		// String defaultTitle = context.getResources().getString(R.string.info_dialog_title);
		notifyUser(context, defaultTitle, message, null);
	}

	public static void notifyUser(Context context, String title, String message)
	{
		notifyUser(context, title, message, null);
	}
	
	public static void notifyUser(Context context, int messageId)
	{
		String message = context.getResources().getString(messageId);
		notifyUser(context, message);
	}
	
	public static void notifyUser(Context context, int titleId, int messageId) {
		notifyUser(context, titleId, messageId, null);
	}
	
	public static void notifyUser(Context context, int titleId, int messageId, final VoidOperationCallback callback)
	{
		String title = context.getResources().getString(titleId);
		String message = context.getResources().getString(messageId);
		notifyUser(context, title, message, callback);
	}

	public static AlertDialog notifyUser(Context context, String title, String message, final VoidOperationCallback callback)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(message).setTitle(title).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				if (callback != null)
				{
					callback.notifyCompleted();
				}
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
		return alert;
	}
	
	public static void promptUser(Context context, int titleId, int messageId, final String positiveButton, final String negativeButton, final OperationCallback<String> callback)
	{
		String title = context.getResources().getString(titleId);
		String message = context.getResources().getString(messageId);
		promptUser(context, title, message, positiveButton, negativeButton, callback);
	}
	
	public static void promptUser(Context context, String title, String message, final String positiveButton, final String negativeButton, final OperationCallback<String> callback)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		
		builder.setMessage(message).setTitle(title).setCancelable(false)
				.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which)
					{
						if (callback != null)
						{
							callback.notifyCompleted(positiveButton);
						}
					}
				}).setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (callback != null)
						{
							callback.notifyCompleted(negativeButton);
						}
					}
				});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void displayError(Context context, Exception exception) {
		// displayError(context, exception, null);
		// TODO
	}
	
//	public static void displayError(Context context, Exception exception, final VoidOperationCallback callback)
//	{
//		if(exception instanceof DisplayableException) {
//			notifyUser(context, R.string.error_dialog_title, ((DisplayableException)exception).getFriendlyMessageId(), callback);
//		}
//		else if(exception instanceof BreezyApiException) {
//			BreezyApiException apiException = (BreezyApiException) exception;
//			
//			String title = context.getResources().getString(R.string.error_dialog_title);
//			if(apiException.getErrorCode() == ErrorCodes.VALIDATION) {
//				String message = "";
//				ValidationError[] validationErrors = apiException.getValidationErrors();
//				
//				for(ValidationError validationError : validationErrors) {
//					 message += validationError.getMessage() + "\n";
//				}
//				
//				notifyUser(context, title, message, callback);
//			}
//			else {
//				notifyUser(context, title, apiException.getDescription(), callback);
//			}
//		}
//		else {
//			notifyUser(context, R.string.error_dialog_title, R.string.unexpected_error_message, callback);
//		}
//	}
	
	public static boolean isNullOrEmpty(String value) {
		return isNullOrEmpty(value, true);
	}
	
	public static boolean isNullOrEmpty(String value, boolean trim) {
		if(value != null) {
			return trim ? value.trim().equalsIgnoreCase("") : value.equalsIgnoreCase("");
		}
		
		return true;
	}
	
	public static boolean isValidEmailAddress(String value) {
		return value.toLowerCase().matches(EmailRegex);
	}

	public static int calculatePixelDisplayFactor(Context c, double dp)
	{
		final float scale = c.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	   /**
     * Generates a PublicKey instance from a string containing the
     * Base64-encoded public key.
     *
     * @param encodedPublicKey Base64-encoded public key
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
     * @throws IllegalArgumentException if encodedPublicKey is invalid
     */
    public static PublicKey generatePublicKey(String encodedPublicKey, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException 
    {
            byte[] decodedKey = Base64.decode(encodedPublicKey,0);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));

    }
	
    /**
     * Verifies that the signature from the server matches the computed
     * signature on the data.  Returns true if the data is correctly signed.
     *
     * @param publicKey public key associated with the developer account
     * @param signedData signed data from server
     * @param signature server signature
     * @return true if the data and signature match
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     * @throws SignatureException 
     */
    public static boolean verifySignedData(PublicKey publicKey, String signedData, String signature, String sigAlgorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig;
            sig = Signature.getInstance(sigAlgorithm);
            sig.initVerify(publicKey);
            sig.update(signedData.getBytes());
            if (!sig.verify(Base64.decode(signature, 0))) {

                return false;
            }
            else
            	return true;
    }
    

    public static long generateNonce() 
    {
    	
        long nonce = random.nextLong();
        return nonce;
    }
    
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
       return resolveInfo.size() > 0;

    }
    
    public static String getFileExtension(Uri uri) {
		String filename = uri.getLastPathSegment();
        return getFileExtension(filename);
	}
    
    public static String getFileExtension(File file) {
		String filename = file.getName();
        return getFileExtension(filename);
	}
    
    public static String getFileExtension(String filename) {
    	String filenameArray[] = filename.split("\\.");
        return filenameArray.length > 1 ? filenameArray[filenameArray.length-1] : "";
    }
    
	public static String getUriDisplayName(Activity a, Uri uri) {
		String name = "Unknown";
		
		String scheme = uri.getScheme();
		if(scheme.equalsIgnoreCase("content")) {
			String[] proj = { MediaStore.MediaColumns.DISPLAY_NAME };
	        Cursor cursor = a.managedQuery(uri, proj, null, null, null);
	        cursor.moveToFirst();
	        
	        int column_index = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
	        if(column_index != -1) {
	        	name = cursor.getString(column_index);
	        }	
		}
		else {
			name = uri.getLastPathSegment();
		}
        
        return name;
	}
	
	public static String getUriMimeType(Activity a, Uri uri) {
		String mimeType = "Unknown";
		
		String scheme = uri.getScheme();
		if(scheme.equalsIgnoreCase("content")) {
			String[] proj = { MediaStore.MediaColumns.MIME_TYPE };
	        Cursor cursor = a.managedQuery(uri, proj, null, null, null);
	        cursor.moveToFirst();
	        
	        int column_index = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE);
	        if(column_index != -1) {
	        	mimeType = cursor.getString(column_index);
	        }
	        else {
	        	String extension = Utilities.getFileExtension(getUriDisplayName(a,uri));
				if(extension != "") {
					mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
				}
	        }
		}
		else {
			String extension = Utilities.getFileExtension(uri);
			mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		}
        
        return mimeType;
	}
	
	public static File writeStringToFile(String text, File dir) throws IOException
	{
		File tmp = File.createTempFile("breezytmp", ".txt", dir);
		
		FileOutputStream fos = null;
		
		try
		{
			fos = new FileOutputStream(tmp);
			fos.write(text.getBytes());
			fos.flush();
			
			return tmp;
		}
		finally
		{
			if (fos != null)
			{
				fos.close();
			}

		}
	}
}
