package com.haoge.shijie.service.impl;

import com.haoge.shijie.dao.CommentatorDao;
import com.haoge.shijie.pojo.CommentatorBean;
import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.service.CommentatorService;
import com.haoge.shijie.service.UserService;
import com.haoge.shijie.util.StrJudgeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentatorServiceImpl implements CommentatorService {
    @Autowired
    private CommentatorDao commentatorDao;
    @Autowired
    private UserService userService;

    /**
     * 查找所有评论
     *
     * @return
     */
    @Override
    public List<CommentatorBean> findCommentatorList() {
        List<CommentatorBean> commentatorBeans = commentatorDao.queryCommentatorList();
        if (commentatorBeans.size() > 0) {
            return commentatorBeans;
        } else {
            throw new RuntimeException("没有数据");
        }
    }

    /**
     * 查找视频的评论
     *
     * @param toVideoId
     * @return
     */
    @Override
    public List<CommentatorBean> findCommByVdoIds(Integer toVideoId) {
        if (StrJudgeUtil.isCorrectInt(toVideoId)) {
            List<CommentatorBean> commentatorBeans = commentatorDao.queryCommByVdoIds(toVideoId);
            if (commentatorBeans.size() > 0) {
                return commentatorBeans;
            } else {
                throw new RuntimeException("没有数据");
            }
        } else {
            throw new RuntimeException("参数不合法");
        }
    }

    /**
     * 查找评论列表根据视频id和用户id
     *
     * @param toVideoId
     * @param toUserId
     * @return
     */
    @Override
    public List<CommentatorBean> findByVdoIdAndTuds(Integer toVideoId, Integer toUserId) {
        if (StrJudgeUtil.isCorrectInt(toVideoId) && StrJudgeUtil.isCorrectInt(toUserId)) {
            List<CommentatorBean> commentatorBeans = commentatorDao.queryByVdoIdAndTuds(toVideoId, toUserId);
            if (commentatorBeans.size() > 0) {
                return commentatorBeans;
            } else {
                throw new RuntimeException("没有数据");
            }
        } else {
            throw new RuntimeException("参数不合法");
        }
    }

    /**
     * 根据videoId查询该视频的评论数
     *
     * @param toVideoId
     * @return
     */
    @Override
    public int findCountByVdoId(Integer toVideoId) {
        if (StrJudgeUtil.isCorrectInt(toVideoId)) {
            int count = commentatorDao.queryCountByVdoId(toVideoId);
            if (count >= 0) {
                return count;
            } else {
                throw new RuntimeException("数据异常");
            }
        } else {
            throw new RuntimeException("参数不合法");
        }
    }

    /**
     * 根据UserId查询该视频的评论者的评论数
     *
     * @param toUserId
     * @return
     */
    @Override
    public int findCountByUserId(Integer toUserId) {
        if (StrJudgeUtil.isCorrectInt(toUserId)) {
            int count = commentatorDao.queryCountByUserId(toUserId);
            if (count >= 0) {
                return count;
            } else {
                throw new RuntimeException("数据异常");
            }
        } else {
            throw new RuntimeException("参数不合法");
        }
    }

    @Override
    @Transactional
    public boolean addCommentator(CommentatorBean commentator) {
        if (StrJudgeUtil.isCorrectInt(commentator.getToUserId()) &&
                StrJudgeUtil.isCorrectInt(commentator.getToVideoId())) {
            try {
                int res = commentatorDao.insertCommentator(commentator);
                if (res > 0) {
                    return true;
                } else {
                    throw new RuntimeException("插入评论失败");
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("参数不合法");
        }
    }

    /**
     * 添加视频评论
     *
     * @param content
     * @param token
     * @return
     */
    @Override
    @Transactional
    public boolean addVideoCommentator(CommentatorBean content, String token) {
        if (content != null && StrJudgeUtil.isCorrectInt(content.getToVideoId())) {
            UserBean userBean = userService.findUserByToken(token);
            content.setUserId(userBean.getUserId());
            content.setToUserId(-1);
            try {
                int res = commentatorDao.insertCommentator(content);
                if (res > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage() + "插入视频评论出错");
            }
        } else {
            throw new RuntimeException("提交评论参数不合法");
        }
    }

    /**
     * 添加评论者的评论
     *
     * @param content 评论
     * @param token
     * @return
     */
    @Override
    @Transactional
    public boolean addUserCommentator(CommentatorBean content, String token) {
        if (content != null && StrJudgeUtil.isCorrectInt(content.getToVideoId()) &&
                StrJudgeUtil.isCorrectInt(content.getToUserId())) {
            UserBean userBean = userService.findUserByToken(token);
            content.setUserId(userBean.getUserId());
            try {
                int res = commentatorDao.insertCommentator(content);
                if (res > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage() + "插入用户评论出错");
            }
        } else {
            throw new RuntimeException("提交评论参数不合法");
        }
    }

    /**
     * 修改评论
     *
     * @param commentator
     * @return
     */
    @Override
    @Transactional
    public boolean modifyCommentator(CommentatorBean commentator) {
        if (commentator != null && StrJudgeUtil.isCorrectInt(commentator.getToVideoId())) {
            try {
                int res = commentatorDao.updateCommentator(commentator);
                if (res > 0) {
                    return true;
                } else {
                    throw new RuntimeException("修改评论失败");
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("参数非法");
        }
    }

    /**
     * 删除评论
     *
     * @param txtId
     * @return
     */
    @Override
    @Transactional
    public boolean delCommentator(Integer txtId) {
        if (StrJudgeUtil.isCorrectInt(txtId)) {
            try {
                int res = commentatorDao.deleteCommentator(txtId);
                if (res > 0) {
                    return true;
                } else {
                    throw new RuntimeException("删除评论失败");
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("参数不合法");
        }
    }


    /**
     * 评论顶一下
     *
     * @param txtId
     * @param token
     * @return
     */
    @Override
    @Transactional
    public boolean modifyCommentTop(Integer txtId, String token) {
        if (StrJudgeUtil.isCorrectInt(txtId)) {
            try {
                int res = commentatorDao.updateCommTopAdd(txtId);
                if (res > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage() + "评论顶一下出错");
            }
        } else {
            throw new RuntimeException("评论顶一下参数错误");
        }
    }

    /**
     * 评论踩一下
     *
     * @param txtId
     * @param token
     * @return
     */
    @Override
    @Transactional
    public boolean modifyCommentTrample(Integer txtId, String token) {
        if (StrJudgeUtil.isCorrectInt(txtId)) {
            try {
                int res = commentatorDao.updateCommTraAdd(txtId);
                if (res > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage() + "评论踩一下出错");
            }
        } else {
            throw new RuntimeException("评论踩一下参数错误");
        }
    }
}
