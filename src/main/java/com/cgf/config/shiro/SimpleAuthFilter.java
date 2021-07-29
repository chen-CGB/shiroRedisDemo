package com.cgf.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.cgf.common.Response;
import com.cgf.common.ResponseCode;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/***
 * @author cgf
 * @Description 解锁请求返回结果
 * @date 2021/4/29
 */
public class SimpleAuthFilter extends UserFilter {

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        Response r = Response.custom(ResponseCode.USER_NOT_LOGIN);
        response.getWriter().print(JSONObject.toJSONString(r));
    }

}
