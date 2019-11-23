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
		//���ϴ��������ļ�����Ϣ�����浽���ݿ�֮ǰ�����ǿ϶���Ҫ�Ȱ��ļ��浽��������savePath
		int rows = uploadFileDao.addFileInfo(uploadFile);
		return rows;
	}

	@Override
	public List<UploadFile> getUploadFiles() {
		return uploadFileDao.getUploadFiles();
	}

	@Override
	public int deleteUploadFile(int id) {
		//�Ȱ����ݿ�����Ϣɾ��
		int rows = uploadFileDao.deleteUploadFile(id);
		//���ðѷ����������ϵ��ļ�ɾ��
		
		return rows;
	}

	@Override
	public void saveFile(HttpServletRequest req, HttpServletResponse resp) {
		//�ļ��������
		
//		addFileInfo(uploadFile);
	}

	@Override
	public void deleteFile(String savePath) {
		// TODO Auto-generated method stub
		
	}

}
