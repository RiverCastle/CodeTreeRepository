package com.codetree.CodeTreeHRM.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerHealthChkController {

    @GetMapping("/ServerOk")
    public String serverOk() {
        return "Hello world";
    }
}
