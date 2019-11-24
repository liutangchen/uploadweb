package cn.ybzy.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import cn.ybzy.utils.UploadFilePropertiesUtil;

@WebListener  //监听器开启监听
public class FileUploadPropertiesListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// web服务器启动的时候执行
		InputStream in = getClass().getClassLoader().getResourceAsStream("uploadfile.properties");
		Properties properties = new Properties();
		try {
			properties.load(in);
			for (Object o : properties.keySet()) {
				String key = (String) o;
				String value = properties.getProperty(key);
				UploadFilePropertiesUtil.getInstance().addProperty(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
