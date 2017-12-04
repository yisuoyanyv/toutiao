package cn.zjlspace;

import cn.zjlspace.dao.LoginTicketDAO;
import cn.zjlspace.dao.NewsDAO;
import cn.zjlspace.dao.UserDAO;
import cn.zjlspace.model.LoginTicket;
import cn.zjlspace.model.News;
import cn.zjlspace.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest

public class ChangeDatabaseTests {
	@Autowired
	UserDAO userDAO;
	@Autowired
    NewsDAO newsDAO;
	@Autowired
    LoginTicketDAO loginTicketDAO;


	@Test
	public void insertNews() {
            News news=new News();
            news.setCommentCount(10);
            Date date =new Date();
            date.setTime(date.getTime()+1000*3600*5*1);
            news.setCreatedDate(date);
            news.setImage("http://ozuxyr4xv.bkt.clouddn.com/9c511ba3462f4a99be86a95037315bad.jpg");
            news.setLikeCount(2);
            news.setUserId(5);
            news.setTitle("我是中文哈哈哈");
            news.setLink("http://www.zjlsapce.cn");
            newsDAO.addnews(news);







	}

}
