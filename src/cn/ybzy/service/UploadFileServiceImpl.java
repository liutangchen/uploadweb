package cn.ybzy.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.ybzy.dao.DaoFactory;
import cn.ybzy.dao.UploadFileDao;
import cn.ybzy.model.UploadFile;

public class UploadFileServiceImpl implements UploadFileService{

	UploadFileDao uploadFileDao = DaoFactory.getUploadFileDao();
	
	@Override
	public int addFileInfo(UploadFile uploadFile) {
		//把上传上来的文件的信息，保存到数据库之前，我们肯定是要先把文件存到服务器的savePath
		int rows = uploadFileDao.addFileInfo(uploadFile);
		return rows;
	}

	@Override
	public List<UploadFile> getUploadFiles() {
		return uploadFileDao.getUploadFiles();
	}

	@Override
	public int deleteUploadFile(int id) {
		//先把数据库中信息删除
		int rows = uploadFileDao.deleteUploadFile(id);
		//还得把服务器磁盘上的文件删除
		
		return rows;
	}

	@Override
	public void saveFile(HttpServletRequest req, HttpServletResponse resp) {
		//文件处理好了
		
//		addFileInfo(uploadFile);
	}

	@Override
	public void deleteFile(String savePath) {
		// TODO Auto-generated method stub
		
	}

}
