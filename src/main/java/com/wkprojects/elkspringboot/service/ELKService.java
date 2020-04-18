package com.wkprojects.elkspringboot.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;

@Service
public class ELKService {
    private static final Logger logger = LogManager.getLogger(ELKService.class);

    public String getCurrentDate() {
        LocalDate localDate = LocalDate.now();
        logger.info("Current date: {}", localDate);
        return localDate.toString();
    }

    public String throwException() {
        String response;
        try {
            throw new Exception("Exception has occured....");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString();
            logger.error("Exception - " + stackTrace);
            response = stackTrace;
        }
        return response;
    }

    public Integer divideTenBy(Integer number) {
        return 10 / number;
    }
}
