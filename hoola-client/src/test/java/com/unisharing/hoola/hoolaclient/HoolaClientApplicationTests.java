package com.unisharing.hoola.hoolaclient;

import com.unisharing.hoola.hoolaclient.dao.ClientUserMapper;
import com.unisharing.hoola.hoolacommon.model.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes =HoolaClientApplication.class )

public class HoolaClientApplicationTests {
    @Autowired
	private ClientUserMapper userMapper;

	@Test
	public void contextLoads() {

	}
   @Test
    public void testAdd()
    {
        UserModel userModel = new UserModel();
        userModel.setUid(9);
        userModel.setLoginPassward("dd");
        userModel.setEmail("www.sohu9.com");
        userMapper.insertUserInfo(userModel);

    }
}
