package com.cgf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cgf.common.APIException;
import com.cgf.common.Response;
import com.cgf.common.ResponseCode;
import com.cgf.dto.UserEditDto;
import com.cgf.entity.SysUser;
import com.cgf.mapper.SysUserMapper;
import com.cgf.service.LoginService;
import com.cgf.utils.EmailCodeUtils;
import com.cgf.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * @description: TODO 类描述
 * @author: cgf
 * @date: 2021/5/31
 **/
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    SysUserMapper userMapper;

    @Autowired
    EmailCodeUtils emailCodeUtils;

    @Override
    public SysUser findUserByEmail(String email) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getEmail, email)
        );
    }


    @Override
    public UserVo login(String userName, String password) {
        // 获取Subject实例对象，用户实例
        Subject currentUser = SecurityUtils.getSubject();
        // 将用户名和密码封装到UsernamePasswordToken
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        UserVo cacheUser;
        SysUser user;
        // 4、认证
        try {
            // 传到 MyShiroRealm 类中的方法进行认证
            currentUser.login(token);
            // 构建缓存用户信息返回给前端
            user = (SysUser) currentUser.getPrincipals().getPrimaryPrincipal();
            //获取用户的权限列表
            cacheUser = userMapper.getUserPermission(user.getUsername());
            //设置其token
            cacheUser.setToken(SecurityUtils.getSubject().getSession().getId().toString());
            log.info(userName+"的token为："+SecurityUtils.getSubject().getSession().getId().toString());
            BeanUtils.copyProperties(user, cacheUser);
        } catch (UnknownAccountException e) {
            log.error("账户不存在异常：", e);
            throw new APIException("账号不存在!");
        } catch (IncorrectCredentialsException e) {
            log.error("凭据错误（密码错误）异常：", e);
            throw new APIException("密码不正确!");
        } catch (AuthenticationException e) {
            log.error("身份验证异常:", e);
            throw new APIException("用户验证失败!");
        }
        return cacheUser;
    }

    //设置用户登录的IP以及登录时间
    @Async
    public void saveLastLogin(SysUser user, String ipAddress){
        user.setLastLoginTime(new Timestamp(new Date().getTime()));
        user.setLastLoginIp(ipAddress);
        userMapper.updateById(user);
    }

    @Async
    public Response sendEmailCode(String receiver){
        try {
            emailCodeUtils.sendEmailCode(receiver);
        }catch (Exception e){
            log.error("发送邮箱失败："+e.getMessage());
            return Response.failed("发送验证码失败!!!");
        }
        return Response.success("验证码发送成功");
    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @Override
    @Transactional
    public Response addUser(UserEditDto user, String verCode) {
        String code = emailCodeUtils.getCode(user.getEmail());
        if (emailCodeUtils.isExpired(user.getEmail())) {
            log.warn("验证码超时");
            return Response.custom(ResponseCode.EMAIL_LOSE_TIME);
        }
        if (!verCode.equals(code)) {
            log.warn("验证码错误");
            return Response.custom(ResponseCode.EMAIL_NOT_CODE);
        }
        user.setCreateTime(new Timestamp(new Date().getTime()));
        user.setStatus(1);
        userMapper.insert(user);
        if (!Objects.isNull(user.getRoleIds())){
            userMapper.batchAddUserRole(user.getUserId(),user.getRoleIds());
        }
        log.info("添加的用户信息："+user.toString());
        return Response.success("添加成功");
    }
}
