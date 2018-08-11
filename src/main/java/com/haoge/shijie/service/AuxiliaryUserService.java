package com.haoge.shijie.service;

import com.haoge.shijie.pojo.AuxiliaryUserBean;

import java.util.List;

public interface AuxiliaryUserService {

    /**
     * 查询所有区域
     * @return
     */
    List<AuxiliaryUserBean> findAuxiliaryUsers();

    /**
     * 根据id查询区域
     * @param userId
     * @return
     */
    AuxiliaryUserBean findAuxiliaryUserById(Integer userId);
    /**
     * 根据userCode查询区域
     * @param userCode
     * @return
     */
    AuxiliaryUserBean findAuxUserByCode(String userCode);

    /**
     * 新增区域信息
     * @param auxiliaryUser
     * @return
     */
    boolean addAuxiliaryUser(AuxiliaryUserBean auxiliaryUser);

    /**
     * 修改区域信息
     * @param auxiliaryUser
     * @return
     */
    boolean modifyAuxiliaryUser(AuxiliaryUserBean auxiliaryUser);

    /**
     * 根据id删除区域
     * @param userId
     * @return
     */
    boolean delAuxiliaryUser(Integer userId);
    /**
     * 激活用户
     * @param code
     * @return
     */
    int activationUser(String code) throws Exception;
}
