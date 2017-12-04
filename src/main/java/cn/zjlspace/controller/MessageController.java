package cn.zjlspace.controller;

import cn.zjlspace.model.HostHolder;
import cn.zjlspace.model.Message;
import cn.zjlspace.model.User;
import cn.zjlspace.model.ViewObject;
import cn.zjlspace.service.MessageService;
import cn.zjlspace.service.UserService;
import cn.zjlspace.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    private static final Logger logger= LoggerFactory.getLogger(MessageController.class);


    @RequestMapping(path = {"/msg/list"},method={RequestMethod.GET})
    public String conversationList(Model model, @Param("conversationId") String conversationId){
        try{
            int localUserId=hostHolder.getUser().getId();
            List<ViewObject> conversations=new ArrayList<>();
            List<Message> conversationList=messageService.getConversationList(localUserId,0,10);
            for(Message msg:conversationList){
                ViewObject vo=new ViewObject();
                vo.set("conversation",msg);
                int targetId=msg.getFromId()==localUserId?msg.getToId():msg.getFromId();
                User user=userService.getUser(targetId);
                vo.set("user",user);
                vo.set("unread",messageService.getConversationUnreadCount(localUserId,msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);

        }catch (Exception e){
            logger.error("获取站内信列表失败"+e.getMessage());

        }
        return "letter";

    }

    @RequestMapping(path = {"/msg/detail"},method={RequestMethod.GET})
    public String conversationDetail(Model model, @Param("conversationId") String conversationId){
        try{
            List<Message> conversationList=messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages=new ArrayList<>();
            for(Message msg:conversationList){
                ViewObject vo=new ViewObject();
                vo.set("message",msg);
                User user=userService.getUser(msg.getFromId());
                if(user==null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);

        }catch (Exception e){
            logger.error("获取详情消息失败"+e.getMessage());

        }
        return "letterDetail";


    }



    @RequestMapping(path = {"/msg/addMessage"},method = {RequestMethod.POST})
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content){
        try{
            Message msg=new Message();
            msg.setContent(content);
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setCreatedDate(new Date());
            msg.setConversationId(fromId<toId?String.format("%d_%d",fromId,toId):String.format("%d_%d",toId,fromId));

            messageService.addMessage(msg);
            return ToutiaoUtil.getJSONString(0,"添加成功");

        }catch (Exception e){
            logger.error("添加消息失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"添加消息失败！");
        }

    }
}
