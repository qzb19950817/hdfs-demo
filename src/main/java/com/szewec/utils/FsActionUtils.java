package com.szewec.utils;

import org.apache.hadoop.fs.permission.FsAction;

/**
 * 根据权限编码解析资源权限
 *
 * @author QZB
 * @create 2019-08-28-17:14
 */
public class FsActionUtils {
    public static FsAction getFsAction(String actionCode) {
        FsAction fsAction = null;

        //无权限
        if ("0".equals(actionCode) || (!actionCode.toLowerCase().contains("r") && !actionCode.toLowerCase().contains("w") && !actionCode.toLowerCase().contains("x"))) {
            fsAction = FsAction.NONE;
        }

        //可读
        if ("1".equals(actionCode) || actionCode.toLowerCase().contains("r")) {
            fsAction = FsAction.READ;
        }

        //可读/写
        if ("3".equals(actionCode) || (actionCode.toLowerCase().contains("r") && actionCode.toLowerCase().contains("w"))) {
            fsAction = FsAction.READ_WRITE;
        }

        //可读/执行
        if ("5".equals(actionCode) || (actionCode.toLowerCase().contains("r") && actionCode.toLowerCase().contains("x"))) {
            fsAction = FsAction.READ_EXECUTE;
        }

        //只写
        if ("2".equals(actionCode) || (actionCode.toLowerCase().contains("2"))) {
            fsAction = FsAction.WRITE;
        }

        //可写/执行
        if ("6".equals(actionCode) || (actionCode.toLowerCase().contains("w") && actionCode.toLowerCase().contains("x"))) {
            fsAction = FsAction.WRITE_EXECUTE;
        }

        //可读/写/执行
        if ("7".equals(actionCode) || (actionCode.toLowerCase().contains("r") && actionCode.toLowerCase().contains("w") && actionCode.toLowerCase().contains("x"))) {
            fsAction = FsAction.ALL;
        }

        return fsAction;
    }
}
