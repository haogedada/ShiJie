package com.haoge.shijie.service;

import com.haoge.shijie.pojo.CommentatorBean;
import com.haoge.shijie.pojo.respModelBean.CommentList;

import java.util.List;

public interface CommentatorService {

    /**
     * 查询所有评论列表区域
     *
     * @return
     */
    List<CommentatorBean> findCommentatorList();

    /**
     * 根据videoId查询该视频的评论列表(不包含子评论)
     *
     * @param toVideoId
     * @return
     */
    List<CommentatorBean> findCommByVdoIds(int toVideoId);

    /**
     * 根据videoId和toUserId查询该视频的子评论列表
     *
     * @param toVideoId
     * @return
     */
    List<CommentatorBean> findByVdoIdAndTuds(int toVideoId, int toTxtId);

    /**
     * 根据videoId查询该视频的评论数
     *
     * @param toVideoId
     * @return
     */
    int findCountByVdoId(int toVideoId);

    /**
     * 根据UserId查询该视频的评论者的评论数
     *
     * @param toVideoId,toUserId
     * @return
     */
    int findCountByAndVidUid(int toVideoId, int toTxtId);


    /**
     * 新增区域信息
     *
     * @param commentator
     * @return
     */
    boolean addCommentator(CommentatorBean commentator);

    /**
     * 添加评论视频
     *
     * @param commentator,token
     * @return 是否成功
     */
    boolean addVideoCommentator(CommentatorBean commentator, String token);

    /**
     * 添加评论用户的评论
     *
     * @param commentator,token
     * @return 是否成功
     */
    boolean addUserCommentator(CommentatorBean commentator, String token);


    /**
     * 修改区域信息
     *
     * @param commentator
     * @return
     */
    boolean modifyCommentator(CommentatorBean commentator);

    /**
     * 根据id删除区域
     *
     * @param txtId
     * @return
     */
    boolean delCommentator(int txtId);


    /**
     * 评论顶一下
     *
     * @param txtId
     * @param token
     * @return 是否成功
     */
    boolean modifyCommentTop(int txtId, String token);

    /**
     * 评论踩一下
     *
     * @param txtId
     * @param token
     * @return 是否成功
     */
    boolean modifyCommentTrample(int txtId, String token);

    /**
     * 根据视频id获取该视频的所有评论
     *
     * @param toVideoId
     * @return
     */
    List<CommentList> findAllCommByVdoId(int toVideoId);
}
