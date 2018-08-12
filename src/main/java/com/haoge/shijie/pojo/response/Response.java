package com.haoge.shijie.pojo.response;

/**
 * Title: 统一响应结构
 * Description:使用REST框架实现前后端分离架构，我们需要首先确定返回的JSON响应结构是统一的，
 * 也就是说，每个REST请求将返回相同结构的JSON响应结构。不妨定义一个相对通用的JSON响应结构，其
 * 中包含两部分：元数据与返回值，其中，元数据表示操作是否成功与返回值消息等，返回值对应服务端方法所返回的数据。
 * { "meta": { "success": true, "message": "ok" }, "data": ... }
 *
 * @author haoge
 */
public class Response {
    private static final String OK = "ok";
    private static final String ERROR = "error";
    private boolean success;
    private String message;
    private Object data;   // 响应内容

    public Response() {

    }

    public Response(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Response metsuccess(Boolean success, Object data) {
        if (success) {
            this.success = success;
            message = OK;
        } else {
            this.success = success;
            message = ERROR;
        }
        this.data = data;
        return this;
    }

    public Response metsuccess(Object data) {
        this.success = true;
        this.message = OK;
        this.data = data;
        return this;
    }

    public Response metsuccess(String message) {
        this.success = true;
        this.message = message;
        this.data = data;
        return this;
    }

    public Response metfailure() {
        this.success = false;
        this.message = ERROR;
        return this;
    }

    public Response metfailure(Object data) {
        this.success = false;
        this.message = OK;
        this.data = data;
        return this;
    }

    public Response metfailure(String message) {
        this.success = false;
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}