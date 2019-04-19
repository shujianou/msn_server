package com.mianshiniu.manager.system.service;

import com.mianshiniu.manager.system.entity.RoleEntity;
import com.mianshiniu.manager.system.entity.UserRoleEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
public interface RoleService extends IService<RoleEntity> {

    List<String> getRoleNameList(String userId);

    /**
     * 根据用户ID获取角色ID集合
     */
    List<UserRoleEntity> getRoleIdByUserId(String userId);
}
