package data;

import mysql.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynastySQL {
    private static Map<Integer, Dynasty> map;

    private static String sql_all = "SELECT * FROM timetable";

    /**
     * 查询所有的朝代
     * @return
     * @throws SQLException
     */
    public static List<Dynasty> getAll() throws SQLException {
        List<Dynasty> list = new ArrayList<>();
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_all);
        ResultSet res = preparedStatement.executeQuery();
        while(res.next()){
            list.add(new Dynasty(
                    res.getInt("cid"),
                    res.getString("cname"),
                    res.getString("title"),
                    res.getInt("syear"),
                    res.getInt("eyear")
            ));
        }
        return list;
    }

    /**
     * 根据CID查询对应的朝代信息
     * @param cid
     * @return
     * @throws SQLException
     */
    public static Dynasty queryCID(int cid) throws SQLException {
        if(map == null) updateMap();
        return map.get(cid);
    }

    /**
     * 更新朝代信息map
     * @throws SQLException
     */
    public static void updateMap() throws SQLException {
        if(map == null) map = new HashMap<>();
        List<Dynasty> list = getAll();
        for(Dynasty t : list){
            map.put(t.getCid(), t);
        }
    }
}
