package com.mianshiniu.manager.system.service;


import com.mianshiniu.manager.system.entity.ResourceEntity;
import com.mianshiniu.manager.system.entity.dto.ResourceDto;
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
public interface ResourceService extends IService<ResourceEntity> {

    List<String> getResKeyList(String userId);

    List<String> getResNameList(String userId);

    List<ResourceDto> getMenuByUserId(String currentUserId, Integer button);

    List<ResourceDto> getMenuByRoleId(String roleId);

    /**
     * 获取所有菜单节点
     */
    List<ResourceDto> menuNodeList();

}
