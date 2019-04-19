package com.mianshiniu.manager.system.service.impl;


import com.mianshiniu.manager.system.entity.UserEntity;
import com.mianshiniu.manager.system.entity.dto.UserAddressListDTO;
import com.mianshiniu.manager.system.entity.dto.UserInfoDTO;
import com.mianshiniu.manager.system.entity.dto.UserListPage;
import com.mianshiniu.manager.system.mapper.UserMapper;
import com.mianshiniu.manager.system.service.UserService;
import com.mianshiniu.manager.user.entity.UserExtEntity;
import com.mianshiniu.manager.user.service.UserExtService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.common.util.SecurityUtils;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {


    /**
     * 重置密码
     */
    @Override
    public String resetPwd(String id) {
        try {
            String newPwd = "123456";
            //SecurityUtil.getResetPwd(8);
            String saltPwd = SecurityUtils.encryptPwd(newPwd, pwdSalt);
            UserEntity userEntity = new UserEntity();
            userEntity.setId(id);
            userEntity.setPassword(saltPwd);
            updateById(userEntity);
            return newPwd;
        } catch (Exception e) {
            log.error("用户重置密码出错,msg:{}", e);
        }
        return null;
    }

    @Override
    public Map<String, List<UserAddressListDTO>> getAddressList(String orgId, String orgName) {
        if (StringUtils.isEmpty(orgId)) {
            orgId = null;
        }
        if (StringUtils.isEmpty(orgName)) {
            orgName = null;
        } else {
            orgName = "%" + orgName + "%";
        }
        List<UserAddressListDTO> addressList = baseMapper.getAddressList(orgId, orgName);
        addressList.forEach(userAddressListDTO -> userAddressListDTO.setOrgName(baseMapper.getOrgFullName(userAddressListDTO.getOrgId())));
        return new UserAddressSort().sort(addressList);
    }

    @Override
    public String getOrgFullName(String orgId) {
        return baseMapper.getOrgFullName(orgId);
    }


    @Override
    public void updatePassword(String oldPwd, String newPwd, UserEntity currentUser) {

        try {
            String saltOldPwd = SecurityUtils.encryptPwd(oldPwd, pwdSalt);
            if (StringUtils.equals(currentUser.getPassword(), saltOldPwd)) {
                String saltNewPwd = SecurityUtils.encryptPwd(newPwd, pwdSalt);
                currentUser.setPassword(saltNewPwd);
                updateById(currentUser);
            }
        } catch (Exception e) {
            log.error("用户修改密码出错,msg:{}", e);
        }
    }

    @Override
    public UserListPage<UserEntity> getUserByOrdId(UserListPage<UserEntity> page) {

        if (StringUtils.isBlank(page.getPhone())) {
            page.setPhone("");
        } else {
            page.setPhone("%" + page.getPhone() + "%");
        }

        if (StringUtils.isBlank(page.getUserName())) {
            page.setUserName("");
        } else {
            page.setUserName("%" + page.getUserName() + "%");
        }

        if (StringUtils.isBlank(page.getOrgId())) {
            page.setOrgId(null);
        }

        if (StringUtils.isBlank(page.getOrgName())) {
            page.setOrgName("");
        } else {
            page.setOrgName("%" + page.getOrgName() + "%");
        }

        if (StringUtils.isBlank(page.getAccount())) {
            page.setAccount("");
        } else {
            page.setAccount("%" + page.getAccount() + "%");
        }

        if (StringUtils.isBlank(page.getEmail())) {
            page.setEmail("");
        } else {
            page.setEmail("%" + page.getEmail() + "%");
        }

        return baseMapper.getUserByOrgId(page, page.getOrgId(), page.getPhone(), page.getUserName(), page.getOrgName(), page.getAccount(), page.getEmail());
    }

    @Override
    public List<String> getUserIdByOrgId(String orgId, String status) {
        return baseMapper.getUserIdByOrgId(orgId, status);
    }

    @Override
    public void saveInfo(UserInfoDTO userInfoDTO) {
        try {
            if (StringUtils.isBlank(userInfoDTO.getId())) {
                //新增

                UserEntity userEntity = new UserEntity();
                userEntity.setAccount(userInfoDTO.getAccount());
                userEntity.setPhone(userInfoDTO.getPhone());
                userEntity.setIdNo(userInfoDTO.getIdNo());
                userEntity.setEmail(userInfoDTO.getEmail());

                if (count(new QueryWrapper<UserEntity>().lambda().in(UserEntity::getStatus, UserEntity.Status.ENABLE, UserEntity.Status.DISABLE)
                        .and(i -> i.eq(UserEntity::getAccount, userEntity.getAccount()).or().eq(UserEntity::getPhone, userEntity.getPhone()).or()
                                .eq(UserEntity::getEmail, userEntity.getEmail()).or().eq(UserEntity::getIdNo, userEntity.getIdNo()))) > 0) {
                    throw new BusinessException(R.失败, "登录账号/手机号/邮箱/身份证已存在,请尝试重新添加");
                }

                userEntity.setUserName(userInfoDTO.getUserName());
                if (StringUtils.isBlank(userInfoDTO.getPassword())) {
                    userEntity.setPassword(SecurityUtils.encryptPwd(defaultPassword, pwdSalt));
                } else if (userInfoDTO.getPassword().length() > 20) {
                    //加密的密码长度为32位,来判断是否修改密码
                    userEntity.setPassword(SecurityUtils.encryptPwd(userInfoDTO.getPassword(), pwdSalt));
                }

                userEntity.setAvatarUrl(userInfoDTO.getAvatarUrl());
                userEntity.setSex(userInfoDTO.getSex());
                userEntity.setStatus(UserEntity.Status.ENABLE);
                userEntity.setCreateTime(new Date());

                save(userEntity);

                UserExtEntity userExtEntity = new UserExtEntity();
                userExtEntity.setUserId(userEntity.getId());
                userExtEntity.setStation(userInfoDTO.getStation());
                userExtEntity.setOffice(userInfoDTO.getOffice());
                userExtEntity.setBirthday(userInfoDTO.getBirthday());

                userExtService.save(userExtEntity);
            } else {
                //修改

                UserEntity userEntity = new UserEntity();
                userEntity.setId(userInfoDTO.getId());
                userEntity.setAccount(userInfoDTO.getAccount());
                userEntity.setPhone(userInfoDTO.getPhone());
                userEntity.setIdNo(userInfoDTO.getIdNo());

                userEntity.setUserName(userInfoDTO.getUserName());
                if (StringUtils.isNotBlank(userInfoDTO.getPassword())) {
                    userEntity.setPassword(SecurityUtils.encryptPwd(userInfoDTO.getPassword(), pwdSalt));
                }

                userEntity.setAvatarUrl(userInfoDTO.getAvatarUrl());
                userEntity.setSex(userInfoDTO.getSex());
                userEntity.setUpdateTime(new Date());

                updateById(userEntity);

                UserExtEntity userExtEntity = new UserExtEntity();
                userExtEntity.setStation(userInfoDTO.getStation());
                userExtEntity.setOffice(userInfoDTO.getOffice());
                userExtEntity.setBirthday(userInfoDTO.getBirthday());

                userExtService.update(userExtEntity, new QueryWrapper<UserExtEntity>().lambda().eq(UserExtEntity::getUserId, userInfoDTO.getId()));
            }
        } catch (Exception e) {
            throw new BusinessException(R.失败, e.getMessage());
        }
    }

    @Autowired
    private UserExtService userExtService;

    @Value("${redi.pwd.salt}")
    public String pwdSalt;

    @Value("${redi.default.init.password}")
    private String defaultPassword;
}
