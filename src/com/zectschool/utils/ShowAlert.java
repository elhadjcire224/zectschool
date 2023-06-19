package com.zectschool.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class ShowAlert {

    public static ButtonType show(Alert.AlertType alertType , String header, String title, String content){
        Alert a = new Alert(alertType);
        a.setHeaderText(null);
        a.setTitle(title);
        a.setContentText(content);
        return a.showAndWait().get();
    }
}
