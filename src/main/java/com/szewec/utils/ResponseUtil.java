package com.szewec.utils;

import java.util.List;

/**
 * 接口响应工具类
 *
 * @author QZB
 * @create 2019-08-27 17:48
 */
public class ResponseUtil {
    /**
     * 操作成功，返回没有分页的列表
     *
     * @param records
     * @return
     */
    public static RequestMessage successListResponse(List records) {
        RequestMessage message = new RequestMessage();
        message.setCode(RequestMessage.SUCCESS_GETS_CODE);
        message.setMessage(RequestMessage.SUCCESS_GETS_MSG);
        message.setData(records);
        return message;
    }

    /**
     * 操作成功，返回单个对象
     *
     * @param obj
     * @return
     */
    public static RequestMessage successObjectResponse(Object obj) {
        RequestMessage message = new RequestMessage();
        message.setCode(RequestMessage.SUCCESS_GETS_CODE);
        message.setMessage(RequestMessage.SUCCESS_GETS_MSG);
        message.setData(obj);
        return message;
    }

    /**
     * 操作成功，只返回结果，没有记录
     *
     * @return
     */
    public static RequestMessage successResponse(String msg) {
        RequestMessage message = new RequestMessage();
        message.setCode(RequestMessage.SUCCESS_GETS_CODE);
        if (null == msg || "".equals(msg)) {
            message.setMessage(RequestMessage.SUCCESS_GETS_CODE);
        } else {
            message.setMessage(msg);
        }
        return message;
    }

    /**
     * 操作成功，只返回结果，没有记录(查询数据)
     *
     * @return
     */
    public static RequestMessage successResponseGets(String msg) {
        RequestMessage message = new RequestMessage();
        message.setCode(RequestMessage.SUCCESS_GETS_CODE);
        if (null == msg || "".equals(msg)) {
            message.setMessage(RequestMessage.SUCCESS_GETS_CODE);
        } else {
            message.setMessage(msg);
        }
        return message;
    }

    /**
     * 操作成功，只返回结果，没有记录(新建或修改)
     *
     * @return
     */
    public static RequestMessage successResponsePosts(String msg) {
        RequestMessage message = new RequestMessage();
        message.setCode(RequestMessage.SUCCESS_POSTS_CODE);
        if (null == msg || "".equals(msg)) {
            message.setMessage(RequestMessage.SUCCESS_POSTS_MSG);
        } else {
            message.setMessage(msg);
        }
        return message;
    }

    /**
     * 操作成功，只返回结果，没有记录(删除)
     *
     * @return
     */
    public static RequestMessage successResponseDelete(String msg) {
        RequestMessage message = new RequestMessage();
        message.setCode(RequestMessage.SUCCESS_DELETES_CODE);
        if (null == msg || "".equals(msg)) {
            message.setMessage(RequestMessage.SUCCESS_DELETES_MSG);
        } else {
            message.setMessage(msg);
        }
        return message;
    }


    /**
     * 操作失败，返回错误描述
     *
     * @param msg
     * @return
     */
    public static RequestMessage failedResponse(String msg, String error) {
        RequestMessage message = new RequestMessage();
        message.setCode(RequestMessage.ERROR_CODE);
        if (null == msg || "".equals(msg)) {
            message.setMessage(RequestMessage.ERROR_MSG);
        } else {
            message.setMessage(msg);
        }
        message.setError(error);
        return message;
    }
}
