package com.cgf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgf.common.Response;
import com.cgf.entity.SysUser;
import com.cgf.vo.UserVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cgf
 * @since 2021-05-09
 */
public interface SysUserService extends IService<SysUser> {
    Response addUser(SysUser user, String verCode);
    Response sendEmailCode(String receiver);
    Response updateUserEmail(String email, String verCode);
    UserVo login(String userName, String password);
    void logout();
    SysUser findUserByEmail(String email);
    SysUser findUserByName(String username);
}
