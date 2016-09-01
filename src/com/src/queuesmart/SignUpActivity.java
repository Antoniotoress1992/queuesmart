package com.src.queuesmart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.src.queuesmart.Constants;


public class SignUpActivity extends Activity{
	static RelativeLayout relSignUp; 
	EditText editUserName , editUserEmail , editUserPhone , editUserCountry , editUserPassword , editUserConfirm, editUserAddress;
	static InputStream is = null;
	static JSONObject jObj = null;
	String json;
	String data =""; 
	String serverURL = "" ;
    int sizeData = 0;
    String[] country;
    String[] countryId;
    AlertDialog.Builder builder;
    String countrySelectedId;
    RelativeLayout btnSignUp , btnSignIn , rlCountry;
    AlertDialog.Builder mSignSelectDialog , mSigleDialogSignUp;
    Button btnUserCountry;
    RelativeLayout rlSignUp;
    String strUserName , strUserEmail , strUserPhone , strUserCountry , strUserPassword , strUserConfirm , strMessage , strUserAddress;
    Global globalInfo;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		globalInfo = (Global) getApplication();
		builder = new AlertDialog.Builder(this);
		
		editUserName = (EditText) findViewById(R.id.edit_user_name);
		editUserEmail = (EditText) findViewById(R.id.edit_user_email);
		editUserPhone = (EditText) findViewById(R.id.edit_user_phone);
		editUserCountry = (EditText) findViewById(R.id.edit_user_country);
		editUserPassword = (EditText) findViewById(R.id.edit_user_password);
		editUserConfirm = (EditText) findViewById(R.id.edit_user_confirm);
		editUserAddress = (EditText) findViewById(R.id.edit_user_adress);
		rlCountry = (RelativeLayout ) findViewById(R.id.layout_country);
		
		btnSignIn = (RelativeLayout) findViewById(R.id.relSignIn); 
		relSignUp = (RelativeLayout) findViewById(R.id.relSignUpButton);
		mSignSelectDialog = new AlertDialog.Builder(this);
		mSigleDialogSignUp = new AlertDialog.Builder(this);
		rlSignUp = (RelativeLayout)findViewById(R.id.rlSignUp);
		
		rlSignUp.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Utils.hideSoftKeyboard(SignUpActivity.this);
				return false;
			}
		});
		relSignUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setData();
				if(!validationCheck()){
	        		AlertDialogManager alertError = new AlertDialogManager();
	        		alertError.showAlertDialog(SignUpActivity.this, "Input Error", strMessage , false);
	        	}else{
	        		new ServiceTask().execute(strUserName, strUserEmail ,strUserPhone ,countrySelectedId,strUserPassword, strUserAddress);
				}
			}
		});
		
		btnSignIn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		
		new ServiceCity().execute();
		mSignSelectDialog.setTitle("City Selection");
		mSigleDialogSignUp.setTitle("Sign Up");
		
		mSignSelectDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		@Override
			   public void onClick(DialogInterface dialog, int which) {
			   // TODO Auto-generated method stub
			   }
		});
		
		mSigleDialogSignUp.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
				   public void onClick(DialogInterface dialog, int which) {
				   // TODO Auto-generated method stub
				   }
		});
		editUserCountry.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					AlertDialog alert = mSignSelectDialog.create();
					alert.show();
				}
		});
		editUserPhone.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		    	if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;
		        // If the event is a key-down event on the "enter" button
		        if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
		          // Perform action on key press
		        	editUserPassword.requestFocus();
	              return true;
		        }
		        return false;
		    }
		});
		editUserEmail.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		    	if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;
		        // If the event is a key-down event on the "enter" button
		        if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
		          // Perform action on key press
		        	editUserPhone.requestFocus();
	              return true;
		        }
		        return false;
		    }
		});
		
		editUserPassword.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		    	if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;
		        // If the event is a key-down event on the "enter" button
		        if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
		          // Perform action on key press
		        	editUserConfirm.requestFocus();
	              return true;
		        }
		        return false;
		    }
		});
		editUserConfirm.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		    	if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;
		        // If the event is a key-down event on the "enter" button
		        if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
		          // Perform action on key press
		        	setData();
		        	if(!validationCheck()){
		        		AlertDialogManager alert = new AlertDialogManager();
		        		alert.showAlertDialog(SignUpActivity.this, "Input Error", strMessage , false);
		        	}else{
		        		setDisableAllview();
		        		new ServiceTask().execute(strUserName, strUserEmail ,strUserPhone ,countrySelectedId,strUserPassword, strUserAddress);
					}
	              return true;
		        }
		        return false;
		    }
		});
		
	}

	 public void setEnableAllView(){
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.frameLayout);
			for (int i = 0; i < layout.getChildCount(); i++) {
			    View child = layout.getChildAt(i);
			    child.setEnabled(true);
			}
		}
	public void setDisableAllview(){
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.frameLayout);
			for (int i = 0; i < layout.getChildCount(); i++) {
			    View child = layout.getChildAt(i);
			    child.setEnabled(false);
			}
	}    
	public void setData(){
		strUserName = editUserName.getText().toString();
		strUserEmail = editUserEmail.getText().toString();
		strUserPhone = editUserPhone.getText().toString();
		strUserCountry = editUserCountry.getText().toString();
		strUserPassword = editUserPassword.getText().toString();
		strUserConfirm = editUserConfirm.getText().toString();
		strUserAddress = editUserAddress.getText().toString();
	}
	
	public Boolean  validationCheck(){
			if(strUserName.equals("")){strMessage = "Please input username correctly";return false;}
			if(!Utils.isValidEmailAddress(strUserEmail)){strMessage = "Please input email correctly";return false;}
			Utils.PhoneNumberValidator phoneCheck = new Utils.PhoneNumberValidator();
			if(!phoneCheck.isValidPhoneNumber(strUserPhone)){
				strMessage = "Please input phonenumber correctly";
				return false;}
			strUserPhone  = strUserPhone.replaceAll("\\D+","");
			if(strUserCountry.equals("")){strMessage = "Please input country correctly";return false;}
			if(strUserAddress.equals("")){strMessage = "Please input Address correctly";return false;}
			if(strUserPassword.equals("") || strUserConfirm.equals("") ||  !strUserPassword.equals(strUserConfirm) ){strMessage = "Please input password correctly";return false;}
		return true;
	}
	
	
	class ServiceCity extends AsyncTask<String, String, Object> {

		@Override
		protected Object doInBackground(String... params) {
			JSONObject jsonObj = new JSONObject();
			serverURL = Constants.Server_Domain+Constants.Server_City_List;
			JSONObject result = Utils.getJSONFromPost(serverURL, jsonObj);
			return result;
		}
    	@Override
		protected void onPostExecute(Object result) {
    		if(result == null){
				builder.setMessage(Constants.messageConnectionFailed);
	    		builder.setCancelable(true);
	    		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	                }
	            });
	    		return;
    		}
			JSONObject jsonObj = (JSONObject) result;
			String strID = "";
			String strName = "";
			String strMsg = "";
			int countJsonData = 0;
			
			try {
				if ( jsonObj.getString("result").equals("success")) {
					try {
						countJsonData = jsonObj.getJSONArray("cities").length();
						country = new String[countJsonData];
						countryId = new String[countJsonData];
						JSONObject objectProject = null;
						for(int i = 0 ; i < countJsonData;i++){
							
							objectProject = jsonObj.getJSONArray("cities").getJSONObject(i);
							strName = objectProject.getString("name");
							strID = objectProject.getString("id");
							country[i] = strName ;
							countryId[i] = strID;	
						}
						mSignSelectDialog.setSingleChoiceItems(country, -1, new DialogInterface.OnClickListener() {
						    @Override
							public void onClick(DialogInterface dialog, int which) {
							     // TODO Auto-generated method stub
							     int nIndex = which;
							     countrySelectedId = countryId[nIndex];
							     editUserCountry.setText(country[nIndex]);
							    }
						});
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						strMsg =  jsonObj.getString("msg");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					
				}else
				{
						try {
							String errorMessage = jsonObj.get("msg").toString();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				  
			}
	}
    class ServiceTask extends AsyncTask<String, String, Object> {
    	
		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			String userName = params[0];
			String userEmail = params[1];
			String userPhone = params[2];
			String userCountry = params[3];
			String userPass = params[4];
			String userAddress = params[5];
			serverURL = Constants.Server_Domain + Constants.Server_SignUp_Url;
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("name", userName.trim());
				jsonObj.put("password", userPass.trim());
				jsonObj.put("email", userEmail.trim());
				jsonObj.put("phone", userPhone.trim());
				jsonObj.put("city_id", userCountry.trim());
				jsonObj.put("address", userAddress.trim());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONObject result = Utils.getJSONFromPost(serverURL, jsonObj);
			return result;
		}
    	@Override
		protected void onPostExecute(Object result) {
			
    		if(result == null){
				builder.setMessage(Constants.messageConnectionFailed);
	    		builder.setCancelable(true);
	    		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	                }
	            });
			}	
			JSONObject jsonObj = (JSONObject) result;
			try {
				if ( jsonObj.getString("result").equals("success")) {
					mSigleDialogSignUp.setMessage(Constants.messageSubmitSignUpSuccess);
					mSigleDialogSignUp.setCancelable(true);
					mSigleDialogSignUp.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
			                startActivity(intent);
			                
			                finish();
			                
			            }
			        });
					AlertDialog alert = mSigleDialogSignUp.create();
					alert.show();
				}else
				{
						try {
							String errorMessage = jsonObj.get("msg").toString();
							mSigleDialogSignUp.setMessage(Constants.messageSubmitSignUpSuccess);
							mSigleDialogSignUp.setCancelable(true);
							mSigleDialogSignUp.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int id) {
					                dialog.cancel();
					               
					            }
					        });
							
							mSignSelectDialog.show();
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setEnableAllView();	  
		}
	}
	
    
}
