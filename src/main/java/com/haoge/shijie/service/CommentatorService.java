package com.haoge.shijie.service;

import com.haoge.shijie.pojo.CommentatorBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentatorService {

    /**
     * 查询所有评论列表区域
     * @return
     */
    List<CommentatorBean> findCommentatorList();
    /**
     * 根据videoId查询该视频的评论列表(不包含子评论)
     * @param toVideoId
     * @return
     */
    List<CommentatorBean> findCommByVdoIds(Integer toVideoId);

    /**
     * 根据videoId和toUserId查询该视频的子评论列表
     * @param toVideoId
     * @return
     */
    List<CommentatorBean> findByVdoIdAndTuds(@Param("toVideoId") Integer toVideoId, @Param("toUserId") Integer toUserId);
    /**
     * 根据videoId查询该视频的评论数
     * @param toVideoId
     * @return
     */
    int findCountByVdoId(Integer toVideoId);
    /**
     * 根据UserId查询该视频的评论者的评论数
     * @param toUserId
     * @return
     */
    int findCountByUserId(Integer toUserId);


    /**
     * 新增区域信息
     * @param commentator
     * @return
     */
    boolean addCommentator(CommentatorBean commentator);
    /**
     * 评论视频
     * @param toVideoId,token,content
     * @return 是否成功
     */
    boolean addVideoCommentator(Integer toVideoId, String token, String content);
    /**
     * 评论用户的评论
     * @param toVideoId,toUserId,token,content
     * @return 是否成功
     */
    boolean addUserCommentator(Integer toVideoId, Integer toUserId, String token, String content);


    /**
     * 修改区域信息
     * @param commentator
     * @return
     */
    boolean modifyCommentator(CommentatorBean commentator);
    /**
     * 根据id删除区域
     * @param txtId
     * @return
     */
    boolean delCommentator(Integer txtId);

    /**
     * 视频顶一下
     * @param toVideoId
     * @param token
     * @return 是否成功
     */
    boolean modifyVideoTop(Integer toVideoId, String token);
    /**
     * 视频踩一下
     * @param VideoId
     * @param token
     * @return 是否成功
     */
    boolean modifyVideoTrample(Integer VideoId, String token);

    /**
     * 视频播放量+1
     * @param VideoId
     * @param token
     * @return 是否成功
     */
    boolean modifyVideoPlayCount(Integer VideoId, String token);

    /**
     * 评论顶一下
     * @param txtId
     * @param token
     * @return 是否成功
     */
    boolean modifyCommentTop(Integer txtId, String token);

    /**
     * 评论踩一下
     * @param txtId
     * @param token
     * @return 是否成功
     */
    boolean modifyCommentTrample(Integer txtId, String token);
}
