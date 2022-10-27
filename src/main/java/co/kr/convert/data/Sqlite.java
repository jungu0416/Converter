package co.kr.convert.data;

import co.kr.convert.db.ConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sqlite{
    private static Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private ResultSetMetaData rsmd;
    private List columnName = new ArrayList();
    private List data;
    private List dataList = new ArrayList();
    private Map map = new HashMap();
    private int columnCount;
    private String fileName;

    public Sqlite(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, Object> getData() throws SQLException {

        String sql = "select * from ecod40s";

        conn = ConnectionSingleton.getConnection("sqlite",fileName);
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        columnCount = rsmd.getColumnCount();

        for(int i=1; i<=columnCount; i++){
            columnName.add(rsmd.getColumnName(i));
        }


        while (rs.next()){
            data = new ArrayList();
            for(int i=1; i<=columnCount; i++){
                String columnTypeName = rsmd.getColumnTypeName(i);
                String values;

                if(columnTypeName.equals("VARCHAR") || columnTypeName.equals("TEXT")){
                    values = checkQuotes(rs.getObject(i)+"");
                    values = "'"+values+"'";
                } else {
                    values = rs.getObject(i)+"";
                }
                data.add(values);
            }
            dataList.add(data);
        }

        map.put("fileName",fileName);
        map.put("cols",columnName);
        map.put("vals",dataList);

        conn.close();

        return map;
    }

    public String checkQuotes(String str){
        str = str.replace("'","\\'");
        return str;
    }

}
