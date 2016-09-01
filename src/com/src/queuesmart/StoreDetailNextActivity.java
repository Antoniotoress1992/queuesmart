package com.src.queuesmart;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StoreDetailNextActivity extends FragmentActivity {

	// Declare
	private LinearLayout slidingPanel;
	private boolean isExpanded;
	private DisplayMetrics metrics;
	private RelativeLayout headerPanel;
	private int panelWidth;
	private int panelWidth1;
	private String serverURL;
	ImageView imgAvatar;
	static InputStream is = null;
	static JSONObject jObj = null;
	String json;
	String data ="",strMessage; 
    int sizeData = 0;
	private ImageView menuRightButton;
	TextView txtStoreName , txtCreateAt , txtWaitingTime , txtTotlaCustomer , txtWaitiedTime , txtCurrentNumber , txtNextNumber;

	String storeId,strCompanyLogo;
	AlertDialog.Builder  mSigleDialog;
	
	Button btnSearch;
	
	ImageView menuBackButton;
	
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters;
	LinearLayout.LayoutParams listViewParameters;
	
	ArrayList<String> projectIdNow = new ArrayList<String>();
	ArrayList<String> projectIdExpired = new ArrayList<String>();
	Global globalInfo;
	SessionManager session;
	WebView browser;
	StoreDetailNextActivity  mInstance;
	
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
		setContentView(R.layout.activity_store_detail_view);
		
		mInstance = StoreDetailNextActivity.this;
		
		txtStoreName = (TextView) findViewById(R.id.txt_store_name);
		txtCreateAt = (TextView) findViewById(R.id.txt_created_at);
		txtWaitingTime = (TextView) findViewById(R.id.txtAddress);
		txtTotlaCustomer = (TextView) findViewById(R.id.txtTotalCustomer);
		txtWaitiedTime = (TextView) findViewById(R.id.txtWaitiedTime);
		txtCurrentNumber = (TextView) findViewById(R.id.txtCurrentNumber);
		txtNextNumber = (TextView) findViewById(R.id.txtNextNumber);
		
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		imgAvatar = (ImageView)findViewById(R.id.imgAvatar);
		
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
		
		
		mSigleDialog = new AlertDialog.Builder(this);
		// Slide the Panel
		menuRightButton = (ImageView) findViewById(R.id.menuViewButton);
		
		menuBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
				finish();
			}
		});
		
		init();
		
					    
	    
	}
   public void init(){
	   txtStoreName.setText(globalInfo.getStoreName());  
	   txtCreateAt.setText(globalInfo.getCreatedAt());  
	   txtWaitingTime.setText(globalInfo.getWaitingTime()) ;
	   txtTotlaCustomer.setText(globalInfo.getTotalCustomer()); 
	   txtWaitiedTime.setText(globalInfo.getWaitingTime());  
	   txtCurrentNumber.setText(globalInfo.getCurrentNumber());  
	   txtNextNumber.setText(globalInfo.getNextNumber());
	   strCompanyLogo = globalInfo.getCompanyLogo();
	   imgAvatar.setImageBitmap(globalInfo.getBmpLogo());
      
   }
   
	
   
  }
