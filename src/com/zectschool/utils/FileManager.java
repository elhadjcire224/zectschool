package com.zectschool.utils;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileManager {


     public static Path BASE_DIR = null;
     public static Path IMAGE_DIR = null;

     public static Path DEFAULT_USER_IMG = null;

    public static void setPaths(Path Path){
        BASE_DIR = Path;
        IMAGE_DIR = Paths.get(BASE_DIR.toString(),"com.zectschool.images");
        DEFAULT_USER_IMG = Paths.get(IMAGE_DIR.toString(),"user.png");
    }


    public static String saveImage(File sourceFile,String imgName,String role) {
//        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss"));
        String extension = sourceFile.getName().split("\\.")[1];
        String fileName = imgName+role+date+"."+extension;
        try {
             Files.copy(sourceFile.toPath(),Paths.get(IMAGE_DIR.toString(),fileName), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){e.printStackTrace();ShowAlert.show(Alert.AlertType.ERROR,null,"file save error","erreur lors d'enregistrement de l'image");}
        return fileName;
    }

    public static File selectImage() {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.
                getExtensionFilters().add(new ExtensionFilter("Selectionnez une Image", "*.png", "*.jpg"));
        return fileChooser.showOpenDialog(null);
    }

    public static Image retrieveImage(String imgName){
        if (imgName == null) return  new Image(DEFAULT_USER_IMG.toString());
        File f = Paths.get(IMAGE_DIR.toString(),imgName).toFile();
        if (!(f.isFile() || f.exists())) ShowAlert.show(Alert.AlertType.ERROR,null,"file error","file does'nt exist");
        return new Image(f.getPath());
    }

    public static Image retrieveSchoolImage(){
        return retrieveImage("school_avatar.jpg");
    }

    public static boolean deleteImage(String imgName){
        Image image = retrieveImage(imgName);
        File f = Path.of(image.getUrl()).toFile();
        if (!f.exists()) return false;
        return f.delete();
    }
}
