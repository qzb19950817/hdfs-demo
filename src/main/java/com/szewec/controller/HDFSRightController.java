package com.szewec.controller;

import com.szewec.utils.HDFSUtils;
import com.szewec.utils.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HDFS用户权限管理控制器
 *
 * @author QZB
 * @create 2019-08-28 17:04
 */
@RestController
@RequestMapping("/api/hdfs/right")
@Api(value = "HDFS用户权限管理")
@Slf4j
public class HDFSRightController {
    @PostMapping(value = "/setPermission")
    @ApiOperation(value = "设置HDFS资源权限", notes = "设置HDFS资源权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceName", value = "资源名称（包含文件路径）", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "ownerRightCode", value = "所属者权限", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "groupRightCode", value = "所属组权限", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "otherRightCode", value = "其他用户权限", paramType = "query", dataType = "string", required = true)
    })
    public Object setPermission(String resourceName, String ownerRightCode, String groupRightCode, String otherRightCode) {
        try {
            if (StringUtils.isNotBlank(resourceName) && StringUtils.isNotBlank(ownerRightCode) && StringUtils.isNotBlank(groupRightCode) && StringUtils.isNotBlank(otherRightCode)) {
                HDFSUtils.setPermission(resourceName, ownerRightCode, groupRightCode, otherRightCode);
                return ResponseUtil.successResponse("HDFS资源权限设置成功！");
            } else {
                return ResponseUtil.failedResponse("HDFS资源权限设置失败！", "HDFS资源名称或权限编码为空！");
            }
        } catch (Exception e) {
            log.error("HDFS资源权限设置异常！", e);
            return ResponseUtil.failedResponse("HDFS资源权限设置异常！", e.getMessage());
        }
    }

    @PostMapping(value = "/setOwner")
    @ApiOperation(value = "设置HDFS资源所属者和所属组", notes = "设置HDFS资源所属者和所属组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceName", value = "资源名称（包含文件路径）", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "ownerName", value = "所属者名称", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "groupName", value = "所属组名称", paramType = "query", dataType = "string", required = true)
    })
    public Object setOwner(String resourceName, String ownerName, String groupName) {
        try {
            if (StringUtils.isNotBlank(resourceName) && StringUtils.isNotBlank(ownerName) && StringUtils.isNotBlank(groupName)) {
                HDFSUtils.setOwner(resourceName, ownerName, groupName);
                return ResponseUtil.successResponse("HDFS资源所属者和所属组设置成功！");
            } else {
                return ResponseUtil.failedResponse("HDFS资源所属者和所属组设置失败！", "HDFS资源名称或所属者信息为空！");
            }
        } catch (Exception e) {
            log.error("HDFS资源所属者和所属组设置异常！", e);
            return ResponseUtil.failedResponse("HDFS资源所属者和所属组设置异常！", e.getMessage());
        }
    }

    @PostMapping(value = "/setReplication")
    @ApiOperation(value = "设置HDFS资源副本系数", notes = "设置HDFS资源副本系数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceName", value = "资源名称（包含文件路径）", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "count", value = "副本系数", paramType = "query", dataType = "short", required = true)
    })
    public Object setReplication(String resourceName, short count) {
        try {
            if (StringUtils.isNotBlank(resourceName)) {
                if (count > 0) {
                    HDFSUtils.setReplication(resourceName, count);
                    return ResponseUtil.successResponse("HDFS资源副本系数设置成功！");
                } else {
                    return ResponseUtil.failedResponse("HDFS资源副本系数设置失败！", "无法将HDFS资源的副本系数设为0或负值！");
                }

            } else {
                return ResponseUtil.failedResponse("HDFS资源副本系数设置失败！", "HDFS资源名称为空！");
            }
        } catch (Exception e) {
            log.error("HDFS资源副本系数设置异常！", e);
            return ResponseUtil.failedResponse("HDFS资源副本系数设置异常！", e.getMessage());
        }
    }
}
