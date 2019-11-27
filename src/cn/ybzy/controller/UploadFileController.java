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
		//��Controller�㣬����index.jspҳ�淢�������ļ���Ϣ���ļ�����������Ϣ
		//������յ����ļ��Ĺ��������ٿ��Ʋ���ʵ�֣�ת����service��ʵ�ֱ����ļ�
		try {
			uploadFileService.saveFile(req, resp);
		}catch (Exception e) {
			System.out.println("controller's error:" + e.getMessage());
		}
	}
}
