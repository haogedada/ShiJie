package com.haoge.shijie.controller;


import com.haoge.shijie.annotation.SerializedField;
import com.haoge.shijie.pojo.respModelBean.Paging;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


//游客控制器
@RestController
public class TouristController {

    @Autowired
    private VideoService videoService;

    //搜索功能
    @GetMapping("/search/{pageIndex}&&{pageSize}")
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean search(@PathVariable("pageIndex") Integer pageIndex,
                               @PathVariable("pageSize") Integer pageSize,
                               @RequestParam("search") String content) {
        Paging paging = videoService.searchVideos(pageIndex, pageSize, content);
        return new ResponseBean().successMethod(paging);
    }
    //按视频分类显示
    @GetMapping("/videoType/{pageIndex}&&{pageSize}")
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean videoByType(@PathVariable("pageIndex") Integer pageIndex,
                                    @PathVariable("pageSize") Integer pageSize,
                                    @RequestParam("videoType") String videoType) {
        Paging paging = videoService.showByType(pageIndex, pageSize, videoType);
        return new ResponseBean().successMethod(paging);
    }

}
