package com.app.A.controller;

import com.app.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/app")
public class HomeController {

    @GetMapping("/app")
    public ResponseEntity<MessageResponse> homeController() {
        MessageResponse msg = new MessageResponse("Welcome  Backend  System");

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
