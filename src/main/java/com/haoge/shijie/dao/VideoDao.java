package com.haoge.shijie.dao;

import com.haoge.shijie.pojo.VideoBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoDao {
    /**
     * 查询所有视频集合区域
     *
     * @return
     */
    List<VideoBean> queryVideoList();

    /**
     * 根据userId查询视频集合区域
     *
     * @return
     */
    List<VideoBean> queryVideosByUid(Integer userId);

    /**
     * 根据videoId查询视频
     *
     * @return
     */
    VideoBean queryVideoByVid(Integer videoId);

    /**
     * 根据视频类型查询列表并按播放量排序
     *
     * @param videoType
     * @return
     */
    List<VideoBean> queryVideosByType(@Param("videoType") String videoType, @Param("start") Integer pageIndex, @Param("end") Integer pageSize);

    /**
     * 根据关键字查询
     *
     * @param keyword
     * @return
     */
    List<VideoBean> queryVideosByAll(@Param("keyword") String keyword, @Param("start") Integer pageIndex, @Param("end") Integer pageSize);

    /**
     * 根据视频类型查询个数
     *
     * @param videoType
     * @return
     */
    int queryCountByType(@Param("videoType") String videoType);

    /**
     * 搜索的总条数
     *
     * @param keyword
     * @return
     */
    int queryCountByAll(@Param("keyword") String keyword);

    /**
     * 查询最后插入的视频
     *
     * @return
     */
    Integer queryVideoLastId();

    /**
     * 新增区域信息
     *
     * @param videoBean
     * @return
     */
    int insertVideo(VideoBean videoBean);

    /**
     * 修改区域信息
     *
     * @param videoBean
     * @return
     */
    int updateVideo(VideoBean videoBean);

    /**
     * 修改踩一下+1区域信息
     *
     * @param videoId
     * @return
     */
    int updateVdoTopAdd(Integer videoId);

    /**
     * 修改顶一下+1区域信息
     *
     * @param videoId
     * @return
     */
    int updateVdeoTraAdd(Integer videoId);

    /**
     * 修改Num加1区域信息
     *
     * @param videoId
     * @return
     */
    int updatePlayCountAdd(Integer videoId);

    /**
     * 根据id删除区域
     *
     * @param videoId
     * @return
     */
    int deleteVideo(Integer videoId);
}
