package com.example.myshoppingapp.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTestController {

    @GetMapping("/rest/test")
    public String test() {
        return "test";
    }
}
