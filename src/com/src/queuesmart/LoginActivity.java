package com.src.queuesmart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;


import com.src.queuesmart.SessionManager;
import com.src.queuesmart.LoginActivity.ServiceTask;

import android.view.View.OnTouchListener;
public class LoginActivity extends Activity {
	private static RelativeLayout relBtn;
	static InputStream is = null;
	static JSONObject jObj = null;
	
	static String json;
	String data =""; 
    int sizeData = 0;
    String serverURL = Constants.Server_Domain + Constants.Server_Sign_Url;
    
    static EditText editEmail;
    static EditText editPassword;
    
    static RelativeLayout rlNewHere ;
    static AlertDialog messageBox;
    static RelativeLayout loginLayout;
    
    String userEmail;
    String userPass;
    
    static AlertDialog.Builder builder;
    ImageView imgGif;
    SessionManager session;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	      
		/* Define Control variables */
		builder = new AlertDialog.Builder(this);
		
		editEmail = (EditText) findViewById(R.id.edit_email);
		editPassword = (EditText) findViewById(R.id.edit_password);
		imgGif = (ImageView) findViewById(R.id.imgGif);
		session = new SessionManager(getApplicationContext()); 
		
		relBtn = (RelativeLayout)findViewById(R.id.relBtn);
		loginLayout = (RelativeLayout)findViewById(R.id.loginLayout);
		
		relBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					
					userEmail = editEmail.getText().toString();
					userPass = editPassword.getText().toString();
			        //call login service
	                if(userPass.equals("") || userEmail.equals("")){
		        		 builder.setMessage(Constants.messageWarranty);
		        		 builder.setCancelable(true);
		        		 messageBox = builder.create();
		        		 messageBox.show();
		        	 }else{
		        		 
		        		 imgGif.setVisibility(View.VISIBLE);
		        		 setDisableAllview();
		        		 Utils.hideSoftKeyboard(LoginActivity.this);
		        		 
		        		 new ServiceTask().execute(userEmail, userPass);
		        	 }
			}
		});
		
		builder = new AlertDialog.Builder(this);
		
		rlNewHere = (RelativeLayout) findViewById(R.id.rlNewHere);
		loginLayout = (RelativeLayout) findViewById(R.id.loginLayout);
		rlNewHere.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(LoginActivity.this , SignUpActivity.class);
				startActivity(intent);
			}
		});
		loginLayout.setOnTouchListener(new OnTouchListener() {
		    
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Utils.hideSoftKeyboard(LoginActivity.this);
				return false;
			}
		});
		editPassword.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		    	if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;
		        if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
		          // Perform action on key press
		        	userEmail = editEmail.getText().toString();
	                userPass = editPassword.getText().toString();
			        //call login service
	                if(!Utils.isValidEmailAddress(userEmail)|| userPass.equals("")){
		        		 builder.setMessage(Constants.messageWarranty);
		        		 builder.setCancelable(true);
		        		 messageBox =  builder.create();
		        		 messageBox.show();
		        	 }else{
		        		 ImageView imgGif = (ImageView) findViewById(R.id.imgGif);
		        		 imgGif.setVisibility(View.VISIBLE);
		        		 Utils.hideSoftKeyboard(LoginActivity.this);
		        		 setDisableAllview();
		        		 new ServiceTask().execute(userEmail, userPass);
		        		 
		        	 }
	              return true;
		        }
		        return false;
		    }
		});
		RelativeLayout rlNewHere = (RelativeLayout) findViewById(R.id.rlSignIn);
		rlNewHere.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(LoginActivity.this , LoginActivity.class);
				intent.putExtra("contactPhone", ""); 
				startActivity(intent);
			}
		});
		
		builder.setMessage(Constants.messageLogin);
		builder.setCancelable(true);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
		messageBox = builder.create();
        init_globalInfo();
	}
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {
	     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
	     return true;
	     }
	     return super.onKeyDown(keyCode, event);    
	}
   
   
   
    public void init_globalInfo(){
    	Global globalInfo = (Global)getApplication();
    	globalInfo.setName("");
    	globalInfo.setEmail("");
    	
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
   
    
   class ServiceTask extends AsyncTask<String, String, Object> {

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String userEmail = params[0];
			String userPass = params[1];
			/* Check the validation */
			if (userEmail == null || userPass.equals("") || 
					userPass == null || userPass.equals("")) {
			}
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("email", userEmail.trim());
				jsonObj.put("password", userPass.trim());
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
	    		
	            messageBox = builder.create();
	            messageBox.show();
	            imgGif.setVisibility(View.INVISIBLE);
	            setEnableAllView();
	            return;
			}
			JSONObject jsonObj = (JSONObject) result;
			Global globalInfo = (Global)getApplication();
			String user_id = "";
			String email = "";
			try {
				if ( jsonObj.getString("result").equals("success")) {
					try {
						user_id =  jsonObj.getString("user_id");
						globalInfo.setUserId(user_id);
						session.createLoginSession(user_id , user_id);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Intent i = new Intent(LoginActivity.this, SearchActivity.class);
					globalInfo.setUserId(user_id);
					i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
					
				}else
				{
						try {
							String errorMessage = jsonObj.get("msg").toString();
							AlertDialogManager alert = new AlertDialogManager();
				            alert.showAlertDialog(LoginActivity.this, "Failed", errorMessage, false);
							imgGif.setVisibility(View.INVISIBLE);
							setEnableAllView();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			} catch (JSONException e) {
				AlertDialogManager alert = new AlertDialogManager();
				alert.showAlertDialog(LoginActivity.this, "failed", Constants.loginFailed, false);
				setEnableAllView();
			}
				  
			}
		}

}


