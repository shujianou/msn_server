package com.mianshiniu.manager.system.service;


import com.mianshiniu.manager.system.entity.UserEntity;
import com.mianshiniu.manager.system.entity.dto.UserAddressListDTO;
import com.mianshiniu.manager.system.entity.dto.UserInfoDTO;
import com.mianshiniu.manager.system.entity.dto.UserListPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 重置密码
     */
    public String resetPwd(String id);

    /**
     * 获取人员通讯录
     */
    public Map<String, List<UserAddressListDTO>> getAddressList(String orgId, String orgName);


    /**
     * 获取组织全称
     */
    public String getOrgFullName(String orgId);


    /**
     * 修改密码
     */
    public void updatePassword(String oldPwd, String newPwd, UserEntity currentUser);

    /**
     * 根据组织ID获取用户列表
     */
    public UserListPage<UserEntity> getUserByOrdId(UserListPage<UserEntity> page);

    /**
     * 根据组织ID获取用户ID
     */
    public List<String> getUserIdByOrgId(String orgId, String status);

    /**
     * 新增/修改用户信息
     */
    public void saveInfo(UserInfoDTO userInfoDTO);

}
