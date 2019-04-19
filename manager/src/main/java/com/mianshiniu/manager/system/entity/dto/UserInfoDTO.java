package com.mianshiniu.manager.system.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息详情
 * Created by Vim 2019/2/17 12:04
 *
 * @author Vim
 */
@Data
@ApiModel("用户信息详情Model")
public class UserInfoDTO implements Serializable {

    @ApiModelProperty("用户ID")
    private String id;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("登录账号")
    private String account;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("身份证")
    private String idNo;


    @ApiModelProperty("座机号")
    private String telephone;

    @ApiModelProperty("办公室")
    private String office;

    @ApiModelProperty("工位")
    private String station;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("生日")
    private String birthday;

    @ApiModelProperty("性别(0:男,1:女)")
    private Integer sex;

    @ApiModelProperty("头像链接")
    private String avatarUrl;
}
