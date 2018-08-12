package com.haoge.shijie.service;

import com.haoge.shijie.pojo.VideoBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface VideoService {

    /**
     * 插入视频记录
     *
     * @param title,content,token
     * @return
     */
    boolean addVideo(String title, String content, String token, MultipartFile[] file, String filePath);

    /**
     * 删除视频
     *
     * @param token
     * @param videoId
     * @return
     */
    boolean delVideo(String token, Integer videoId);

    /**
     * 修改视频及修改封面
     *
     * @param videoBean
     * @param token
     * @param filePath
     * @param coverFile
     * @return
     */
    boolean modifyVideo(VideoBean videoBean, String token, String filePath, MultipartFile coverFile);

    /**
     * 仅仅修改视频信息
     *
     * @param request
     * @return
     */
    boolean modifyVideo(HttpServletRequest request);

    /**
     * 根据视频id查找视频
     *
     * @param videoId
     * @return
     */
    VideoBean findVideo(Integer videoId);
}
