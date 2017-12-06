package com.unisharing.hoola.hoolaclient.web;

import com.unisharing.hoola.hoolaclient.service.user.IClientUserService;
import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private IRedisUserService userRedisService;
    @Autowired
    private IClientUserService clientUserService;
    @RequestMapping("/user")
    public String login(){
        UserModel userModel = new UserModel();
        userModel.setEmail("zhongDDD@hao.com");
        userModel.setAvatarUrl("http://www.baiduEE.com");
        userModel.setBackgroundUrl("http://www.sohuSS.com");
        userModel.setCreateTime(System.currentTimeMillis());
        userModel.setIsTalent(2);
        userModel.setLastLoginTime(System.currentTimeMillis());
        userModel.setAtitude(22.3f);
        userModel.setMobile("22222222222");

        userRedisService.insertUser(userModel);
        clientUserService.insertRegister(userModel);
        return "hello";
    }
}
