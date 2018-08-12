package com.haoge.shijie.service.impl;

import com.haoge.shijie.dao.CommentatorDao;
import com.haoge.shijie.dao.UserDao;
import com.haoge.shijie.dao.VideoDao;
import com.haoge.shijie.pojo.CommentatorBean;
import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.service.CommentatorService;
import com.haoge.shijie.util.JWTUtil;
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
    private UserDao userDao;
    @Autowired
    private VideoDao videoDao;


    @Override
    public List<CommentatorBean> findCommentatorList() {
        return commentatorDao.queryCommentatorList();
    }

    @Override
    public List<CommentatorBean> findCommByVdoIds(Integer toVideoId) {
        if (toVideoId == null || toVideoId < 0) {
            throw new RuntimeException("toVideoId值非法");
        }
        return commentatorDao.queryCommByVdoIds(toVideoId);
    }

    @Override
    public List<CommentatorBean> findByVdoIdAndTuds(Integer toVideoId, Integer toUserId) {
        if (toVideoId == null || toVideoId < 0) {
            throw new RuntimeException("toVideoId,toUserId值非法");
        }
        return commentatorDao.queryByVdoIdAndTuds(toVideoId, toUserId);
    }

    @Override
    public int findCountByVdoId(Integer toVideoId) {
        if (toVideoId == null || toVideoId < 0) {
            throw new RuntimeException("toVideoId值非法");
        }
        return commentatorDao.queryCountByVdoId(toVideoId);
    }

    @Override
    public int findCountByUserId(Integer toUserId) {
        if (toUserId == null || toUserId < 0) {
            throw new RuntimeException("toVideoId值非法");
        }
        return commentatorDao.queryCountByUserId(toUserId);
    }

    @Override
    @Transactional
    public boolean addCommentator(CommentatorBean commentator) {
        if ((commentator.getToUserId() != null && commentator.getToUserId() >= 0) &&
                (commentator.getToVideoId() != null && commentator.getToVideoId() >= 0)) {
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
            throw new RuntimeException("toUserId,toVideoId值非法");
        }
    }

    @Override
    @Transactional
    public boolean addVideoCommentator(Integer toVideoId, String token, String content) {
        if (StrJudgeUtil.isCorrectInt(toVideoId) &&
                StrJudgeUtil.isCorrectStr(token) &&
                StrJudgeUtil.isCorrectStr(content)) {
            String userName = JWTUtil.getUsername(token);
            UserBean userBean = userDao.queryUserByName(userName);
            CommentatorBean commentator = new CommentatorBean();
            commentator.setToVideoId(toVideoId);
            commentator.setTxtContext(content);
            commentator.setUserId(userBean.getUserId());
            commentator.setToUserId(-1);
            commentator.setCommentatorTipNum(0);
            commentator.setCommentatorTrampleNum(0);
            try {
                int res = commentatorDao.insertCommentator(commentator);
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

    @Override
    @Transactional
    public boolean addUserCommentator(Integer toVideoId, Integer toUserId, String token, String content) {
        if (StrJudgeUtil.isCorrectInt(toVideoId) &&
                StrJudgeUtil.isCorrectStr(token) &&
                StrJudgeUtil.isCorrectStr(content) &&
                StrJudgeUtil.isCorrectInt(toUserId)) {
            String userName = JWTUtil.getUsername(token);
            UserBean userBean = userDao.queryUserByName(userName);
            CommentatorBean commentator = new CommentatorBean();
            commentator.setToVideoId(toVideoId);
            commentator.setTxtContext(content);
            commentator.setUserId(userBean.getUserId());
            commentator.setToUserId(toUserId);
            commentator.setCommentatorTipNum(0);
            commentator.setCommentatorTrampleNum(0);
            try {
                int res = commentatorDao.insertCommentator(commentator);
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

    @Override
    @Transactional
    public boolean modifyCommentator(CommentatorBean commentator) {
        if ((commentator.getToUserId() != null && commentator.getToUserId() >= 0) &&
                (commentator.getToVideoId() != null && commentator.getToVideoId() >= 0)) {
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
            throw new RuntimeException("toUserId,toVideoId值非法");
        }
    }

    @Override
    @Transactional
    public boolean delCommentator(Integer txtId) {
        if (txtId != null && txtId >= 0) {
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
            throw new RuntimeException("txtId非法");
        }
    }

    @Override
    @Transactional
    public boolean modifyVideoTop(Integer VideoId, String token) {
        if (StrJudgeUtil.isCorrectInt(VideoId)) {
            try {
                int res = videoDao.updateVdoTopAdd(VideoId);
                if (res > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage() + "顶一下出错");
            }
        } else {
            throw new RuntimeException("顶一下视频参数错误");
        }
    }

    @Override
    @Transactional
    public boolean modifyVideoTrample(Integer VideoId, String token) {
        if (StrJudgeUtil.isCorrectInt(VideoId)) {
            try {
                int res = videoDao.updateVdeoTraAdd(VideoId);
                if (res > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage() + "踩一下出错");
            }
        } else {
            throw new RuntimeException("踩一下视频参数错误");
        }
    }

    @Override
    @Transactional
    public boolean modifyVideoPlayCount(Integer VideoId, String token) {
        if (StrJudgeUtil.isCorrectInt(VideoId)) {
            try {
                int res = videoDao.updatePlayCountAdd(VideoId);
                if (res > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage() + "播放量+1出错");
            }
        } else {
            throw new RuntimeException("修改播放量参数错误");
        }
    }

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
