package com.cgf.common;


import lombok.Getter;

/**
 *返回参数错误码枚举
 */
@Getter
public enum ResponseCode {
    SUCCESS(200, "操作成功"),

    /* 用户错误 */
    USER_NOT_LOGIN(201, "用户未登录"),
    USER_NOT_PASSWORD(202, "新旧密码不可重复"),
    EMAIL_LOSE_TIME(203,"验证码已经失效！！！"),
    EMAIL_NOT_CODE(204,"验证码不正确！！！"),
    EMAIL_NOT_LIVE(205,"邮箱地址重复"),

    /* 业务错误 */
    NO_PERMISSION(403, "没有权限"),
    FAILED(400, "响应失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    METHOD_NOT_ALLOWED(405, "不支持当前请求方法"),
    UNSUPPORTED_MEDIA_TYPE(415,"不支持当前媒体类型"),
    UNPROCESSABLE_ENTITY(422,"所上传文件大小超过最大限制，上传失败！"),
    INTERNAL_SERVER_ERROR(500,"服务内部异常"),

    ERROR(5000, "未知错误");

    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

