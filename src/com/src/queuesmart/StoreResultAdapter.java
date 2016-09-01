package com.src.queuesmart;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class StoreResultAdapter extends BaseAdapter  {

	private LayoutInflater mInflater;
	public SearchResultActivity mInstance;
	Global globalInfo;
	private ArrayList<String> List_STORENAME;
	private ArrayList<String> LIST_WAITIEDAMOUNT;
	private ArrayList<String> LIST_CURRENTAMOUNT;
	private ArrayList<String> LIST_LASTAMOUNT;
	private ArrayList<String> LIST_QUEUENO;
	
    public StoreResultAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        
    }
    
    public StoreResultAdapter(Context context, ArrayList<String> listStoreName, ArrayList<String> listWaitedAmount , ArrayList<String> listCurrentAmount , ArrayList<String> listLastAmount , ArrayList<String> listQueue) {
    	mInflater = LayoutInflater.from(context);
    	mInstance = (SearchResultActivity)context; 
    	List_STORENAME = listStoreName;
    	LIST_WAITIEDAMOUNT = listWaitedAmount;
    	LIST_CURRENTAMOUNT = listCurrentAmount;
    	LIST_LASTAMOUNT = listLastAmount;
    	LIST_QUEUENO = listQueue ;
    }
    
	public int getCount() {
		// TODO Auto-generated method stub
		return List_STORENAME.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Button btnApply = null;
        
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_search_result, null);
            holder = new ViewHolder();
            holder.txtStoreName = (TextView) convertView.findViewById(R.id.txtStoreName);
            holder.txtEstimatedWaitingAmount = (TextView) convertView.findViewById(R.id.txtEstimatedWaitingAmount);
            holder.txtCurrentAmount = (TextView) convertView.findViewById(R.id.txtCurrentAmount);
            holder.txtLastAmount = (TextView) convertView.findViewById(R.id.txtLastAmount);
            holder.txtQueue = (TextView) convertView.findViewById(R.id.txtQueue);
            
            holder.btnApply = (Button)convertView.findViewById(R.id.btnApply);
             
            holder.btnApply.setOnClickListener(new View.OnClickListener() {
    
	            	public void onClick(View v) {
	                        //TODO
	            			mInstance.requestApply(position);
	            			
	                   }
    
               });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.txtStoreName.setText(List_STORENAME.get(position));
        holder.txtEstimatedWaitingAmount.setText(LIST_WAITIEDAMOUNT.get(position));
        holder.txtCurrentAmount.setText(LIST_CURRENTAMOUNT.get(position));
        holder.txtLastAmount.setText(LIST_LASTAMOUNT.get(position));
        
        if(LIST_QUEUENO.get(position).equals("null")){
        	holder.btnApply.setVisibility(View.VISIBLE);
        	holder.txtQueue.setVisibility(View.INVISIBLE);
        }else{
        	holder.btnApply.setVisibility(View.INVISIBLE);
        	holder.txtQueue.setVisibility(View.VISIBLE);
        	holder.txtQueue.setText(LIST_QUEUENO.get(position));
        }
        
        return convertView;
    }
   
    static class ViewHolder {
    	TextView txtStoreName;
        TextView txtEstimatedWaitingAmount;
        TextView txtCurrentAmount;
        TextView txtLastAmount;
        TextView txtQueue;
        Button btnApply;
    }	

}
