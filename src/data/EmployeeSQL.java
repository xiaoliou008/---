package data;

import mysql.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSQL {
    private static String sql_all = "SELECT * FROM employee";
    private static String sql_query = "SELECT * FROM employee WHERE pname LIKE ? AND job LIKE ?";
    private static String sql_update = "UPDATE employee SET pname = ?, job = ? WHERE pid = ?";
    private static String sql_delete = "DELETE FROM employee WHERE pid = ?";
    private static String sql_insert = "INSERT INTO employee VALUES(?, ?, ?, ?)";
    private static String sql_queryName = "SELECT * FROM employee WHERE pid = ?";
    private static String sql_login = "SELECT * FROM employee WHERE pid = ? AND psword = ?";
    private static String sql_updatePassword = "UPDATE employee SET psword = ? WHERE pid = ?";

    /**
     * 查询所有的员工
     * @return
     * @throws SQLException
     */
    public static List<Employee> getAll() throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_all);
        return query(preparedStatement);
    }

    /**
     * 按条件查询员工
     * @param name
     * @param job
     * @return
     * @throws SQLException
     */
    public static List<Employee> query(String name, String job) throws SQLException {
        if(name == null) name = "";
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_query);
        preparedStatement.setString(1, "%" + name + "%");
        preparedStatement.setString(2, job.equals("全部") ? "%" : job);
        return query(preparedStatement);
    }

    /**
     * 公用代码
     * @param preparedStatement
     * @return
     * @throws SQLException
     */
    private static List<Employee> query(PreparedStatement preparedStatement) throws SQLException {
        List<Employee> list = new ArrayList<>();
        ResultSet res = preparedStatement.executeQuery();
        while(res.next()){
            list.add(new Employee(
                    res.getString("pid"),
                    res.getString("pname"),
                    res.getString("psword"),
                    res.getString("job")
            ));
        }
        return list;
    }

    /**
     * 修改员工信息
     * @param pid
     * @param pname
     * @param job
     * @throws SQLException
     */
    public static void update(String pid, String pname, String job) throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_update);
        preparedStatement.setString(1, pname);
        preparedStatement.setString(2, job);
        preparedStatement.setString(3, pid);
        preparedStatement.executeUpdate();
    }

    /**
     * 删除员工
     * @param employee
     * @throws SQLException
     */
    public static void delete(Employee employee) throws SQLException {
        delete(employee.getPid());
    }

    /**
     * 删除员工
     * @param pid
     * @throws SQLException
     */
    public static void delete(String pid) throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_delete);
        preparedStatement.setString(1, pid);
        preparedStatement.execute();
    }

    /**
     * 执行insert操作添加员工信息
     * @param name
     * @param psword
     * @param job
     * @throws SQLException
     */
    public static void insert(String name, String psword, String job) throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_insert);
        preparedStatement.setString(1, String.valueOf(getID()));
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, psword);
        preparedStatement.setString(4, job);
        preparedStatement.execute();
    }

    /**
     * 用于查询最小可行的员工编号
     * @return
     * @throws SQLException
     */
    private static int getID() throws SQLException {
        List<Employee> list = getAll();
        for (int i = 1; i <= list.size(); i++) {        // 编号默认从1开始
            if (Integer.parseInt(list.get(i-1).getPid()) != i) {        // 下标从0开始
                return i;       // 返回最小的可行编号
            }
        }
        return list.size() + 1;     // 最大编号+1
    }

    public static String queryName(String pid) throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_queryName);
        preparedStatement.setString(1, pid);
        ResultSet res = preparedStatement.executeQuery();
        if(res.next()){
            return res.getString("pname");
        } else return null;
    }

    /**
     * 员工登陆
     * @param pid
     * @param psword
     * @return
     * @throws SQLException
     */
    public static Employee login(String pid, String psword) throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_login);
        preparedStatement.setString(1, pid);
        preparedStatement.setString(2, psword);
        ResultSet res = preparedStatement.executeQuery();
        Employee employee = null;
        if(res.next()){
            employee = new Employee(
                    res.getString("pid"),
                    res.getString("pname"),
                    res.getString("psword"),
                    res.getString("job")
            );
        }
        return employee;
    }

    public static void updatePassword(Employee employee, String psword) throws SQLException {
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_updatePassword);
        preparedStatement.setString(1, psword);
        preparedStatement.setString(2, employee.getPid());
        preparedStatement.execute();
    }
}
