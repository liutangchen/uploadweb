package cn.ybzy.utils;

import java.util.HashMap;
import java.util.Map;

public class UploadFilePropertiesUtil {
	private Map<String, String> map = new HashMap<>();

	// 构造方法私有化
	private UploadFilePropertiesUtil() {
	}

	private static UploadFilePropertiesUtil instance = null;

	public static UploadFilePropertiesUtil getInstance() {
		if (instance == null) {
			instance = new UploadFilePropertiesUtil();
		}
		return instance;
	}

	public void addProperty(String key, String value) {
		map.put(key, value);
	}

	public String getProperty(String key) {
		return map.get(key);
	}

}
