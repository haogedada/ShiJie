package com.haoge.shijie.dao;


import com.haoge.shijie.pojo.AuxiliaryUserBean;

import java.util.List;

public interface AuxiliaryUserDao {
    /**
     * 查询所有区域
     *
     * @return
     */
    List<AuxiliaryUserBean> queryAuxiliaryUserList();

    /**
     * 根据id查询区域
     *
     * @param userId
     * @return
     */
    AuxiliaryUserBean queryAuxiliaryUserById(Integer userId);

    /**
     * 根据userCode查询区域
     *
     * @param userCode
     * @return
     */
    AuxiliaryUserBean queryAuxUserByCode(String userCode);

    /**
     * 新增区域信息
     *
     * @param user
     * @return
     */
    int insertAuxiliaryUser(AuxiliaryUserBean user);

    /**
     * 修改区域信息
     *
     * @param user
     * @return
     */
    int updateAuxiliaryUser(AuxiliaryUserBean user);

    /**
     * 根据id删除区域
     *
     * @param userId
     * @return
     */
    int deleteAuxiliaryUser(Integer userId);
}
