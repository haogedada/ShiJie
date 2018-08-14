package com.haoge.shijie.controller;

import com.haoge.shijie.annotation.SerializedField;
import com.haoge.shijie.pojo.CommentatorBean;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.CommentatorService;
import com.haoge.shijie.service.VideoService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/comment")
public class InteractionController {

    @Autowired
    private CommentatorService service;
    @Autowired
    private VideoService videoService;

    //通过to_videoId获取视频所有评论（不包括子评论）
    @GetMapping("/comment/{toVideoId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean getComments(@PathVariable("toVideoId") Integer toVideoId) {
        List<CommentatorBean> commentators = service.findCommByVdoIds(toVideoId);
        return new ResponseBean().successMethod(commentators);
    }

    //通过to_videoId,to_userId获取视频所有子评论
    @GetMapping("/comment/{to_videoId}/{to_userId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean getCommentator(@PathVariable("to_videoId") Integer toVideoId,
                                       @PathVariable("to_userId") Integer toUserId) {
        List<CommentatorBean> commentators = service.findByVdoIdAndTuds(toVideoId, toUserId);
        return new ResponseBean().successMethod(commentators);
    }

    //顶一下视频
    @PutMapping("/video/top/{to_videoId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean videoTop(@PathVariable("to_videoId") Integer toVideoId,
                                 @RequestHeader("Authorization") String token) {
        boolean success = videoService.modifyVideoTop(toVideoId, token);
        if (success) {
            return new ResponseBean().successMethod();
        }
        return new ResponseBean().failMethod(500, "顶一下视频未知错误");
    }

    //踩一下视频
    @PutMapping("/video/trample/{to_videoId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean videoTrample(@PathVariable("to_videoId") Integer VideoId,
                                     @RequestHeader("Authorization") String token) {
        boolean success = videoService.modifyVideoTrample(VideoId, token);
        if (success) {
            return new ResponseBean().successMethod();
        }
        return new ResponseBean().failMethod(500, "踩一下视频未知错误");
    }

    //视频播放量+1
    @PutMapping("/video/playcount/{to_videoId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean videoPlayCount(@PathVariable("to_videoId") Integer VideoId,
                                       @RequestHeader("Authorization") String token) {
        boolean success = videoService.modifyVideoPlayCount(VideoId, token);
        if (success) {
            return new ResponseBean().successMethod();
        }
        return new ResponseBean().failMethod(500, "修改播放量未知错误");
    }


    //评论某个视频
    @PostMapping("/comment/{to_videoId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean addVideoCommentator(@PathVariable("to_videoId") Integer toVideoId,
                                            @RequestHeader("Authorization") String token,
                                            @RequestParam("content") String content) {
        CommentatorBean commentator = new CommentatorBean();
        commentator.setToVideoId(toVideoId);
        commentator.setTxtContext(content);
        commentator.setCommentatorTipNum(0);
        commentator.setCommentatorTrampleNum(0);
        boolean success = service.addVideoCommentator(commentator, token);
        if (success) {
            return new ResponseBean().successMethod();
        }
        return new ResponseBean().failMethod(50, "评论视频未知错误");
    }

    //评论某个人的评论
    @PostMapping("/comment/{to_videoId}/{to_userId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean addUserCommentator(@PathVariable("to_videoId") Integer toVideoId,
                                           @PathVariable("to_userId") Integer toUserId,
                                           @RequestHeader("Authorization") String token,
                                           @RequestParam("content") String content) {
        CommentatorBean commentator = new CommentatorBean();
        commentator.setToVideoId(toVideoId);
        commentator.setTxtContext(content);
        commentator.setToUserId(toUserId);
        commentator.setCommentatorTipNum(0);
        commentator.setCommentatorTrampleNum(0);
        boolean success = service.addUserCommentator(commentator, token);
        if (success) {
            return new ResponseBean().successMethod();
        }
        return new ResponseBean().failMethod(50, "评论未知错误");
    }

    //顶一下评论
    @PutMapping("/comment/top/{txt_id}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean commentTop(@PathVariable("txt_id") Integer txtId,
                                   @RequestHeader("Authorization") String token) {
        boolean success = service.modifyCommentTop(txtId, token);
        if (success) {
            return new ResponseBean().successMethod();
        }
        return new ResponseBean().failMethod(500, "顶一下评论未知错误");
    }

    //踩一下评论
    @PutMapping("/comment/trample/{txt_id}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean commentTrample(@PathVariable("txt_id") Integer txtId,
                                       @RequestHeader("Authorization") String token) {
        boolean success = service.modifyCommentTrample(txtId, token);
        if (success) {
            return new ResponseBean().successMethod();
        }
        return new ResponseBean().failMethod(500, "踩一下评论未知错误");
    }

}
