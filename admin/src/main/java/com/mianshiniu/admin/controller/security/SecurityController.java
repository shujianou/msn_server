package com.mianshiniu.admin.controller.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mianshiniu.manager.system.entity.UserEntity;
import com.mianshiniu.manager.system.service.ResourceService;
import com.mianshiniu.manager.system.service.UserService;
import com.redimybase.common.util.SequenceUtils;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.security.shiro.constant.SecurityConst;
import com.redimybase.security.shiro.dao.UserCheckDao;
import com.redimybase.security.shiro.token.UserToken;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 权限控制Controller
 * Created by Irany 2018/5/13 23:35
 */
@RestController
@RequestMapping("security")
@Api(tags = "权限控制接口")
public class SecurityController {


    /**
     * 权限校验
     */
    @ApiOperation(value = "权限校验")
    @RequestMapping(value = "auth")
    public R<?> auth(@ApiParam(value = "校验类型") String type) {
        try {

            String msg = "未登录的用户";

            Subject subject = SecurityUtils.getSubject();

            if ((subject != null && subject.isAuthenticated()) || StringUtils.isEmpty(type)) {
                //type = "2";
                return new R<>(R.无权限, msg);
            }

            //int sign = 0;
            if (StringUtils.equals(SecurityConst.用户登录校验失败, type)) {
                msg = "用户名或密码错误";

            } else if (StringUtils.equals(SecurityConst.用户被封禁, type)) {
                msg = "用户被封禁";
            } else if (StringUtils.equals(SecurityConst.登录成功, type)) {
                return new R<>(R.登录成功, "登录成功");
            }
            return new R<>(R.无权限, msg);
        }catch (Exception e){
            throw  new BusinessException(R.无权限,"用户凭证已过期");
        }
    }


    @ApiOperation(value = "登录")
    @RequestMapping("login")
    public R<?> login(@RequestBody UserToken token) {
        Subject subject = SecurityUtils.getSubject();

        try {
            if (token.isAutoReg()) {
                //如果是自动注册则自动创建账号
                if (userService.count(new QueryWrapper<UserEntity>().lambda().eq(UserEntity::getAccount, token.getAccount())) > 0) {
                    return R.fail("自动创建账号失败");
                }
                UserEntity userEntity = new UserEntity();
                userEntity.setAccount(token.getAccount());
                userEntity.setPassword("1".equalsIgnoreCase(useSalt) ? SecurityUtil.encryptPwd(token.getPassword(), pwdSalt) : token.getPassword());
                userEntity.setCreateTime(new Date());
                userEntity.setUserName(SequenceUtils.getSequenceInStr("会员"));
                userEntity.setStatus(UserEntity.Status.ENABLE);
                userService.save(userEntity);
            }
            subject.login(new UsernamePasswordToken(token.getAccount(), token.getPassword(), true));
            return new R<>( subject.getSession().getId());
        } catch (IncorrectCredentialsException e) {
            return R.fail("用户名或密码错误");
        } catch (LockedAccountException e) {
            return R.fail("该用户已被冻结");
        } catch (AuthenticationException e) {
            return R.fail("该用户不存在");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.custom(404, "未知错误");
    }

    @RequestMapping(value = "info", name = "获取当前登录用户信息")
    @ApiOperation(value = "获取当前登录用户信息")
    public R<?> info() {
        R<Map<String, Object>> result = new R<>();
        Map<String, Object> data = new HashMap<>();
        data.put("user", SecurityUtil.getCurrentUser());
        data.put("resources", resourceService.getMenuByUserId(SecurityUtil.getCurrentUserId(),0));
        result.setData(data);
        return result;
    }

    @Value("${redi.pwd.salt}")
    private String pwdSalt;

    @Value("${redi.pwd.useSalt}")
    private String useSalt;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCheckDao userCheckDao;

    @Autowired
    private ResourceService resourceService;

}
