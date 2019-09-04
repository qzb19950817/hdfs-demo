package com.szewec.utils;

public class RequestMessage {
    /* 服务器成功返回用户请求数据的状态码 */
    public static String SUCCESS_GETS_CODE = "200";
    /* 服务器成功返回用户请求数据的提示信息 */
    public static String SUCCESS_GETS_MSG = "ok";

    /* 新建或修改数据成功返回状态码 */
    public static String SUCCESS_POSTS_CODE = "201";
    /* 新建或修改数据成功返回提示信息 */
    public static String SUCCESS_POSTS_MSG = "ok";

    /* 请求进入后台排队（异步请求）成功状态码 */
    public static String SUCCESS_ACCEPTED_CODE = "202";
    /* 请求进入后台排队（异步请求）成功提示信息 */
    public static String SUCCESS_ACCEPTED_MSG = "Accepted";

    /* 删除数据成功返回状态码 */
    public static String SUCCESS_DELETES_CODE = "204";
    /* 删除数据成功返回提示信息 */
    public static String SUCCESS_DELETES_MSG = "ok";

    /* 服务器发生错误状态码 */
    public static String ERROR_CODE = "500";
    /* 服务器发生错误提示信息 */
    public static String ERROR_MSG = "error";

    /* 状态码 */
    private String code;
    /* 提示信息 */
    private String message;
    /* 服务器异常信息 */
    private String error;
    /* 成功返回的数据 */
    private Object data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
