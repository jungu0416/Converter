package co.kr.convert.data;

import co.kr.convert.db.ConnectionSingleton;
import org.sqlite.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Mysql{
    private static Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private String fileName;
    private String sql;
    private Map map;

    public Mysql(Map map) {
        this.map = map;
        this.fileName = (String)map.get("fileName");
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

        conn.close();

    }

    public void createTable() throws Exception{
        sql = "CREATE TABLE IF NOT EXISTS `ecod40s_"+fileName+"` SELECT * FROM `ecod40s`";
        pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public boolean checkTable() throws Exception {
        sql = "SELECT 1 FROM Information_schema.tables \n" +
                "WHERE table_schema = 'ems_api' \n" +
                "AND table_name = \'ecod40s"+"_"+fileName + "\'";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        if(rs.next()){
            return true;
        }
        return false;
    };

    public void dropTable() throws Exception {
        sql = "drop table ecod40s" + "_" + fileName;
        pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();
    };

    public void insertData() throws Exception{
        List cols = (ArrayList)map.get("cols");
        List vals = (ArrayList)map.get("vals");
        List val;

        for(int i=0; i<vals.size(); i++){
            val = (ArrayList)vals.get(i);

            sql = "insert into ecod40s" + "_" + fileName + " (" + String.join(",", cols) +") "+"values (" + String.join(",", val) + ")";
            System.out.println(sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();
        }
    };

}
