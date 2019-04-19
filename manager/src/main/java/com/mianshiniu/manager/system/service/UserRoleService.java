package com.mianshiniu.manager.system.service;


import com.mianshiniu.manager.system.entity.UserRoleEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
public interface UserRoleService extends IService<UserRoleEntity> {

    /**
     * 设置用户角色
     */
    public void setUserRole(String id, String roleIds);
}
