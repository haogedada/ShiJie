package com.haoge.shijie.service.impl;

import com.haoge.shijie.dao.AuxiliaryUserDao;
import com.haoge.shijie.dao.UserDao;
import com.haoge.shijie.pojo.AuxiliaryUserBean;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.AuxiliaryUserService;
import com.haoge.shijie.util.CodeUtil;
import com.haoge.shijie.util.DesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuxiliaryUserServiceImpl implements AuxiliaryUserService {
    @Autowired
    private AuxiliaryUserDao auxiliaryUserDao;
    @Autowired
    private UserDao userDao;
    @Override
    public List<AuxiliaryUserBean> findAuxiliaryUsers() {
        return auxiliaryUserDao.queryAuxiliaryUserList();
    }
    @Override
    public AuxiliaryUserBean findAuxiliaryUserById(Integer userID) {
        return auxiliaryUserDao.queryAuxiliaryUserById(userID);
    }
    @Override
    public AuxiliaryUserBean findAuxUserByCode(String userCode) {
        return auxiliaryUserDao.queryAuxUserByCode(userCode);
    }

    @Override
    @Transactional
    public boolean addAuxiliaryUser(AuxiliaryUserBean auxiliaryUser) {
        if(auxiliaryUser.getUserId()!=null&&!auxiliaryUser.getUserId().equals("")){
            try{
                int res=auxiliaryUserDao.insertAuxiliaryUser(auxiliaryUser);
                if(res>0){
                    return true;
                }else {
                    throw new RuntimeException("AuxiliaryUser插入用户信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException("AuxiliaryUser插入用户信息失败！："+e.getMessage());
            }
        }else {
            throw new RuntimeException("AuxiliaryUser用户id不能为空！");
        }
    }

    @Override
    @Transactional
    public boolean modifyAuxiliaryUser(AuxiliaryUserBean auxiliaryUser) {
        if(auxiliaryUser.getUserId()!=null&&!auxiliaryUser.getUserId().equals("")){
            try{
                int res=auxiliaryUserDao.updateAuxiliaryUser(auxiliaryUser);
                if(res>0){
                    return true;
                }else {
                    throw new RuntimeException("AuxiliaryUser更新用户信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException("AuxiliaryUser插入用户信息失败！："+e.getMessage());
            }
        }else {
            throw new RuntimeException("AuxiliaryUser用户id不能为空！");
        }
    }

    @Override
    @Transactional
    public boolean delAuxiliaryUser(Integer userID) {
        if(userID!=null&&userID>0){
        try {
            int res = auxiliaryUserDao.deleteAuxiliaryUser(userID);
            if(res>0){
                return true;
            }else {
                throw new RuntimeException("AuxiliaryUser删除用户信息失败！");
            }
        }catch (Exception e){
            throw new RuntimeException("AuxiliaryUser删除用户信息失败："+e.getMessage());
        }
        }else{
        throw new RuntimeException("AuxiliaryUser用户id不能为空！");
       }
}

    @Override
    @Transactional
    public int activationUser(String code){
        if(code!=null&&!code.equals("")&&!code.equals("-1")) {
            String codeTime = code.split("-")[0];
            AuxiliaryUserBean auxiliaryUser = null;
            if ((System.currentTimeMillis() -(( Long.valueOf(codeTime)/609218)+19970715)) < (1000)*60*60*2) {
                try {
                    auxiliaryUser = auxiliaryUserDao.queryAuxUserByCode(code);
                    auxiliaryUser.setUserCode("-1");
                    try {
                        int res = auxiliaryUserDao.updateAuxiliaryUser(auxiliaryUser);
                        if (res > 0) {
                            return 1;
                        } else {
                           return 2;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage() + "激活码更新出错\n");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage()+"激活码错误或不存在\n");
                }
            }else {
            try {
                auxiliaryUser = auxiliaryUserDao.queryAuxUserByCode(code);
                try {
                    int res = auxiliaryUserDao.deleteAuxiliaryUser(auxiliaryUser.getUserId());
                    if (res > 0) {
                        try {
                            int ures = userDao.deleteUser(auxiliaryUser.getUserId());
                            if (ures > 0) {
                                return 0;
                            }else {
                                throw new RuntimeException("User删除失败\n");
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage() + "User删除出错\n");
                        }
                    } else {
                        throw new RuntimeException("AuxiliaryUser删除出错\n");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage() + "AuxiliaryUser激活码删除出错\n");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage()+"激活码错误或不存在\n");
            }
       }
        }
        return -1;
    }
}
