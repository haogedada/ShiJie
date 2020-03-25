package com.haoge.shijie.service.impl;

import com.haoge.shijie.dao.LoginDao;
import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.LoginService;
import com.haoge.shijie.service.UserService;
import com.haoge.shijie.util.JWTUtil;
import com.haoge.shijie.util.StrJudgeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginDao loginDao;
    @Autowired
    private UserService userService;

    @Override
    public UserBean findUserByName(String userName) {
        UserBean userBean = null;
        if (userName.equals("") || userName == null) {
            throw new RuntimeException("用户账号不合法");
        } else {
            userBean = loginDao.userLoginQueryByName(userName);
            if (userBean != null && StrJudgeUtil.isCorrectStr(userBean.getUserName())) {
                return userBean;
            } else {
                throw new RuntimeException("无结果");
            }
        }

    }

    @Override
    public ResponseBean getToken(String userName, String userPassword) {
        UserBean user = null;
        UserBean userBean = null;
        if (!StringUtils.isEmpty(userName) &&StringUtils.isEmpty(userPassword)) {
            try {
                userBean = loginDao.userLoginQueryByName(userName);
                user = userService.findUserAndAuxById(userBean.getUserId());
                if (userBean == null || user == null) {
                    throw new RuntimeException("用户不存在");
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("用户账号不合法");
        }
        if (!user.getAuxiliaryUserBean().getUserCode().equals("-1")) {
            return new ResponseBean().failMethod(110, "Account number is not activated");
        } else if (user.getUserPassword().equals(userPassword)) {
            //登录成功
            if (StrJudgeUtil.isCorrectStr(user.getHeadimgUrl()) &&
                    StrJudgeUtil.isCorrectStr(user.getUserSex()) &&
                    StrJudgeUtil.isCorrectStr(user.getUserNickname()) &&
                    StrJudgeUtil.isCorrectStr(user.getBardianSign()) &&
                    StrJudgeUtil.isCorrectStr(user.getUserBirthday())) {
                return new ResponseBean().successMethod(JWTUtil.sign(userName, userPassword));
            } else {
                //第一次登陆,没有修改资料
                return new ResponseBean(199, "first login", JWTUtil.sign(userName, userPassword));
            }

        } else if (!user.getUserPassword().equals(userPassword) || !user.getUserName().equals(userName)) {
            return new ResponseBean().failMethod(105, "username or password error");
        } else {
            throw new RuntimeException("login error");
        }
    }
}
