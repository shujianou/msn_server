package com.mianshiniu.manager.system.service.impl;

import com.mianshiniu.manager.system.entity.PositionEntity;
import com.mianshiniu.manager.system.entity.UserOrgEntity;
import com.mianshiniu.manager.system.entity.UserPositionEntity;
import com.mianshiniu.manager.system.mapper.PositionMapper;
import com.mianshiniu.manager.system.service.PositionService;
import com.mianshiniu.manager.system.service.UserOrgService;
import com.mianshiniu.manager.system.service.UserPositionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 岗位表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-06
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, PositionEntity> implements PositionService {

    @Override
    public String getLeadershipId(String userId) {
        //流程发起人所属部门的关联实体
        UserOrgEntity userOrgEntity = userOrgService.getOne(new QueryWrapper<UserOrgEntity>().eq("user_id",userId).select("org_id"));

        //部门负责人
        PositionEntity positionEntity = positionService.getOne(new QueryWrapper<PositionEntity>().eq("org_id", userOrgEntity.getOrgId()).eq("level_id", PositionEntity.Level.部门负责人));
        //部门负责人与岗位的关联表
        UserPositionEntity userPositionEntity = userPositionService.getOne(new QueryWrapper<UserPositionEntity>().eq("position_id", positionEntity.getId()).select("user_id"));
        return userPositionEntity.getUserId();
    }


    @Autowired
    private UserOrgService userOrgService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private UserPositionService userPositionService;
}
