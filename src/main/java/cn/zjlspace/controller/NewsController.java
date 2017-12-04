package cn.zjlspace.controller;

import cn.zjlspace.dao.CommentDAO;
import cn.zjlspace.model.*;
import cn.zjlspace.service.*;
import cn.zjlspace.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {
    private static final Logger logger= LoggerFactory.getLogger(NewsController.class);
    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;



    @RequestMapping(path = {"/news/{newsId}"},method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){
        News news=newsService.getById(newsId);
        if(news!=null){
            int localUserId=hostHolder.getUser()!=null?hostHolder.getUser().getId():0;

            if(localUserId!=0){
                model.addAttribute("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));
            }else {
                model.addAttribute("like", 0);
            }

            //comment
            List<Comment> comments=commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs=new ArrayList<ViewObject>();
            model.addAttribute("news",news);
            model.addAttribute("owner",userService.getUser(news.getUserId()));
            for(Comment comment:comments){
                ViewObject vo=new ViewObject();
                vo.set("comment",comment);
                vo.set("user",userService.getUser(comment.getUserId()));
                commentVOs.add(vo);
            }
            model.addAttribute("comments",commentVOs);

        }
        return "detail";
    }

    @RequestMapping(path = {"/user/addNews"},method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                         @RequestParam("title") String title,
                         @RequestParam("link") String link
                         ){
        try{
            News news=new News();
            if(hostHolder.getUser()!=null){
                news.setUserId(hostHolder.getUser().getId());
            }else{
                //匿名id
                news.setUserId(3);
            }
//            logger.info(title);
            news.setImage(image);
            news.setTitle(title);
            news.setCreatedDate(new Date());
            news.setLink(link);

            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0,"添加成功");

        }catch (Exception e){
            logger.error("读取图片错误"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"添加失败");
        }
    }

    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content){
        try{
            //TODO 过滤敏感词
            Comment comment=new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addCommnet(comment);
            //更新news里的评论数量
            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(),count);
            //怎么异步化

        }catch (Exception e){
            logger.error("添加评论失败"+e.getMessage());
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }

    @RequestMapping(path = {"/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response){
        try{
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+imageName))
                    ,response.getOutputStream());
        }catch (Exception e){
            logger.error("读取图片错误"+e.getMessage());
        }
    }


    @RequestMapping(value = "/uploadImage",method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file")MultipartFile file){
        try{
//            String fileUrl=newsService.savaImage(file);
            String fileUrl=qiniuService.saveImage(file);
            if(fileUrl==null){
                return ToutiaoUtil.getJSONString(1,"上传失败");
            }
            return ToutiaoUtil.getJSONString(0,fileUrl);


        }catch (Exception e){
            e.printStackTrace();

            logger.error("上传图片失败！"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传失败");


        }

    }


}
