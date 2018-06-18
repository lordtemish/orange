package com.dynamica.orange.Classes;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by lordtemich on 12/13/17.
 */
public class FileUploader {
    Logger logger= Logger.getLogger(FileUploader.class);
    private String pal="/home/ps/files/";
    String rootPath = System.getProperty("catalina.home");
    public FileUploader(){}

    public String upload(MultipartFile file, String name){
       try {
            byte[] bytes = file.getBytes();
            String phot="photo";
            rootPath=pal;
           logger.info(rootPath);
           File dir=new File(rootPath+File.separator+"photo");
             if(!dir.exists()){
                 dir.mkdirs();
             }
             File serverf=new File(dir.getAbsolutePath()+File.separator+name+"."+file.getContentType().split("/")[1]);
             if(serverf.exists()){
                 return "";
             }
             BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverf));
                             stream.write(bytes);
                             stream.close();
                             
                            return serverf.getAbsolutePath();

           /* Path path= Paths.get(rootPath+"/photo");
            if(!new File(rootPath+"/photo").exists()){
                if(new File(rootPath+"/photo").mkdir()){

                }
                else{

                }
            }
            if(new File(path.resolve(name+"."+file.getContentType().split("/")[1]).toString()).exists()){
                return "";
            }
            else {
                Files.copy(file.getInputStream(), path.resolve(name + "." + file.getContentType().split("/")[1]));
                return path.resolve(name + "." + file.getContentType().split("/")[1]).toString();
            }*/
        }
        catch (IOException e){

            return e.getMessage();
        }
        catch (Exception e){

            return e.getMessage();
        }


    }
    public String uploadMessageFile(MultipartFile file, String name){
        try {
            byte[] bytes = file.getBytes();
            String phot="chat";
            rootPath=pal;
            logger.info(rootPath);
            File dir=new File(rootPath+File.separator+phot);
            if(!dir.exists()){
                dir.mkdirs();
            }
            File serverf=new File(dir.getAbsolutePath()+File.separator+name+"."+file.getContentType().split("/")[1]);
            if(serverf.exists()){
                return "";
            }
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverf));
            stream.write(bytes);
            stream.close();
            return serverf.getAbsolutePath();

           /* Path path= Paths.get("chat");
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
            }    */
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
            String phot="order";
            rootPath=pal;
            logger.info(rootPath);
            File dir=new File(rootPath+File.separator+phot);
            if(!dir.exists()){
                dir.mkdirs();
            }
            File serverf=new File(dir.getAbsolutePath()+File.separator+name+"."+file.getContentType().split("/")[1]);
            if(serverf.exists()){
                return "";
            }
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverf));
            stream.write(bytes);
            stream.close();
            return serverf.getAbsolutePath();
           /* Path path= Paths.get("order");
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
            }    */
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
    public boolean deleteFile(String url){
        try {
            Files.delete(Paths.get(url));
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

    public String encodeFileToBase64Binary(String fileName)
            throws IOException {

        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.encodeBase64(bytes);
        String encodedString = new String(encoded);

        return encodedString;
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }
}
