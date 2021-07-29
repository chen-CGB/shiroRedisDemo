package com.cgf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author cgf
 * @since 2020-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value="Users扩展对象", description="")
public class UserVo implements Serializable{


    @ApiModelProperty(value = "主键")
    private Integer userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "性别（0女 1男）")
    private Integer sex;

    @ApiModelProperty(value = "所读学校")
    private String school;

    @ApiModelProperty(value = "所读专业")
    private String major;

    @ApiModelProperty(value = "学历")
    private Integer education;

    @ApiModelProperty(value = "所在城市")
    private String city;

    @ApiModelProperty(value = "账号状态（0正常1异常2注销）")
    private Integer state;

    @ApiModelProperty(value = "token")
    @NotEmpty
    private String token;
}
