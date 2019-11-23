package cn.ybzy.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.ybzy.model.UploadFile;

public interface UploadFileService {
	// ����
	public int addFileInfo(UploadFile uploadFile);

	// ��ȡ�������ϴ����������ϵ��ļ�����Ϣ�б�
	public List<UploadFile> getUploadFiles();

	// ɾ��
	public int deleteUploadFile(int id);
	
	//�����ļ��ķ���
	public void saveFile(HttpServletRequest req,HttpServletResponse resp);
	
	//ɾ���ļ��ķ���
	public void deleteFile(String savePath);
}
