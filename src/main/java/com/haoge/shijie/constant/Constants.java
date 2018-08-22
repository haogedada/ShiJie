package com.haoge.shijie.constant;

/**
 * 常量类
 */
public class Constants {
    /**
     * 邮箱类型
     * 1.激活邮件
     * 2.验证码邮件
     */
    public enum mailType {
        ACTIVATION(1, "activation"), CODE(2, "verificationCode");
        private final Integer value;
        private final String name;

        private mailType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

    }

    /**
     * 朋友类型
     * 1.粉丝
     * 2.关注
     */
    public enum friendType {
        FANS(1, "fans"), FOLLOW(2, "follow");
        private final Integer value;
        private final String name;

        friendType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * url类型
     * 1.视频url
     * 2.视频封面图片url
     * 3.头像图片url
     */
    public enum urlType {
        VIDEOURL(1, "http://www.haogedada.top/api/upLoadFile/videoFile/"),
        VIDEOCOVERURL(2, "http://www.haogedada.top/api/upLoadFile/videoCover/"),
        HEADURL(3, "http://www.haogedada.top/api/upLoadFile/headImage/");
        private final Integer value;
        private final String name;

        urlType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 路径类型
     * 1.视频路径
     * 2.视频封面路径
     * 3.头像图片路径
     */
    public enum pathType {
        VIDEOPATH(1, "videoFile" + java.io.File.separator),
        VIDEOCOVERPATH(2, "videoCover" + java.io.File.separator),
        HEADPATH(3, "headImage" + java.io.File.separator);
        private final Integer value;
        private final String name;

        pathType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 文件前缀名
     * 1.视频前缀
     * 2.视频封面前缀
     * 3.头像图片前缀
     */
    public enum prefixType {
        COVERIMGPREFIX(1, "videocover-"),
        VIDEOPREFIX(2, "video-"),
        HEADIMGPREFIX(3, "userheadimg-");
        private final Integer value;
        private final String name;

        prefixType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 角色
     * 1.用户
     * 2.管理员
     */
    public enum roleType {
        USERROLE(1, "user"),
        ADMINROLE(2, "admin");
        private final Integer value;
        private final String name;

        roleType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 视频类型
     */
    public enum videoType {
        SOCIOLOGY(1, "sociology", "社会"),
        WORLD(2, "world", "世界"),
        SPORTS(3, "sports", "体育"),
        LIFE(4, "life", "生活"),
        TECH(5, "tech", "科技"),
        ENTERTAINMENT(6, "entertainment", "娱乐"),
        MOVIE(7, "movie", "电影"),
        AUTO(8, "auto", "汽车"),
        TASTE(9, "taste", "美食"),
        MUSIC(10, "music", "音乐"),
        BUSINESS(11, "business", "商业"),
        HOT(12, "hot", "热门");
        private final Integer index;
        private final String name;
        private final String value;

        videoType(Integer index, String name, String value) {
            this.index = index;
            this.name = name;
            this.value = value;
        }

        public static videoType getVideoType(String typeName) {
            for (int i = 0; i < values().length; i++) {
                if (values()[i].getName().equals(typeName)) {
                    return values()[i];
                }
            }
            return null;
        }

        public Integer getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }


}
