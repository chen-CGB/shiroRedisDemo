package com.cgf.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: TODO 页码类
 * @author: cgf
 * @date: 2021/5/9
 **/
@Data
public class BaseRequest {

    @ApiModelProperty(value = "当前页", example = "1")
    private Integer pageNum;

    @ApiModelProperty(value = "页大小", example = "10")
    private Integer pageSize;
}