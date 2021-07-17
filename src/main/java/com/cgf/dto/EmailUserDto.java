package com.cgf.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @PackageName com.cgf.entity
 * @ClassName EmailUserVo
 * @Description //TODO
 * @Author cgf
 * @Date 2021/1/17 21:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="用户注册", description="")
public class EmailUserDto implements Serializable{
    @ApiModelProperty(name = "用户名")
    @NotEmpty
    private String username;

    @ApiModelProperty(name = "密码")
    @NotEmpty
    private String password;

    @ApiModelProperty(name = "邮箱地址")
    @Email
    private String email;

    @ApiModelProperty(name = "期望工作职位")
    private Integer[] expectPosition;

    @ApiModelProperty(name = "验证码")
    private String code;
}
