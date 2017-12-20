package com.unisharing.hoola.hoolaclient.web;

import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolaredis.pubsub.IRedisPublisher;
import com.unisharing.hoola.hoolaredis.service.jms.IRedisJMSMessageService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class BaseController {
    @Autowired
    IRedisUserService redisUserService;
    @Autowired
    IRedisPublisher hoolaPublisher;
    @Autowired
    IRedisJMSMessageService redisJMSMessageService;


    public UserModel getUser(String token){
        long uid = this.getUid(token);
        if(uid>0){
            UserModel model = redisUserService.getUser(uid);
            if (model != null && model.getUid()>0){
                return model;
            }
        }
        return null;
    }

    public long getUid(String token) {
        long uid = redisUserService.getUid(token);
        return uid;
    }
}
