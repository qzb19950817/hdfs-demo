package com.szewec.entity;

import org.apache.hadoop.fs.permission.FsPermission;

/**
 * 封装HDFS资源信息
 *
 * @author QZB
 * @create 2019-08-28-11:22
 */
public class HDFSResource {
    // 资源路径
    private String path;
    // 格式化后的资源大小
    private String size;
    // 真实资源大小
    private Long length;
    // 资源类型
    private String type;
    // 副本系数
    private short replication;
    // 数据块大小
    private long blocklength;
    // 格式化后的数据块大小
    private String blockSize;
    // 修改时间
    private String modificationTime;
    // 访问时间
    private String accessTime;
    // 资源权限
    private FsPermission permission;
    // 所属着
    private String owner;
    // 所属组
    private String group;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public short getReplication() {
        return replication;
    }

    public void setReplication(short replication) {
        this.replication = replication;
    }

    public String getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(String modificationTime) {
        this.modificationTime = modificationTime;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public FsPermission getPermission() {
        return permission;
    }

    public void setPermission(FsPermission permission) {
        this.permission = permission;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public long getBlocklength() {
        return blocklength;
    }

    public void setBlocklength(long blocklength) {
        this.blocklength = blocklength;
    }

    public String getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(String blockSize) {
        this.blockSize = blockSize;
    }
}
