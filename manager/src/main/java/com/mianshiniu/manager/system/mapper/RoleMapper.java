package com.mianshiniu.manager.system.mapper;

import com.mianshiniu.manager.system.entity.RoleEntity;
import com.mianshiniu.manager.system.entity.UserRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {
    List<String> getRoleNameList(String userId);

    @Select("select role_id from t_user_role where user_id=#{userId}")
    List<UserRoleEntity> getRoleIdByUserId(@Param("userId") String userId);
}
