package com.rainbow.car.car.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author caijiatao
 * @date 2019/3/12 4:29 PM
 */
public class BaseResult {
    private static final String OK = "ok";
    private static final String ERROR = "error";

    @JSONField
    private String status = OK;

    @JSONField
    private String msg;

    @JSONField
    private Object data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public BaseResult toError(){
        status = ERROR;
        return this;
    }

}
