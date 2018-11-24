package com.haoge.shijie.service.impl;

import com.haoge.shijie.dao.AuxiliaryUserDao;
import com.haoge.shijie.pojo.AuxiliaryUserBean;
import com.haoge.shijie.service.AuxiliaryUserService;
import com.haoge.shijie.service.UserService;
import com.haoge.shijie.util.StrJudgeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuxiliaryUserServiceImpl implements AuxiliaryUserService {
    @Autowired
    private AuxiliaryUserDao auxiliaryUserDao;
    @Autowired
    private UserService userService;

    @Override
    public List<AuxiliaryUserBean> findAuxiliaryUsers() {
        List<AuxiliaryUserBean> auxiliaryUsers = auxiliaryUserDao.queryAuxiliaryUserList();
        if (auxiliaryUsers.size() > 0) {
            return auxiliaryUsers;
        } else {
            throw new RuntimeException("没有集合");
        }
    }

    @Override
    public AuxiliaryUserBean findAuxiliaryUserById(int userId) {
        if (StrJudgeUtil.isCorrectInt(userId)) {
            AuxiliaryUserBean bean = auxiliaryUserDao.queryAuxiliaryUserById(userId);
            if (bean == null) {
                throw new RuntimeException("不存在该用户信息");
            } else {
                return bean;
            }
        } else {
            throw new RuntimeException("用户id不合法");
        }
    }

    @Override
    public AuxiliaryUserBean findAuxUserByCode(String userCode) {
        if (StrJudgeUtil.isCorrectStr(userCode)) {
            AuxiliaryUserBean auxiliaryUserBean = auxiliaryUserDao.queryAuxUserByCode(userCode);
            if (auxiliaryUserBean == null) {
                throw new RuntimeException("激活码不存在");
            } else {
                return auxiliaryUserBean;
            }
        } else {
            throw new RuntimeException("激活码不合法");
        }
    }

    @Override
    @Transactional
    public boolean addAuxiliaryUser(AuxiliaryUserBean auxiliaryUser) {
        if (auxiliaryUser.getUserId() != null && !auxiliaryUser.getUserId().equals("")) {
            try {
                int res = auxiliaryUserDao.insertAuxiliaryUser(auxiliaryUser);
                if (res > 0) {
                    return true;
                } else {
                    throw new RuntimeException("AuxiliaryUser插入用户信息失败！");
                }
            } catch (Exception e) {
                throw new RuntimeException("AuxiliaryUser插入用户信息失败！：" + e.getMessage());
            }
        } else {
            throw new RuntimeException("AuxiliaryUser用户id不能为空！");
        }
    }

    @Override
    @Transactional
    public boolean modifyAuxiliaryUser(AuxiliaryUserBean auxiliaryUser) {
        if (StrJudgeUtil.isCorrectInt(auxiliaryUser.getUserId())) {
            try {
                int res = auxiliaryUserDao.updateAuxiliaryUser(auxiliaryUser);
                if (res > 0) {
                    return true;
                } else {
                    throw new RuntimeException("AuxiliaryUser更新用户信息失败！");
                }
            } catch (Exception e) {
                throw new RuntimeException("AuxiliaryUser插入用户信息失败！：" + e.getMessage());
            }
        } else {
            throw new RuntimeException("用户id不合法！");
        }
    }

    @Override
    @Transactional
    public boolean delAuxiliaryUser(int userId) {
        if (StrJudgeUtil.isCorrectInt(userId)) {
            try {
                int res = auxiliaryUserDao.deleteAuxiliaryUser(userId);
                if (res > 0) {
                    return true;
                } else {
                    throw new RuntimeException("AuxiliaryUser删除用户信息失败！");
                }
            } catch (Exception e) {
                throw new RuntimeException("AuxiliaryUser删除用户信息失败：" + e.getMessage());
            }
        } else {
            throw new RuntimeException("用户id不合法");
        }
    }

    /**
     * 激活用户
     *
     * @param code
     * @return
     */
    @Override
    @Transactional
    public boolean activationUser(String code) {
        if (StrJudgeUtil.isCorrectStr(code) && !code.equals("-1")) {
            String codeTime = code.split("-")[0];
            AuxiliaryUserBean auxiliaryUser = null;
            if ((System.currentTimeMillis() - ((Long.valueOf(codeTime) / 609218) + 19970715)) < (1000) * 60 * 60 * 2) {
                try {
                    auxiliaryUser = this.findAuxUserByCode(code);
                    auxiliaryUser.setUserCode("-1");
                    boolean res = this.modifyAuxiliaryUser(auxiliaryUser);
                    if (res) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                try {
                    auxiliaryUser = findAuxUserByCode(code);
                    boolean res = this.delAuxiliaryUser(auxiliaryUser.getUserId());
                    if (res) {
                        boolean ures = userService.delUser(auxiliaryUser.getUserId());
                        if (ures) {
                            throw new RuntimeException("激活码失效，请重新注册");
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        } else {
            throw new RuntimeException("激活码不合法");
        }
        return false;
    }
}
