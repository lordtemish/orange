package com.dynamica.orange.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;

public class FireBaseMain {
    Logger log= LoggerFactory.getLogger(FireBaseMain.class);
    String path="/home/lordtemich/Desktop/orange-1a2f9-firebase-adminsdk-feh42-1bba7ef45d.json";
    FirebaseApp app;
    public FireBaseMain(){
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(path);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://orange-1a2f9.firebaseio.com")
                    .build();

            app=FirebaseApp.initializeApp(options);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public FirebaseApp getApp() {
        return app;
    }

    public boolean sendMessage(String mess, String token){
        try {
            String registrationToken = token;

            // See documentation on defining a message payload.
            Message message = Message.builder()
                    .putData("score", "850")
                    .putData("message", mess)
                    .setToken(registrationToken)
                    .build();

            // Send a message to the device corresponding to the provided
            // registration token.
            String response = FirebaseMessaging.getInstance().send(message);
            // Response is a message ID string.
           log.info("Successfully sent message: " + response);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
