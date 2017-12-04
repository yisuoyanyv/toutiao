package cn.zjlspace.service;

import cn.zjlspace.dao.LoginTicketDAO;
import cn.zjlspace.dao.UserDAO;
import cn.zjlspace.model.LoginTicket;
import cn.zjlspace.model.User;
import cn.zjlspace.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
     public User getUser(int id){
         return userDAO.selectById(id);

     }
     public Map<String,Object> register(String username, String password){
         Map<String,Object> map=new HashMap<String,Object>();
         if(StringUtils.isBlank(username)){
             map.put("msgname","用户名不能为空");
             return map;

         }

         //密码强度等
         if(StringUtils.isBlank(password)){
             map.put("msgpwd","密码不能为空");
             return map;

         }
         User user=userDAO.selectByName(username);
         if(user!=null){
             map.put("msgname","用户名已经被注册！请换一个用户名");
             return map;
         }
         user=new User();
         user.setName(username);
         user.setSalt(UUID.randomUUID().toString().substring(0,5));
         user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
         user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));

         userDAO.addUser(user);
         String ticket=addLoginTicket(user.getId());
         return map;
     }

    public Map<String,Object> login(String username, String password){
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;

        }

        //密码强度等
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;

        }
        User user=userDAO.selectByName(username);
        if(user==null){
            map.put("msgname","用户名不存在");
            return map;
        }
        if(!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","密码不正确");
            return map;
        }
        map.put("userId",user.getId());
        //ticket
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);


        return map;
    }
    private String addLoginTicket(int userId){
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();

    }


}
