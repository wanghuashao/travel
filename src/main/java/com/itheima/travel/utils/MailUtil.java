package com.itheima.travel.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.util.Properties;

/**
 * 发送邮件工具类
 */
public final class MailUtil {
	private MailUtil(){}
	/**
	 * 发送邮件
	 * 参数一:发送邮件给谁
	 * 参数二:激活码
	 */
	public static void sendMail(String toEmail, String code) throws Exception {

		//1_创建Java程序与163邮件服务器的连接对象
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", "smtp.qq.com");   // 发件人的邮箱的 SMTP 服务器地址
		props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				//邮件服务器在外网(指定外网邮件的账户和密码！)
				return new PasswordAuthentication("2573339869@qq.com", "fmclpcnbxolydjjd");
			}
		};
		Session session = Session.getInstance(props, auth);
		//2_创建一封邮件

		// Message对象:
		Message message = new MimeMessage(session);
		// 设置发件人：
		message.setFrom(new InternetAddress("2573339869@qq.com"));
		// 设置收件人:
		message.setRecipient(RecipientType.TO, new InternetAddress(toEmail));
		// 设置主题:
		message.setSubject("来自黑马旅游用户注册成功之后的激活邮件!");
		// 设置内容：
		String url = "http://localhost:8080/travel/UserServlet?action=active&code="+code;
		message.setContent("<h1>来自黑马旅游用户注册成功之后的激活邮件!激活请点击以下链接！</h1><h3><a href='"+url+"'>"+url+"</a></h3>","text/html;charset=UTF-8");
		//3_发送邮件
		Transport.send(message);
	}
	/**
	 * 测试类
	 */
	public static void main(String[] args) throws Exception{
		String toEmail = "13253366971@163.com";
		String code = "asdfasfasfasfasfasfasdfasfasfasfasfasfasfasfas";
		sendMail(toEmail,code);
		System.out.println("发送成功。。。");
	}
}








