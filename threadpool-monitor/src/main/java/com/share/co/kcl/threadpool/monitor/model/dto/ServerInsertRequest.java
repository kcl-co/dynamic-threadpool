package com.share.co.kcl.threadpool.monitor.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ServerInsertRequest {

    @NotBlank(message = "服务代码不能为空")
    @ApiModelProperty(value = "服务代码")
    private String serverCode;

    @NotBlank(message = "服务名称不能为空")
    @ApiModelProperty(name = "服务名称")
    private String serverName;

}
