package com.src.queuesmart;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StatusAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	
	private ArrayList<String> List_STORENAME;
	private ArrayList<String> LIST_WAITIEDAMOUNT;
	private ArrayList<String> LIST_CURRENTAMOUNT;
	private ArrayList<String> LIST_QUEUE;
	
    public StatusAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }
    
    public StatusAdapter(Context context, ArrayList<String> listStoreName, ArrayList<String> listWaitedAmount , ArrayList<String> listCurrentAmount , ArrayList<String> listQueue) {
    	mInflater = LayoutInflater.from(context);
    	List_STORENAME = listStoreName;
    	LIST_WAITIEDAMOUNT = listWaitedAmount;
    	LIST_CURRENTAMOUNT = listCurrentAmount;
    	LIST_QUEUE = listQueue;
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_search_status, null);
            holder = new ViewHolder();
            holder.txtStoreName = (TextView) convertView.findViewById(R.id.txtStoreName);
            holder.txtEstimatedWaitingAmount = (TextView) convertView.findViewById(R.id.txtEstimatedWaitingAmount);
            holder.txtCurrentAmount = (TextView) convertView.findViewById(R.id.txtCurrentAmount);
            holder.txtQueue = (TextView) convertView.findViewById(R.id.txtQueue);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.txtStoreName.setText(List_STORENAME.get(position));
        holder.txtEstimatedWaitingAmount.setText(LIST_WAITIEDAMOUNT.get(position));
        holder.txtCurrentAmount.setText(LIST_CURRENTAMOUNT.get(position));
        holder.txtQueue.setText(LIST_QUEUE.get(position));
        return convertView;
    }

    static class ViewHolder {
    	TextView txtStoreName;
        TextView txtEstimatedWaitingAmount;
        TextView txtCurrentAmount;
        TextView txtQueue;
    }	

}
