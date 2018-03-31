package com.dynamica.orange.Receiver;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrangeMessageListener {

    private static final Logger log = Logger.getLogger(OrangeMessageListener.class);
    OrangeMessageListener(){

    }
    public void receiveMessage(Map<String, String> message){
        log.info("Receiveed  <"+message+">");
    }

}
