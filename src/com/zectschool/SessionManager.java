package com.zectschool;


import javax.swing.*;

public class SessionManager {
    public static JButton currentPageBtn = null;

    public static void setCurrentPageBtn(JButton newBtn) {
        if (currentPageBtn == newBtn) return;
//        if (currentPageBtn != null) currentPageBtn.getStyleClass().remove("active");
//        SessionManager.currentPageBtn = newBtn;
//        currentPageBtn.getStyleClass().add("active");
    }

    public static Object user = null;

    public static void logout(){
        user = null;
        currentPageBtn = null;
    }


}
