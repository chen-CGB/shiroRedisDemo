package com.cgf.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Email;
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
@ApiModel(value="Users扩展对象", description="")
public class EmailUserDto implements Serializable{
    @ApiModelProperty(value = "用户名")
    @NotEmpty
    private String username;

    @ApiModelProperty(value = "密码")
    @NotEmpty
    private String password;

    @ApiModelProperty(value = "邮箱地址")
    @Email
    private String email;

    @ApiModelProperty(value = "期望工作职位")
    private Integer[] expectPosition;

    @ApiModelProperty(value = "验证码")
    private String code;
}
