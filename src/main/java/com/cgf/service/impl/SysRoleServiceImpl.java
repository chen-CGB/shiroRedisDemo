package com.cgf.service.impl;

import com.cgf.entity.SysRole;
import com.cgf.mapper.SysRoleMapper;
import com.cgf.service.SysRoleService;
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
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> getUserRoles(Integer userId) {
        return sysRoleMapper.findUserRoles(userId);
    }
}
