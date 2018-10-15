package com.threedsoft.picking;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScheduledLogger {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

/*    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        LocalDateTime date = new LocalDateTime();
        log.info("The time is now {}", dateFormat.format(date));
    }*/
}