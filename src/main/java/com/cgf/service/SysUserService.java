package com.cgf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgf.common.Response;
import com.cgf.entity.SysUser;

/**
 * @author cgf
 * @since 2021-05-09
 */
public interface SysUserService extends IService<SysUser> {

    SysUser findUserByName(String username);
    Response updateUserEmail(String email, String verCode);
}
