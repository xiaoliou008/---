package mysql;

import java.sql.*;

/**
 * 创建数据库连接的工厂
 */
public class ConnectionFactory {
    public static Connection create(String url, String root, String pwd){
        Connection conn = null;
        System.out.println("正在连接数据库...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","root");
            if(conn != null) {
                System.out.println("成功连接到数据库。");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("数据库驱动加载错误，" + e.toString());
        } catch (SQLException e) {
            System.out.println("数据库连接错误，" + e.toString());
        }
        return conn;
    }

    public static String getSQLTime(Connection conn) throws SQLException {
        String sql = "SELECT now()";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet res = statement.executeQuery();
        if(res.next()){
            return res.getString(1);
        }
        return null;
    }
}
