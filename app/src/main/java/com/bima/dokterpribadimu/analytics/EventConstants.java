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
    public static final String PARAM_RATING           = "Rating";

    /*
    * Screen names to be sent to Analytics
    * For more details, please refer to the documentation in:
    * https://docs.google.com/document/d/1gAD8_K4ZmpebFDF-ij_EuEMlLoBO6L-kS0bqxbPto3E/edit?usp=sharing
    * */
    public static final String SCREEN_LOADING           = "Scr_Loading";
    public static final String SCREEN_LANDING           = "Scr_Landing";
    public static final String SCREEN_REGISTER          = "Scr_Register";
    public static final String SCREEN_REGISTER_NAME     = "Scr_Register_Name";
    public static final String SCREEN_LOGIN             = "Scr_Login";
    public static final String SCREEN_HOME              = "Scr_Home";
    public static final String SCREEN_MENU_DRAWER       = "Scr_Menu_Drawer";
    public static final String SCREEN_DOCTOR_CALL       = "Scr_Doctor_Call";
    public static final String SCREEN_BOOK_CALL         = "Scr_Book_Call";
    public static final String SCREEN_PARTNERS_HOME     = "Scr_Partners_Home";
    public static final String SCREEN_PARTNERS_MAP      = "Scr_Partners_Map";
    // The search keyword must also be sent to Analytics
    public static final String SCREEN_PARTNERS_SEARCH   = "Scr_Partners_Search";
    public static final String SCREEN_PARTNERS_DETAILS  = "Scr_Partners_Details";
    public static final String SCREEN_NEWS              = "Scr_News";
    public static final String SCREEN_NEWS_DETAILS      = "Scr_News_Details";
    public static final String SCREEN_PROFILE           = "Scr_Profile";
    public static final String SCREEN_ABOUT_BIMA        = "Scr_About_BIMA";
    public static final String SCREEN_SUBSCRIPTION      = "Scr_Subscription";

    /*
    * Window names to be sent to Analytics
    * */
    public static final String WINDOW_PARTNERS_CATEGORIES           = "Wnd_Partners_Categories";
    public static final String WINDOW_PARTNERS_MAP_DETAILS_SHORT    = "Wnd_Partners_Map_Details_Short";
    public static final String WINDOW_PARTNERS_MAP_DETAILS_FULL     = "Wnd_Partners_Map_Details_Full";
    public static final String WINDOW_PARTNERS_DETAILS_FULL         = "Wnd_Partners_Details_Full";

    /*
    * Dialog names to be sent to Analytics
    * */
    public static final String DIALOG_REGISTER_SUCCESS                  = "Dlg_Register_Success";
    public static final String DIALOG_GENERAL_STATUS_FAILED             = "Dlg_General_Status_Failed";
    public static final String DIALOG_LOGIN_FORGOT_PASSWORD             = "Dlg_Login_Forgot_Password";
    public static final String DIALOG_LOGIN_FORGOT_PASSWORD_SUCCESS     = "Dlg_Login_Forgot_Password_Success";
    public static final String DIALOG_LOGIN_SUCCESS                     = "Dlg_Login_Success";
    public static final String DIALOG_DOCTOR_CALL_LATE_HOURS            = "Dlg_Doctor_Call_Late_Hours";
    public static final String DIALOG_DOCTOR_CALL_RATING                = "Dlg_Doctor_Call_Rating";
    public static final String DIALOG_DOCTOR_CALL_RATING_SUCCESS        = "Dlg_Doctor_Call_Rating_Success";
    public static final String DIALOG_BOOK_CALL_PHONE_NUMBER            = "Dlg_Book_Call_Phone_Number";
    public static final String DIALOG_BOOK_CALL_SUCCESS                 = "Dlg_Book_Call_Success";
    public static final String DIALOG_PROFILE_CHANGE_PASSWORD           = "Dlg_Profile_Change_Password";
    public static final String DIALOG_PROFILE_CHANGE_PASSWORD_SUCCESS   = "Dlg_Profile_Change_Password_Success";
    public static final String DIALOG_PROFILE_CHANGE_PHONE              = "Dlg_Profile_Change_Phone";
    public static final String DIALOG_PROFILE_CHANGE_PHONE_SUCCESS      = "Dlg_Profile_Change_Phone_Success";
    public static final String DIALOG_SUBSCRIPTION_GOOGLEPLAY_SUCCESS   = "Dlg_Subscription_GooglePlay_Success";
    public static final String DIALOG_SUBSCRIPTION_BACKEND_SUCCESS      = "Dlg_Subscription_Backend_Success";

    /*
    * Button names to be sent to Analytics
    * */
    public static final String BTN_LOGIN_SCREEN_LANDING                 = "Btn_Login_Scr_Landing";
    public static final String BTN_REGISTER_SCREEN_LANDING              = "Btn_Register_Scr_Landing";
    public static final String BTN_REGISTER_SCREEN_REGISTER             = "Btn_Register_Scr_Register";
    public static final String BTN_FB_SCREEN_REGISTER                   = "Btn_FB_Scr_Register";
    public static final String BTN_GPLUS_SCREEN_REGISTER                = "Btn_Gplus_Scr_Register";
    public static final String BTN_SEND_SCREEN_REGISTER_NAME            = "Btn_Send_Scr_Register_Name";
    public static final String BTN_LOGIN_SCREEN_LOGIN                   = "Btn_Login_Scr_Login";
    public static final String BTN_FB_SCREEN_LOGIN                      = "Btn_FB_Scr_Login";
    public static final String BTN_GPLUS_SCREEN_LOGIN                   = "Btn_Gplus_Scr_Login";
    public static final String BTN_DOCTOR_SCREEN_HOME                   = "Btn_Doctor_Scr_Home";
    public static final String BTN_PARTNERS_SCREEN_HOME                 = "Btn_Partners_Scr_Home";
    public static final String BTN_NEWS_SCREEN_HOME                     = "Btn_News_Scr_Home";
    public static final String BTN_PROFILE_SCREEN_HOME                  = "Btn_Profile_Scr_Home";
    public static final String BTN_SUBSCRIBE_SCREEN_HOME                = "Btn_Subscribe_Scr_Home";
    public static final String BTN_BIMA_SCREEN_MENU_DRAWER              = "Btn_Bima_Scr_Menu_Drawer";
    public static final String BTN_DOCTOR_SCREEN_MENU_DRAWER            = "Btn_Doctor_Scr_Menu_Drawer";
    public static final String BTN_PARTNERS_SCREEN_MENU_DRAWER          = "Btn_Partners_Scr_Menu_Drawer";
    public static final String BTN_NEWS_SCREEN_MENU_DRAWER              = "Btn_News_Scr_Menu_Drawer";
    public static final String BTN_ABOUT_SCREEN_MENU_DRAWER             = "Btn_About_Scr_Menu_Drawer";
    public static final String BTN_SUBSCRIBE_SCREEN_MENU_DRAWER         = "Btn_Subscribe_Scr_Menu_Drawer";
    public static final String BTN_PROFILE_SCREEN_MENU_DRAWER           = "Btn_Profile_Scr_Menu_Drawer";
    public static final String BTN_SIGNOUT_SCREEN_MENU_DRAWER           = "Btn_Signout_Scr_Menu_Drawer";
    public static final String BTN_BOOK_SCREEN_DOCTOR_CALL              = "Btn_Book_Scr_Doctor_Call";
    public static final String BTN_SUBSCRIBE_SCREEN_DOCTOR_CALL         = "Btn_Subscribe_Scr_Doctor_Call";
    public static final String BTN_BOOK_SCREEN_BOOK_CALL                = "Btn_Book_Scr_Book_Call";
    public static final String BTN_PARTNERS_SCREEN_PARTNERS_HOME        = "Btn_Partners_Scr_Partners_Home";
    public static final String BTN_SUBSCRIBE_SCREEN_PARTNERS_HOME       = "Btn_Subscribe_Scr_Partners_Home";
    public static final String BTN_MYLOCATION_SCREEN_PARTNERS_MAP       = "Btn_MyLocation_Scr_Partners_Map";
    public static final String BTN_DIRECTIONS_SCREEN_PARTNERS_MAP       = "Btn_Directions_Scr_Partners_Map";
    public static final String BTN_CATEGORY_SCREEN_PARTNERS_MAP         = "Btn_Category_Scr_Partners_Map";
    public static final String BTN_CATEGORY_WINDOW_CATEGORIES           = "Btn_Category_Wnd_Categories";
    public static final String BTN_MYLOCATION_SCREEN_PARTNERS_DETAILS   = "Btn_MyLocation_Scr_Partners_Details";
    public static final String BTN_DIRECTIONS_SCREEN_PARTNERS_DETAILS   = "Btn_Directions_Scr_Partners_Details";
    public static final String BTN_SETTING_SCREEN_PROFILE               = "Btn_Setting_Scr_Profile";
    public static final String BTN_PHONE_EDIT_SCREEN_PROFILE            = "Btn_Phone_Edit_Scr_Profile";
    public static final String BTN_PASSWORD_EDIT_SCREEN_PROFILE         = "Btn_Password_Edit_Scr_Profile";
    public static final String BTN_CSPHONE_SCREEN_ABOUT_BIMA            = "Btn_CSphone_Scr_About_Bima";
    public static final String BTN_WEB_SCREEN_ABOUT_BIMA                = "Btn_Web_Scr_About_Bima";
    public static final String BTN_FACEBOOK_SCREEN_ABOUT_BIMA           = "Btn_Facebook_Scr_About_Bima";
    public static final String BTN_TWITTER_SCREEN_ABOUT_BIMA            = "Btn_Twitter_Scr_About_Bima";
    public static final String BTN_EMAIL_SCREEN_ABOUT_BIMA              = "Btn_Email_Scr_About_Bima";
    public static final String BTN_ACTIVATE_SCREEN_SUBSCRIPTION         = "Btn_Activate_Scr_Subscription";

}
