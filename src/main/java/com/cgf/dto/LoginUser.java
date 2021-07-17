package com.cgf.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Users扩展对象", description="")
public class LoginUser {

    @ApiModelProperty(value = "账号")
    @NotNull(message = "账号不能为空")
    private String username;

    @ApiModelProperty(value = "用户密码")
    @NotNull(message = "密码不能为空")
    private String password;
}
