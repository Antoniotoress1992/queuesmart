package com.src.queuesmart;


import com.src.utils.*;

import java.io.InputStream;
import java.util.ArrayList;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

public class StatusActivity extends FragmentActivity {

	// Declare
	private LinearLayout slidingPanel;
	private boolean isExpanded;
	private DisplayMetrics metrics;
	private RelativeLayout headerPanel;
	private int panelWidth;
	private int panelWidth1;
	private String serverURL;
	static InputStream is = null;
	static JSONObject jObj = null;
	String json;
	String userId;
	String data ="",strMessage; 
    int sizeData = 0 , countStore;
	private ImageView menuRightButton;
	private  StatusAdapter mDispAdt ;
	
	ArrayList<String> strId = new ArrayList<String>();
	ArrayList<String> strStoreName = new ArrayList<String>();
	ArrayList<String> strCompanyName = new ArrayList<String>();
	ArrayList<String> strEstimatedWaiting = new ArrayList<String>();
	ArrayList<String> strCurrent = new ArrayList<String>();
	ArrayList<String> strQueue = new ArrayList<String>();
	AlertDialog.Builder builder;
	ListView lstSearchStatus;
	
	AlertDialog.Builder  mSigleDialog;
	
	Button btnApply;
	TextView txtSearchNo;
	ImageView imgGoToAdd;
	
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters;
	LinearLayout.LayoutParams listViewParameters;
	Bundle extras;
	
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
		setContentView(R.layout.activity_status);
		userId = globalInfo.getUserId();
		builder = new AlertDialog.Builder(this);
		lstSearchStatus = (ListView) findViewById(R.id.lstSearchStatus);
		//init 
		
		// Initialize
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
		
		txtSearchNo = (TextView) findViewById(R.id.txtSearchNo);
		
		mSigleDialog = new AlertDialog.Builder(this);
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
		
		lstSearchStatus.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				String id = strId.get(position);
				globalInfo.setStoreId(id);
				Intent intent = new Intent(StatusActivity.this, StoreDetailActivity.class);
				startActivity(intent);
			}
		});
	    init();
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
	public void init(){
		setDisableAllview();
		new ServiceStatus().execute(userId);
	  
   }
	class ServiceStatus extends AsyncTask<String, String, Object> {

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			serverURL = Constants.Server_Domain + Constants.Server_Store_Status;
			
			userId  = params[0];
			
			/* Check the validation */
			
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("user_id", userId.trim());
				
				
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
	            alert.showAlertDialog(StatusActivity.this, Constants.messageConnectionTitle, Constants.messageConnectionFailed, false);
	            setEnableAllView();
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
			String queue = "";

			 
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
							id = objectProject.getString("store_id");
							storeName  = objectProject.getString("store_name");
							estimatedWaiting = objectProject.getString("estimated_waiting");
							current = objectProject.getString("current");
							queue = objectProject.getString("mine");
							
							strId.add(id);
							strStoreName.add(storeName);
							strEstimatedWaiting.add(estimatedWaiting);
							strCurrent.add(current);
							strQueue.add(queue);
							
							
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(countJsonData == 0){
							txtSearchNo.setVisibility(View.VISIBLE);
							
				    		return;
					 }else{
						 txtSearchNo.setVisibility(View.GONE);
						 if (mDispAdt == null) {
								mDispAdt = new StatusAdapter(StatusActivity.this, strStoreName , strEstimatedWaiting , strCurrent , strQueue);
								lstSearchStatus.setAdapter(mDispAdt);
							} else {
								mDispAdt.notifyDataSetChanged();
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

