package com.haoge.shijie.dao;

import com.haoge.shijie.pojo.CommentatorBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentatorDao {
    /**
     * 查询所有评论列表区域
     *
     * @return
     */
    List<CommentatorBean> queryCommentatorList();

    /**
     * 根据videoId查询该视频的评论列表(不包含子评论)
     *
     * @param toVideoId
     * @return
     */
    List<CommentatorBean> queryCommByVdoIds(Integer toVideoId);

    /**
     * 根据videoId查询该视频的评论列表(包含子评论)
     *
     * @param toVideoId
     * @return
     */
    List<CommentatorBean> queryAllCommByVdoIds(Integer toVideoId);

    /**
     * 根据videoId和toUserId查询该视频的子评论列表
     *
     * @param toVideoId
     * @return
     */
    List<CommentatorBean> queryByVdoIdAndTuds(@Param("toVideoId") Integer toVideoId, @Param("toTxtId") Integer toTxtId);

    /**
     * 根据videoId查询该视频的评论数
     *
     * @param toVideoId
     * @return
     */
    int queryCountByVdoId(Integer toVideoId);

    /**
     * 根据UserId查询该视频的评论者的评论数
     *
     * @param toVideoId,toUserId
     * @return
     */
    int queryCountByVidAndUid(@Param("toVideoId") Integer toVideoId,@Param("toTxtId") Integer toTxtId);


    /**
     * 新增区域信息
     *
     * @param commentator
     * @return
     */
    int insertCommentator(CommentatorBean commentator);

    /**
     * 修改区域信息
     *
     * @param commentator
     * @return
     */
    int updateCommentator(CommentatorBean commentator);

    /**
     * 修改Num加1区域信息
     *
     * @param txtId
     * @return
     */
    int updateCommTopAdd(Integer txtId);

    /**
     * 修改Num减1区域信息
     *
     * @param txtId
     * @return
     */
    int updateCommTraAdd(Integer txtId);

    /**
     * 根据id删除区域
     *
     * @param txtId
     * @return
     */
    int deleteCommentator(Integer txtId);
}
