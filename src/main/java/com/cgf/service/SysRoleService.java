package com.cgf.service;

import com.cgf.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cgf
 * @since 2021-05-09
 */
public interface SysRoleService extends IService<SysRole> {
    List<SysRole> getUserRoles(Integer userId);
}
