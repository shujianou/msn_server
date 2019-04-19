package com.mianshiniu.admin.controller.system;


import com.mianshiniu.manager.system.entity.PositionEntity;
import com.mianshiniu.manager.system.mapper.PositionMapper;
import com.mianshiniu.manager.system.service.impl.PositionServiceImpl;
import com.redimybase.framework.web.TableController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门职位Controller
 * Created by Vim 2019/1/6 11:59
 *
 * @author Vim
 */
@RestController
@RequestMapping("system/position")
@Api(tags = "部门职位接口")
public class PositionController extends TableController<String, PositionEntity, PositionMapper, PositionServiceImpl> {



    @Autowired
    private PositionServiceImpl service;
    @Override
    protected PositionServiceImpl getService() {
        return service;
    }
}
