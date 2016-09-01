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

public class SearchResultActivity extends FragmentActivity {

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
	String data ="",strMessage; 
    int sizeData = 0 , countStore;
	private ImageView menuRightButton;
	private  StoreResultAdapter mDispAdt ;	 
	AlertDialog.Builder builder,mSigleDialogSignUp;
	ArrayList<String> strId = new ArrayList<String>();
	ArrayList<String> strStoreName = new ArrayList<String>();
	ArrayList<String> strAddress = new ArrayList<String>();
	ArrayList<String> strCompanyName = new ArrayList<String>();
	ArrayList<String> strEstimatedWaiting = new ArrayList<String>();
	ArrayList<String> strCurrent = new ArrayList<String>();
	ArrayList<String> strLast = new ArrayList<String>();
	ArrayList<String> strQueue = new ArrayList<String>();
	
	ListView lstSearchResult;
	
	AlertDialog.Builder  mSigleDialog;
	
	Button btnApply;
	
	ImageView imgGoToAdd;
	
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters;
	LinearLayout.LayoutParams listViewParameters;
	Bundle extras;
	String userId,storeId;
	
	
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
		setContentView(R.layout.activity_search_result);
		
		SearchResultActivity mInstance;
		
		builder = new AlertDialog.Builder(this);
		mSigleDialogSignUp = new AlertDialog.Builder(this);
		lstSearchResult = (ListView) findViewById(R.id.lstSearchResult);
		//init 
		countStore = globalInfo.getCountStore();
		
		Store[] stores = new Store[countStore];
		stores = globalInfo.getStore();
		
		for(int i = 0 ; i < countStore ; i++){
			strId.add(stores[i].getId());
			strStoreName.add(stores[i].getstoreName());
			strAddress.add(stores[i].getAddress());
			strCompanyName.add(stores[i].getCompanyName());
			strEstimatedWaiting.add(stores[i].getEstimatedWaiting());
			strCurrent.add(stores[i].getCurrent());
			strLast.add(stores[i].getLast());
			strQueue.add(stores[i].getQueueNo());
		}
		if (mDispAdt == null) {
			mDispAdt = new StoreResultAdapter(SearchResultActivity.this, strStoreName , strEstimatedWaiting , strCurrent , strLast , strQueue);
			lstSearchResult.setAdapter(mDispAdt);
		} else {
			mDispAdt.notifyDataSetChanged();
		}
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
		
		lstSearchResult.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				String id = strId.get(position);
				globalInfo.setStoreId(id);
				Intent intent = new Intent(SearchResultActivity.this, StoreDetailActivity.class);
				startActivity(intent);
			}
		});
	
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
	public void requestApply(int position){
		userId = globalInfo.getUserId();
		setDisableAllview();
		new ServiceApply().execute(userId,strId.get(position));
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
	    		AlertDialog alert = builder.create();
	    		setEnableAllView();
	    		alert.show();
	    		
			}	
			JSONObject jsonObj = (JSONObject) result;
			try {
				if ( jsonObj.getString("result").equals("success")) {
					builder.setMessage(jsonObj.getString("msg"));
					builder.setCancelable(true);
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			                
			                
			            }
			        });
					AlertDialog alert = builder.create();
					alert.show();
				}else
				{
						try {
							String errorMessage = jsonObj.get("msg").toString();
							builder.setMessage(errorMessage);
							builder.setCancelable(true);
							builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int id) {
					                dialog.cancel();
					            }
					        });
							AlertDialog alert = builder.create();
							alert.show();
							
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
