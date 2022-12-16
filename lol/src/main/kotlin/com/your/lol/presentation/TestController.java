package com.your.lol.presentation;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class TestController {

    @RequestMapping("/test")
    public String test() {
        return "ok";
    }
}
