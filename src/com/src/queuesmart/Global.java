package com.src.queuesmart;



import android.app.Application;
import android.graphics.Bitmap;

public  class  Global extends Application {
    public String name;
	public String email;
    public String userId;
    public int countStore;
    public Store[] stores;
    
    public String storeId;
    public String storeName;
    public String companyName;
    public String waitingTime;
    public String nextNumber;
    public String categoryName;
    public String cityName;
    public String createdAt;
    public String address;
    public String totalCustomer;
    public String currentNumber;
    public String companyLogo;
    public Bitmap bmpLogo;

    public Bitmap getBmpLogo(){
    	return bmpLogo;
    }
    public void setBmpLogo(Bitmap bmpLogo){
    	this.bmpLogo = bmpLogo;
    }
    public int getCountStore(){
    	return countStore;
    }
    public void setCompanyLogo(String companyLogo){
    	this.companyLogo = companyLogo;
    }
    public String getCompanyLogo(){
    	return companyLogo;
    }
    public void setCountStore(int countStore){
    	this.countStore = countStore;
    }
    public String getStoreId(){
    	return storeId;
    }
    public void setStoreId(String storeId){
    	this.storeId = storeId;
    }
    public String getStoreName(){
    	return storeName;
    }
    public void setStoreName(String storeName){
    	this.storeName = storeName;
    }
    
    public String getCompanyName(){
    	return companyName;
    }
    public void setCompanyName(String companyName){
    	this.companyName = companyName;
    }
    public String getWaitingTime(){
    	return waitingTime;
    }
    public void setWaitingTime(String waitingTime){
    	this.waitingTime = waitingTime;
    }
    public String getNextNumber(){
    	return nextNumber;
    }
    public void setNextNumber(String nextNumber){
    	this.nextNumber = nextNumber;
    }
    public String getCategoryName(){
    	return categoryName;
    }
    public void setCategoryName(String categoryName){
    	this.categoryName = categoryName;
    }
    public String getCityName(){
    	return cityName;
    }
    public void setCityName(String cityName){
    	this.cityName = cityName;
    }
    public String getCreatedAt(){
    	return createdAt;
    }
    public void setCreatedAt(String createdAt){
    	this.createdAt = createdAt;
    }
   
    public String getTotalCustomer(){
    	return totalCustomer;
    }
    public void setTotalCustomer(String totalCustomer){
    	this.totalCustomer = totalCustomer;
    }
    public String getAddress(){
    	return address;
    }
    public void setAddress(String address){
    	this.address = address; 
    }
    
    public String getCurrentNumber(){
    	return currentNumber;
    }
    public void setCurrentNumber(String currentTime){
    	this.currentNumber = currentTime; 
    }
    public Store[] getStore(){
    	return stores;
    }
    
    public  void setStore(Store[] stores){
    	this.stores = stores;
    }
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
} 
