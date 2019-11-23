package cn.ybzy.dao;

public class DaoFactory {
	public static UploadFileDao getUploadFileDao() {
		return new UploadFileDaoImpl();
	}
}
