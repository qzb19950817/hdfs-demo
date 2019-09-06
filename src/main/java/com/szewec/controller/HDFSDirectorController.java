package com.szewec.controller;

import com.szewec.utils.HDFSUtils;
import com.szewec.utils.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HDFS目录管理控制器
 *
 * @author QZB
 * @create 2019-08-27 17:31
 */
@RestController
@RequestMapping("/api/hdfs/director")
@Api(value = "HDFS目录管理")
@Slf4j
public class HDFSDirectorController {

    @GetMapping(value = "/isExist")
    @ApiOperation(value = "判断目录是否存在", notes = "判断目录是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "directorName", value = "目录名称（多级目录用“/”隔开）", paramType = "query", dataType = "string", required = true)
    })
    public Object isExist(String directorName) {
        try {
            if (StringUtils.isNotBlank(directorName)) {
                return ResponseUtil.successObjectResponse(HDFSUtils.resourceIsExist(directorName));
            } else {
                return ResponseUtil.failedResponse("判断目录是否存在失败！", "目录名称为空！");
            }
        } catch (Exception e) {
            log.error("判断目录是否存在异常！", e);
            return ResponseUtil.failedResponse("判断目录是否存在异常！", e.getMessage());
        }
    }

    @GetMapping(value = "/isDirector")
    @ApiOperation(value = "判断某个目录名称是否是真实目录", notes = "判断某个目录名称是否是真实目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "directorName", value = "目录名称（多级目录用“/”隔开）", paramType = "query", dataType = "string", required = true)
    })
    public Object isDirector(String directorName) {
        try {
            if (StringUtils.isNotBlank(directorName)) {
                return ResponseUtil.successObjectResponse(HDFSUtils.isDirector(directorName));
            } else {
                return ResponseUtil.failedResponse("判断某个目录名称是否是真实目录失败！", "目录名称为空！");
            }
        } catch (Exception e) {
            log.error("判断某个目录名称是否是真实目录异常！", e);
            return ResponseUtil.failedResponse("判断某个目录名称是否是真实目录异常！", e.getMessage());
        }
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "递归新增目录", notes = "递归新增目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "directorName", value = "目录名称（多级目录用“/”隔开，若目录开头为”/“则会在HDFS根目录下创建，否则会在当前用户的home目录下创建。）", paramType = "query", dataType = "string", required = true)
    })
    public Object add(String directorName) {
        try {
            if (StringUtils.isNotBlank(directorName)) {
                return ResponseUtil.successObjectResponse(HDFSUtils.mkDirs(directorName));
            } else {
                return ResponseUtil.failedResponse("递归新增目录失败！", "目录名称为空！");
            }
        } catch (Exception e) {
            log.error("递归新增目录异常！", e);
            return ResponseUtil.failedResponse("递归新增目录异常！", e.getMessage());
        }
    }

    @PostMapping(value = "/del")
    @ApiOperation(value = "递归删除目录", notes = "递归删除目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "directorName", value = "目录名称（多级目录用“/”隔开，若目录开头为”/“则会在HDFS根目录下删除，否则会在当前用户的home目录下删除。）", paramType = "query", dataType = "string", required = true)
    })
    public Object del(String directorName) {
        try {
            if (StringUtils.isNotBlank(directorName)) {
                return ResponseUtil.successObjectResponse(HDFSUtils.delResources(directorName,true));
            } else {
                return ResponseUtil.failedResponse("递归删除目录失败！", "目录名称为空！");
            }
        } catch (Exception e) {
            log.error("递归删除目录异常！", e);
            return ResponseUtil.failedResponse("递归删除目录异常！", e.getMessage());
        }
    }

    @PostMapping(value = "/rename")
    @ApiOperation(value = "重命名（移动）目录", notes = "重命名（移动）目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldDirectorName", value = "旧目录名称（多级目录用“/”隔开）", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "newDirectorName", value = "新目录名称（多级目录用“/”隔开）", paramType = "query", dataType = "string", required = true)
    })
    public Object rename(String oldDirectorName, String newDirectorName) {
        try {
            if (StringUtils.isNotBlank(oldDirectorName) && StringUtils.isNotBlank(newDirectorName)) {
                return ResponseUtil.successObjectResponse(HDFSUtils.renameResource(oldDirectorName, newDirectorName));
            } else {
                return ResponseUtil.failedResponse("目录重命名（移动）失败！", "目录名称为空！");
            }
        } catch (Exception e) {
            log.error("目录重命名（移动）异常！", e);
            return ResponseUtil.failedResponse("目录重命名（移动）异常！", e.getMessage());
        }
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "查看某个目录下所有的资源", notes = "查看某个目录下所有的资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "directorName", value = "目录名称（多级目录用“/”隔开）", paramType = "query", dataType = "string", required = true)
    })
    public Object list(String directorName) {
        try {
            if (StringUtils.isNotBlank(directorName)) {
                return ResponseUtil.successListResponse(HDFSUtils.listFiles(directorName));
            } else {
                return ResponseUtil.failedResponse("资源查看失败！", "目录名称为空！");
            }
        } catch (Exception e) {
            log.error("资源查看异常！", e);
            return ResponseUtil.failedResponse("资源查看异常！", e.getMessage());
        }
    }
}
