package com.cgf.controller;

import com.cgf.common.Response;
import com.cgf.dto.EmailUserDto;
import com.cgf.dto.LoginUser;
import com.cgf.entity.SysUser;
import com.cgf.service.SysUserService;
import com.cgf.utils.EncryptUtil;
import com.cgf.utils.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
@Api("用户登录注册controller")
@CrossOrigin
@Slf4j
public class LoginController {

    @Autowired
    SysUserService userService;

    @ApiOperation(value = "登录请求", notes = "根据用户名称和密码登录")
    @PostMapping("/login")
    public Response login(@RequestBody  @Valid LoginUser loginUser){
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        log.info("用户登录,用户名:"+userName+",密码:"+password);
        return Response.success().data(userService.login(userName, password));
    }


    @GetMapping("export")
    public ResponseEntity<Void> export(HttpServletResponse response) throws Exception {
        List<SysUser> exportData = userService.list();
        ExcelUtils.exportExcel("用户列表",SysUser.class,exportData,response);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "注销")
    @PostMapping("/logout")
    public Response logout(){
        userService.logout();
        return Response.success("登出成功！");
    }

    /**
     * 发送验证码到指定邮箱
     */
    @ApiOperation(value = "发送验证码",notes = "时间为60s后过期")
    @ApiImplicitParam(name = "email", value = "邮箱地址", required = true, dataType = "Email")
    @PostMapping("/sendCode")
    public Response sendCode(@Email @RequestParam("email") String receiver) {
        return userService.sendEmailCode(receiver);
    }

    /**
     * 注册用户
     */
    @ApiOperation(value = "添加用户")
    @ApiImplicitParam(name = "verCode", value = "验证码", required = true, dataType = "String")
    @PostMapping("/")
    public Response addUser(@RequestBody EmailUserDto user) {
        if (!Objects.isNull(userService.findUserByEmail(user.getEmail()))) {
            return Response.failed("邮箱已被注册！！！");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUser, user);
        addDeal(sysUser);
        sysUser.setCreateTime(new Timestamp(new Date().getTime()));
        sysUser.setState(0);
        return userService.addUser(sysUser,user.getCode());
    }

    private void addDeal(SysUser user) {
        String salt = EncryptUtil.createSalt();
        String pwd = "";
        try {
            pwd = EncryptUtil.encryptMd5(user.getPassword(), salt);
        } catch (Exception e) {
            log.error("加密错误", e);
        }
        user.setPassword(pwd);
        user.setSalt(salt);
    }

}
