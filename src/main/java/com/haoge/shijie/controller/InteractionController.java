package com.haoge.shijie.controller;

import com.haoge.shijie.annotation.SerializedField;
import com.haoge.shijie.pojo.CommentatorBean;
import com.haoge.shijie.pojo.respModelBean.CommentList;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.CommentatorService;
import com.haoge.shijie.service.UserService;
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
    @Autowired
    private UserService userService;

    //通过to_videoId获取视频所有评论（不包括子评论）
    @GetMapping("/comment/{toVideoId}")
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean getComments(@PathVariable("toVideoId") int toVideoId) {
        List<CommentatorBean> commentators = service.findCommByVdoIds(toVideoId);
        return new ResponseBean().successMethod(commentators);
    }

    //通过to_videoId,to_userId获取视频所有子评论
    @GetMapping("/comment/{to_videoId}/{to_txtId}")
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean getCommentator(@PathVariable("to_videoId") int toVideoId,
                                       @PathVariable("to_txtId") int toTxtId) {
        List<CommentatorBean> commentators = service.findByVdoIdAndTuds(toVideoId, toTxtId);
        return new ResponseBean().successMethod(commentators);
    }

    //通过to_videoId获取视频所有评论
    @GetMapping("/comments/{toVideoId}")
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean getCommentList(@PathVariable("toVideoId") int toVideoId) {
        List<CommentList> commentLists = service.findAllCommByVdoId(toVideoId);
        return new ResponseBean().successMethod(commentLists);
    }

    //顶一下视频
    @PutMapping("/video/top/{toVideoId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean videoTop(@PathVariable("toVideoId") int toVideoId,
                                 @RequestHeader("Authorization") String token) {
        boolean success = videoService.modifyVideoTop(toVideoId, token);
        if (success) {
            return new ResponseBean().successMethod();
        }
        return new ResponseBean().failMethod(500, "顶一下视频未知错误");
    }

    //踩一下视频
    @PutMapping("/video/trample/{toVideoId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean videoTrample(@PathVariable("toVideoId") int VideoId,
                                     @RequestHeader("Authorization") String token) {
        boolean success = videoService.modifyVideoTrample(VideoId, token);
        if (success) {
            return new ResponseBean().successMethod();
        }
        return new ResponseBean().failMethod(500, "踩一下视频未知错误");
    }

    //视频播放量+1
    @PutMapping("/video/playcount/{toVideoId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean videoPlayCount(@PathVariable("toVideoId") int VideoId,
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
    public ResponseBean addVideoCommentator(@PathVariable("to_videoId") int toVideoId,
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
        return new ResponseBean().failMethod(500, "评论视频未知错误");
    }

    //评论某个人的评论
    @PostMapping("/comment/{to_videoId}/{to_txtId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean addUserCommentator(@PathVariable("to_videoId") int toVideoId,
                                           @PathVariable("to_txtId") int toTxtId,
                                           @RequestHeader("Authorization") String token,
                                           @RequestParam("content") String content) {
        CommentatorBean commentator = new CommentatorBean();
        commentator.setToVideoId(toVideoId);
        commentator.setTxtContext(content);
        commentator.setToTxtId(toTxtId);
        commentator.setCommentatorTipNum(0);
        commentator.setCommentatorTrampleNum(0);
        boolean success = service.addUserCommentator(commentator, token);
        if (success) {
            return new ResponseBean().successMethod();
        }
        return new ResponseBean().failMethod(500, "评论未知错误");
    }

    //顶一下评论
    @PutMapping("/comment/top/{txt_id}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean commentTop(@PathVariable("txt_id") int txtId,
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
    public ResponseBean commentTrample(@PathVariable("txt_id") int txtId,
                                       @RequestHeader("Authorization") String token) {
        boolean success = service.modifyCommentTrample(txtId, token);
        if (success) {
            return new ResponseBean().successMethod();
        }
        return new ResponseBean().failMethod(500, "踩一下评论未知错误");
    }

    //收藏视频
    @PostMapping("/collectVideo/{videoId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean collectVideo(@RequestHeader("Authorization") String token,
                                     @PathVariable("videoId") int videoId) {
        boolean success = userService.collectUserVideo(token, videoId);
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            return new ResponseBean().failMethod(500, "收藏视频未知错误");
        }
    }

    //取消收藏视频
    @DeleteMapping("/collectVideo/{videoId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean cancelCollectVideo(@RequestHeader("Authorization") String token,
                                           @PathVariable("videoId") int videoId) {
        boolean success = userService.cancelCollectUserVideo(token, videoId);
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            return new ResponseBean().failMethod(500, "取消收藏视频未知错误");
        }

    }
}
