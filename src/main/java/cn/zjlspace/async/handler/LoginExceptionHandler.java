package cn.zjlspace.async.handler;

import cn.zjlspace.async.EventHandler;
import cn.zjlspace.async.EventModel;
import cn.zjlspace.async.EventType;
import cn.zjlspace.model.Message;
import cn.zjlspace.service.MessageService;
import cn.zjlspace.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class LoginExceptionHandler implements EventHandler{
    @Autowired
    MessageService messageService;
    @Autowired
    MailSender mailSender;


    @Override
    public void doHandle(EventModel model) {
        //判断是否有异常登陆
        Message message=new Message();
        int fromId=1;
        int toId=model.getActorId();
        message.setToId(model.getActorId());
        message.setContent("你上次的登陆异常！");
        message.setFromId(3);
        message.setCreatedDate(new Date());
        message.setConversationId(fromId<toId?String.format("%d_%d",fromId,toId):String.format("%d_%d",toId,fromId));

        messageService.addMessage(message);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("username",model.getExt("username"));

        mailSender.sendWithHTMLTemplate(model.getExt("emial"),
                "登陆异常","mails/welcome",map);



    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
