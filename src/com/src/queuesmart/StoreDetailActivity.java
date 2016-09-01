package com.src.queuesmart;


import com.src.utils.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.src.queuesmart.Utils;

import android.os.AsyncTask;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.Button;

public class StoreDetailActivity extends FragmentActivity {

	// Declare
	private LinearLayout slidingPanel;
	private boolean isExpanded;
	private DisplayMetrics metrics;
	private RelativeLayout headerPanel;
	private int panelWidth;
	private int panelWidth1;
	private String serverURL;
	String userId , storeId;
	static InputStream is = null;
	static JSONObject jObj = null;
	String json;
	String data ="",strMessage; 
    int sizeData = 0;
    AlertDialog.Builder builder;
    AlertDialog.Builder mSignSelectDialog , mSigleDialog;
	private ImageView menuRightButton;
	Button btnApply;
	TextView txtStoreName,txtCompanyName,txtWaitingTime,txtNextNumber,txtCategoryName,txtCityName,txtCreatedAt,txtWebDetail;
	TextView txtQueue;
	String strStoreName , strCompanyName , strWaitingTime , strNextNumber , strCategoryName , strCityName , strWebDetail;
	String strCreatedAt,strAddress ,strTotalCustomer,strWaitiedTime,strCurrentNumber,strCompanyLogo,strQueue;
	
	ImageView menuGoToNext;
	
	
	Button btnSearch;
	
	ImageView imgGoToAdd;
	
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters;
	LinearLayout.LayoutParams listViewParameters;
	
	ArrayList<String> projectIdNow = new ArrayList<String>();
	ArrayList<String> projectIdExpired = new ArrayList<String>();
	Global globalInfo;
	SessionManager session;
	WebView browser;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		session =  new SessionManager(getApplicationContext());
		session.checkLogin();
		globalInfo = (Global) getApplication();
		if(globalInfo.getUserId().toString().equals("")){
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
			finish();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_detail);
		
		builder = new AlertDialog.Builder(this);
		mSignSelectDialog = new AlertDialog.Builder(this);
		mSigleDialog = new AlertDialog.Builder(this);
		txtStoreName = (TextView) findViewById(R.id.txtStoreNameDetail);
		txtCompanyName = (TextView) findViewById(R.id.txtCompanyNameDetail);
		txtWaitingTime = (TextView) findViewById(R.id.txtWaitingTime);
		txtNextNumber = (TextView) findViewById(R.id.txtNextNumber);
		txtCategoryName = (TextView) findViewById(R.id.txtCategoryName);
		txtCityName = (TextView) findViewById(R.id.txtCityName);
		txtQueue = (TextView) findViewById(R.id.txtQueue);
		btnApply = (Button) findViewById(R.id.btnApply);
		
		menuGoToNext = (ImageView) findViewById(R.id.menuGoToNext);
		menuGoToNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StoreDetailActivity.this , StoreDetailNextActivity.class);
				startActivity(intent);
			}
		});
		
		browser = (WebView) findViewById(R.id.webview);
        browser.getSettings().setJavaScriptEnabled(true);
        
		String user_id = globalInfo.getUserId().toString();
		storeId =  globalInfo.getStoreId();
		
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		panelWidth = (int) ((metrics.widthPixels) * -0.75);
		panelWidth1 = (int) ((metrics.widthPixels) * 0.75);

		headerPanel = (RelativeLayout) findViewById(R.id.header);
		headerPanelParameters = (LinearLayout.LayoutParams) headerPanel.getLayoutParams();
		headerPanelParameters.width = metrics.widthPixels;
		headerPanel.setLayoutParams(headerPanelParameters);

		slidingPanel = (LinearLayout) findViewById(R.id.slidingPanel);
		slidingPanelParameters = (FrameLayout.LayoutParams) slidingPanel.getLayoutParams();
		slidingPanelParameters.width = metrics.widthPixels;
		slidingPanel.setLayoutParams(slidingPanelParameters);
		
		
		btnApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userId = globalInfo.getUserId();
				storeId = globalInfo.getStoreId();
				setDisableAllview();
				new ServiceApply().execute(userId,storeId);
			}
		});
		mSigleDialog = new AlertDialog.Builder(this);
		// Slide the Panel
		menuRightButton = (ImageView) findViewById(R.id.menuViewButton);
		menuRightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
				finish();
			}
		});
		
		init();
					    
	    
	}
   public void init(){
	  
	   new ServiceStoreDetail().execute(storeId);
   }
   public void setEnableAllView(){
	   FrameLayout layout = (FrameLayout) findViewById(R.id.frameLayout);
		for (int i = 0; i < layout.getChildCount(); i++) {
		    View child = layout.getChildAt(i);
		    child.setEnabled(true);
		}
	}
	public void setDisableAllview(){
		FrameLayout layout = (FrameLayout) findViewById(R.id.frameLayout);
		for (int i = 0; i < layout.getChildCount(); i++) {
		    View child = layout.getChildAt(i);
		    child.setEnabled(false);
		}
	}    
	private void runThread(final URL url) {
		new Thread() {
   		public void run() {
                      
                   	   Bitmap bitmap = null;
							try {
								bitmap = BitmapFactory.decodeStream(url.openStream());
								globalInfo.setBmpLogo(bitmap);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}catch(Exception e){
								e.printStackTrace();
							}
                      }
           
       }.start();
   }
   
   
	
   
   class ServiceStoreDetail extends AsyncTask<String, String, Object> {

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			serverURL = Constants.Server_Domain + Constants.Server_Store_Detail;
			/* Check the validation */
			storeId = params[0];
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("user_id", globalInfo.getUserId().toString());
				jsonObj.put("store_id", storeId.trim());
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
				AlertDialogManager alert = new AlertDialogManager();
	            alert.showAlertDialog(StoreDetailActivity.this, Constants.messageConnectionTitle, Constants.messageConnectionFailed, false);
	            return;
	        }
			JSONObject jsonObj = (JSONObject) result;
			try {
				if ( jsonObj.getString("result").equals("success")) {
					try {
						strStoreName = jsonObj.getString("store_name");
						strCompanyName = jsonObj.getString("company_name");
						strWaitingTime = jsonObj.getString("estimated_waiting");
						strNextNumber = jsonObj.getString("next_number");
						strCategoryName = jsonObj.getString("category_name");
						strCityName = jsonObj.getString("city_name");
						strWebDetail = jsonObj.getString("description");
						strQueue = jsonObj.getString("queue_no");
						
						if(strQueue.equals("null")){
							btnApply.setVisibility(View.VISIBLE);
							txtQueue.setVisibility(View.INVISIBLE);
						}else{
							txtQueue.setVisibility(View.VISIBLE);
							txtQueue.setText(strQueue);
							btnApply.setVisibility(View.INVISIBLE);
						}
						
						txtStoreName.setText(strStoreName);
						txtCompanyName.setText(strCompanyName);
						txtWaitingTime.setText(strWaitingTime);
						txtNextNumber.setText(strNextNumber);;
						txtCategoryName.setText(strCategoryName);
						txtCityName.setText(strCityName);
						browser.loadData(strWebDetail, "text/html", "UTF-8");
						browser.setBackgroundColor(Color.TRANSPARENT);
						
						strCreatedAt  = jsonObj.getString("company_created_at");
						strAddress	= jsonObj.getString("address");
						strTotalCustomer = jsonObj.getString("total_customers_served");
						strCurrentNumber = jsonObj.getString("current_number");
						strNextNumber = jsonObj.getString("next_number");
						strCompanyLogo = jsonObj.getString("company_logo");
						
						globalInfo.setCreatedAt(strCreatedAt);
						globalInfo.setAddress(strAddress);
						globalInfo.setTotalCustomer(strTotalCustomer);
						globalInfo.setCurrentNumber(strCurrentNumber);
						globalInfo.setNextNumber(strNextNumber);
						globalInfo.setWaitingTime(strWaitingTime);
						globalInfo.setStoreName(strStoreName);
						globalInfo.setCompanyLogo(strCompanyLogo);
						
						URL url = null;
						   try {
					    	   url = new URL(strCompanyLogo.toString());
					    	   runThread(url);
					       } catch (Exception e) {
					           Log.e("Error", e.getMessage());
					           e.printStackTrace();
					           
					       }
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}else
				{
					try {
							String errorMessage = jsonObj.get("msg").toString();
							AlertDialogManager alert = new AlertDialogManager();
							alert.showAlertDialog(StoreDetailActivity.this, "Failed", errorMessage, false);
						
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			} catch (JSONException e) {
				AlertDialogManager alert = new AlertDialogManager();
				alert.showAlertDialog(StoreDetailActivity.this, "failed", Constants.loginFailed, false);
			}
			
		}
   }
   class ServiceApply extends AsyncTask<String, String, Object> {
   	
		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			userId = params[0];
			storeId = params[1];
			
			serverURL = Constants.Server_Domain + Constants.Server_Store_Apply;
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("user_id", userId.trim());
				jsonObj.put("store_id", storeId.trim());
				
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
	    		setEnableAllView();
			}	
			JSONObject jsonObj = (JSONObject) result;
			try {
				if ( jsonObj.getString("result").equals("success")) {
					mSigleDialog.setMessage(jsonObj.getString("msg"));
					mSigleDialog.setCancelable(true);
					mSigleDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			                
			                
			            }
			        });
					AlertDialog alert = mSigleDialog.create();
					alert.show();
				}else
				{
						try {
							String errorMessage = jsonObj.get("msg").toString();
							mSigleDialog.setMessage(errorMessage);
							mSigleDialog.setCancelable(true);
							mSigleDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int id) {
					                dialog.cancel();
					            }
					        });
							mSigleDialog.show();
							
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
