package com.haoge.shijie.controller;


import com.haoge.shijie.annotation.SerializedField;
import com.haoge.shijie.pojo.VideoBean;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.VideoService;
import com.haoge.shijie.util.StrJudgeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "视频控制器")
@RestController
public class VideoController {

    private final String UPLOADPATH = "/upLoadFile/";
    @Autowired
    private VideoService videoService;

    //视频上传
    @PostMapping("/user/video")
    @ApiOperation("视频上传")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean upLoadVideo(@RequestParam("file")@ApiParam("文件") MultipartFile[] file,
                                    @RequestParam("title")@ApiParam("视频标题") String title,
                                    @RequestParam("content")@ApiParam("视频描述") String content,
                                    @RequestParam("type")@ApiParam("视频类型") String type,
                                    @RequestHeader("Authorization") String token,
                                    HttpServletRequest request) {
        String filePath = request.getServletContext().getRealPath(UPLOADPATH);
        VideoBean videoBean = new VideoBean();
        videoBean.setVideoTitle(title);
        videoBean.setVideoContent(content);
        videoBean.setVideoType(type);
        boolean success = videoService.addVideo(videoBean, token, file, filePath);
        //String fileName = file.getOriginalFilename();
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            return new ResponseBean().failMethod(500, "上传失败");
        }
    }

    //获取视频
    @GetMapping("/user/video/{videoId}")
    @ApiOperation("获取视频")
    @SerializedField(includes = {"code", "msg", "data"})
    public ResponseBean getVideo(@PathVariable("videoId")@ApiParam("视频id") int videoId) {
        VideoBean videoBean = videoService.findVideoByVid(videoId);
        return new ResponseBean().successMethod(videoBean);
    }

    //删除视频
    @DeleteMapping("/user/video/{videoId}")
    @RequiresAuthentication
    @ApiOperation("删除视频")
    @SerializedField(includes = {"code", "msg", "data"})
    public ResponseBean delVideo(@RequestHeader("Authorization") String token,
                                 @PathVariable("videoId")@ApiParam("视频id") int videoId
    ) {
        boolean success = videoService.delVideoByVid(token, videoId);
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            return new ResponseBean().failMethod(500, "删除失败");
        }
    }

    //修改视频
    @PostMapping("/user/modifyVideo")
    @RequiresAuthentication
    @ApiOperation("修改视频")
    @SerializedField(includes = {"code", "msg", "data"})
    public ResponseBean modifyVideo(@RequestHeader("Authorization") String token,
                                    @RequestParam("videoId")@ApiParam("视频id") int videoId,
                                    @RequestParam("title")@ApiParam("视频标题") String videoTitle,
                                    @RequestParam("content")@ApiParam("视频描述") String videoContent,
                                    @RequestParam("type")@ApiParam("视频类型") String videoType,
                                    @RequestParam("coverfile")@ApiParam("视频封面文件") MultipartFile coverFile,
                                    HttpServletRequest request) {
        boolean success = false;
        if (coverFile.getSize() <= 0) {
            success = videoService.modifyVideo(request);
        } else {
            VideoBean videoBean = new VideoBean();
            videoBean.setVideoId(videoId);
            videoBean.setVideoTitle(videoTitle);
            videoBean.setVideoContent(videoContent);
            videoBean.setVideoType(videoType);
            String filePath = request.getServletContext().getRealPath(UPLOADPATH);
            success = videoService.modifyVideo(videoBean, token, filePath, coverFile);
        }
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            return new ResponseBean().failMethod(500, "修改失败");
        }
    }
}
