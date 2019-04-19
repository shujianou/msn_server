package com.mianshiniu.manager.system.mapper;

import com.mianshiniu.manager.system.entity.UserEntity;
import com.mianshiniu.manager.system.entity.dto.UserAddressListDTO;
import com.mianshiniu.manager.system.entity.dto.UserListPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
//@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {


    public List<UserAddressListDTO> getAddressList(@Param("orgId") String orgId, @Param("orgName") String orgName);

    public UserListPage<UserEntity> getUserByOrgId(UserListPage<UserEntity> page, @Param("orgId") String orgId,
                                                   @Param("phone") String phone, @Param("userName") String userName,
                                                   @Param("orgName")String orgName,@Param("account") String account,
                                                   @Param("email") String email);

    public String getOrgFullName(@Param("orgId") String orgId);

    public List<String> getUserIdByOrgId(@Param("orgId") String orgId, @Param("status") String status);
}
