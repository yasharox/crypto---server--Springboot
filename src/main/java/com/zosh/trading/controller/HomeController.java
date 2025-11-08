package com.zosh.trading.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home () {
        return " Welcome to crypto trading platform home page!! ";
    }
}
