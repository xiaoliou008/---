package mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * 与MySQL服务器连接
 * 单例模式，保证一个客户端只使用一个连接
 */
public class MySQL {
    private static Connection conn;
    private static String url = "jdbc:mysql://localhost:3306/museum";

    public static Connection getConnection() throws SQLException {
        if(conn != null && !conn.isClosed()) return conn;
        conn = ConnectionFactory.create(url, "root", "root");
        return conn;
    }

    public static void close() throws SQLException {
        if(conn != null){
            conn.close();
            conn = null;
            System.out.println("成功关闭数据库连接");
            return;
        }
        System.out.println("错误：关闭不存在的数据库连接");
    }

    public static String getTime() throws SQLException {
        ResultSet res = getConnection().prepareStatement("SELECT now()").executeQuery();
        if(res.next()){
            return res.getString(1);
        } else return null;
    }

    public static String getDate() throws SQLException {
        return getTime().split(" ")[0];
    }

    public static LocalDate getLocalDate() throws SQLException {
        return LocalDate.parse(getDate());
    }
}
