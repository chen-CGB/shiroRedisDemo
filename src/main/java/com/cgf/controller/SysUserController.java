package com.cgf.controller;


import com.cgf.common.Response;
import com.cgf.entity.SysUser;
import com.cgf.service.SysUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @ApiOperation(value = "修改用户邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @PostMapping("/email")
    @RequiresPermissions("user:update")
    public Response updateUserEmail(@RequestParam String email, @RequestParam String code){
        return userService.updateUserEmail(email, code);
    }

    @ApiOperation(value = "用户列表")
    @GetMapping("/userList")
    @RequiresPermissions("user:view")//权限管理;
    public List<SysUser> findUserList() {
        return userService.list();
    }


    /**
     * 用户添加;
     */
    @PostMapping("/userAdd")
    @RequiresPermissions("user:add")
    public String userInfoAdd(){
        return "userAdd";
    }



    /**
     * 用户修改;
     */
    @PostMapping("/userEdit")
    //@RequiresPermissions("user:edit")
    public String userEdit(){
        return "userEdit";
    }

    /**
     * 用户删除;
     */
    @DeleteMapping("/userDel")
    @RequiresPermissions("user:del")
    public String userDel(){
        return "userDel";
    }


}

