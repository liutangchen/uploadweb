package cn.ybzy.dao;

import java.util.List;

import cn.ybzy.model.UploadFile;

public class UploadFileDaoImpl extends BaseDao<UploadFile> implements UploadFileDao {

	@Override
	public int addFileInfo(UploadFile uploadFile) {
		String sql = "INSERT INTO uploadfiles(old_file_name,file_type,file_size,"
				+ "save_path,save_time,file_desc,save_name) VALUES(?,?,?,?,?,?,?);";
		return super.update(sql, uploadFile.getOldFileName(),uploadFile.getFileType(),uploadFile.getFileSize(),uploadFile.getSavePath(),
				uploadFile.getSaveTime(),uploadFile.getFileDesc(),uploadFile.getSaveName());
	}

	@Override
	public List<UploadFile> getUploadFiles() {
		String sql = "SELECT\r\n" + 
				"uploadfiles.id,\r\n" + 
				"uploadfiles.old_file_name AS oldFileName,\r\n" + 
				"uploadfiles.file_type AS fileType,\r\n" + 
				"uploadfiles.file_size AS fileSize,\r\n" + 
				"uploadfiles.save_path AS savePath,\r\n" + 
				"uploadfiles.save_time AS saveTime,\r\n" +
				"uploadfiles.file_desc AS fileDesc,\r\n" +
				"uploadfiles.save_name AS saveName\r\n" +
				"FROM\r\n" + 
				"uploadfiles;\r\n" + 
				"";
		return super.getList(sql);
	}

	@Override
	public int deleteUploadFile(int id) {
		String sql = "delete from uploadfiles where id = ?";
		return super.update(sql, id);
	}

}
