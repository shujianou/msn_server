package com.mianshiniu.manager.system.service.impl;


import com.mianshiniu.manager.system.entity.RoleResourceEntity;
import com.mianshiniu.manager.system.entity.UserEntity;
import com.mianshiniu.manager.system.mapper.RoleResourceMapper;
import com.mianshiniu.manager.system.service.RoleResourceService;
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
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResourceEntity> implements RoleResourceService {

    /**
     * 设置角色对应的菜单
     */
    @Override
    public void setRoleResource(String id, String resourceIds, UserEntity currentUser) {
        //移除该角色所有的菜单权限
        remove(new QueryWrapper<RoleResourceEntity>().eq("role_id", id));

        //重新遍历添加
        String[] resourceIdList = StringUtils.split(resourceIds, ",");

        for (String resourceId : resourceIdList) {
            RoleResourceEntity roleResourceEntity = new RoleResourceEntity();
            roleResourceEntity.setCreateTime(new Date());
            roleResourceEntity.setCreator(currentUser.getUserName());
            roleResourceEntity.setCreatorId(currentUser.getId());
            roleResourceEntity.setResourceId(resourceId);
            roleResourceEntity.setRoleId(id);
            save(roleResourceEntity);
        }

    }
}
