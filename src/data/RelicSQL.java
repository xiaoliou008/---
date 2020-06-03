package data;

import com.mysql.cj.protocol.Resultset;
import mysql.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询文物相关
 */
public class RelicSQL {
    private static String sql_all = "SELECT * FROM relic";
    private static String sql_query = "SELECT *\n" +
            "FROM relic\n" +
            "WHERE rname LIKE ?\n" +
            "\tAND tid LIKE ?\n" +
            "\tAND cid LIKE ?";
    private static String sql_update = "UPDATE relic\n" +
            "SET rname = ?\n" +
            ",tid = ?\n" +
            ", cid = ?\n" +
            "WHERE rid = ?";
    private static String sql_delete = "DELETE FROM relic\n" +
            "WHERE rid = ?";
    private static String sql_insert = "INSERT INTO relic VALUES(?, ?, ?, ?, 0, 0, ?, ?, NULL)";
    private static String sql_queryName = "SELECT rname FROM relic WHERE rid = ?";
    private static String sql_setImage = "UPDATE relic SET image = ? WHERE rid = ?";

    /**
     * 查询所有的文物
     *
     * @return
     * @throws SQLException
     */
    public static List<Relic> getAll() throws SQLException {
        List<Relic> list = new ArrayList<>();
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_all);
        ResultSet res = preparedStatement.executeQuery();
        while (res.next()) {
            list.add(new Relic(
                    res.getString("rid"),
                    res.getString("rname"),
                    res.getInt("tid"),
                    res.getInt("cid"),
                    res.getBoolean("display"),
                    res.getBoolean("broken"),
                    res.getString("unity"),
                    res.getInt("num"),
                    res.getString("image")
            ));
        }
        return list;
    }

    /**
     * 按条件查询文物
     *
     * @param name
     * @param type
     * @param dynasty
     * @return
     * @throws SQLException
     */
    public static List<Relic> query(String name, RelicType type, Dynasty dynasty) throws SQLException {
        List<Relic> list = new ArrayList<>();
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_query);
        // 模糊查询文物名称
        if (name == null) name = "";
        preparedStatement.setString(1, "%" + name + "%");
        // 查询文物类型
        if (type.isAll())
            preparedStatement.setString(2, "%");
        else
            preparedStatement.setString(2, String.valueOf(type.getTid()));
        // 查询文物年代
        if (dynasty.isAll())
            preparedStatement.setString(3, "%");
        else
            preparedStatement.setString(3, String.valueOf(dynasty.getCid()));
        // 开始查询
        ResultSet res = preparedStatement.executeQuery();
        while (res.next()) {
            list.add(new Relic(
                    res.getString("rid"),
                    res.getString("rname"),
                    res.getInt("tid"),
                    res.getInt("cid"),
                    res.getBoolean("display"),
                    res.getBoolean("broken"),
                    res.getString("unity"),
                    res.getInt("num"),
                    res.getString("image")
            ));
        }
        return list;
    }

    /**
     * 修改文物信息
     * @param rid
     * @param rname
     * @param relicType
     * @param dynasty
     * @throws SQLException
     */
    public static void update(String rid, String rname, RelicType relicType, Dynasty dynasty) throws SQLException {
        if (relicType.isAll() || dynasty.isAll()) throw new SQLException();
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_update);
        preparedStatement.setString(1, rname);
        preparedStatement.setString(2, String.valueOf(relicType.getTid()));
        preparedStatement.setString(3, String.valueOf(dynasty.getCid()));
        preparedStatement.setString(4, rid);
        preparedStatement.executeUpdate();
    }

    /**
     * 删除文物
     * @param relic
     * @throws SQLException
     */
    public static void delete(Relic relic) throws SQLException {
        delete(relic.getRid());
    }

    /**
     * 删除文物
     * @param rid
     * @throws SQLException
     */
    public static void delete(String rid) throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_delete);
        preparedStatement.setString(1, rid);
        preparedStatement.execute();
    }

    /**
     * 执行insert操作添加文物信息
     * @param name
     * @param type
     * @param dynasty
     * @param unity
     * @param num
     * @throws SQLException
     */
    public static void insert(String name, RelicType type, Dynasty dynasty, String unity, int num) throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_insert);
        preparedStatement.setString(1, String.valueOf(getID()));
        preparedStatement.setString(2, name);
        preparedStatement.setInt(3, type.getTid());
        preparedStatement.setInt(4, dynasty.getCid());
        preparedStatement.setString(5, unity);
        preparedStatement.setInt(6, num);
        preparedStatement.execute();
    }

    /**
     * 用于查询最小可行的文物编号
     * @return
     * @throws SQLException
     */
    private static int getID() throws SQLException {
        List<Relic> list = getAll();
        for (int i = 1; i <= list.size(); i++) {        // 编号默认从1开始
            if (Integer.parseInt(list.get(i-1).getRid()) != i) {        // 下标从0开始
                return i;       // 返回最小的可行编号
            }
        }
        return list.size() + 1;     // 最大编号+1
    }

    public static String queryName(String rid) throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_queryName);
        preparedStatement.setString(1, rid);
        ResultSet res = preparedStatement.executeQuery();
        if(res.next()){
            return res.getString(1);
        } else return null;
    }

    public static void setImage(Relic relic, String path) throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_setImage);
        preparedStatement.setString(1, path);
        preparedStatement.setString(2, relic.getRid());
        preparedStatement.execute();
    }
}
