package com.mianshiniu.manager.system.service.impl;


import com.mianshiniu.manager.system.entity.PositionEntity;
import com.mianshiniu.manager.system.entity.UserPositionEntity;
import com.mianshiniu.manager.system.mapper.UserPositionMapper;
import com.mianshiniu.manager.system.service.UserPositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户岗位关联表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-06
 */
@Service
public class UserPositionServiceImpl extends ServiceImpl<UserPositionMapper, UserPositionEntity> implements UserPositionService {

    @Override
    public List<PositionEntity> getPositionByUserIdAndOrgId(String userId, String orgId) {
        return null;
    }
}
