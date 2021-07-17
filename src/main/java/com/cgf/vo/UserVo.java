package com.cgf.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cgf.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author cgf
 * @since 2020-12-06
 */
@Data
@ApiModel(value="Users扩展对象", description="")
public class UserVo implements Serializable{

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "姓名")
    private String nickname;

    @ApiModelProperty(value = "上一次登录时间")
    private Timestamp lastLoginTime;

    @ApiModelProperty(value = "上一次登录IP")
    private String lastLoginIp;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "角色列表")
    private List<Integer> roleIds;

    @ApiModelProperty(value = "目录列表")
    private Set<String> menuList;

    @ApiModelProperty(value = "权限列表")
    private Set<String> permissionList;
}
