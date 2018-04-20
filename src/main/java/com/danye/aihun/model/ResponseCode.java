package com.danye.aihun.model;

public final class ResponseCode {

    private final Integer code;
    private final String desc;

    public final static ResponseCode SUCCESS = new ResponseCode(1, "成功");
    public final static ResponseCode FAILURE = new ResponseCode(2, "失败");

    public ResponseCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


}
