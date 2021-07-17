package com.cgf.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class APIException extends RuntimeException {

    @ApiModelProperty(value = "错误码")
    private int code;
    @ApiModelProperty(value = "提示消息")
    private String msg;

    public APIException() {
        this(1001, "接口错误");
    }

    public APIException(String msg) {
        this(1001, msg);
    }

    public APIException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}


