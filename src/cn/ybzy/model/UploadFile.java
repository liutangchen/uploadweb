package cn.ybzy.model;

import java.util.Date;

public class UploadFile {
	private int id;
	private String oldFileName;
	private String fileType;
	private String fileSize;
	private String savePath;
	private Date saveTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOldFileName() {
		return oldFileName;
	}

	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}

	public UploadFile() {
		super();
	}

	public UploadFile(int id, String oldFileName, String fileType, String fileSize, String savePath, Date saveTime) {
		super();
		this.id = id;
		this.oldFileName = oldFileName;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.savePath = savePath;
		this.saveTime = saveTime;
	}

	@Override
	public String toString() {
		return "UploadFile [id=" + id + ", oldFileName=" + oldFileName + ", fileType=" + fileType + ", fileSize="
				+ fileSize + ", savePath=" + savePath + ", saveTime=" + saveTime + "]";
	}
}
