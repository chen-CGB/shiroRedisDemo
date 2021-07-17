package com.cgf.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果的实体
 * @param <T>
 */
@Data
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "响应码")
    private int code;

    @ApiModelProperty(value = "提示消息")
    private String msg;

    @ApiModelProperty(value = "返回的数据体")
    private T data;

    //使用自定义状态
    public static Response custom(ResponseCode resultCode){
        Response result  = new Response();
        result.setCode(resultCode.getCode());
        result.setMsg(resultCode.getMsg());
        return result;
    }

    //操作成功
    public static Response success(){
        Response result  = new Response();
        result.setCode(ResponseCode.SUCCESS.getCode());
        result.setMsg(ResponseCode.SUCCESS.getMsg());
        return result;
    }
    public static Response success(String msg){
        Response result  = new Response();
        result.setCode(ResponseCode.SUCCESS.getCode());
        result.setMsg(msg);
        return result;
    }

    //操作失败
    public static Response failed(){
        Response result  = new Response();
        result.setCode(ResponseCode.FAILED.getCode());
        result.setMsg(ResponseCode.FAILED.getMsg());
        return result;
    }
    public static Response failed(String msg){
        Response result  = new Response();
        result.setCode(ResponseCode.FAILED.getCode());
        result.setMsg(msg);
        return result;
    }

    //链式添加data
    public Response data(T data){
        this.setData(data);
        return this;
    }

    public Response(){}
    public Response(T data) {
        this(ResponseCode.SUCCESS, data);
    }

    public Response(ResponseCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public Response(ResponseCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.data = data;
    }
}

