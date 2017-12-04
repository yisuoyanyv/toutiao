package cn.zjlspace.async.handler;

import cn.zjlspace.async.EventHandler;
import cn.zjlspace.async.EventModel;
import cn.zjlspace.async.EventType;
import cn.zjlspace.model.Message;
import cn.zjlspace.model.User;
import cn.zjlspace.service.MessageService;
import cn.zjlspace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Override
    public void doHandle(EventModel model) {
        Message message=new Message();
        int fromId=model.getActorId();
        int toId=model.getEntityOwnerId();
        message.setFromId(fromId);
        message.setToId(toId);
        User user=userService.getUser(model.getActorId());
        message.setContent("用户"+user.getName()+"赞了你的咨询，httP://127.0.0.1:8080/news/"+model.getEntityId());
        message.setCreatedDate(new Date());
        message.setConversationId(fromId<toId?String.format("%d_%d",fromId,toId):String.format("%d_%d",toId,fromId));
        messageService.addMessage(message);


    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
