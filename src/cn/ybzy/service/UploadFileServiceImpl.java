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
		// 把上传上来的文件的信息，保存到数据库之前，我们肯定是要先把文件存到服务器的savePath
		int rows = uploadFileDao.addFileInfo(uploadFile);
		return rows;
	}

	@Override
	public List<UploadFile> getUploadFiles() {
		return uploadFileDao.getUploadFiles();
	}

	@Override
	public int deleteUploadFile(int id) {
		// 先把数据库中信息删除
		int rows = uploadFileDao.deleteUploadFile(id);
		// 还得把服务器磁盘上的文件删除

		return rows;
	}

	@Override
	public void saveFile(HttpServletRequest req, HttpServletResponse resp) {
		// 先把文件保存下来，到服务器指定的目录
		// 1.获取和创建保存文件的最终目录和临时目录
		String savePath = req.getSession().getServletContext().getRealPath(saveDir); // 保存文件的服务器上的绝对路径
		String tempPath = req.getSession().getServletContext().getRealPath(tempDir); // 临时目录
		File tempFile = new File(tempPath);
		if (!tempFile.exists()) {
			tempFile.mkdirs(); // 如果临时目录不存在的话，我用代码的方式，创建一个新目录
		}

		// 2.创建一个DiskFileItemFactory工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(Integer.parseInt(sizeThreshold)); // 单位：字节 , 100KB,上传的文件<100KB,放在内存中，>100KB,放tempPath
		factory.setRepository(tempFile); // 设置上传文件的临时目录

		// 3.创建一个文件上传解析器
		ServletFileUpload servletFileUpload = new ServletFileUpload(factory); // 得到文件的解析器
		servletFileUpload.setFileSizeMax(Long.parseLong(fileSizeMax)); // 限制上传单个文件的大小在20M以内
		servletFileUpload.setHeaderEncoding("UTF-8"); // 防止上传的中文信息是乱码
		servletFileUpload.setSizeMax(Long.parseLong(sizeMax)); // 限制多个文件同时上传的时候，总大小最大值40M

		// 4.判断提交上来的数据是否是上传表单的数据，是不是Multipart编码方式
		// ServletFileUpload.isMultipartContent(request)
		if (!ServletFileUpload.isMultipartContent(req)) {
			throw new RuntimeException("上传文件的Form的编码方式不正确！");
		}

		// 5.使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合
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
					if (fileItem.isFormField()) { // 是表单域
						desc = fileItem.getString("UTF-8");
						//每一次为desc 赋值：代表着一个文件已经上传，意味着完成了一次文件的上传操作
						//在这里把上传上来文件的信息写入数据库
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
					} else { // 是文件域
						fileName = fileItem.getName(); // 拿到文件的名字 xxx.doc xxx.txt
						// 注意事项：IE拿到的fileName是带有绝对路径，D:\abc\xxx.doc ; 火狐浏览器拿到的 xxx.doc
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						fileType = fileItem.getContentType(); // 拿到文件的类型image/jpg
						fileSize = fileItem.getSize(); // 拿到文件的总大小
						// 拿到文件后缀名
						fileEx = fileName.substring(fileName.lastIndexOf(".") + 1);
						// 验证后缀的合法性
						if (this.fileEX.indexOf(fileEx) == -1) {
							throw new RuntimeException("禁止上传该文件类型！");
						}
						// 将文件流写入保存的目录中(生成新的文件名，避免一个目录中文件太多而生成新的存储目录)
						saveFileName = makeFileName(fileName);
						realSavePath = makePath(saveFileName, savePath);
						// 先创建一个输出流
						out = new FileOutputStream(realSavePath + "\\" + saveFileName);
						in = fileItem.getInputStream();
						// 建立缓存区，做一个搬运文件数据流的勺子
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
			throw new RuntimeException("上传文件总大小超出了限制：" + Integer.parseInt(sizeMax) / (1024 * 1024) + "MB!");
		} catch (FileUploadBase.FileSizeLimitExceededException e) {
			throw new RuntimeException("上传单个文件的大小超出了限制：" + Integer.parseInt(fileSizeMax) / (1024 * 1024) + "MB!");
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
		// 再把文件的信息保存到数据库

	}

	@Override
	public void deleteFile(String savePath) {
		// TODO Auto-generated method stub

	}

	private String makePath(String saveFileName, String savePath) {
		// 拿到文件名字，在内存当中地址，hashCode值
		int hashCode = saveFileName.hashCode();
		int dir1 = hashCode & 0xf; // dir1的值，这个与运算结果范围0-15
		int dir2 = (hashCode >> 4) & 0xf; // 得到的结果还是0-15范围内
		// 用dir1,dir2构造我的新的存储文件的目录
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
