package cn.ybzy.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.ybzy.model.UploadFile;

public interface UploadFileService {
	// 新增
	public int addFileInfo(UploadFile uploadFile);

	// 获取到所有上传到服务器上的文件的信息列表
	public List<UploadFile> getUploadFiles();

	// 删除
	public int deleteUploadFile(int id);
	
	//保存文件的方法
	public void saveFile(HttpServletRequest req,HttpServletResponse resp);
	
	//删除文件的方法
	public void deleteFile(String savePath);
}
