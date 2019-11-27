package cn.ybzy.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.ybzy.dao.DaoFactory;
import cn.ybzy.dao.UploadFileDao;
import cn.ybzy.model.UploadFile;
import cn.ybzy.utils.UploadFilePropertiesUtil;

public class UploadFileServiceImpl implements UploadFileService {

	UploadFileDao uploadFileDao = DaoFactory.getUploadFileDao();

	private String saveDir = UploadFilePropertiesUtil.getInstance().getProperty("savePath");
	private String tempDir = UploadFilePropertiesUtil.getInstance().getProperty("tempPath");
	private String sizeThreshold = UploadFilePropertiesUtil.getInstance().getProperty("sizeThreshold");
	private String sizeMax = UploadFilePropertiesUtil.getInstance().getProperty("sizeMax");
	private String fileSizeMax = UploadFilePropertiesUtil.getInstance().getProperty("fileSizeMax");
	private String fileEX = UploadFilePropertiesUtil.getInstance().getProperty("fileEX");

	@Override
	public int addFileInfo(UploadFile uploadFile) {
		// ���ϴ��������ļ�����Ϣ�����浽���ݿ�֮ǰ�����ǿ϶���Ҫ�Ȱ��ļ��浽��������savePath
		int rows = uploadFileDao.addFileInfo(uploadFile);
		return rows;
	}

	@Override
	public List<UploadFile> getUploadFiles() {
		return uploadFileDao.getUploadFiles();
	}

	@Override
	public int deleteUploadFile(int id) {
		// �Ȱ����ݿ�����Ϣɾ��
		int rows = uploadFileDao.deleteUploadFile(id);
		// ���ðѷ����������ϵ��ļ�ɾ��

		return rows;
	}

	@Override
	public void saveFile(HttpServletRequest req, HttpServletResponse resp) {
		// �Ȱ��ļ�������������������ָ����Ŀ¼
		// 1.��ȡ�ʹ��������ļ�������Ŀ¼����ʱĿ¼
		String savePath = req.getSession().getServletContext().getRealPath(saveDir); // �����ļ��ķ������ϵľ���·��
		String tempPath = req.getSession().getServletContext().getRealPath(tempDir); // ��ʱĿ¼
		File tempFile = new File(tempPath);
		if (!tempFile.exists()) {
			tempFile.mkdirs(); // �����ʱĿ¼�����ڵĻ������ô���ķ�ʽ������һ����Ŀ¼
		}

		// 2.����һ��DiskFileItemFactory����
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(Integer.parseInt(sizeThreshold)); // ��λ���ֽ� , 100KB,�ϴ����ļ�<100KB,�����ڴ��У�>100KB,��tempPath
		factory.setRepository(tempFile); // �����ϴ��ļ�����ʱĿ¼

		// 3.����һ���ļ��ϴ�������
		ServletFileUpload servletFileUpload = new ServletFileUpload(factory); // �õ��ļ��Ľ�����
		servletFileUpload.setFileSizeMax(Long.parseLong(fileSizeMax)); // �����ϴ������ļ��Ĵ�С��20M����
		servletFileUpload.setHeaderEncoding("UTF-8"); // ��ֹ�ϴ���������Ϣ������
		servletFileUpload.setSizeMax(Long.parseLong(sizeMax)); // ���ƶ���ļ�ͬʱ�ϴ���ʱ���ܴ�С���ֵ40M

		// 4.�ж��ύ�����������Ƿ����ϴ��������ݣ��ǲ���Multipart���뷽ʽ
		// ServletFileUpload.isMultipartContent(request)
		if (!ServletFileUpload.isMultipartContent(req)) {
			throw new RuntimeException("�ϴ��ļ���Form�ı��뷽ʽ����ȷ��");
		}

		// 5.ʹ��ServletFileUpload�����������ϴ����ݣ�����������ص���һ��List<FileItem>����
		String desc = "";
		String fileName = "";
		String fileType = "";
		long fileSize = 0;
		String fileEx = "";
		String saveFileName = "";
		String realSavePath = "";
		OutputStream out = null;
		InputStream in = null;
		try {
			List<FileItem> fileList = servletFileUpload.parseRequest(req);
			if (fileList != null && fileList.size() > 0) {
				for (FileItem fileItem : fileList) {
					if (fileItem.isFormField()) { // �Ǳ���
						desc = fileItem.getString("UTF-8");
						//ÿһ��Ϊdesc ��ֵ��������һ���ļ��Ѿ��ϴ�����ζ�������һ���ļ����ϴ�����
						//��������ϴ������ļ�����Ϣд�����ݿ�
						if (!"".equals(fileName)) {
							UploadFile uf = new UploadFile();
							uf.setFileDesc(desc);
							uf.setFileSize(fileSize+"");
							uf.setFileType(fileType);
							uf.setOldFileName(fileName);
							uf.setSavePath(realSavePath);
							uf.setSaveName(saveFileName);
							uf.setSaveTime(new Date());
							//addFileInfo
							addFileInfo(uf);
						}
					} else { // ���ļ���
						fileName = fileItem.getName(); // �õ��ļ������� xxx.doc xxx.txt
						// ע�����IE�õ���fileName�Ǵ��о���·����D:\abc\xxx.doc ; ���������õ��� xxx.doc
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						fileType = fileItem.getContentType(); // �õ��ļ�������image/jpg
						fileSize = fileItem.getSize(); // �õ��ļ����ܴ�С
						// �õ��ļ���׺��
						fileEx = fileName.substring(fileName.lastIndexOf(".") + 1);
						// ��֤��׺�ĺϷ���
						if (this.fileEX.indexOf(fileEx) == -1) {
							throw new RuntimeException("��ֹ�ϴ����ļ����ͣ�");
						}
						// ���ļ���д�뱣���Ŀ¼��(�����µ��ļ���������һ��Ŀ¼���ļ�̫��������µĴ洢Ŀ¼)
						saveFileName = makeFileName(fileName);
						realSavePath = makePath(saveFileName, savePath);
						// �ȴ���һ�������
						out = new FileOutputStream(realSavePath + "\\" + saveFileName);
						in = fileItem.getInputStream();
						// ��������������һ�������ļ�������������
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = in.read(buffer)) > 0) {
							out.write(buffer, 0, len);
						}
						in.close();
						out.close();
					}
				}
			}
		} catch (FileUploadBase.SizeLimitExceededException e) {
			throw new RuntimeException("�ϴ��ļ��ܴ�С���������ƣ�" + Integer.parseInt(sizeMax) / (1024 * 1024) + "MB!");
		} catch (FileUploadBase.FileSizeLimitExceededException e) {
			throw new RuntimeException("�ϴ������ļ��Ĵ�С���������ƣ�" + Integer.parseInt(fileSizeMax) / (1024 * 1024) + "MB!");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// �ٰ��ļ�����Ϣ���浽���ݿ�

	}

	@Override
	public void deleteFile(String savePath) {
		// TODO Auto-generated method stub

	}

	private String makePath(String saveFileName, String savePath) {
		// �õ��ļ����֣����ڴ浱�е�ַ��hashCodeֵ
		int hashCode = saveFileName.hashCode();
		int dir1 = hashCode & 0xf; // dir1��ֵ���������������Χ0-15
		int dir2 = (hashCode >> 4) & 0xf; // �õ��Ľ������0-15��Χ��
		// ��dir1,dir2�����ҵ��µĴ洢�ļ���Ŀ¼
		String dir = savePath + "\\" + dir1 + "\\" + dir2;
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return dir;
	}

	private String makeFileName(String fileName) {
		// uuid
		return UUID.randomUUID().toString() + "_" + fileName;
	}

}
