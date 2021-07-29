package com.cgf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgf.common.Response;
import com.cgf.common.ResponseCode;
import com.cgf.entity.SysUser;
import com.cgf.exception.LoginException;
import com.cgf.mapper.SysUserMapper;
import com.cgf.service.SysUserService;
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
    public SysUser findUserByEmail(String email) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getEmail, email)
        );
    }

    @Override
    public SysUser findUserByName(String username) {
        return userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, username));
    }

    @Override
    public UserVo login(String userName, String password) {
        // 获取Subject实例对象，用户实例
        Subject currentUser = SecurityUtils.getSubject();
        // 将用户名和密码封装到UsernamePasswordToken
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        UserVo cacheUser;
        // 4、认证
        try {
            // 传到 MyShiroRealm 类中的方法进行认证
            currentUser.login(token);
            // 构建缓存用户信息返回给前端
            SysUser user = (SysUser) currentUser.getPrincipals().getPrimaryPrincipal();
            cacheUser = UserVo.builder()
                    .token(currentUser.getSession().getId().toString())
                    .build();
            BeanUtils.copyProperties(user, cacheUser);
        } catch (UnknownAccountException e) {
            log.error("账户不存在异常：", e);
            throw new LoginException("账号不存在!", e);
        } catch (IncorrectCredentialsException e) {
            log.error("凭据错误（密码错误）异常：", e);
            throw new LoginException("密码不正确!", e);
        } catch (AuthenticationException e) {
            log.error("身份验证异常:", e);
            throw new LoginException("用户验证失败!", e);
        }
        return cacheUser;
    }

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
    public Response addUser(SysUser user, String verCode) {
        String code = emailCodeUtils.getCode(user.getEmail());
        if (emailCodeUtils.isExpired(verCode)) {
            log.warn("验证码超时");
            return Response.custom(ResponseCode.EMAIL_LOSE_TIME);
        }
        if (!verCode.equals(code)) {
            log.warn("验证码错误");
            return Response.custom(ResponseCode.EMAIL_NOT_CODE);
        }
        user.setCreateTime(new Timestamp(new Date().getTime()));
        userMapper.insert(user);
        log.info("添加的用户信息："+user.toString());
        return Response.success("添加成功");
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
        if (emailCodeUtils.isExpired(verCode)) {
            log.warn("验证码超时");
            return Response.custom(ResponseCode.EMAIL_LOSE_TIME);
        }
        if (!verCode.equals(code)) {
            log.warn("验证码错误");
            return Response.custom(ResponseCode.EMAIL_NOT_CODE);
        }
        user.setEmail(email);
        user.setUpdateOperator(Long.valueOf(user.getUserId()));
        user.setUpdateTime(new Timestamp(new Date().getTime()));
        userMapper.updateById(user);
        log.info("成功修改邮箱"+email);
        return Response.success("修改邮箱成功");
    }


}
