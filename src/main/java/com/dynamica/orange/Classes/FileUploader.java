package com.dynamica.orange.Classes;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by lordtemich on 12/13/17.
 */
public class FileUploader {
    public FileUploader(){}

    public String upload(MultipartFile file, String name){
        try {
            byte[] bytes = file.getBytes();
            Path path= Paths.get("/data/photo");
            if(!new File("/data/photo").exists()){
                if(new File("/data/photo").mkdir()){

                }
                else{

                }
            }
            if(new File(name+"."+file.getContentType().split("/")[1]).exists()){
                return "";
            }
            else {
                Files.copy(file.getInputStream(), path.resolve(name + "." + file.getContentType().split("/")[1]));
                return name + "." + file.getContentType().split("/")[1];
            }
        }
        catch (IOException e){

            return "";
        }
        catch (Exception e){

            return "";
        }
    }
    public String uploadMessageFile(MultipartFile file, String name){
        try {
            byte[] bytes = file.getBytes();
            Path path= Paths.get("chat");
            if(!new File("chat").exists()){
                if(new File("chat").mkdir()){

                }
                else{

                }
            }
            if(new File(name+"."+file.getContentType().split("/")[1]).exists()){
                return "";
            }
            else {
                Files.copy(file.getInputStream(), path.resolve(name + "." + file.getContentType().split("/")[1]));
                return name + "." + file.getContentType().split("/")[1];
            }
        }
        catch (IOException e){

            return "";
        }
        catch (Exception e){

            return "";
        }
    }
    public String uploadOrderFile(MultipartFile file, String name){
        try {
            byte[] bytes = file.getBytes();
            Path path= Paths.get("order");
            if(!new File("order").exists()){
                if(new File("order").mkdir()){

                }
                else{

                }
            }
            if(new File(name+"."+file.getContentType().split("/")[1]).exists()){
                return "";
            }
            else {
                Files.copy(file.getInputStream(), path.resolve(name + "." + file.getContentType().split("/")[1]));
                return name + "." + file.getContentType().split("/")[1];
            }
        }
        catch (IOException e){

            return "";
        }
        catch (Exception e){

            return "";
        }
    }

    public boolean deleteMessageFile(String url){
        try {
            Files.delete(Paths.get("chat/" + url));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePhoto(String url){
        try {
            Files.delete(Paths.get("photo/" + url));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
