package com.szewec.controller;

import com.szewec.utils.HDFSUtils;
import com.szewec.utils.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * HDFS目录文件控制器
 *
 * @author QZB
 * @create 2019-08-27 17:31
 */
@RestController
@RequestMapping("/api/hdfs/file")
@Api(value = "HDFS文件管理")
@Slf4j
public class HDFSFileController {
    @GetMapping(value = "/isExist")
    @ApiOperation(value = "判断文件是否存在", notes = "判断文件是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名称（包含文件路径）", paramType = "query", dataType = "string", required = true)
    })
    public Object isExist(String fileName) {
        try {
            if (StringUtils.isNotBlank(fileName)) {
                return ResponseUtil.successObjectResponse(HDFSUtils.resourceIsExist(fileName));
            } else {
                return ResponseUtil.failedResponse("判断文件是否存在失败！", "文件名称为空！");
            }
        } catch (Exception e) {
            log.error("判断文件是否存在异常！", e);
            return ResponseUtil.failedResponse("判断文件是否存在异常！", e.getMessage());
        }
    }

    @GetMapping(value = "/isFile")
    @ApiOperation(value = "判断某个文件名称是否是真实文件", notes = "判断某个文件名称是否是真实文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名称（包含文件路径）", paramType = "query", dataType = "string", required = true)
    })
    public Object isFile(String fileName) {
        try {
            if (StringUtils.isNotBlank(fileName)) {
                return ResponseUtil.successObjectResponse(HDFSUtils.isFile(fileName));
            } else {
                return ResponseUtil.failedResponse("判断某个文件名称是否是真实文件失败！", "文件名称为空！");
            }
        } catch (Exception e) {
            log.error("判断某个文件名称是否是真实文件异常！", e);
            return ResponseUtil.failedResponse("判断某个文件名称是否是真实文件异常！", e.getMessage());
        }
    }

    @PostMapping(value = "/rename")
    @ApiOperation(value = "重命名文件", notes = "重命名文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldFileName", value = "旧文件名称（包含文件路径）", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "newFileName", value = "新文件名称（包含文件路径）", paramType = "query", dataType = "string", required = true)
    })
    public Object rename(String oldFileName, String newFileName) {
        try {
            if (StringUtils.isNotBlank(oldFileName) && StringUtils.isNotBlank(newFileName)) {

                return ResponseUtil.successObjectResponse(HDFSUtils.renameResource(oldFileName, newFileName));
            } else {
                return ResponseUtil.failedResponse("文件重命名失败！", "文件名称为空！");
            }
        } catch (Exception e) {
            log.error("文件重命名异常！", e);
            return ResponseUtil.failedResponse("文件重命名异常！", e.getMessage());
        }
    }

    @PostMapping(value = "/del")
    @ApiOperation(value = "删除文件", notes = "删除文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名称（包含文件路径）", paramType = "query", dataType = "string", required = true)
    })
    public Object del(String fileName) {
        try {
            if (StringUtils.isNotBlank(fileName)) {
                return ResponseUtil.successObjectResponse(HDFSUtils.delResources(fileName, false));
            } else {
                return ResponseUtil.failedResponse("文件删除失败！", "文件名称为空！");
            }
        } catch (Exception e) {
            log.error("文件删除异常！", e);
            return ResponseUtil.failedResponse("文件删除异常！", e.getMessage());
        }
    }

    @PostMapping(value = "/cat")
    @ApiOperation(value = "查看文件内容", notes = "查看文件内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名称（包含文件路径）", paramType = "query", dataType = "string", required = true)
    })
    public Object cat(String fileName) {
        try {
            if (StringUtils.isNotBlank(fileName)) {
                HDFSUtils.cat(fileName);
                return ResponseUtil.successResponse("查看文件内容成功！");
            } else {
                return ResponseUtil.failedResponse("查看文件内容失败！", "文件名称为空！");
            }
        } catch (Exception e) {
            log.error("查看文件内容异常！", e);
            return ResponseUtil.failedResponse("查看文件内容异常！", e.getMessage());
        }
    }

    @PostMapping(value = "/create")
    @ApiOperation(value = "创建文件", notes = "创建文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名称（包含文件路径）", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "fileContent", value = "文件内容", paramType = "query", dataType = "string", required = false)
    })
    public Object create(String fileName, String fileContent) {
        try {
            if (StringUtils.isNotBlank(fileName)) {
                HDFSUtils.create(fileName, fileContent);
                return ResponseUtil.successResponse("文件创建成功！");
            } else {
                return ResponseUtil.failedResponse("文件创建失败！", "文件创建为空！");
            }
        } catch (Exception e) {
            log.error("文件创建异常！", e);
            return ResponseUtil.failedResponse("文件创建异常！", e.getMessage());
        }
    }

    @PostMapping(value = "/upload")
    @ApiOperation(value = "上传本地文件到HDFS", notes = "上传本地文件到HDFS")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "localFilePath", value = "本地文件路径", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "targetDirector", value = "目标文件夹", paramType = "query", dataType = "string", required = true)
    })
    public Object upload(String localFilePath, String targetDirector) {
        try {
            if (StringUtils.isNotBlank(localFilePath) && StringUtils.isNotBlank(targetDirector)) {
                HDFSUtils.copyFromLocalFile(localFilePath, targetDirector);
                return ResponseUtil.successResponse("文件上传成功！");
            } else {
                return ResponseUtil.failedResponse("文件上传失败！", "本地文件路径或目标文件夹为空！");
            }
        } catch (Exception e) {
            log.error("文件上传异常！", e);
            return ResponseUtil.failedResponse("文件上传异常！", e.getMessage());
        }
    }

    @PostMapping(value = "/uploadWithProgress")
    @ApiOperation(value = "上传本地大体加文件到HDFS，并显示进度条。", notes = "上传本地大体加文件到HDFS，并显示进度条。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "localFilePath", value = "本地文件路径", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "targetDirector", value = "目标文件夹", paramType = "query", dataType = "string", required = true)
    })
    public Object uploadWithProgress(String localFilePath, String targetDirector) {
        try {
            if (StringUtils.isNotBlank(localFilePath) && StringUtils.isNotBlank(targetDirector)) {
                HDFSUtils.copyFromLocalFileWithProgress(localFilePath, targetDirector);
                return ResponseUtil.successResponse("文件上传成功！");
            } else {
                return ResponseUtil.failedResponse("文件上传失败！", "本地文件路径或目标文件夹为空！");
            }
        } catch (Exception e) {
            log.error("文件上传异常！", e);
            return ResponseUtil.failedResponse("文件上传异常！", e.getMessage());
        }
    }

    @PostMapping(value = "/uploadFile")
    @ApiOperation(value = "上传本地大体加文件到HDFS，并显示进度条。", notes = "上传本地大体加文件到HDFS，并显示进度条。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "targetDirector", value = "目标文件夹", paramType = "query", dataType = "string", required = true)
    })
    public Object uploadFile(String targetDirector, HttpServletRequest request) {
        try {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = new ArrayList<>();
            for (List list : multipartHttpServletRequest.getMultiFileMap().values()) {
                files.addAll(list);
            }

            if (StringUtils.isNotBlank(targetDirector) && !CollectionUtils.isEmpty(files)) {
                HDFSUtils.copyFromLocalFileWithProgress(targetDirector, files);
                return ResponseUtil.successResponse("文件上传成功！");
            } else {
                return ResponseUtil.failedResponse("文件上传失败！", "目标文件夹或文件信息为空！");
            }
        } catch (Exception e) {
            log.error("文件上传异常！", e);
            return ResponseUtil.failedResponse("文件上传异常！", e.getMessage());
        }
    }

    @PostMapping(value = "/download")
    @ApiOperation(value = "将HDFS上的文件下载到本地", notes = "将HDFS上的文件下载到本地")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "HDFS上文件名称（包含文件路径）", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "targetDirector", value = "目标文件夹", paramType = "query", dataType = "string", required = true)
    })
    public Object download(String fileName, String targetDirector) {
        try {
            if (StringUtils.isNotBlank(fileName) && StringUtils.isNotBlank(targetDirector)) {
                HDFSUtils.copyToLocalFile(fileName, targetDirector);
                return ResponseUtil.successResponse("文件下载成功！");
            } else {
                return ResponseUtil.failedResponse("文件下载失败！", "HDFS上文件名称或目标文件夹为空！");
            }
        } catch (Exception e) {
            log.error("文件下载异常！", e);
            return ResponseUtil.failedResponse("文件下载异常！", e.getMessage());
        }
    }

}
