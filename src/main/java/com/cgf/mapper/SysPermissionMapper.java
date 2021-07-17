package com.cgf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgf.entity.SysPermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cgf
 * @since 2021-05-09
 */
@Repository
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    List<SysPermission> findRolePermissions(@Param("roleId") Integer roleId);
}
