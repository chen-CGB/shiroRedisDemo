package com.cgf.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: TODO 页码类
 * @author: cgf
 * @date: 2021/5/9
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse {

    @ApiModelProperty(value = "页大小", example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum;

    @ApiModelProperty(value = "记录总数", example = "10")
    private Integer total;

    @ApiModelProperty(value = "总页数")
    private Integer totalPages;

    @ApiModelProperty(value = "内容")
    private Object content;
}