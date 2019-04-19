package com.mianshiniu.manager.system.service.impl;


import com.mianshiniu.manager.system.entity.UserRoleEntity;
import com.mianshiniu.manager.system.mapper.UserRoleMapper;
import com.mianshiniu.manager.system.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    @Override
    public void setUserRole(String id, String roleIds) {
        remove(new QueryWrapper<UserRoleEntity>().eq("user_id", id));

        String[] roleIdList = StringUtils.split(roleIds, ",");

        for (String roleId : roleIdList) {
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setCreateTime(new Date());
            userRoleEntity.setRoleId(roleId);
            userRoleEntity.setUserId(id);
            save(userRoleEntity);
        }
    }
}
