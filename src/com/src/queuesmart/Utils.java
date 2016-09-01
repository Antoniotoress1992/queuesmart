package com.src.queuesmart;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
 
public class Utils {
    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     *               - pass null if you don't want icon
     * */
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	
	public static boolean isNumeric(String str)  
	    {  
	      try  
	      {  
	        double d = Double.parseDouble(str);  
	      }  
	      catch(NumberFormatException nfe)  
	      {  
	        return false;  
	      }  
	      return true;  
	}
	public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
	}
	
	public static class PhoneNumberValidator {

	    private Pattern VALID_PHONE_NUMBER = Pattern.compile("^[0-9.()-]{10,25}$");

	    public boolean isValidPhoneNumber(String s) {
	    	 Matcher m = VALID_PHONE_NUMBER.matcher(s);
	    	 
	         return m.matches();
	    }
	}
	
	public static JSONObject getJSONFromPost(String url, JSONObject param) {
     	 
        // Making HTTP request
		InputStream is = null;
		JSONObject jObj = null;
		String json= null;
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
    	Iterator<String> keys = param.keys();
	    while(keys.hasNext()){
	        String key = keys.next();
	        String val = null;
	        try{
	             urlParameters.add(new BasicNameValuePair(key, param.getString(key)));
	        }catch(Exception e){
	        	val = "Error";
	        }
	    }
	    try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
        	HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("User-Agent", Constants.USER_AGENT);
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();  
        } catch (UnsupportedEncodingException e) {
        	return null;
        } catch (ClientProtocolException e) {
        	return null;
        } catch (IOException e) {
        	return null;
        } catch (Exception e) {
        	return null;
        }
         
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;
 
    }
 
}