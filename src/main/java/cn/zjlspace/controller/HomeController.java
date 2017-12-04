package cn.zjlspace.controller;

import cn.zjlspace.model.EntityType;
import cn.zjlspace.model.HostHolder;
import cn.zjlspace.model.News;
import cn.zjlspace.model.ViewObject;
import cn.zjlspace.service.LikeService;
import cn.zjlspace.service.NewsService;
import cn.zjlspace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;

    private List<ViewObject> getNews(int userId,int offset,int limit){
        List<News> newsList=newsService.getLatestNews(userId,offset,limit);
        int localUserId=hostHolder.getUser()!=null?hostHolder.getUser().getId():0;

        List<ViewObject> vos=new ArrayList<>();
        for(News news:newsList){
            ViewObject vo=new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));

            if(localUserId!=0){
                vo.set("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));
            }else{
                vo.set("like",0);
            }
            vos.add(vo);
        }
        return vos;


    }

    @RequestMapping(value = {"/","/index"},method = {RequestMethod.GET,RequestMethod.POST})
    public String index(Model model){
        model.addAttribute("vos",getNews(0,0,10));

        return "home";

    }

    @RequestMapping(path={"/user/{userId}"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId, @RequestParam(value = "pop",defaultValue = "0") int pop){
        model.addAttribute("vos",getNews(userId,0,10));
        model.addAttribute("pop",pop);
        return "home";
    }

}
