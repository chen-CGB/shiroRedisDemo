package com.cgf.service.impl;

import com.cgf.entity.SysPermission;
import com.cgf.mapper.SysPermissionMapper;
import com.cgf.service.SysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cgf
 * @since 2021-05-09
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public List<SysPermission> getRolePermissions(Integer roleId) {
        return sysPermissionMapper.findRolePermissions(roleId);
    }
}
