package com.mianshiniu.admin.controller.system;

import com.mianshiniu.manager.system.entity.UserEntity;
import com.mianshiniu.manager.system.entity.UserRoleEntity;
import com.mianshiniu.manager.system.entity.dto.UserInfoDTO;
import com.mianshiniu.manager.system.entity.dto.UserListPage;
import com.mianshiniu.manager.system.mapper.UserMapper;
import com.mianshiniu.manager.system.service.UserOrgService;
import com.mianshiniu.manager.system.service.UserRoleService;
import com.mianshiniu.manager.system.service.impl.UserServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 用户信息Controller
 * Created by Vim 2018/11/23 13:30
 *
 * @author Vim
 */
@RestController
@RequestMapping("user")
@Api(tags = {"用户信息接口"})
public class UserController extends TableController<String, UserEntity, UserMapper, UserServiceImpl> {


    @PostMapping("saveInfo")
    @ApiOperation("新增/修改用户信息")
    public R<?> saveInfo(UserInfoDTO userInfoDTO) {
        service.saveInfo(userInfoDTO);
        return R.ok();
    }

    @PostMapping("getUserRoleId")
    @ApiOperation("获取用户所拥有的角色ID")
    public R<?> getUserRoleId(String userId){
        return new R<>(userRoleService.list(new QueryWrapper<UserRoleEntity>().lambda().eq(UserRoleEntity::getUserId, userId).select(UserRoleEntity::getId, UserRoleEntity::getRoleId)));
    }


    /**
     * 获取用户列表
     */
    @PostMapping("getUserList")
    @ApiOperation(value = "获取用户列表")
    public R<?> getUserList(UserListPage<UserEntity> page) {
        return new R<>(service.getUserByOrdId(page));
    }

    /**
     * 获取人员通讯录
     *
     * @param orgId 组织ID,不传默认查询全部人员通讯录
     */
    @PostMapping("getAddressList")
    @RequiresPermissions("system_address_list")
    @ApiOperation(value = "获取人员通讯录")
    public R<?> getAddressList(@ApiParam(value = "组织ID,不传默认查询全部人员通讯录") String orgId, @ApiParam(value = "组织名称") String orgName) {
        return new R<>(service.getAddressList(orgId, orgName));
    }

    /**
     * 重置随机密码
     */
    @PostMapping("resetPwd")
    @RequiresPermissions("system_user")
    @ApiOperation(value = "重置随机密码")
    public R<?> resetPassword(@ApiParam(value = "用户ID") String id) {
        return R.ok("新密码为:" + service.resetPwd(id));
    }


    /**
     * 设置用户角色
     */
    @PostMapping("serUserRole")
    @RequiresPermissions("system_user")
    @ApiOperation(value = "设置用户角色")
    public R<?> serUserRole(@ApiParam(value = "用户ID") String id, @ApiParam(value = "角色ID集合") String roleIdList) {

        userRoleService.setUserRole(id, roleIdList);
        return R.ok();
    }

    /**
     * 迁移用户(从一个组织迁移到另一个组织)
     */
    @PostMapping("move")
    @ApiOperation(value = "迁移用户(从一个组织迁移到另一个组织)")
    public R<?> move(@ApiParam(value = "需要迁移的组织ID") String sourceOrgId, @ApiParam(value = "目标组织ID") String targetOrgId) {
        return R.ok();
    }

    @Override
    public void beforeSave(UserEntity entity) {
        if (StringUtils.isBlank(entity.getId())) {

            if (service.count(new QueryWrapper<UserEntity>().in("status", UserEntity.Status.ENABLE, UserEntity.Status.DISABLE)
                    .and(i -> i.eq("account", entity.getAccount()).or().eq("phone", entity.getPhone()).or()
                            .eq("email", entity.getEmail()).or().eq("id_no", entity.getIdNo()))) > 0) {

                throw new BusinessException(R.失败, "登录账号/手机号/邮箱/身份证已存在,请尝试重新添加");
            }

            //如果密码为空默认123456
            if (StringUtils.isBlank(entity.getPassword())) {
                try {
                    entity.setPassword(SecurityUtil.encryptPwd(defaultPassword,pwdSalt));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            entity.setStatus(UserEntity.Status.ENABLE);
            entity.setCreateTime(new Date());

        } else {
            entity.setUpdateTime(new Date());
        }
    }

    @Override
    public void afterSave(UserEntity entity) {
        /*if (userOrgService.count(new QueryWrapper<UserOrgEntity>().lambda().eq(UserOrgEntity::getUserId, entity.getId())) ==0) {
            UserOrgEntity userOrgEntity = new UserOrgEntity();
            userOrgEntity.setOrgId(entity.getOrgId());
            userOrgEntity.setUserId(entity.getId());
            userOrgEntity.setCreateTime(new Date());
            userOrgService.save(userOrgEntity);
        }*/
    }

    @Override
    public R<?> delete(String id) {
        if (StringUtils.isBlank(id)) {
            return R.fail();
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setStatus(UserEntity.Status.DELETED);
        service.updateById(userEntity);
        return R.ok();
    }

    @Override
    public R<?> deleteBatchIds(String ids) {
        for (String id : StringUtils.split(ids, ",")) {
            this.delete(id);
        }
        return R.ok();
    }
    @Value("${redi.pwd.salt}")
    public String pwdSalt;

    @Value("${redi.default.init.password}")
    private String defaultPassword;

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserOrgService userOrgService;

    @Override
    protected UserServiceImpl getService() {
        return service;
    }

}
