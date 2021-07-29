package com.cgf.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

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
  @TableId(value = "userId", type = IdType.AUTO)
  @Excel(name = "userId", orderNum = "0", width = 15)
  private Integer userId;


  @ApiModelProperty(value = "用户名")
  @TableField("user_name")
  @NotEmpty
  @Excel(name = "userId", orderNum = "0", width = 15)
  private String userName;

  @ApiModelProperty(value = "邮箱")
  @Email
  @Excel(name = "邮箱", orderNum = "1", width = 20)
  private String email;

  @ApiModelProperty(value = "密码")
  @NotEmpty
  @Excel(name = "密码", orderNum = "3", width = 15)
  private String password;

  @ApiModelProperty(value = "加密盐")
  private String salt;

  @ApiModelProperty(value = "性别（0女 1男）")
  @Excel(name = "性别", orderNum = "4", width = 15)
  private Integer sex;

  @ApiModelProperty(value = "所读学校")
  @Excel(name = "所读学校", orderNum = "5", width = 15)
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
  private Integer state;

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
  @TableField("update_operator")
  private Long updateOperator;

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
    return this.userName + this.salt;
  }
}
