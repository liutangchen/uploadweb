package cn.ybzy.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * jdbc工具类
 * 
 * @author liutangchen
 *
 */
public class JdbcUtils {

	// 数据库连接池，c3p0
	private static DataSource dataSource = null;
	static { // 静态代码块只会被执行一次
		dataSource = new ComboPooledDataSource("mysql");
	}

	/**
	 * 获取数据库mysql的数据连接对象conn
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConn(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void rollBackTransation(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
