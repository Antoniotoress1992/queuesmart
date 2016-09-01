
package com.src.queuesmart;



import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.src.queuesmart.Global;

//Left Menu 
public class LeftMenuFragment extends Fragment implements OnClickListener{
	static RelativeLayout rlLeftSignOut , rlLeftStatus , rlLeftSearch;
	
	Global globalInfo;
	Intent intent;
	SessionManager session;
	RelativeLayout rlBanner , rlFrame;
	int stageWidth , stageHeight;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.leftmenu, container, false);
		rlLeftStatus  = (RelativeLayout) rootView.findViewById(R.id.rl_left_status);
		rlLeftSearch  = (RelativeLayout) rootView.findViewById(R.id.rl_left_home);
		rlLeftSignOut  = (RelativeLayout) rootView.findViewById(R.id.rl_left_signout);
		
		rlLeftStatus.setOnClickListener(this);	
		rlLeftSearch.setOnClickListener(this);
		rlLeftSignOut.setOnClickListener(this);
		
		Display display = getActivity().getWindowManager().getDefaultDisplay(); 
		stageWidth = display.getWidth();
		stageHeight = display.getHeight();
		
		rlBanner = (RelativeLayout) rootView.findViewById(R.id.rlBanner);
		rlFrame = (RelativeLayout) rootView.findViewById(R.id.rlFrame);
		
		RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams)rlBanner.getLayoutParams();
		lp.width = (int) (0.75 * stageWidth);
		rlBanner.setLayoutParams(lp);
		
		//return inflater.inflate(R.layout.leftmenu, container, false);
		session = new SessionManager(getActivity().getApplicationContext());
		
		///set width
		
		
		
		return rootView;
	}
	public void onClick(View v) {
		globalInfo = (Global)getActivity().getApplication();
		
        switch (v.getId()) {
        case R.id.rl_left_status:
        	rlLeftStatus.setBackgroundColor(getResources().getColor(R.color.red));
        	rlLeftSearch.setBackgroundColor(getResources().getColor(R.color.black));
        	rlLeftSignOut.setBackgroundColor(getResources().getColor(R.color.black));
        	
        	intent = new Intent( this.getActivity(), StatusActivity.class);
        	intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(intent);
	    	
	    	break;
		case R.id.rl_left_home:
			rlLeftStatus.setBackgroundColor(getResources().getColor(R.color.black));
			rlLeftSearch.setBackgroundColor(getResources().getColor(R.color.red));
        	rlLeftSignOut.setBackgroundColor(getResources().getColor(R.color.black));
        	
        	intent = new Intent( this.getActivity(), SearchActivity.class);
        	intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	intent.putExtra("user_id", globalInfo.getUserId());
	    	startActivity(intent);
	        break;
	    
		case R.id.rl_left_signout:
			rlLeftStatus.setBackgroundColor(getResources().getColor(R.color.black));
			rlLeftSearch.setBackgroundColor(getResources().getColor(R.color.black));
        	rlLeftSignOut.setBackgroundColor(getResources().getColor(R.color.red));
        	globalInfo.setUserId("");
        	session.logoutUser();
        	intent = new Intent( this.getActivity(), LoginActivity.class);
        	startActivity(intent);
	        break;
		    
        }
    }
	
}
