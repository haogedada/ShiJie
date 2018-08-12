package com.haoge.shijie.service.impl;

import com.haoge.shijie.constant.Constants;
import com.haoge.shijie.dao.AuxiliaryUserDao;
import com.haoge.shijie.dao.UserDao;
import com.haoge.shijie.pojo.AuxiliaryUserBean;
import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.service.RegisterService;
import com.haoge.shijie.util.CodeUtil;
import com.haoge.shijie.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.haoge.shijie.constant.Constants.mailType.CODE;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private AuxiliaryUserDao auxiliaryUserDao;

    //查询数据库中是否存在该email
    @Override
    public boolean isExistenceEmail(String email) {
        int res = userDao.queryUserByEmail(email);
        if (res > 0) {
            return true;
        } else {
            return false;
        }
    }

    //根据userName查询数据库中是否存在该用户
    @Override
    public boolean isExistenceUser(String userName) {
        try {
            UserBean user = userDao.queryUserByName(userName);
            if (user == null || user.getUserId() == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 发送验证码邮件，设置邮件类型为验证码
     *
     * @param userName,email
     * @param email
     * @return
     */
    @Override
    @Transactional
    public boolean getUserCode(String userName, String email) {
        UserBean userBean = userDao.queryUserByName(userName);
        if (userBean != null && userBean.getUserEmail().equals(email)) {
            String code = CodeUtil.RandomCode();
            String dataCode = System.currentTimeMillis() + "-" + code;
            AuxiliaryUserBean auxiliaryUserBean = auxiliaryUserDao.queryAuxiliaryUserById(userBean.getUserId());
            auxiliaryUserBean.setCode(dataCode);
            try {
                int res = auxiliaryUserDao.updateAuxiliaryUser(auxiliaryUserBean);
                if (res > 0) {
                    String mailType =CODE.getName();
                    new Thread(new MailUtil(email, code, mailType)).start();
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                throw new RuntimeException("出现错误" + e.getMessage());
            }
        } else {
            throw new RuntimeException("用户或邮箱不正确");
        }
    }
}
