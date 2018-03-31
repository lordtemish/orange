package com.dynamica.orange.Service;

import com.dynamica.orange.SpringBootMain;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrangeServiceImpl implements OrangeService{
    private static final Logger log = Logger.getLogger(OrangeServiceImpl.class);
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public OrangeServiceImpl(RabbitTemplate template){
        rabbitTemplate=template;
    }

    @Override
    public void sendOrangeMessage(String id) {
        Map<String, String> actionmap = new HashMap<>();
        actionmap.put("id", id);
        log.info("Sending the index request through queue message");
        rabbitTemplate.convertAndSend(SpringBootMain.SFG_MESSAGE_QUEUE, actionmap);
    }
}
