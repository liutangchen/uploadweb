package cn.ybzy.dao;

import java.util.List;

import cn.ybzy.model.UploadFile;

public interface UploadFileDao {
	// ����
	public int addFileInfo(UploadFile uploadFile);

	// ��ȡ�������ϴ����������ϵ��ļ�����Ϣ�б�
	public List<UploadFile> getUploadFiles();

	// ɾ��
	public int deleteUploadFile(int id);
}
