package com.unisharing.hoola.hoolaclient.web;

import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private IRedisUserService userRedisService;
    @RequestMapping("/user")
    public String login(){
        UserModel userModel = new UserModel();
        userModel.setEmail("zhong@hao.com");
        userModel.setUid(333l);
        userModel.setAvatarUrl("http://www.baidu.com");
        userModel.setBackgroundUrl("http://www.sohu.com");
        userModel.setCreateTime(System.currentTimeMillis());
        userModel.setIsTalent(2);
        userModel.setLastLoginTime(System.currentTimeMillis());
        userModel.setLatitude(22.3f);
        userModel.setMobile("22222222222");

        userRedisService.insertUser(userModel);
        return "hello";
    }
}
