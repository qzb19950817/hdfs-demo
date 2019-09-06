package com.szewec.utils;

import com.szewec.entity.HDFSResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * HDFS目录和文件操作工具类
 *
 * @author QZB
 * @create 2019-08-27-17:34
 */
@Component
@Slf4j
public class HDFSUtils {
    // HDFS文件系统服务器的地址以及端口
    private static String HDFS_PATH;
    // HDFS文件系统服务器的地址以及端口
    private static String HDFS_USER;
    // HDFS文件系统的操作对象
    private static FileSystem fileSystem = null;
    // 配置对象
    private static Configuration configuration = null;

    @Value("${hdfs.path}")
    private String hdfsPath;
    @Value("${hdfs.user}")
    private String hdfsUser;

    @PostConstruct
    public void init() {
        HDFS_PATH = hdfsPath;
        HDFS_USER = hdfsUser;
    }

    /**
     * 初始化configuration和fileSystem
     *
     * @throws Exception
     */
    private static void setUp() throws Exception {
        configuration = new Configuration();
        // 第一参数是服务器的URI，第二个参数是配置对象，第三个参数是文件系统的用户名
        fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, HDFS_USER);
    }

    /**
     * 释放资源
     *
     * @throws Exception
     */
    private static void tearDown() throws Exception {
        configuration = null;
        fileSystem.close();
    }

    /**
     * 检查文件或目录是否存在
     *
     * @param name 文件或目录名称
     * @return
     * @throws IOException
     */
    public static boolean resourceIsExist(String name) throws Exception {
        setUp();
        return fileSystem.exists(new Path(name));
    }

    /**
     * 获取当前登录用户的在HDFS文件系统中的家目录
     *
     * @return
     * @throws Exception
     */
    public static Path getHomeDirectory() throws Exception {
        setUp();
        return fileSystem.getHomeDirectory();
    }

    /**
     * 判断某个目录名称是否是真实目录
     *
     * @param directorName 目录名称
     * @return
     * @throws Exception
     */
    public static boolean isDirector(String directorName) throws Exception {
        setUp();
        return fileSystem.isDirectory(new Path(directorName));
    }

    /**
     * 在HDFS根目录下递归创建目录
     *
     * @param directorName 完整的目录路径
     * @throws Exception
     */
    public static boolean mkDirs(String directorName) throws Exception {
        if (resourceIsExist(directorName)) {
            throw new Exception("要创建的目录已存在！");
        }

        return fileSystem.mkdirs(new Path(directorName));
    }

    /**
     * 递归删除HDFS根目录下的目录或文件
     *
     * @param name           完整的目录或文件路径
     * @param isRecursionDel 是否递归删除
     * @throws Exception
     */
    public static boolean delResources(String name, Boolean isRecursionDel) throws Exception {
        if (!resourceIsExist(name)) {
            throw new Exception("要删除的目录或文件不存在！");
        }

        // 第二个参数指定是否要递归删除，false=否，true=是
        return fileSystem.delete(new Path(name), isRecursionDel);
    }

    /**
     * 给目录或文件重命名
     *
     * @param oldName 旧目录或文件名称
     * @param newName 新目录或文件名称
     * @throws Exception
     */
    public static boolean renameResource(String oldName, String newName) throws Exception {
        if (!resourceIsExist(oldName)) {
            throw new Exception("要重命名的目录或文件不存在！");
        }

        return fileSystem.rename(new Path(oldName), new Path(newName));
    }

    /**
     * 判断某个文件名称是否是真实文件
     *
     * @param fileName 文件名称（包含文件路径）
     * @return
     * @throws Exception
     */
    public static boolean isFile(String fileName) throws Exception {
        setUp();
        return fileSystem.isFile(new Path(fileName));
    }

    /**
     * 查看某个目录下所有的文件
     *
     * @param directorName 目录名称
     * @return
     * @throws Exception
     */
    public static List<HDFSResource> listFiles(String directorName) throws Exception {
        List<HDFSResource> result = new ArrayList<>();
        if (!resourceIsExist(directorName)) {
            throw new Exception("要查看的目录不存在！");
        }

        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(directorName));

        if (ArrayUtils.isNotEmpty(fileStatuses)) {
            HDFSResource hdfsResource = null;

            for (FileStatus fileStatus : fileStatuses) {
                hdfsResource = new HDFSResource();
               /* log.info("这是一个：" + (fileStatus.isDirectory() ? "文件夹" : "文件"));
                log.info("副本系数：" + fileStatus.getReplication());
                log.info("大小：" + fileStatus.getLen());
                log.info("路径：" + fileStatus.getPath() + "\n");*/
                hdfsResource.setPath(fileStatus.getPath().toString());
                hdfsResource.setLength(fileStatus.getLen());
                hdfsResource.setSize(FileUtils.formatFileSize(fileStatus.getLen()));

                if (fileStatus.isDirectory()) {
                    hdfsResource.setType("目录");
                } else {
                    hdfsResource.setType("文件");
                }

                hdfsResource.setReplication(fileStatus.getReplication());
                hdfsResource.setBlocklength(fileStatus.getBlockSize());
                hdfsResource.setBlockSize(FileUtils.formatFileSize(fileStatus.getBlockSize()));
                hdfsResource.setModificationTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(fileStatus.getModificationTime())));
                hdfsResource.setAccessTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(fileStatus.getAccessTime())));
                hdfsResource.setPermission(fileStatus.getPermission());
                hdfsResource.setOwner(fileStatus.getOwner());
                hdfsResource.setGroup(fileStatus.getGroup());

                result.add(hdfsResource);
            }
        }

        return result;
    }

    /**
     * 查看某个文件的内容
     *
     * @param fileName 文件名称
     * @throws Exception
     */
    public static void cat(String fileName) throws Exception {
        if (!resourceIsExist(fileName)) {
            throw new Exception("要查看的文件不存在！");
        }

        // 读取文件
        FSDataInputStream in = fileSystem.open(new Path(fileName));
        // 将文件内容输出到控制台上，第三个参数表示输出多少字节的内容
        IOUtils.copyBytes(in, System.out, 1024);
        System.out.println("\n");
        IOUtils.closeStream(in);
    }

    /**
     * 创建文件并写入内容
     *
     * @param fileName    文件名称
     * @param fileContent 文件内容
     * @throws Exception
     */
    public static void create(String fileName, String fileContent) throws Exception {
        if (resourceIsExist(fileName) || isFile(fileName)) {
            throw new Exception("要创建的文件已存在！");
        }
        // 创建文件，第二个参数表示是否重写。
        FSDataOutputStream outputStream = fileSystem.create(new Path(fileName), false);
        // 写入文件内容
        outputStream.write(fileContent.getBytes());
        outputStream.flush();
        IOUtils.closeStream(outputStream);
    }

    /**
     * 上传本地文件到HDFS
     *
     * @param localFilePath  本地文件路径
     * @param targetDirector 目标文件夹
     * @throws Exception
     */
    public static void copyFromLocalFile(String localFilePath, String targetDirector) throws Exception {
        //如果目标文件夹不存在则创建
        if (!resourceIsExist(targetDirector)) {
            fileSystem.mkdirs(new Path(targetDirector));
        }

        String fileName = localFilePath.substring(localFilePath.lastIndexOf(File.separator), localFilePath.length());
        if (resourceIsExist(targetDirector + File.separator + fileName)) {
            throw new Exception("目标目录下已存在同名文件！");
        }

        Path localPath = new Path(localFilePath);
        Path hdfsPath = new Path(targetDirector);
        // 第一个参数是本地文件的路径，第二个则是HDFS的路径
        fileSystem.copyFromLocalFile(localPath, hdfsPath);
    }


    /**
     * 上传本地大体加文件到HDFS，并显示进度条。
     *
     * @param localFilePath  本地文件路径
     * @param targetDirector 目标文件夹
     * @throws Exception
     */
    public static void copyFromLocalFileWithProgress(String localFilePath, String targetDirector) throws Exception {
        //如果目标文件夹不存在则创建
        if (!resourceIsExist(targetDirector)) {
            fileSystem.mkdirs(new Path(targetDirector));
        }

        String fileName = localFilePath.substring(localFilePath.lastIndexOf(File.separator), localFilePath.length());
        if (resourceIsExist(targetDirector + File.separator + fileName)) {
            throw new Exception("目标目录下已存在同名文件！");
        }

        final float fileSize = new File(localFilePath).length() / 65536;
        InputStream in = new BufferedInputStream(new FileInputStream(new File(localFilePath)));

        FSDataOutputStream outputStream = fileSystem.create(new Path(targetDirector + File.separator + fileName), new Progressable() {
            long fileCount = 0;

            @Override
            public void progress() {
                fileCount++;
                System.out.println("总进度：" + (fileCount / fileSize) * 100 + "%");
            }
        });
        IOUtils.copyBytes(in, outputStream, 4096);
        IOUtils.closeStream(in);
        IOUtils.closeStream(outputStream);
    }

    /**
     * 把HDFS的文件下载到本地
     *
     * @param fileName       HDFS上的文件名称（包含文件路径）
     * @param targetDirector 目标存储路径
     * @throws Exception
     */
    public static void copyToLocalFile(String fileName, String targetDirector) throws Exception {
        if (!resourceIsExist(fileName)) {
            throw new Exception("要下载的文件不存在！");
        }

        // 如果目标目录不存在则创建
        File file = new File(targetDirector);
        if (!file.exists()) {
            file.mkdirs();
        }

        String fileNameStr = fileName.substring(fileName.lastIndexOf("/"), fileName.length());
        OutputStream outputStream = new FileOutputStream(new File(targetDirector + File.separator + fileNameStr));
        FSDataInputStream in = fileSystem.open(new Path(fileName));
        IOUtils.copyBytes(in, outputStream, 1024);
        IOUtils.closeStream(in);
        IOUtils.closeStream(outputStream);
    }

    /**
     * 上传文件
     *
     * @param targetDirector 目标文件夹
     * @param files          文件信息
     */
    public static void copyFromLocalFileWithProgress(String targetDirector, List<MultipartFile> files) throws Exception {
        //如果目标文件夹不存在则创建
        if (!resourceIsExist(targetDirector)) {
            fileSystem.mkdirs(new Path(targetDirector));
        }

        if (files != null && files.size() > 0) {
            InputStream in = null;
            FSDataOutputStream outputStream = null;
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                if (resourceIsExist(targetDirector + File.separator + fileName)) {
                    throw new Exception("目标目录下已存在同名文件！");
                }

                final float fileSize = file.getSize() / 65536;
                in = new BufferedInputStream(file.getInputStream());

                outputStream = fileSystem.create(new Path(targetDirector + File.separator + fileName), new Progressable() {
                    long fileCount = 0;

                    @Override
                    public void progress() {
                        fileCount++;
                        System.out.println("总进度：" + (fileCount / fileSize) * 100 + "%");
                    }
                });
                IOUtils.copyBytes(in, outputStream, 4096);

            }

            IOUtils.closeStream(in);
            IOUtils.closeStream(outputStream);
        }

    }

    /**
     * 设置HDFS资源权限
     *
     * @param resourceName   资源名称（包含路径）
     * @param ownerRightCode 所属者权限
     * @param groupRightCode 所属组权限
     * @param otherRightCode 其他用户权限
     * @throws Exception
     */
    public static void setPermission(String resourceName, String ownerRightCode, String groupRightCode, String otherRightCode) throws Exception {
        if (!resourceIsExist(resourceName)) {
            throw new Exception("要设置HDFS权限的资源不存在！");
        }
        FsPermission fsPermission = new FsPermission(FsActionUtils.getFsAction(ownerRightCode), FsActionUtils.getFsAction(groupRightCode), FsActionUtils.getFsAction(otherRightCode));
        fileSystem.setPermission(new Path(resourceName), fsPermission);
    }

    /**
     * 设置HDFS资源所属者和所属组
     *
     * @param resourceName 资源名称（包含路径）
     * @param ownerName    所属者名称
     * @param groupName    所属组名称
     * @throws Exception
     */
    public static void setOwner(String resourceName, String ownerName, String groupName) throws Exception {
        if (!resourceIsExist(resourceName)) {
            throw new Exception("要设置HDFS所属者和所属组的资源不存在！");
        }
        fileSystem.setOwner(new Path(resourceName), ownerName, groupName);
    }

    /**
     * 设置HDFS资源副本系数
     *
     * @param resourceName 资源名称
     * @param count        副本系数
     * @throws Exception
     */
    public static void setReplication(String resourceName, short count) throws Exception {
        if (!resourceIsExist(resourceName)) {
            throw new Exception("要设置HDFS副本系数的资源不存在！");
        }
        fileSystem.setReplication(new Path(resourceName), count);
    }
}
