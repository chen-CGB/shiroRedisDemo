package com.cgf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgf.entity.SysRole;
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
public interface SysRoleMapper extends BaseMapper<SysRole> {
    List<SysRole> findUserRoles(@Param("userId") Integer userId);
}
