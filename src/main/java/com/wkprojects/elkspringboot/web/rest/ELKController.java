package com.wkprojects.elkspringboot.web.rest;

import org.apache.logging.log4j.LogManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.apache.logging.log4j.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;

@RestController
public class ELKController {

    private static final Logger logger = LogManager.getLogger(ELKController.class);

    private final RestTemplate restTemplate;

    public ELKController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/elk")
    public String helloWorld() {
        String response = "Welcome to JavaInUse" + LocalDate.now();
        logger.info(response);
        return response;
    }

    @GetMapping("/exception")
    public String exception() {
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
}
