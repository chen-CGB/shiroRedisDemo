package com.cgf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgf.common.Response;
import com.cgf.common.ResponseCode;
import com.cgf.entity.SysUser;
import com.cgf.mapper.SysUserMapper;
import com.cgf.service.SysUserService;
import com.cgf.utils.EmailCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cgf
 * @since 2021-05-09
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    SysUserMapper userMapper;

    @Autowired
    EmailCodeUtils emailCodeUtils;


    @Override
    public SysUser findUserByName(String username) {
        return userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    @Override
    @Transactional
    public Response updateUserEmail(String email, String verCode) {
        // 构建缓存用户信息返回给前端
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        String code = emailCodeUtils.getCode(email);
        if (user.getEmail().equals(email)){
            log.warn("邮箱地址重复");
            return Response.custom(ResponseCode.EMAIL_LOSE_TIME);
        }
        if (emailCodeUtils.isExpired(email)) {
            log.warn("验证码超时");
            return Response.custom(ResponseCode.EMAIL_LOSE_TIME);
        }
        if (!verCode.equals(code)) {
            log.warn("验证码错误");
            return Response.custom(ResponseCode.EMAIL_NOT_CODE);
        }
        user.setEmail(email);
        user.setUpdater(user.getUserId());
        user.setUpdateTime(new Timestamp(new Date().getTime()));
        userMapper.updateById(user);
        log.info("成功修改邮箱"+email);
        return Response.success("修改邮箱成功");
    }


}
