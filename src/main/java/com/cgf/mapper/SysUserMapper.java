package com.cgf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgf.entity.SysUser;
import com.cgf.vo.UserVo;
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
public interface SysUserMapper extends BaseMapper<SysUser> {
    void batchAddUserRole(@Param("userId") Integer userId, @Param("roleIds") List<Integer> roleIds);

    int removeUserAllRole(@Param("userId") Integer userId);

    UserVo getUserPermission(@Param("username") String username);
}
