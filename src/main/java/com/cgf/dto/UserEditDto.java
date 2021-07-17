package com.cgf.dto;

import com.cgf.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: cgf
 * @date: 2021/5/30
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="用户信息权限扩展", description="")
public class UserEditDto extends SysUser {
    @ApiModelProperty(value = "权限列表")
    List<Integer> roleIds;
}
