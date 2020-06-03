package data;

import mysql.MySQL;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisplaySQL {
    private static String sql_all = "SELECT * FROM display ORDER BY dtime DESC ";
    private static String sql_query = "SELECT * FROM display WHERE dtime = ?";
    private static String sql_insert = "INSERT INTO display VALUES(?, ?, ?)";
    private static String sql_delete = "DELETE FROM display WHERE rid = ? AND dtime = ?";

    /**
     * 查询指定日期的所有展览文物
     * @param date
     * @return
     * @throws SQLException
     */
    public static List<Display> query(Date date) throws SQLException {
        System.out.println(date);
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_query);
        preparedStatement.setDate(1, date);
        return query(preparedStatement);
    }

    /**
     * 用字符串表示日期
     * @param date
     * @return
     * @throws SQLException
     */
    public static List<Display> query(String date) throws SQLException{
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_query);
        preparedStatement.setString(1, date);
        return query(preparedStatement);
    }

    /**
     * 公用代码
     * @param preparedStatement
     * @return
     * @throws SQLException
     */
    private static List<Display> query(PreparedStatement preparedStatement) throws SQLException {
        List<Display> list = new ArrayList<>();
        ResultSet res = preparedStatement.executeQuery();
        while(res.next()){
            list.add(new Display(
                    res.getString("rid"),
                    res.getDate("dtime"),
                    res.getString("place")
            ));
        }
        return list;
    }

    public static void insert(String rid, String dtime, String place) throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_insert);
        preparedStatement.setString(1, rid);
        preparedStatement.setString(2, dtime);
        preparedStatement.setString(3, place);
        preparedStatement.execute();
    }

    public static void delete(Display display) throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_delete);
        preparedStatement.setString(1, display.getRid());
        preparedStatement.setString(2, display.getDtime().toString());
        preparedStatement.execute();
    }

    public static List<Display> getAll() throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_all);
        return query(preparedStatement);
    }
}
