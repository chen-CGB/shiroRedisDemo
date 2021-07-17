package com.cgf.controller;

import com.cgf.common.Response;
import com.cgf.dto.EmailUserDto;
import com.cgf.dto.LoginUser;
import com.cgf.dto.UserEditDto;
import com.cgf.entity.SysUser;
import com.cgf.service.LoginService;
import com.cgf.utils.EncryptUtil;
import com.cgf.utils.IpUtils;
import com.cgf.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@Api("用户登录注册controller")
@CrossOrigin
@Slf4j
public class LoginController {

    @Autowired
    LoginService userService;

    @Autowired
    RedisSessionDAO redisSessionDAO;

    @ApiOperation(value = "登录请求", notes = "根据用户名称和密码登录")
    @PostMapping("/login")
    public UserVo login(HttpServletRequest request, @RequestBody  @Valid LoginUser loginUser){
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        String ipAddress = IpUtils.getIpAddr(request);
        log.info("用户登录,用户名:"+userName+",密码:"+password+"登录IP地址为："+ipAddress);
        UserVo user = userService.login(userName, password);
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(user,sysUser);
        userService.saveLastLogin(sysUser,ipAddress);
        return user;
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
    @ApiOperation(value = "发送验证码",notes = "时间为五分钟后过期")
    @ApiImplicitParam(name = "email", value = "邮箱地址", required = true, dataType = "Email")
    @PostMapping("/sendCode")
    public Response sendCode(@Email @RequestParam("email") String receiver) {
        return userService.sendEmailCode(receiver);
    }

    /**
     * 注册用户
     */
    @ApiOperation(value = "注册用户")
    @PostMapping("/")
    public Response register(@RequestBody EmailUserDto user) {
        if (!Objects.isNull(userService.findUserByEmail(user.getEmail()))) {
            return Response.failed("邮箱已被注册！！！");
        }
        UserEditDto sysUser = new UserEditDto();
        BeanUtils.copyProperties(user, sysUser);
        addDeal(sysUser);
        return userService.addUser(sysUser,user.getCode());
    }

    private void addDeal(UserEditDto user) {
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


    @ApiOperation(value = "当前登录用户数", response = Response.class)
    @GetMapping("/userCount")
    public Response loginUserCount() {
        Set<String> hosts = new HashSet<>();
        log.info(redisSessionDAO.getActiveSessions().toString());
        redisSessionDAO.getActiveSessions().forEach(session -> hosts.add(session.getHost()));
        return Response.success().data(String.valueOf(hosts.size()));
    }


}
