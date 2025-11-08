package com.example.test.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/mainPage")
    public String hello() {
        return "Hello, page!";
    }
}