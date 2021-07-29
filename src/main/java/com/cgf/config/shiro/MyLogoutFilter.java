package com.cgf.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.cgf.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.AdviceFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/***
 * @author cgf
 * @Description 登出过滤器
 * @date 2021/4/29
 */
@Slf4j
public class MyLogoutFilter extends AdviceFilter {

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.logout();
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        }
        response.setContentType("application/json; charset=utf-8");
        Response r = Response.success();
        response.getWriter().print(JSONObject.toJSONString(r));
        return false;
    }

}
