package data;

import mysql.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelicTypeSQL {
    private static Map<Integer, RelicType> map;

    private static String sql_all = "SELECT * FROM rtype";

    /**
     * 查询所有的文物类型
     * @return
     * @throws SQLException
     */
    public static List<RelicType> getAll() throws SQLException {
        List<RelicType> list = new ArrayList<>();
        PreparedStatement preparedStatement = MySQL.getConnection().prepareStatement(sql_all);
        ResultSet res = preparedStatement.executeQuery();
        while(res.next()){
            list.add(new RelicType(
                    res.getInt("tid"),
                    res.getString("tname"),
                    res.getString("tdesc")
            ));
        }
        return list;
    }

    /**
     * 根据TID查询对应的分类信息
     * @param tid
     * @return
     * @throws SQLException
     */
    public static RelicType queryTID(int tid) throws SQLException {
        if(map == null) updateMap();
        return map.get(tid);
    }

    /**
     * 更新分类信息map
     * @throws SQLException
     */
    public static void updateMap() throws SQLException {
        if(map == null) map = new HashMap<>();
        List<RelicType> list = getAll();
        for(RelicType t : list){
            map.put(t.getTid(), t);
        }
    }
}
