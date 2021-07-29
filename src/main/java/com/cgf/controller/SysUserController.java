package com.cgf.controller;


import com.cgf.common.Response;
import com.cgf.entity.SysUser;
import com.cgf.service.SysUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cgf
 * @since 2021-05-09
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    SysUserService userService;

    RedisSessionDAO redisSessionDAO;

    @ApiOperation(value = "修改用户邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @PostMapping("/email")
    public Response updateUserEmail(@RequestParam String email, @RequestParam String code){
        return userService.updateUserEmail(email, code);
    }

    @ApiOperation(value = "当前登录用户数", response = Response.class)
    @GetMapping("/userCount")
    public Response loginUserCount() {
        Set<String> hosts = new HashSet<>();
        redisSessionDAO.getActiveSessions().forEach(session -> hosts.add(session.getHost()));
        return Response.success().data(String.valueOf(hosts.size()));
    }

    @ApiOperation(value = "用户列表")
    @GetMapping("/userList")
    @RequiresPermissions("user:view")//权限管理;
    public List<SysUser> findUserList() {
        return userService.list();
    }


    /**
     * 用户添加;
     * @return
     */
    @PostMapping("/userAdd")
    @RequiresPermissions("user:add")//权限管理;
    public String userInfoAdd(){
        return "userAdd";
    }

    /**
     * 用户删除;
     * @return
     */
    @DeleteMapping("/userDel")
    @RequiresPermissions("user:del")//权限管理;
    public String userDel(){
        return "userDel";
    }


}

