package cn.ybzy.service;

public class ServiceFactory {
	public static UploadFileService getUploadFileService() {
		return new UploadFileServiceImpl();
	}
}
