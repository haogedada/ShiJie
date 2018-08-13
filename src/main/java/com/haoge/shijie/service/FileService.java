package com.haoge.shijie.service;

import org.springframework.web.multipart.MultipartFile;


public interface FileService {

    /**
     * 上传文件
     *
     * @param file
     * @param filePath
     * @param fileName
     * @return
     */
    boolean upLoadFile(MultipartFile[] file, String[] filePath, String[] fileName) throws Exception;

}
