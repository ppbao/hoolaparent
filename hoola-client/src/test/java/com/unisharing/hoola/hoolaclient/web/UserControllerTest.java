package com.unisharing.hoola.hoolaclient.web;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private UserController userController;
    @Test
    public void testContext(){
        Assert.assertNotNull(userController);

    }
    @Test
    public void myUploadContent() {
    }

    @Test
    public void myLikeContent() {
    }

    @Test
    public void myForwardContent() {
    }

    @Test
    public void myAllContent() {
    }

    @Test
    public void userMain() {
    }

    @Test
    public void timeLine() {
    }

    @Test
    public void timeLineNew() {
    }

    @Test
    public void editUser() {
    }

    @Test
    public void deleteUserVideo() {
    }

    @Test
    public void getSetting() {
    }

    @Test
    public void settingUserLikeShare() {
    }

    @Test
    public void settingIsCamera() {
    }

    @Test
    public void settingUserRecommend() {
    }

    @Test
    public void settingUserMessage() {
    }

    @Test
    public void settingUserBanner() {
    }
}