package spring.boot.redis;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import spring.boot.redis.base.BaseJunit4Test;

public class MailTest extends BaseJunit4Test {

	@Autowired
	private JavaMailSender mailSender;

	/**
	 * 文本邮件
	 */
	@Test
	public void sendEmailTest() throws Exception {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("389394744@qq.com");
		msg.setTo("zjx_jacky@163.com");
		msg.setSubject("主题：简单邮件");
		msg.setText("测试邮件内容");

		mailSender.send(msg);
	}

	/**
	 * 附件邮件
	 * 
	 * @throws Exception
	 */
	@Test
	public void sendAttachmentsMail() throws Exception {

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom("389394744@qq.com");
		helper.setTo("zjx_jacky@163.com");
		helper.setSubject("主题：有附件");
		helper.setText("有附件的邮件");

		FileSystemResource file = new FileSystemResource(new File("C:\\Users\\111512180\\Desktop\\testMail.gif"));
		helper.addAttachment("附件-1.jpg", file);
		helper.addAttachment("附件-2.jpg", file);

		mailSender.send(mimeMessage);

	}

	/**
	 * 嵌入静态资源邮件
	 * 
	 * @throws Exception
	 */
	@Test
	public void sendInlineMail() throws Exception {

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom("389394744@qq.com");
		helper.setTo("zjx_jacky@163.com");
		helper.setSubject("主题：嵌入静态资源");
		helper.setText("<html><body><img src=\"cid:testMail\" ></body></html>", true);

		FileSystemResource file = new FileSystemResource(new File("C:\\Users\\111512180\\Desktop\\testMail.gif"));
		helper.addInline("testMail", file);

		mailSender.send(mimeMessage);

	}

	/**
	 * 模板邮件
	 * 
	 * @throws Exception
	 */
	@Autowired
	private VelocityEngine velocityEngine;

	@Test
	public void sendTemplateMail() throws Exception {

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom("389394744@qq.com");
		helper.setTo("zjx_jacky@163.com");
		helper.setSubject("主题：模板邮件");

//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("username", "zhengjiaxing");
//		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/template.vm", "UTF-8", model);
//		helper.setText(text, true);

		VelocityContext context = new VelocityContext();
		context.put("username", "zhengjiaxing");

		StringWriter stringWriter = new StringWriter();
		velocityEngine.mergeTemplate("templates/template.vm", "UTF-8", context, stringWriter);
		String text = stringWriter.toString();
		helper.setText(text, true);

		mailSender.send(mimeMessage);
	}
}
