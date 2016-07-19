package com.bima.dokterpribadimu.analytics;

/**
 * Created by gustavo.santos on 7/19/2016.
 */
public class EventConstants {
    /*
    * Event types
    * */
    public static final String TYPE_VIEW_SCREEN  = "view_screen";
    public static final String TYPE_VIEW_DIALOG  = "view_dialog";
    public static final String TYPE_VIEW_WINDOW  = "view_window";
    public static final String TYPE_BUTTON_CLICK = "button_click";
    public static final String TYPE_SEARCH       = "search";
    public static final String TYPE_ITEM_VIEW    = "item_view";

    /*
    * Event parameters
    * */
    public static final String PARAM_EVENT_NAME       = "Name";
    public static final String PARAM_SEARCH_KEYWORD   = "Keyword";
    public static final String PARAM_CALL_TOPIC       = "Call_topic";
    public static final String PARAM_CALL_SUBTOPIC    = "Call_subtopic";
    public static final String PARAM_NEWS_CATEGORY    = "Category";
    public static final String PARAM_NEWS_TITLE       = "Title";
    public static final String PARAM_NEWS_DATE        = "Date";
    public static final String PARAM_CATEGORY         = "Category";
    public static final String PARAM_MESSAGE          = "Message";

    /*
    * Screen names to be sent to Analytics
    * For more details, please refer to the documentation in:
    * https://docs.google.com/document/d/1gAD8_K4ZmpebFDF-ij_EuEMlLoBO6L-kS0bqxbPto3E/edit?usp=sharing
    * */
    public static final String SCREEN_LOADING           = "00_Screen_Loading";
    public static final String SCREEN_LANDING           = "01_Screen_Landing";
    public static final String SCREEN_REGISTER          = "02_Screen_Register";
    public static final String SCREEN_LOGIN             = "03_Screen_Login";
    public static final String SCREEN_HOME              = "04_Screen_Home";
    public static final String SCREEN_MENU_DRAWER       = "05_Screen_Menu_Drawer";
    public static final String SCREEN_DOCTOR_CALL       = "06_Screen_Doctor_Call";
    public static final String SCREEN_BOOK_CALL         = "07_Screen_Book_Call";
    public static final String SCREEN_PARTNERS_HOME     = "08_Screen_Partners_Home";
    public static final String SCREEN_PARTNERS_MAP      = "09_Screen_Partners_Map";
    // The search keyword must also be sent to Analytics
    public static final String SCREEN_PARTNERS_SEARCH   = "10_Screen_Partners_Search";
    public static final String SCREEN_PARTNERS_DETAILS  = "11_Screen_Partners_Details";
    public static final String SCREEN_NEWS              = "12_Screen_News";
    public static final String SCREEN_NEWS_DETAILS      = "13_Screen_News_Details";
    public static final String SCREEN_PROFILE           = "14_Screen_Profile";
    public static final String SCREEN_ABOUT_BIMA        = "15_Screen_About_BIMA";
    public static final String SCREEN_SUBSCRIPTION      = "16_Screen_Subscription";

    /*
    * Window names to be sent to Analytics
    * */
    public static final String WINDOW_PARTNERS_CATEGORIES           = "09_Window_Partners_Categories";
    public static final String WINDOW_PARTNERS_MAP_DETAILS_SHORT    = "09_Window_Partners_Map_Details_Short";
    public static final String WINDOW_PARTNERS_MAP_DETAILS_FULL     = "09_Window_Partners_Map_Details_Full";
    public static final String WINDOW_PARTNERS_DETAILS_FULL         = "11_Window_Partners_Details_Full";

    /*
    * Dialog names to be sent to Analytics
    * */
    public static final String DIALOG_REGISTER_EMAIL_ALREADY_EXISTS     = "00_Screen_Loading";
    public static final String DIALOG_REGISTER_SUCCESS                  = "02_Dialog_Register_Success";
    public static final String DIALOG_GENERAL_STATUS_FAILED             = "Dialog_General_Status_Failed";
    public static final String DIALOG_LOGIN_FORGOT_PASSWORD             = "03_Dialog_Login_Forgot_Password";
    public static final String DIALOG_LOGIN_FORGOT_PASSWORD_SUCCESS     = "03_Dialog_Login_Forgot_Password_Success";
    public static final String DIALOG_LOGIN_SUCCESS                     = "03_Dialog_Login_Success";
    public static final String DIALOG_BOOK_CALL_PHONE_NUMBER            = "07_Dialog_Book_Call_Phone_Number";
    public static final String DIALOG_BOOK_CALL_SUCCESS                 = "07_Dialog_Book_Call_Success";
    public static final String DIALOG_PROFILE_CHANGE_PASSWORD           = "14_Dialog_Profile_Change_Password";
    public static final String DIALOG_PROFILE_CHANGE_PASSWORD_SUCCESS   = "14_Dialog_Profile_Change_Password_Success";
    public static final String DIALOG_PROFILE_CHANGE_PHONE              = "14_Dialog_Profile_Change_Phone";
    public static final String DIALOG_PROFILE_CHANGE_PHONE_SUCCESS      = "14_Dialog_Profile_Change_Phone_Success";
    public static final String DIALOG_SUBSCRIPTION_GOOGLEPLAY_SUCCESS   = "16_Dialog_Subscription_GooglePlay_Success";
    public static final String DIALOG_SUBSCRIPTION_BACKEND_SUCCESS      = "16_Dialog_Subscription_Backend_Success";

    /*
    * Button names to be sent to Analytics
    * */
    public static final String BTN_LOGIN_SCREEN_LANDING                 = "01_Screen_Landing_Btn_Login";
    public static final String BTN_REGISTER_SCREEN_LANDING              = "01_Screen_Landing_Btn_Register";
    public static final String BTN_REGISTER_SCREEN_REGISTER             = "02_Screen_Register_Btn_Register";
    public static final String BTN_FB_SCREEN_REGISTER                   = "02_Screen_Register_Btn_FB";
    public static final String BTN_GPLUS_SCREEN_REGISTER                = "02_Screen_Register_Btn_Gplus";
    public static final String BTN_LOGIN_SCREEN_LOGIN                   = "03_Screen_Login_Btn_Login";
    public static final String BTN_FB_SCREEN_LOGIN                      = "03_Screen_Login_Btn_FB";
    public static final String BTN_GPLUS_SCREEN_LOGIN                   = "03_Screen_Login_Btn_Gplus";
    public static final String BTN_DOCTOR_SCREEN_HOME                   = "04_Screen_Home_Btn_Doctor";
    public static final String BTN_PARTNERS_SCREEN_HOME                 = "04_Screen_Home_Btn_Partners";
    public static final String BTN_NEWS_SCREEN_HOME                     = "04_Screen_Home_Btn_News";
    public static final String BTN_PROFILE_SCREEN_HOME                  = "04_Screen_Home_Btn_Profile";
    public static final String BTN_SUBSCRIBE_SCREEN_HOME                = "04_Screen_Home_Btn_Subscribe";
    public static final String BTN_DOCTOR_SCREEN_MENU_DRAWER            = "05_Screen_Menu_Drawer_Btn_Doctor";
    public static final String BTN_PARTNERS_SCREEN_MENU_DRAWER          = "05_Screen_Menu_Drawer_Btn_Partners";
    public static final String BTN_NEWS_SCREEN_MENU_DRAWER              = "05_Screen_Menu_Drawer_Btn_News";
    public static final String BTN_ABOUT_SCREEN_MENU_DRAWER             = "05_Screen_Menu_Drawer_Btn_About";
    public static final String BTN_SUBSCRIBE_SCREEN_MENU_DRAWER         = "05_Screen_Menu_Drawer_Btn_Subscribe";
    public static final String BTN_PROFILE_SCREEN_MENU_DRAWER           = "05_Screen_Menu_Drawer_Btn_Profile";
    public static final String BTN_SIGNOUT_SCREEN_MENU_DRAWER           = "05_Screen_Menu_Drawer_Btn_Signout";
    public static final String BTN_BOOK_SCREEN_DOCTOR_CALL              = "06_Screen_Doctor_Call_Btn_Book";
    public static final String BTN_BOOK_SCREEN_BOOK_CALL                = "07_Screen_Book_Call_Book";
    public static final String BTN_PARTNERS_SCREEN_PARTNERS_HOME        = "08_Screen_Partners_Home_Btn_Partners";
    public static final String BTN_MYLOCATION_SCREEN_PARTNERS_MAP       = "09_Screen_Partners_Map_Btn_MyLocation";
    public static final String BTN_DIRECTIONS_SCREEN_PARTNERS_MAP       = "09_Screen_Partners_Map_Btn_Directions";
    public static final String BTN_CATEGORY_SCREEN_PARTNERS_MAP         = "09_Screen_Partners_Map_Btn_Category";
    public static final String BTN_CATEGORY_WINDOW_CATEGORIES           = "09_Wnd_Categories_Btn_Category";
    public static final String BTN_MYLOCATION_SCREEN_PARTNERS_DETAILS   = "11_Screen_Partners_Details_Btn_MyLocation";
    public static final String BTN_DIRECTIONS_SCREEN_PARTNERS_DETAILS   = "11_Screen_Partners_Details_Btn_Directions";
    public static final String BTN_SETTING_SCREEN_PROFILE               = "14_Screen_Profile_Btn_Setting";
    public static final String BTN_PHONE_EDIT_SCREEN_PROFILE            = "14_Screen_Profile_Btn_Phone_Edit";
    public static final String BTN_PASSWORD_EDIT_SCREEN_PROFILE         = "14_Screen_Profile_Btn_Password_Edit";
    public static final String BTN_CSPHONE_SCREEN_ABOUT_BIMA            = "15_Screen_About_Bima_CSphone";
    public static final String BTN_WEB_SCREEN_ABOUT_BIMA                = "15_Screen_About_Bima_Web";
    public static final String BTN_FACEBOOK_SCREEN_ABOUT_BIMA           = "15_Screen_About_Bima_Btn_Facebook";
    public static final String BTN_TWITTER_SCREEN_ABOUT_BIMA            = "15_Screen_About_Bima_Btn_Twitter";
    public static final String BTN_EMAIL_SCREEN_ABOUT_BIMA              = "15_Screen_About_Bima_Btn_Email";
    public static final String BTN_ACTIVATE_SCREEN_SUBSCRIPTION         = "16_Screen_Subscription_Btn_Activate";

}
