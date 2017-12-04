package cn.zjlspace;

import cn.zjlspace.util.MailSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTest {

    @Autowired
    MailSender mailSender;
@Test
    public  void mailSender() {
        Map<String,Object> map=new HashMap<String,Object>();

        map.put("username","张景龙");

        mailSender.sendWithHTMLTemplate("840834298@qq.com",
                "你好！亲","mails/welcome.html",map);

    }
}
