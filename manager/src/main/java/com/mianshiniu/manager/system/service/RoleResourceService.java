package com.mianshiniu.manager.system.service;


import com.mianshiniu.manager.system.entity.RoleResourceEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mianshiniu.manager.system.entity.UserEntity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
public interface RoleResourceService extends IService<RoleResourceEntity> {

    /**
     * 设置角色对应的菜单
     */
    public void setRoleResource(String id, String resourceIds, UserEntity currentUser);
}
