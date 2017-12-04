package cn.zjlspace.controller;

import cn.zjlspace.async.EventModel;
import cn.zjlspace.async.EventProducer;
import cn.zjlspace.async.EventType;
import cn.zjlspace.model.EntityType;
import cn.zjlspace.model.HostHolder;
import cn.zjlspace.model.News;
import cn.zjlspace.service.LikeService;
import cn.zjlspace.service.NewsService;
import cn.zjlspace.util.ToutiaoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @Autowired
    NewsService newsService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId")int newsId){
        int userId=hostHolder.getUser().getId();
        News news=newsService.getById(newsId);
        long likeCount=likeService.like(userId, EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int) likeCount);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(newsId)
        .setEntityType(EntityType.ENTITY_NEWS).setEntityOwnerId(news.getUserId()));
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String disLike(@RequestParam("newsId")int newsId){
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.disLike(userId, EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int) likeCount);
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
