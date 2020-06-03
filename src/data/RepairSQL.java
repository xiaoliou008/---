package data;

import mysql.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepairSQL {
    private static String sql_query = "SELECT eid, relic.rid, repair_info.broken, stime, result, restore, etime, pid\n" +
            "FROM repair_info, relic, rtype\n" +
            "WHERE repair_info.rid = relic.rid\n" +
            "\tAND relic.tid = rtype.tid\n" +
            "\tAND relic.rname LIKE ?\n" +
            "\tAND rtype.tid LIKE ?\n" +
            "\tAND stime >= ?\n" +
            "\tAND stime <= ?";
    private static String sql_query_notime = "SELECT eid, relic.rid, repair_info.broken, stime, result, restore, etime, pid\n" +
            "FROM repair_info, relic, rtype\n" +
            "WHERE repair_info.rid = relic.rid\n" +
            "\tAND relic.tid = rtype.tid\n" +
            "\tAND relic.rname LIKE ?\n" +
            "\tAND rtype.tid LIKE ?";
    private static String sql_insert = "INSERT INTO repair_info VALUES (?, ?, ?, ?, '待修复', NULL, NULL, NULL)";
    private static String sql_update_rcv = "UPDATE repair_info SET result = '修复中', pid = ? WHERE eid = ?";
    private static String sql_update_finish =
            "UPDATE repair_info SET result = '修复成功', restore = ?, etime = ? WHERE eid = ?";
    private static String sql_update_begin_relic = "UPDATE relic SET broken = 1 WHERE rid = ?";
    private static String sql_update_finish_relic = "UPDATE relic SET broken = 0 WHERE rid = ?";

    /**
     * 公用代码
     * @param preparedStatement
     * @return
     * @throws SQLException
     */
    public static List<Repair> query(PreparedStatement preparedStatement) throws SQLException {
        ResultSet res = preparedStatement.executeQuery();
        List<Repair> list = new ArrayList<>();
        while (res.next()) {
            if (res.getString("result").equals("未修复")) {
                list.add(new Repair(
                        res.getString("eid"),
                        res.getString("rid"),
                        res.getString("broken"),
                        res.getString("stime")
                ));
            } else {
                list.add(new Repair(
                        res.getString("eid"),
                        res.getString("rid"),
                        res.getString("broken"),
                        res.getString("stime"),
                        res.getString("result"),
                        res.getString("restore"),
                        res.getString("etime"),
                        res.getString("pid")
                ));
            }
        }
        return list;
    }

    public static List<Repair> query(String name, RelicType type, String date) throws SQLException {
        if(name == null) name = "";
        if(type == null) throw new SQLException();
        if(date == null || date.length() < 2) date = MySQL.getDate();
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_query);
        preparedStatement.setString(1, "%" + name + "%");
        preparedStatement.setString(2, type.isAll() ? "%" : String.valueOf(type.getTid()));
        preparedStatement.setString(3, date + " 00:00:00");
        preparedStatement.setString(4, date + " 23:59:59");
        return query(preparedStatement);
    }

    public static List<Repair> query(String name, RelicType type) throws SQLException {
        if(name == null) name = "";
        if(type == null) throw new SQLException();
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_query_notime);
        preparedStatement.setString(1, "%" + name + "%");
        preparedStatement.setString(2, type.isAll() ? "%" : String.valueOf(type.getTid()));
        return query(preparedStatement);
    }

    /**
     * 插入修复信息
     * @param rid
     * @param broken
     * @param datetime
     * @throws SQLException
     */
    public static void insert(String rid, String broken, String datetime) throws SQLException {
        // 开始事务
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement("COMMIT;");
        preparedStatement.execute();
        // 先执行插入操作
        try {
            preparedStatement = MySQL.getConnection().prepareStatement(sql_insert);
            preparedStatement.setString(1, String.valueOf(getID()));
            preparedStatement.setString(2, rid);
            preparedStatement.setString(3, broken);
            preparedStatement.setString(4, datetime);
            preparedStatement.execute();
        } catch (SQLException e){       // 执行失败，回滚
            e.printStackTrace();
            preparedStatement = MySQL.getConnection().prepareStatement("ROLLBACK;");
            preparedStatement.execute();
            throw e;
        }
        // 然后执行修改操作
        // 开始修改relic表
        try {
            preparedStatement = MySQL.getConnection().prepareStatement(sql_update_begin_relic);
            preparedStatement.setString(1, rid);
            preparedStatement.execute();
        } catch (SQLException e){   // 这里失败一定要回滚
            preparedStatement = MySQL.getConnection().prepareStatement("ROLLBACK;");
            preparedStatement.execute();
            throw new SQLException();
        }
        // 提交事务
        preparedStatement = MySQL.getConnection().prepareStatement("COMMIT;");
        preparedStatement.execute();
    }

    private static int getID() throws SQLException {
        List<Repair> list = getAll();
        for (int i = 1; i <= list.size(); i++) {        // 编号默认从1开始
            if (Integer.parseInt(list.get(i-1).getEid()) != i) {        // 下标从0开始
                return i;       // 返回最小的可行编号
            }
        }
        return list.size() + 1;     // 最大编号+1
    }

    public static List<Repair> getAll() throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement("SELECT * FROM repair_info");
        return query(preparedStatement);
    }

    public static void receive(Employee employee, Repair repair) throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_update_rcv);
        preparedStatement.setString(1, employee.getPid());
        preparedStatement.setString(2, repair.getEid());
        preparedStatement.execute();
    }

    public static void finish(String restore, Repair repair) throws SQLException {
        //开始事务
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement("BEGIN;");
        preparedStatement.execute();
        // 开始修改repair_info表
        try {
            preparedStatement = MySQL.getConnection().prepareStatement(sql_update_finish);
            preparedStatement.setString(1, restore);
            preparedStatement.setString(2, MySQL.getTime());
            preparedStatement.setString(3, repair.getEid());
            preparedStatement.execute();
        } catch (SQLException e){
            preparedStatement = MySQL.getConnection().prepareStatement("ROLLBACK;");
            preparedStatement.execute();
            throw new SQLException();
        }
        // 开始修改relic表
        try {
            preparedStatement = MySQL.getConnection().prepareStatement(sql_update_finish_relic);
            preparedStatement.setString(1, repair.getRid());
            preparedStatement.execute();
        } catch (SQLException e){   // 这里失败一定要回滚
            preparedStatement = MySQL.getConnection().prepareStatement("ROLLBACK;");
            preparedStatement.execute();
            throw new SQLException();
        }
        // 提交事务
        preparedStatement = MySQL.getConnection().prepareStatement("COMMIT;");
        preparedStatement.execute();
    }

}
