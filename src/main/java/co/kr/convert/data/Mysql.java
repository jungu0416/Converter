package co.kr.convert.data;

import co.kr.convert.db.ConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Mysql{
    private static Connection conn;
    private Statement pstmt;
    private ResultSet rs;
    private String fileName;
    private String tableName;
    private String sql;
    private Map map;

    public Mysql(Map map,String fileName, String tableName) {
        this.map = map;
        this.fileName = fileName;
        this.tableName =tableName;
    }

    public void setData() throws Exception {
        conn = ConnectionSingleton.getConnection("mysql","nothing");

        //해당 월에 테이블이 있는지 체크
        if(checkTable()){
            System.out.println("테이블 있음");
            dropTable();
        }else{
            System.out.println("테이블 없음");
        }

        //테이블 생성
        createTable();

        //데이터 마이그레이션
        insertData();

        if ( rs != null ) try{rs.close();}catch(Exception e){}
        if ( pstmt != null ) try{pstmt.close();}catch(Exception e){}
        if ( conn != null ) try{conn.close();}catch(Exception e){}
    }

    public void createTable() throws Exception{
        sql = "CREATE TABLE IF NOT EXISTS `"+ tableName + "_" + fileName + "` SELECT * FROM `"+tableName+"`";
        //pstmt = conn.prepareStatement(sql);
        //pstmt.executeUpdate();
        pstmt = conn.createStatement();
        pstmt.executeUpdate(sql);
        pstmt.close();
    }

    public boolean checkTable() throws Exception {
        sql = "SELECT 1 FROM Information_schema.tables \n" +
                "WHERE table_schema = 'ems_api' \n" +
                "AND table_name = \'"+ tableName + "_" + fileName + "\'";
        //pstmt = conn.prepareStatement(sql);
        //rs = pstmt.executeQuery();
        pstmt = conn.createStatement();
        rs = pstmt.executeQuery(sql);

        if(rs.next()){
            return true;
        }
        return false;
    };

    public void dropTable() throws Exception {
        sql = "drop table "+ tableName + "_" + fileName;
        //pstmt = conn.prepareStatement(sql);
        //pstmt.executeUpdate();
        pstmt = conn.createStatement();
        pstmt.executeUpdate(sql);
        pstmt.close();
    };

    public void insertData() throws Exception{
        List cols = (ArrayList)map.get("cols");
        List vals = (ArrayList)map.get("vals");
        List val;

        for(int i=0; i<vals.size(); i++){
            val = (ArrayList)vals.get(i);

            sql = "insert into "+tableName + "_" + fileName + " (" + String.join(",", cols) +") "+"values (" + String.join(",", val) + ")";
            System.out.println(sql);
            //pstmt = conn.prepareStatement(sql);
            //pstmt.executeUpdate();
            pstmt = conn.createStatement();
            pstmt.executeUpdate(sql);
            pstmt.close();
        }
    };

}
