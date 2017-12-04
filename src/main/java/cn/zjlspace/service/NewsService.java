package cn.zjlspace.service;

import cn.zjlspace.dao.NewsDAO;
import cn.zjlspace.model.News;
import cn.zjlspace.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id, count);
    }
    public int updateLikeCount(int id, int count) {
        return newsDAO.updateLikeCount(id, count);
    }

    public News getById(int newsId ){
        return newsDAO.getById(newsId);
    }

    public List<News> getLatestNews(int userId,int offset,int limit){
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }
    public int addNews(News news) {
        newsDAO.addnews(news);
        return news.getId();
    }



    public String savaImage(MultipartFile file) throws Exception {
        int doPos=file.getOriginalFilename().lastIndexOf(".");
        if(doPos<0){
            return null;
        }
        String fileExt=file.getOriginalFilename().substring(doPos+1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(fileExt)){
           return null;
        }
        String fileName= UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR+fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+fileName;


    }


}
