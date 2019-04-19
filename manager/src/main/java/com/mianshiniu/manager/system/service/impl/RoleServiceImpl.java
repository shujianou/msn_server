package com.mianshiniu.manager.system.service.impl;


import com.mianshiniu.manager.system.entity.RoleEntity;
import com.mianshiniu.manager.system.entity.UserRoleEntity;
import com.mianshiniu.manager.system.mapper.RoleMapper;
import com.mianshiniu.manager.system.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    @Override
    public List<String> getRoleNameList(String userId) {
        return baseMapper.getRoleNameList(userId);
    }

    @Override
    public List<UserRoleEntity> getRoleIdByUserId(String userId) {
        return baseMapper.getRoleIdByUserId(userId);
    }
}
