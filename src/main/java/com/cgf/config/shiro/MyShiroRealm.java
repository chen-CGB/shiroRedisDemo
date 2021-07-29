package com.cgf.config.shiro;

import com.cgf.entity.SysPermission;
import com.cgf.entity.SysRole;
import com.cgf.entity.SysUser;
import com.cgf.service.SysPermissionService;
import com.cgf.service.SysRoleService;
import com.cgf.service.SysUserService;
import com.cgf.utils.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: TODO 类描述
 * @author: cgf
 * @date: 2021/5/9
 **/
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysPermissionService sysPermissionService;

    /**
     * @Author cgf
     * @Description //TODO 授权
     * @Date 13:51 2021/5/9
     * @Param [principals]
     **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("——————开始进行授权验证操作——————");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        //如果身份认证的时候没有传入User对象，这里只能取到userName
        //也就是SimpleAuthenticationInfo构造的时候第一个参数传递需要User对象
        SysUser user = (SysUser)principals.getPrimaryPrincipal();
        //查询用户角色
        List<SysRole> roles = sysRoleService.getUserRoles(user.getUserId());
        for (SysRole role : roles) {
            authorizationInfo.addRole(role.getRole());
            // 根据角色查询权限
            List<SysPermission> permissions = sysPermissionService.getRolePermissions(role.getRoleId());
            for (SysPermission p : permissions) {
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }
        return authorizationInfo;
    }

    /**
     * @Author cgf
     * @Description //TODO 要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确
     * @Date 14:25 2021/5/9
     * @Param [token]
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        log.info("——————开始进行身份认证操作——————");
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());
        //通过username从数据库中查找 User对象.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        SysUser user = sysUserService.findUserByName(username);
        if (user == null) {
            throw new AuthenticationException();
        }
        String curPwd = "";
        try {
            //进行转义
            curPwd = EncryptUtil.encryptMd5(password, user.getSalt());
            log.info("登录密码为： "+curPwd);
        } catch (Exception e) {
            log.error("", e);
        }
        if (user.getState() == 2) {
            throw new DisabledAccountException();
        } else if (!curPwd.equals(user.getPassword())) {//判断是否跟数据库系统
            throw new AuthenticationException();
        }

        return new SimpleAuthenticationInfo(
                // 这里传入的是user对象，比对的是用户名，直接传入用户名也没错，但是在授权部分就需要自己重新从数据库里取权限
                user,
                // 密码
                password,
                // salt = username + salt
                //ByteSource.Util.bytes(user.getCredentialsSalt()),
                // realm name
                getName()
        );
    }
}
