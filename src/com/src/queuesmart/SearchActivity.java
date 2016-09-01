package com.src.queuesmart;


import com.src.utils.*;

import java.io.InputStream;
import java.util.ArrayList;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import android.widget.Button;

public class SearchActivity extends FragmentActivity {

	// Declare
	private LinearLayout slidingPanel;
	private boolean isExpanded;
	private DisplayMetrics metrics;
	private RelativeLayout headerPanel;
	private int panelWidth;
	private int panelWidth1;
	private String serverURL;
	private CheckBox chkTime;
	TextView txtFirstRange , txtSecondRange;
	
	static InputStream is = null;
	static JSONObject jObj = null;
	String json;
	String data ="",strMessage; 
    int sizeData = 0;
	private ImageView menuRightButton;
	AlertDialog.Builder builder;
	String[] city;
	String[] cityId;
	String[] category;
	String[] categoryId;
	String citySelectedId , categorySelectedId;
	
	EditText editStoreName, editCompanyName , editCategoryName , editCityName;
	String strStoreName, strCompanyName , strMin , strMax , strChecked;
	
	AlertDialog.Builder mSelectCityDialog , mSelectCategoryDialog , mSigleDialog;
	
	Button btnSearch;
	
	ImageView imgGoToAdd;
	
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters;
	LinearLayout.LayoutParams listViewParameters;
	Bundle extras;
	
	RelativeLayout rlWrapper;
	
	ArrayList<String> projectIdNow = new ArrayList<String>();
	ArrayList<String> projectIdExpired = new ArrayList<String>();
	Global globalInfo;
	SessionManager session;
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
		setContentView(R.layout.search);
		
		builder = new AlertDialog.Builder(this);
		String user_id = globalInfo.getUserId().toString();
		
		
		// Initialize
		txtFirstRange = (TextView) findViewById(R.id.txtFirstRange);
		txtSecondRange = (TextView) findViewById(R.id.txtSecondRange);
		
		
		
		editStoreName = (EditText) findViewById(R.id.editStoreName);
		editCompanyName = (EditText) findViewById(R.id.editCompanyName);
		editCategoryName = (EditText) findViewById(R.id.editCategoryName);
		editCityName = (EditText) findViewById(R.id.editCityName);
		
		chkTime = (CheckBox)findViewById(R.id.chkTime);
		
		
		btnSearch = (Button) findViewById(R.id.btnSearch);
		
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		panelWidth = (int) ((metrics.widthPixels) * -0.75);
		panelWidth1 = (int) ((metrics.widthPixels) * 0.75);

		headerPanel = (RelativeLayout) findViewById(R.id.header);
		headerPanelParameters = (LinearLayout.LayoutParams) headerPanel.getLayoutParams();
		headerPanelParameters.width = metrics.widthPixels;
		headerPanel.setLayoutParams(headerPanelParameters);
		
		rlWrapper = (RelativeLayout) findViewById(R.id.wrapper);

		slidingPanel = (LinearLayout) findViewById(R.id.slidingPanel);
		slidingPanelParameters = (FrameLayout.LayoutParams) slidingPanel.getLayoutParams();
		slidingPanelParameters.width = metrics.widthPixels;
		slidingPanel.setLayoutParams(slidingPanelParameters);
		
		mSelectCityDialog = new AlertDialog.Builder(this);
		mSelectCategoryDialog = new AlertDialog.Builder(this);
		mSigleDialog = new AlertDialog.Builder(this);
		
		chkTime.setChecked(true);
		// Slide the Panel
		menuRightButton = (ImageView) findViewById(R.id.menuViewButton);
		menuRightButton.setOnClickListener(new OnClickListener() {
	   
			@Override
			public void onClick(View v) {
				if (!isExpanded) {
					isExpanded = true;
					// Expand

					//FragmentManager fragmentManager = getFragmentManager();
					FragmentManager fragmentManager = getSupportFragmentManager();

					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.menuPanel,	new LeftMenuFragment());
					fragmentTransaction.commit();
					
					new ExpandAnimation(slidingPanel, panelWidth1,
							Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);

				} else {
					isExpanded = false;
					// Collapse
					
					new CollapseAnimation(slidingPanel, panelWidth1,
							TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
							TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f,
							0, 0.0f);

				}
			}
		});
		
		rlWrapper.setOnTouchListener(new OnTouchListener() {
		    
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Utils.hideSoftKeyboard(SearchActivity.this);
				return false;
			}
		});
		editCityName.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				AlertDialog alert = mSelectCityDialog.create();
				alert.show();
			}
		});
		editCategoryName.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				AlertDialog alert = mSelectCategoryDialog.create();
				alert.show();
			}
		});
		mSelectCityDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
				   public void onClick(DialogInterface dialog, int which) {
				   // TODO Auto-generated method stub
				   }
		});
		mSelectCategoryDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
				   public void onClick(DialogInterface dialog, int which) {
				   // TODO Auto-generated method stub
				   }
		});
		btnSearch.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				setData();
				setDisableAllview();
				btnSearch.setEnabled(false);
				new ServiceStoreSearch().execute(strStoreName, strCompanyName , categorySelectedId , citySelectedId ,
						strMin , strMax , strChecked);
				
			}
		});
		RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(this , SearchActivity.this);
	        // Set the range
		rangeSeekBar.setRangeValues(0, 60);
	    rangeSeekBar.setSelectedMinValue(0);
	    rangeSeekBar.setSelectedMaxValue(60);

	        // Add to layout
	    RelativeLayout layout = (RelativeLayout) findViewById(R.id.rlSeekSlider);
	    layout.addView(rangeSeekBar);
	    
	    init();
	}
   public void init(){
	   setDisableAllview();
	   new ServiceCity().execute();
	   new ServiceCategory().execute();
	   strChecked = "Y";
   }
   public void setTextMin(String minText){
	   txtFirstRange.setText(minText);
   }   
   public void setTextMax(String maxText){
	   txtSecondRange.setText(maxText);
   } 
   public void itemClicked(View v) {
       //code to check if this checkbox is checked!
       CheckBox checkBox = (CheckBox)v;
       if(checkBox.isChecked()){
    	   strChecked = "Y";
       }else{
    	   strChecked = "N";
       }
   }
   public void setData(){
	   	strStoreName = editStoreName.getText().toString();
	   	strCompanyName  = editCompanyName.getText().toString();
	   	strMin  = txtFirstRange.getText().toString();
	   	strMax  = txtSecondRange.getText().toString();
	   	
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
   			AlertDialogManager alert = new AlertDialogManager();
	            alert.showAlertDialog(SearchActivity.this, Constants.messageConnectionTitle, Constants.messageConnectionFailed, false);
	            setEnableAllView();
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
						city = new String[countJsonData];
						cityId = new String[countJsonData];
						JSONObject objectProject = null;
						for(int i = 0 ; i < countJsonData;i++){
							
							objectProject = jsonObj.getJSONArray("cities").getJSONObject(i);
							strName = objectProject.getString("name");
							strID = objectProject.getString("id");
							city[i] = strName ;
							cityId[i] = strID;	
						}
						mSelectCityDialog.setTitle("City Selection");
						mSelectCityDialog.setCancelable(true);
						mSelectCityDialog.setSingleChoiceItems(city, -1, new DialogInterface.OnClickListener() {
						    
							@Override
							public void onClick(DialogInterface dialog, int which) {
							     // TODO Auto-generated method stub
							     int nIndex = which;
							     citySelectedId = cityId[nIndex];
							     editCityName.setText(city[nIndex]);
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
   class ServiceCategory extends AsyncTask<String, String, Object> {

		@Override
		protected Object doInBackground(String... params) {
			
			JSONObject jsonObj = new JSONObject();
			serverURL = Constants.Server_Domain+Constants.Server_Category_List;
			JSONObject result = Utils.getJSONFromPost(serverURL, jsonObj);
			return result;
		}
  	@Override
		protected void onPostExecute(Object result) {
  		if(result == null){
  			AlertDialogManager alert = new AlertDialogManager();
	            alert.showAlertDialog(SearchActivity.this, Constants.messageConnectionTitle, Constants.messageConnectionFailed, false);
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
						countJsonData = jsonObj.getJSONArray("categories").length();
						category = new String[countJsonData];
						categoryId = new String[countJsonData];
						JSONObject objectProject = null;
						for(int i = 0 ; i < countJsonData;i++){
							
							objectProject = jsonObj.getJSONArray("categories").getJSONObject(i);
							strName = objectProject.getString("name");
							strID = objectProject.getString("id");
							category[i] = strName ;
							categoryId[i] = strID;	
						}
						mSelectCategoryDialog.setTitle("Category Selection");
						mSelectCategoryDialog.setCancelable(true);
						mSelectCategoryDialog.setSingleChoiceItems(category, -1, new DialogInterface.OnClickListener() {
						    
							@Override
							public void onClick(DialogInterface dialog, int which) {
							     // TODO Auto-generated method stub
							     int nIndex = which;
							     categorySelectedId = categoryId[nIndex];
							     editCategoryName.setText(category[nIndex]);
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
     
   
   
   
 
   class ServiceStoreSearch extends AsyncTask<String, String, Object> {

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			serverURL = Constants.Server_Domain + Constants.Server_Store_Search;
			
			strStoreName  = params[0];
			strCompanyName   = params[1];
			categorySelectedId    = params[2];
			citySelectedId    = params[3];
			strMin = params[4];
			strMax = params[5];
			strChecked = params[6];
			if(categorySelectedId == null){categorySelectedId = "";}
			if(citySelectedId == null){citySelectedId = "";}
			/* Check the validation */
			
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("user_id", globalInfo.getUserId().toString());
				jsonObj.put("store_name", strStoreName.trim());
				jsonObj.put("company_name", strCompanyName.trim());
				jsonObj.put("category_id", categorySelectedId.trim());
				jsonObj.put("city_id", citySelectedId.trim());
				jsonObj.put("min", strMin.trim());
				jsonObj.put("max", strMax.trim());
				jsonObj.put("is_all", strChecked.trim());
				
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
	            alert.showAlertDialog(SearchActivity.this, Constants.messageConnectionTitle, Constants.messageConnectionFailed, false);
	            setEnableAllView();
	            btnSearch.setEnabled(true);
	            return;
	        }
			JSONObject jsonObj = (JSONObject) result;
			String is_expired = "0";
			int countJsonData = 0 ;
			String id = "";
			String storeName = "";
			String address = "";
			String companyName = "";
			String estimatedWaiting = "";
			String current = "";
			String last = "";
			String queueNo = "";

			 
			try {
				 countJsonData = jsonObj.getJSONArray("stores").length();
				 Store[] stores = new Store[countJsonData];
				 
				 for(int i = 0 ; i < countJsonData;i++){
						JSONObject objectProject = null;
						try {
							objectProject = jsonObj.getJSONArray("stores").getJSONObject(i);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							id = objectProject.getString("id");
							storeName  = objectProject.getString("store_name");
							address  = objectProject.getString("address");
							companyName = objectProject.getString("company_name");
							estimatedWaiting = objectProject.getString("estimated_waiting");
							current = objectProject.getString("current");
							last = objectProject.getString("last");
							queueNo = objectProject.getString("queue_no");
							Store IndividualStore = new Store();
							
							IndividualStore.setId(id);
							IndividualStore.setStoreName(storeName);
							IndividualStore.setAddress(address);
							IndividualStore.setCompanyName(companyName) ;
							IndividualStore.setEstimatedWaiting(estimatedWaiting);
							IndividualStore.setCurrent(current);
							IndividualStore.setLast(last);
							IndividualStore.setQueueNo(queueNo);
							stores[i] = IndividualStore;
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				 	globalInfo.setStore(stores);
				 	globalInfo.setCountStore(countJsonData);
				 	if(countJsonData == 0){
				 		builder.setMessage(Constants.messageNoSearch);
			    		builder.setCancelable(true);
			    		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int id) {
			                    dialog.cancel();
			                }
			            });
			    		setEnableAllView();
			    		btnSearch.setEnabled(true);
			    		builder.show();
			    		return;
				 	}
				 	
				 	Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
				 	startActivity(intent);
				 	btnSearch.setEnabled(true);
				 	finish();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			btnSearch.setEnabled(true);
			
			
		}
   }
}
