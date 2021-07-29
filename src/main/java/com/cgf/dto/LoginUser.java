package com.cgf.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginUser {

    @ApiModelProperty(value = "账号")
    @NotNull(message = "账号不能为空")
    private String username;

    @ApiModelProperty(value = "用户密码")
    @NotNull(message = "密码不能为空")
    private String password;
}
