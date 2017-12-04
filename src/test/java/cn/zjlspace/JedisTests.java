package cn.zjlspace;


import cn.zjlspace.model.News;
import cn.zjlspace.model.User;
import cn.zjlspace.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest

public class JedisTests {
    @Autowired
    JedisAdapter jedisAdapter;



	@Test
	public void testObject() {
        User user=new User();
        user.setHeadUrl("http://image.nowcoder.com/head/100t.png");
        user.setName("user1");
        user.setPassword("pwd");
        user.setSalt("salt");
        jedisAdapter.setObject("user1xx",user);
        //127.0.0.1:6379> get user1xx
       // "{\"headUrl\":\"http://image.nowcoder.com/head/100t.png\",\"id\":0,\"name\":\"user1\",\"password\":\"pwd\",\"salt\":\"salt\"}"

         User u=jedisAdapter.getObject("user1xx",User.class);
        System.out.println(ToStringBuilder.reflectionToString(u));

	}

}
