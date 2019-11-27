package cn.ybzy.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.ybzy.service.ServiceFactory;
import cn.ybzy.service.UploadFileService;
import cn.ybzy.utils.UploadFilePropertiesUtil;

@WebServlet(urlPatterns = { "/uploadServlet" })
public class UploadFileController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	UploadFileService uploadFileService = ServiceFactory.getUploadFileService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//这Controller层，接收index.jsp页面发送来的文件信息，文件本身，描述信息
		//保存接收到的文件的工作，不再控制层里实现，转发到service，实现保存文件
		try {
			uploadFileService.saveFile(req, resp);
		}catch (Exception e) {
			System.out.println("controller's error:" + e.getMessage());
		}
	}
}
