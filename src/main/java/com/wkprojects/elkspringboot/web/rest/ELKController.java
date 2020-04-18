package com.wkprojects.elkspringboot.web.rest;

import com.wkprojects.elkspringboot.service.ELKService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ELKController {
    private static final Logger logger = LogManager.getLogger(ELKController.class);

    private final ELKService elkService;

    public ELKController(ELKService elkService) {
        this.elkService = elkService;
    }

    @GetMapping("/exception")
    public ResponseEntity<String> throwException() {
        String reponse;
        try{
            throw new Exception("Une exception est levée ..");
        } catch (Exception e){
            logger.error("Une exception est levée ..");
            e.printStackTrace();
            reponse = e.getMessage();
        }
        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }

    @GetMapping("/date")
    public ResponseEntity<String> getCurrentDate() {
        return new ResponseEntity<>(elkService.getCurrentDate(), HttpStatus.OK);
    }

/*    @GetMapping("/exception")
    public ResponseEntity<String> throwException() {
        return new ResponseEntity<>(elkService.throwException(), HttpStatus.OK);
    }*/

    @GetMapping("/divide-ten-by/{number}")
    public ResponseEntity<Integer> divideTenBy(@PathVariable Integer number) {
        return new ResponseEntity<>(elkService.divideTenBy(number), HttpStatus.OK);
    }
}
