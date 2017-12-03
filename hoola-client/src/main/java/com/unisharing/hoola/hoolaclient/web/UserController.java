package com.unisharing.hoola.hoolaclient.web;

import com.unisharing.hoola.hoolaredis.service.UserRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserRedisService userRedisService;
    @RequestMapping("/user")
    public String login(){
        userRedisService.setUser("redis","verygood");
        return "hello";
    }
}
