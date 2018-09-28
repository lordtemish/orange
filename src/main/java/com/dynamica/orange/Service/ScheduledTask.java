package com.dynamica.orange.Service;

import com.dynamica.orange.Classes.Token;
import com.dynamica.orange.Repo.TokenRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTask {
    @Autowired
    TokenRepo tokenRepo;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 24*60000)
    public void reportCurrentTime() {
        List<Token> tokens=tokenRepo.findByCurtimestartLessThan(new Date().getTime()-(7*24*60000));
        for(Token i:tokens){
            log.info(""+i.getId()+" "+i.getCurtimestart()+" "+i.getIp());
            tokenRepo.delete(i);
        }
    }
}
