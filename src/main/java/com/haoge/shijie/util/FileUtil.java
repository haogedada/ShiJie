package com.haoge.shijie.util;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Future;

import static com.haoge.shijie.constant.Constants.prefixType.HEADIMGPREFIX;

@Component
public class FileUtil {
    private final static String PREFIX_VIDEO = "video/";
    private final static String PREFIX_IMAGE = "image/";
    private final String TITLE = "视界";
    private Font font = new Font("微软雅黑", Font.PLAIN, 40);
    private Color color = new Color(255, 255, 255, 128);

    /**
     * 根据文件类型转换成后缀名
     *
     * @param fileType 文件类型
     * @return 文件后缀名
     */
    public static String fileTypeConvert(String fileType) {
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        MimeType type = null;
        try {
            type = allTypes.forName(fileType);
        } catch (MimeTypeException e) {
            e.printStackTrace();
        }
        return type.getExtension();
    }

    /**
     * 根据文件后缀名判断 文件是否是视频文件
     *
     * @param fileType 文件类型
     * @return 是否是视频文件
     */
    public static boolean isVedioFile(String fileType) {
        if (StrJudgeUtil.isCorrectStr(fileType) && fileType.contains(PREFIX_VIDEO)) {
            return true;
        }
        return false;
    }

    /**
     * 根据文件后缀名判断 文件是否是图片文件
     *
     * @param fileType 文件类型
     * @return 是否是视频文件
     */
    public static boolean isImageFile(String fileType) {
        if (StrJudgeUtil.isCorrectStr(fileType) && fileType.contains(PREFIX_IMAGE)) {
            return true;
        }
        return false;
    }

    private static int getFontWidth(Font font, String str) {
        return sun.font.FontDesignMetrics.getMetrics(font).stringWidth(str) + 15;
//高度
//        System.out.println( fm.getHeight() );
//单个字符宽度
//        System.out.println( fm.charWidth( 'A' ));
//整个字符串的宽度
    }

    private static int getFontHeigh(Font font) {
        return sun.font.FontDesignMetrics.getMetrics(font).getHeight() - 30;
    }

    /**
     * 获取视频时长，单位为秒
     *
     * @param videoPath 视频路径
     * @return 时长（s）
     */
    public static int getVideoTime(String videoPath) {
        Integer times = 0;
        try {
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoPath);
            ff.start();
            times = (int) ff.getLengthInTime() / (1000 * 1000);
            ff.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * 异步文件上传,如果是图片文件就添加水印
     *
     * @param file,filePath,fileName 文件字节,文件路径,文件名
     */
    @Async
    public Future<String> uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
        if (isImgTypeByExt(fileName) && !fileName.contains(HEADIMGPREFIX.getName())) {
            boolean success = addWaterMark(filePath + fileName);
            if (success) {
                return new AsyncResult<String>("ok");
            }
        }
        return new AsyncResult<String>("ok");
    }

    //判断文件是否是图片
    public boolean isImgTypeByExt(String fileName) {
        if (fileName == null) {
            return false;
        } else {
            // 获取文件后缀名并转化为写，用于后续比较
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
            // 创建图片类型数组
            String img[] = {"jpg", "jpeg", "png", "bmp", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd",
                    "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf"};
            for (int i = 0; i < img.length; i++) {
                if (img[i].equals(fileType)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     * @param videoPath       源视频文件路径
     * @param imgPath,imgName 截取帧的图片存放路径和文件名
     * @throws Exception
     */
    @Async
    public void fetchPic(String videoPath, String imgPath, String imgName) throws Exception {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoPath);
        ff.start();
        int lenght = ff.getLengthInFrames();
        File targetFile = new File(imgPath + imgName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        int i = 0;
        Frame frame = null;
        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            frame = ff.grabFrame();
            if ((i > 5) && (frame.image != null)) {
                break;
            }
            i++;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage srcBi = converter.getBufferedImage(frame);
        int owidth = srcBi.getWidth();
        int oheight = srcBi.getHeight();


        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        Graphics2D g = bi.createGraphics();
        // 加水印
        g.setColor(color);           //设置水印颜色
        g.setFont(font);              //设置字体
        //设置水印的坐标
        int x = (width - getFontWidth(font, TITLE));
        int y = (height - getFontHeigh(font));
        g.drawString(TITLE, x, y);  //画出水印
        g.dispose();
        try {
            ImageIO.write(bi, "jpg", targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ff.stop();
    }

    /**
     * 图片添加水印
     *
     * @param srcImgPath 源图片路径
     */
    public boolean addWaterMark(String srcImgPath) {
        try {
            // 读取原图片信息
            File srcImgFile = new File(srcImgPath);//得到文件
            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            g.setColor(color); //根据图片的背景设置水印颜色
            g.setFont(font);              //设置字体
            //设置水印的坐标
            int x = (srcImgWidth - getFontWidth(font, TITLE));
            int y = (srcImgHeight - getFontHeigh(font));
            g.drawString(TITLE, x, y);  //画出水印
            g.dispose();
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream(srcImgPath);
            ImageIO.write(bufImg, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}