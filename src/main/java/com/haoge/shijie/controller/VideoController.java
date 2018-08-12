package com.haoge.shijie.controller;


import com.haoge.shijie.annotation.SerializedField;
import com.haoge.shijie.pojo.VideoBean;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.VideoService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class VideoController {

    private final String UPLOADPATH = "/upLoadFile/";

    @Autowired
    private VideoService videoService;

    //视频上传
    @PostMapping("/user/video")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean upLoadVideo(@RequestParam("file") MultipartFile[] file,
                                    @RequestParam("title") String title,
                                    @RequestParam("content") String content,
                                    @RequestHeader("Authorization") String token,
                                    HttpServletRequest request) {
        String filePath = request.getServletContext().getRealPath(UPLOADPATH);
        boolean success = videoService.addVideo(title, content, token, file, filePath);
        //String fileName = file.getOriginalFilename();
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            return new ResponseBean().failMethod(500, "上传失败");
        }
    }

    //获取视频
    @GetMapping("/user/video")
    @SerializedField(includes = {"code", "msg", "data"})
    public ResponseBean getVideo(@RequestParam("videoId") Integer videoId) {
        VideoBean videoBean = videoService.findVideo(videoId);
        return new ResponseBean().successMethod(videoBean);
    }

    //删除视频
    @DeleteMapping("/user/video")
    @SerializedField(includes = {"code", "msg", "data"})
    public ResponseBean delVideo(@RequestHeader("Authorization") String token,
                                 @RequestParam("videoId") Integer videoId
    ) {
        boolean success = videoService.delVideo(token, videoId);
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            return new ResponseBean().failMethod(500, "删除失败");
        }
    }

    //修改视频
    @PutMapping("/user/video")
    @SerializedField(includes = {"code", "msg", "data"})
    public ResponseBean modifyVideo(@RequestHeader("Authorization") String token,
                                    @RequestParam("videoId") Integer videoId,
                                    @RequestParam("title") String videoTitle,
                                    @RequestParam("content") String videoContent,
                                    @RequestParam("coverfile") MultipartFile coverFile,
                                    HttpServletRequest request) {
        VideoBean videoBean = new VideoBean();
        videoBean.setVideoId(videoId);
        videoBean.setVideoTitle(videoTitle);
        videoBean.setVideoContent(videoContent);
        String filePath = request.getServletContext().getRealPath(UPLOADPATH);
        boolean success = videoService.modifyVideo(videoBean, token, filePath, coverFile);
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            return new ResponseBean().failMethod(500, "修改失败");
        }

    }
}
