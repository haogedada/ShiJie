package com.haoge.shijie.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceTest {

    @Autowired
    private FileService fileService;
    @Test
    public void deleteFile() {
        boolean deleteFile = fileService.deleteFile("C:\\Users\\haoge\\Desktop\\新建文件夹\\", "Desktop//新建文件夹/classes.zip");
        System.out.println(deleteFile);
    }
}