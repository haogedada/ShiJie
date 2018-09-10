package com.haoge.shijie.pojo.respModelBean;

import com.haoge.shijie.pojo.CommentatorBean;

import java.util.List;
public class CommentList{
        private int commentNum;
        private CommentatorBean comment;
        private List<CommentatorBean> comments;
        public CommentList(){
        }

    public CommentList(int commentNum, CommentatorBean comment, List<CommentatorBean> comments) {
        this.commentNum = commentNum;
        this.comment = comment;
        this.comments = comments;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public CommentatorBean getComment() {
        return comment;
    }

    public void setComment(CommentatorBean comment) {
        this.comment = comment;
    }

    public List<CommentatorBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentatorBean> comments) {
        this.comments = comments;
    }
}

