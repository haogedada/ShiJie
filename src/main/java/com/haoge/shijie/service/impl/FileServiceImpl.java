package com.haoge.shijie.service.impl;

import com.haoge.shijie.service.FileService;
import com.haoge.shijie.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Future;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileUtil fileUtil;

    /**
     * 上传视频，根据数组file，filePath，fileName的长度来判断上传文件是单个
     * 还是多个，还是用户上传，还是自动生成封面
     *
     * @param file
     * @param filePath
     * @param fileName
     * @return
     * @throws Exception
     */
    @Override
    public boolean upLoadFile(MultipartFile[] file, String[] filePath, String[] fileName) throws Exception {
        Future<String> ok1 = fileUtil.uploadFile(file[0].getBytes(), filePath[0], fileName[0]);
        if (fileName.length == 1 && filePath.length == 1 && file.length == 1) {
            for (; ; ) {
                if (ok1.isDone()) {
                    return true;
                }
                Thread.sleep(100);
            }
        } else if (file.length == 2 && fileName.length == 2 && filePath.length == 2) {
            for (; ; ) {
                Future<String> ok2 = fileUtil.uploadFile(file[1].getBytes(), filePath[1], fileName[1]);
                if (ok1.isDone() && ok2.isDone()) {
                    return true;
                }
                Thread.sleep(100);
            }
        } else if (file.length == 1 && fileName.length == 2 && filePath.length == 2) {
            for (; ; ) {
                if (ok1.isDone()) {
                    fileUtil.fetchPic(filePath[0] + fileName[0], filePath[1], fileName[1]);
                    return true;
                }
                Thread.sleep(100);
            }
        }
        return false;
    }
}
