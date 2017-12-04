package cn.zjlspace.controller;

import cn.zjlspace.async.EventModel;
import cn.zjlspace.async.EventProducer;
import cn.zjlspace.async.EventType;
import cn.zjlspace.service.UserService;
import cn.zjlspace.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
public class LoginController {
    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(value = {"/reg"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember",defaultValue = "0") int rememberme,
                      HttpServletResponse response){
        try{
            Map<String,Object> map=userService.register(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme>0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);

                return ToutiaoUtil.getJSONString(0,"注册成功！");
            }else{
                return ToutiaoUtil.getJSONString(1,map);
            }

        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"注册异常！");
        }


    }

    @RequestMapping(value = {"/login"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember",defaultValue = "0") int rememberme,
                      HttpServletResponse response){
        try{
            Map<String,Object> map=userService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme>0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                eventProducer.fireEvent(new EventModel(EventType.LOGIN).setActorId((int) map.get("userId"))
                        .setExt("username","zjl").setExt("email","zjl@qq.com"));

                return ToutiaoUtil.getJSONString(0,"登陆成功！");
            }else{
                return ToutiaoUtil.getJSONString(1,map);
            }

        }catch (Exception e){
            logger.error("登陆异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"登陆异常！");
        }


    }

    @RequestMapping(value = {"/logout"},method = {RequestMethod.GET,RequestMethod.POST})

    public String login(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";

    }




}

