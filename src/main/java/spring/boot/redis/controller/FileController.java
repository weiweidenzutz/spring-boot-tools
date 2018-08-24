package spring.boot.redis.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/file")
public class FileController {

	private static final Logger log = LoggerFactory.getLogger(FileController.class);
	
	private static final String FILE_PATH = "D:/temp/test/upload/";

	@RequestMapping("/fileUpload")
	public String index(HttpServletRequest request) {
		File filePath = new File(FILE_PATH);
		if (!filePath.exists()) {
			return "error";
		}
		
		List<String> fileNameList = new ArrayList<String>();
		File[] fileArray = filePath.listFiles();
		for (File file : fileArray) {
			fileNameList.add(file.getName());
		}
		request.setAttribute("fileNameList", fileNameList);
		return "fileUpload";
	}

	@RequestMapping(value = "/upload")
	public String upload(@RequestParam("file") MultipartFile file) {
		try {
			if (file.isEmpty()) {
				return "文件为空";
			}
			// 获取文件名
			String fileName = file.getOriginalFilename();
			log.info("上传的文件名为：" + fileName);
			// 获取文件的后缀名
			String suffixName = fileName.substring(fileName.lastIndexOf("."));
			log.info("文件的后缀名为：" + suffixName);
			// 设置文件存储路径
			String path = FILE_PATH + fileName;
			File dest = new File(path);
			// 检测是否存在目录
			if (!dest.getParentFile().exists()) {
				dest.getParentFile().mkdirs();// 新建文件夹
			}
			file.transferTo(dest);// 文件写入
			
		} catch (IllegalStateException | IOException e) {
			log.error("上传异常，" + e.getMessage(), e);
			return "error";
		} 
		return "success";
	}

	@PostMapping("/batch")
	public String handleFileUpload(HttpServletRequest request) {
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		MultipartFile file = null;
		BufferedOutputStream stream = null;
		for (int i = 0; i < files.size(); ++i) {
			file = files.get(i);
			File filePa = new File(FILE_PATH);
			if (!filePa.exists()) {
				filePa.mkdirs();
			}
			if (!file.isEmpty()) {
				try {
					byte[] bytes = file.getBytes();
					stream = new BufferedOutputStream(new FileOutputStream(new File(FILE_PATH + file.getOriginalFilename())));// 设置文件路径及名字
					stream.write(bytes);// 写入
					stream.close();
				} catch (Exception e) {
					stream = null;
					log.error("第 " + i + " 个文件上传失败 ==> " + e.getMessage());
					return "error";
				}
			} else {
				log.error("第 " + i + " 个文件上传失败因为文件为空");
				return "error";
			}
		}
		return "success";
	}

	@GetMapping("/download")
	public String downloadFile(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String fileName = request.getParameter("fileName");// 文件名
		//fileName = new String(fileName.getBytes("ISO8859-1"), "GB2312");
		System.out.println(fileName);
		if (fileName != null) {
			// 设置文件路径
			File file = new File(FILE_PATH + fileName);
			// File file = new File(realPath , fileName);
			if (file.exists()) {
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
				response.setContentType("multipart/form-data");
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				OutputStream os = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}

}
