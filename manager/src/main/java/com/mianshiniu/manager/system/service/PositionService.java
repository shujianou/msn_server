package com.mianshiniu.manager.system.service;


import com.mianshiniu.manager.system.entity.PositionEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 岗位表 服务类
 * </p>
 *
 * @author vim
 * @since 2019-01-06
 */
public interface PositionService extends IService<PositionEntity> {

    /**
     * 获取上级领导用户ID
     */
    public String getLeadershipId(String userId);
}
