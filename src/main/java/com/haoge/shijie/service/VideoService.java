package com.haoge.shijie.service;

import com.haoge.shijie.pojo.VideoBean;
import com.haoge.shijie.pojo.respModelBean.AppHomePageBean;
import com.haoge.shijie.pojo.respModelBean.Paging;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface VideoService {

    /**
     * 插入视频记录
     *
     * @param video,content,token
     * @return
     */
    boolean addVideo(VideoBean video, String token, MultipartFile[] file, String filePath);

    /**
     * 删除视频
     *
     * @param token
     * @param videoId
     * @return
     */
    boolean delVideoByVid(String token, int videoId);

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
    VideoBean findVideoByVid(int videoId);

    /**
     * 视频顶一下
     *
     * @param toVideoId
     * @param token
     * @return 是否成功
     */
    boolean modifyVideoTop(int toVideoId, String token);

    /**
     * 视频踩一下
     *
     * @param VideoId
     * @param token
     * @return 是否成功
     */
    boolean modifyVideoTrample(int VideoId, String token);

    /**
     * 视频播放量+1
     *
     * @param VideoId
     * @param token
     * @return 是否成功
     */
    boolean modifyVideoPlayCount(int VideoId, String token);

    /**
     * 搜索视频
     *
     * @param pageIndex
     * @param pageSize
     * @param content
     * @return
     */
    Paging searchVideos(int pageIndex, int pageSize, String content);

    /**
     * 按视频分类显示
     *
     * @param pageIndex
     * @param pageSize
     * @param videoType
     * @return
     */
    Paging showByType(int pageIndex, int pageSize, String videoType);

    /**
     * 视频首页
     *
     * @param eachTypeNum
     * @return
     */
    AppHomePageBean showHomePage(int eachTypeNum);

    /**
     *视界首页数据
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ArrayList HomePageData(int pageIndex, int pageSize);


}
