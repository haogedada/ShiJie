package com.haoge.shijie.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class MailUtil implements Runnable {
    private String email;// 收件人邮箱
    private String code;// 激活码
    private String mailType;//邮件类型

    public MailUtil(String email, String code, String mailType) {
        this.email = email;
        this.code = code;
        this.mailType = mailType;
    }

    public void run() {
        // 1.创建连接对象javax.mail.Session
        // 2.创建邮件对象 javax.mail.Message
        // 3.发送一封激活邮件
        String from = "1797016822@qq.com";// 发件人电子邮箱
        String host = "smtp.qq.com"; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)
        Properties properties = System.getProperties();// 获取系统属性

        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
        properties.setProperty("mail.smtp.auth", "true");// 打开认证

        try {
            //QQ邮箱需要下面这段代码，163邮箱不需要
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);
            // 1.获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("1797016822@qq.com", "qaqfjxogoahfcaic"); // 发件人邮箱账号、授权码
                }
            });
            // 2.创建邮件对象
            Message message = new MimeMessage(session);
            //设置自定义发件人昵称
            String nick = "";
            String title = "";
            String content = "";
            try {
                String nickText = "";
                String titleText = "";
                if (mailType.equals("activation")) {
                    nickText = "视界客服";
                    titleText = "视界账号注册激活";
                    content = "<html>\n" +
                            "<head></head><body>" +
                            "<div style= \"background:#DBF1ED\">" +
                            "<h2>感谢您注册视界！请点击以下链接进行验证，完成您的注册过程：\n" +
                            "</h2><h3><a href='http://www.haogedada.top/api/register/activation?code=" + code + "'>" +
                            "http://www.haogedada.top/api/register/activation?code=" + code + "</href>\n" +
                            "</h3><div style=\"font-size: 1.25em\">\n" +
                            "注：如果链接无法点击打开,请将上面链接复制粘贴到浏览器地址栏中访问。感谢对视界的支持！<br>\n" +
                            "敬请关注视界动态：本邮件由系统自动发送，请勿直接回复！<br>\n" +
                            "如有您有任何疑问，可通过邮件 1797016822@qq.com 联系我们！(两小时内点击链接激活有效)</div>" +
                            "</div>" +
                            "</body></html>";
                } else if (mailType.equals("verificationCode")) {
                    nickText = "视界";
                    titleText = "忘记密码验证码邮件";
                    content = "<html>\n" +
                            "<head>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "<div style= \"background:#DBF1ED\">" +
                            "<h2><strong>尊敬的视界用户：</strong></h2>\n" +
                            "<h3>\n" +
                            "&emsp;&emsp;您正在进行找回密码，要完成该操作，请在30分钟内输入如下验证码：<br>\n" +
                            "<div style=\"padding-top: 25px;padding-bottom: 25px\">\n" +
                            "<div align=\"center\" style=\"font-size: 2em;background:#CCD6EC;color:#C92124\">" + code + "</div>\n" +
                            "</div>\n" +
                            "如果您输入验证码提示已过期，请重新获取验证码，感谢您的配合与支持！\n" +
                            "<br>（如非本人操作，请忽略此邮件）\n" +
                            "</h3>\n" +
                            "</div>" +
                            "</body></html>";
                }
                nick = javax.mail.internet.MimeUtility.encodeText(nickText, "gb2312", null);
                title = javax.mail.internet.MimeUtility.encodeText(titleText, "gb2312", null);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 2.1设置发件人
            message.setFrom(new InternetAddress(nick + " <" + from + ">"));
            // 2.2设置接收人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 2.3设置邮件主题
            message.setSubject(title);
            // 2.4设置邮件时间
            message.setSentDate(new Date());
            // 2.5设置邮件内容
            message.setContent(content, "text/html;charset=UTF-8");
            // 3.发送邮件
            Transport.send(message);
            System.out.println("邮件成功发送!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
