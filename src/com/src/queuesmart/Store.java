package com.src.queuesmart;
import android.app.Application;

public class Store {
    public String id, storeName , address , companyName , estimatedWaiting , current ,last , queue_no;
    public Store(String id, String storeName , String address , String companyName , String estimateWaiting , String current , String last , String queue_no ) {
        this.id = id;
        this.storeName = storeName;
        this.address = address ;
        this.companyName = companyName ;
        this.estimatedWaiting = estimateWaiting ;
        this.current = current ;
        this.last = last ;
        this.queue_no = queue_no;
    };
    public Store(){
    	this.id="";
    	this.storeName = "";
    	this.address="";
    	this.companyName ="";
    	this.estimatedWaiting="";
    	this.current="";
    	this.last="";
    	this.queue_no = "";
    }
    
    public void setQueueNo(String queue_no){
    	this.queue_no = queue_no;
    }
    public String getQueueNo(){
    	return queue_no;
    }
    
    public void setId(String id){
    	this.id = id ;
    }
    public String getId(){
    	return id;
    }
    
    public void setStoreName(String storeName){
    	this.storeName = storeName ;
    }
    public String getstoreName(){
    	return storeName;
    }
    
    public void setAddress(String address){
    	this.address = address ;
    }
    public String getAddress(){
    	return address;
    }
    
    public void setCompanyName(String companyName){
    	this.companyName = companyName ;
    }
    public String getCompanyName(){
    	return companyName;
    }
    
    public void setEstimatedWaiting(String estimatedWaiting){
    	this.estimatedWaiting = estimatedWaiting ;
    }
    public String getEstimatedWaiting(){
    	return estimatedWaiting;
    }
    
    public void setCurrent(String current){
    	this.current = current ;
    }
    public String getCurrent(){
    	return current;
    }
    public void setLast(String last){
    	this.last = last;
    }
    public String getLast(){
    	return last;
    }
    
}