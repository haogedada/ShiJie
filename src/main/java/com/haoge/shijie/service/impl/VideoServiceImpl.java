package com.haoge.shijie.service.impl;

import com.haoge.shijie.dao.VideoDao;
import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.pojo.VideoBean;
import com.haoge.shijie.service.FileService;
import com.haoge.shijie.service.UserService;
import com.haoge.shijie.service.VideoService;
import com.haoge.shijie.util.DateUtil;
import com.haoge.shijie.util.FileUtil;
import com.haoge.shijie.util.StrJudgeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import static com.haoge.shijie.constant.Constants.pathType.VIDEOCOVERPATH;
import static com.haoge.shijie.constant.Constants.pathType.VIDEOPATH;
import static com.haoge.shijie.constant.Constants.prefixType.COVERIMGPREFIX;
import static com.haoge.shijie.constant.Constants.prefixType.VIDEOPREFIX;
import static com.haoge.shijie.constant.Constants.urlType.VIDEOCOVERURL;
import static com.haoge.shijie.constant.Constants.urlType.VIDEOURL;

@Service
public class VideoServiceImpl implements VideoService {


    @Autowired
    private VideoDao videoDao;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    /**
     * 仅修改视频信息
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public boolean modifyVideo(HttpServletRequest request) {
        Integer videoId = Integer.valueOf(request.getParameter("videoId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String token = request.getHeader("Authorization");
        if (StrJudgeUtil.isCorrectInt(videoId) &&
                StrJudgeUtil.isCorrectStr(title) &&
                StrJudgeUtil.isCorrectStr(content)) {
            UserBean userBean = userService.findUserByToken(token);
            VideoBean videoBean = videoDao.queryVideoByVid(videoId);
            if (userBean.getUserId() == videoBean.getUserId()) {
                try {
                    videoBean.setVideoTitle(title);
                    videoBean.setVideoContent(content);
                    int res = videoDao.updateVideo(videoBean);
                    if (res > 0) {
                        return true;
                    } else {
                        throw new RuntimeException("修改失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("用户无权限进行该操作");
            }
        } else {
            throw new RuntimeException("参数不合法");
        }
        return false;
    }

    /**
     * 修改视频信息及重新上传封面
     *
     * @param videoBean
     * @param token
     * @param filePath
     * @param coverFile
     * @return
     */
    @Override
    @Transactional
    public boolean modifyVideo(VideoBean videoBean, String token, String filePath, MultipartFile coverFile) {
        if (StrJudgeUtil.isCorrectInt(videoBean.getVideoId()) &&
                StrJudgeUtil.isCorrectStr(videoBean.getVideoTitle()) &&
                StrJudgeUtil.isCorrectStr(videoBean.getVideoContent())) {
            UserBean userBean = userService.findUserByToken(token);
            VideoBean videoBean1 = videoDao.queryVideoByVid(videoBean.getVideoId());
            if (userBean.getUserId() == videoBean1.getUserId()) {
                String[] coversPath = new String[]{filePath + VIDEOCOVERPATH};
                MultipartFile[] coversFile = new MultipartFile[]{coverFile};
                String[] coversName = null;
                String imgType = coverFile.getContentType();
                if (FileUtil.isImageFile(imgType)) {
                    String coverName = COVERIMGPREFIX.getName() + videoBean1.getVideoId() + FileUtil.fileTypeConvert(imgType);
                    coversName = new String[]{coverName};
                    try {
                        videoBean1.setVideoTitle(videoBean.getVideoTitle());
                        videoBean1.setVideoContent(videoBean.getVideoContent());
                        videoBean1.setVideoCoverUrl(coverName);
                        boolean success = fileService.upLoadFile(coversFile, coversPath, coversName);
                        int res = videoDao.updateVideo(videoBean1);
                        if (res > 0 && success) {
                            return true;
                        } else {
                            throw new RuntimeException("修改失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    throw new RuntimeException("文件格式非法");
                }
            } else {
                throw new RuntimeException("用户无权限进行该操作");
            }
        } else {
            throw new RuntimeException("参数不合法");
        }
        return false;
    }

    @Override
    public VideoBean findVideo(Integer videoId) {
        if (StrJudgeUtil.isCorrectInt(videoId)) {
            VideoBean videoBean = videoDao.queryVideoByVid(videoId);
            if (videoBean != null) {
                return videoBean;
            } else {
                throw new RuntimeException("视频不存在");
            }
        } else {
            throw new RuntimeException("参数不合法");
        }
    }

    /**
     * 根据视频id删除视频
     *
     * @param token
     * @param videoId
     * @return
     */
    @Override
    @Transactional
    public boolean delVideo(String token, Integer videoId) {
        if (StrJudgeUtil.isCorrectInt(videoId)) {
            UserBean userBean = userService.findUserByToken(token);
            VideoBean videoBean = videoDao.queryVideoByVid(videoId);
            if (videoBean.getUserId() == userBean.getUserId()) {
                try {
                    int res = videoDao.deleteVideo(videoId);
                    if (res > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                throw new RuntimeException("该用户无权限进行该操作");
            }
        } else {
            throw new RuntimeException("参数不合法");
        }

    }

    /**
     * 添加视频，通过上传文件file的长度判断用户上传文件个数
     *
     * @param title,content,token
     * @param content
     * @param token
     * @param file
     * @return返回保存到数据库资源url的文件名
     */
    @Override
    @Transactional
    public boolean addVideo(String title, String content, String token, MultipartFile[] file, String filePath) {
        String[] fileType = null;
        String[] filesPath = new String[]{filePath + VIDEOPATH.getName(), filePath + VIDEOCOVERPATH.getName()};
        if (file.length == 1) {
            fileType = new String[]{file[0].getContentType()};
        } else if (file.length == 2) {
            fileType = new String[]{file[0].getContentType(), file[1].getContentType()};
        }
        //视频格式的判断
        if (!FileUtil.isVedioFile(fileType[0])) {
            throw new RuntimeException("上传视频文件格式错误");
        } else if (file.length == 2) {
            if (!FileUtil.isImageFile(fileType[1])) {
                throw new RuntimeException("上传封面文件格式错误");
            }
        }
        if (StrJudgeUtil.isCorrectStr(title) && StrJudgeUtil.isCorrectStr(content)) {
            UserBean user = userService.findUserByToken(token);
            Integer videoId = videoDao.queryVideoLastId();
            if (StrJudgeUtil.isCorrectInt(videoId)) {
                //根据videoType获取文件后缀名
                String videoext = FileUtil.fileTypeConvert(fileType[0]);
                String videoName = VIDEOPREFIX.getName() + user.getUserId() + "-" + videoId + videoext;
                String coverName = null;
                String[] filesName = null;
                //判断用户是否上传封面，
                if (file.length == 1 && fileType.length == 1) {
                    coverName = COVERIMGPREFIX.getName() + videoId + ".jpg";
                } else if (file.length == 2 && fileType.length == 2) {
                    String coverext = FileUtil.fileTypeConvert(fileType[1]);
                    coverName = COVERIMGPREFIX.getName() + videoId + coverext;
                }
                filesName = new String[]{videoName, coverName};
                try {
                    boolean success = fileService.upLoadFile(file, filesPath, filesName);
                    if (success) {
                        //获取视频时间并转换为时长制
                        int time = FileUtil.getVideoTime(filesPath[0] + filesName[0]);
                        String videoTime = DateUtil.secToTime(time);
                        VideoBean videoBean = new VideoBean();
                        videoBean.setUserId(user.getUserId());
                        videoBean.setVideoTitle(title);
                        videoBean.setVideoContent(content);
                        videoBean.setPlayerCount(0);
                        videoBean.setVideoTipNum(0);
                        videoBean.setVideoTrampleNum(0);
                        videoBean.setVideoUrl(VIDEOURL.getName() + videoName);
                        videoBean.setVideoCoverUrl(VIDEOCOVERURL.getName() + coverName);
                        videoBean.setVideoTime(videoTime);
                        int res = videoDao.insertVideo(videoBean);
                        if (res > 0) {
                            return true;
                        } else {
                            throw new RuntimeException("插入视频失败");
                        }
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("上传文件出错" + e.getMessage());
                }
            }
        }
        return false;
    }

}
