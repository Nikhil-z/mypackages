package com.konka.upgrade.service;

public class UpgradeType {
    /**
     * 下载XML
     */
    public static final int DOWNLOAD_XML = 1;
    /**
     * 下载XML校验文件
     */
    public static final int DOWNLOAD_XML_VERIFY_FILE = 2;
    /**
     * XML校验
     */
    public static final int XML_VERIFY = 3;
    /**
     * 解析XML
     */
    public static final int PARSE_XML = 4;
    /**
     * 监测到升级，等待用户操作
     */
    public static final int UPGRADE_FOUND = 5;
    /**
     * 下载升级文件校验文件
     */
    public static final int DOWNLOAD_UPGRADE_VERIFY_FILE = 6;
    /**
     * 下载升级文件
     */
    public static final int DOWNLOAD_UPGRADE_FILE = 7;
    /**
     * 校验文件
     */
    public static final int UPGRADE_FILE_VERIFY = 8;
    /**
     * 下载升级说明
     */
    public static final int DOWNLOAD_IMGINFO = 9;
    /**
     *  下载完成，等待用户进一步操作
     */
    public static final int DOWNLOAD_COMPLETE = 11;
    /**
     * 写入升级文件
     */
    public static final int INSTALL_UPGRADE_FILE = 12;
    /**
     * 用户取消
     */
    public static final int USER_CANCEL = 13;
    /**
     * 下载失败
     */
    public static final int DOWNLOAD_ERROR = -1;
    /**
     * 校验失败
     */
    public static final int FILE_VERIFY_ERROR = -2;
    /**
     * xml解析失败
     */
    public static final int XML_PARSE_ERROR = -3;
    /**
     * 未找到到升级
     */
    public static final int UPGRADE_NOT_FOUND = -4;
    /**
     * 未找到到升级(在强制升级下，弹出为找到升级)
     */
    public static final int UPGRADE_NOT_FOUND_FORCE = -5;
    /**
     * 写入升级信息失败
     */
    public static final int INTALL_UPGRADE_FILE_ERROR = -6;
}
