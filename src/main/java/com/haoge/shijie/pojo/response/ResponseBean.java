package com.haoge.shijie.pojo.response;

public class ResponseBean {

    // http 状态码
    private int code;

    // 返回信息
    private String msg;

    // 返回的数据
    private Object data;

    private static final String SUCCESS="success";
    private static final  String FAIL="fail";
    private static final  int SUCCESSCODE=200;

    public ResponseBean(){
}
    public ResponseBean(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public ResponseBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public  ResponseBean successMethod(String data){
        this.code=SUCCESSCODE;
        this.msg=SUCCESS;
        this.data=data;
        return this;
    }
    public ResponseBean successMethod(Object data){
        this.code=SUCCESSCODE;
        this.msg=SUCCESS;
        this.data=data;
        return this;
    }
    public ResponseBean successMethod(){
        this.code=SUCCESSCODE;
        this.msg=SUCCESS;
        return this;
    }
    public ResponseBean failMethod(int code, String msg){
        this.code = code;
        this.msg = msg;
        return this;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
