package cn.ybzy.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * jdbc������
 * 
 * @author liutangchen
 *
 */
public class JdbcUtils {

	// ���ݿ����ӳأ�c3p0
	private static DataSource dataSource = null;
	static { // ��̬�����ֻ�ᱻִ��һ��
		dataSource = new ComboPooledDataSource("mysql");
	}

	/**
	 * ��ȡ���ݿ�mysql���������Ӷ���conn
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
