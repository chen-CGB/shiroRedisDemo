package com.cgf.service;

import com.cgf.common.Response;
import com.cgf.dto.UserEditDto;
import com.cgf.entity.SysUser;
import com.cgf.vo.UserVo;

/**
 * @description: TODO 类描述
 * @author: cgf
 * @date: 2021/5/31
 **/

public interface LoginService {
    Response addUser(UserEditDto user, String verCode);
    Response sendEmailCode(String receiver);
    UserVo login(String userName, String password);
    public void saveLastLogin(SysUser user, String ipAddress);
    void logout();
    SysUser findUserByEmail(String email);
}
