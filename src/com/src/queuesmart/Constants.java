package com.src.queuesmart;

public class Constants {
	
	public static final String Server_Domain = "http://queuesmart.ly";
    public static final String Server_Sign_Url = "/api/v1/auth/login";
    public static final String Server_SignUp_Url = "/api/v1/auth/signup";
	public static final String Server_City_List = "/api/v1/cities";
	public static final String Server_Category_List = "/api/v1/categories";
	public static final String Server_Store_Search = "/api/v1/store/search";
	public static final String Server_Store_Detail ="/api/v1/store/detail";
	public static final String Server_Store_Status ="/api/v1/queue/status";
	public static final String Server_Store_Apply ="/api/v1/queue/apply";
	public static final String USER_AGENT = "Mozilla/5.0";
	
	
	
	public static final String messageLogin = "The username and password are invalid";
	public static final String loginFailed = "You have failed to login";
	public static final String messageWarranty = "Please input  username and password correctly";
    public static final String messageSubmitFailed = "You have failed to submit gifts";
    public static final String messageSubmitSuccess = "You have successed to submit gifts";
    public static final String messageSubmitGift = "You have successed to submit bank";
    public static final String messageSubmitSignUpSuccess = "You have successed to sign up";
    public static final String messageSubmitSignUpFailed = "You have failed to sign up";
    public static final String messageAvailableWarranty = "The available amount should be more than zero";
   
    public static final String messageConnectionFailed = "The connection failed";
    public static final String messageConnectionTitle = "Connection";
    public static final String messageInputErrorTitle = "Input";
    public static final String messageInputErrorMessage = "Please input content correctly";
    public static final String messageBankTrnasferFailed = "You failed to sumbit to transfer bank";
    public static final String messageNoSearch = "There is no search result";
    
    public static final String messageStoretNameAddFailed = "Please input store name correctly";
    public static final String messageCompanyAddFailed = "Please input company name correctly";
    public static final String messageCityAddFailed = "Please input city name correctly";
    public static final String messageCategoryFailed = "Please input category name correctly";
 
}
