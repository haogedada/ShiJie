package com.haoge.shijie.controller;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.haoge.shijie.annotation.SerializedField;
import com.haoge.shijie.constant.Constants;
import com.haoge.shijie.dto.UserDTO;
import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.pojo.VideoBean;
import com.haoge.shijie.pojo.respModelBean.AppHomePageBean;
import com.haoge.shijie.pojo.respModelBean.Paging;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.UserService;
import com.haoge.shijie.service.VideoService;
import com.haoge.shijie.util.FileUtil;
import com.haoge.shijie.util.ModelMapperUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//游客控制器
@Api(tags = "游客控制器")
@RestController
public class TouristController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private UserService userService;

    //搜索功能
    @GetMapping("/search/{pageIndex}&&{pageSize}")
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean search(@PathVariable("pageIndex") int pageIndex,
                               @PathVariable("pageSize") int pageSize,
                               @RequestParam("search") String content) {
        Paging paging = videoService.searchVideos(pageIndex, pageSize, content);
        return new ResponseBean().successMethod(paging);
    }

    //按视频分类显示
    @GetMapping("/videoType/{pageIndex}&&{pageSize}")
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean videoByType(@PathVariable("pageIndex") int pageIndex,
                                    @PathVariable("pageSize") int pageSize,
                                    @RequestParam("videoType") String videoType) {
        Paging paging = videoService.showByType(pageIndex, pageSize, videoType);
        return new ResponseBean().successMethod(paging);
    }

    /**
     * 视界app首页
     *
     * @param eachTypeNum
     * @return
     */
    @GetMapping("/app/homepage/{eachTypeNum}")
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean getHomePage(@PathVariable("eachTypeNum") int eachTypeNum) {
        AppHomePageBean appHomePageBean = videoService.showHomePage(eachTypeNum);
        return new ResponseBean().successMethod(appHomePageBean);
    }

    /**
     * 视界首页分类数据
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/home/{pageIndex}&&{pageSize}")
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean getHomePageData(@PathVariable("pageIndex") int pageIndex,
                                 @PathVariable("pageSize") int pageSize) {
           ArrayList videoList  = videoService.HomePageData(pageIndex,pageSize);
              if(videoList.size()>0){
                  return new ResponseBean().successMethod(videoList);
              }else {
                  throw new RuntimeException("数据异常");
              }
    }

    //获取所有视频分类
    @GetMapping("/video/allVideoType")
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean getAllVideoType() {
        List videoTypes = new ArrayList();
        Constants.videoType[] videoTypeArr = Constants.videoType.values();
        for (int i = 0; i < videoTypeArr.length; i++) {
            videoTypes.add(videoTypeArr[i].getName() + ":" + videoTypeArr[i].getValue());
        }
        if (videoTypes.size() > 0) {
            return new ResponseBean().successMethod(videoTypes);
        } else {
            return new ResponseBean().failMethod(500, "获取视频分类列表失败");
        }
    }

    /**
     * 获取软件版本信息
     * @param request
     * @return
     */
    @GetMapping("/AppVersions")
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean getAppVersions( HttpServletRequest request) {
        String versionsPath =  request.getServletContext().getRealPath("AppVersions.json");
        String json="";
        try{
            json = FileUtil.readToString(versionsPath);
        }catch (Exception e){
            throw new RuntimeException("可能json文件不存在"+e.getMessage());
        }
        Map m = new Gson().fromJson(json,Map.class);
        String appVersions = (String) m.get("appVersions");
        String updateMsg = (String) m.get("updateMsg");
        return new ResponseBean().successMethod(m);
    }

    @GetMapping("/test")
    @SerializedField(includes = {"code", "msg", "data"})
    public ResponseBean test(){
       UserBean userBean = userService.findUserById(1);
       UserDTO userDTO = ModelMapperUtil.getStrictModelMapper().map(userBean,UserDTO.class);
      return new ResponseBean().successMethod(userDTO);
    }
}
