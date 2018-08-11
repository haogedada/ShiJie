package com.haoge.shijie.util;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Future;
import javax.imageio.ImageIO;

@Component
public class FileUtil {
    private final static String PREFIX_VIDEO="video/";
    private final static String PREFIX_IMAGE="image/";
    private final static String TITLE="视界";
    /**
     * 异步文件上传
     * @param file,filePath,fileName 文件字节,文件路径,文件名
     *
     */
    @Async
    public Future<String> uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
        return new AsyncResult<String>("ok");
    }
    /**
     * 根据文件类型转换成后缀名
     * @param fileType 文件类型
     * @return 文件后缀名
     */
    public static String fileTypeConvert(String fileType){
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
     * @param fileType 文件类型
     * @return 是否是视频文件
     */
    public static boolean isVedioFile(String fileType){
        if (StrJudgeUtil.isCorrectStr(fileType)&&fileType.contains(PREFIX_VIDEO)){
            return true;
        }
        return false;
    }
    /**
     * 根据文件后缀名判断 文件是否是图片文件
     * @param fileType 文件类型
     * @return 是否是视频文件
     */
    public static boolean isImageFile(String fileType){
        if (StrJudgeUtil.isCorrectStr(fileType)&&fileType.contains(PREFIX_IMAGE)){
            return true;
        }
        return false;
    }
    /**
     * 获取指定视频的帧并保存为图片至指定目录
     * @param videoPath  源视频文件路径
     * @param imgPath,imgName 截取帧的图片存放路径和文件名
     * @throws Exception
     */
    @Async
    public void fetchPic(String videoPath, String imgPath,String imgName) throws Exception{
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoPath);
        ff.start();
        int lenght = ff.getLengthInFrames();
        File targetFile = new File(imgPath+imgName);
        if(!targetFile.exists()){
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
        Java2DFrameConverter converter =new Java2DFrameConverter();
        BufferedImage srcBi =converter.getBufferedImage(frame);
        int owidth = srcBi.getWidth();
        int oheight = srcBi.getHeight();

        Font font = new Font("微软雅黑", Font.PLAIN, 40);
        Color color=new Color(255,255,255,128);

        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH),0, 0, null);
        Graphics2D g = bi.createGraphics();
        // 加水印
        g.setColor(color);           //设置水印颜色
        g.setFont(font);              //设置字体
        //设置水印的坐标
        int x =  (width - getFontWidth(font,TITLE));
        int y =  (height - getFontHeigh(font));
        g.drawString(TITLE, x, y);  //画出水印
        g.dispose();
        try {
            ImageIO.write(bi, "jpg", targetFile);
        }catch (Exception e) {
            e.printStackTrace();
        }
        ff.stop();
    }

    private static int getFontWidth(Font font,String str){
        return sun.font.FontDesignMetrics.getMetrics(font).stringWidth(str)+15;
//高度
//        System.out.println( fm.getHeight() );
//单个字符宽度
//        System.out.println( fm.charWidth( 'A' ));
//整个字符串的宽度
    }
    private static int getFontHeigh(Font font){
        return  sun.font.FontDesignMetrics.getMetrics(font).getHeight()-30;
    }
    /**
     * 获取视频时长，单位为秒
     * @param videoPath 视频路径
     * @return 时长（s）
     */
    public static int getVideoTime(String videoPath){
        Integer times = 0;
        try {
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoPath);
            ff.start();
            times = (int)ff.getLengthInTime()/(1000*1000);
            ff.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }
}