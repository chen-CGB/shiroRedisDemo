package com.cgf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Timestamp;

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
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value="SysUser对象", description="")
public class SysUser implements Serializable{

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "主键")
  @TableId(value = "user_id", type = IdType.AUTO)
  private Integer userId;


  @ApiModelProperty(value = "用户名")
  @TableField("username")
  @NotEmpty
  private String username;


  @ApiModelProperty(value = "姓名")
  @TableField("nickname")
  private String nickname;

  @ApiModelProperty(value = "邮箱")
  @Email
  private String email;

  @ApiModelProperty(value = "密码")
  private String password;

  @ApiModelProperty(value = "加密盐")
  private String salt;

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

  //@ApiModelProperty(value = "期望工作职位")
  //@TableField("createTime")
  //private String expectPosition;

  @ApiModelProperty(value = "账号状态（0正常1异常2注销）")
  private Integer status;

  @DateTimeFormat(pattern="yyyy")
  @ApiModelProperty(value = "毕业年份")
  @TableField("year_gaduation")
  private String yearGaduation;

  @ApiModelProperty(value = "注册时间")
  @TableField("create_time")
  private Timestamp createTime;

  @ApiModelProperty(value = "修改时间")
  @TableField("update_time")
  private Timestamp updateTime;

  @ApiModelProperty(value = "修改人用户ID")
  @TableField("updater")
  private Integer updater;

  @ApiModelProperty(value = "修改人用户ID")
  @TableField("creator")
  private Integer creator;


  @ApiModelProperty(value = "简历地址")
  private String resume;

  @ApiModelProperty(value = "上一次登录时间")
  @TableField("last_login_time")
  private Timestamp lastLoginTime;

  @ApiModelProperty(value = "上一次登录IP")
  @TableField("last_login_ip")
  private String lastLoginIp;


  /**
   * 密码盐. 重新对盐重新进行了定义，用户名+salt，这样就不容易被破解，可以采用多种方式定义加盐
   *
   * @return
   */
  public String getCredentialsSalt() {
    return this.username + this.salt;
  }
}
