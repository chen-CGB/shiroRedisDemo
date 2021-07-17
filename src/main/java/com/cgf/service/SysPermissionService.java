package com.cgf.service;

import com.cgf.entity.SysPermission;
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
public interface SysPermissionService extends IService<SysPermission> {
    List<SysPermission> getRolePermissions(Integer roleId);
}
