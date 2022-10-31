package co.kr.convert.db;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    // 하나의 프로세스에서 공통으로 사용할 수 있는 공용자원(static)
    private static java.sql.Connection conn;

    public static java.sql.Connection getConnection(String dsn, String folderName, String fileName) {
        try {
            if( dsn.equals("mysql")) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                // 드라이버들이 읽히기만 하면 자동 객체가 생성되고 DriverManager에 등록된다.
                // mysql과 연결시키기
                String url = "jdbc:mysql://koreasoft.iptime.org:37306/ems_api?serverTimezone=UTC&useSSL=false";
                conn = DriverManager.getConnection(url, "emsuser22", "ks2022emsuser1!@#");

                //System.out.println("mysql 연결 완료");
            } else if(dsn.equals("sqlite")) {

                Class.forName("org.sqlite.JDBC").newInstance();
                String path = "\\\\192.168.10.12\\share\\EMS-DB\\data\\paradox\\"+folderName+"\\"+fileName+".db";
                System.out.println("path --->" + path);
                conn = DriverManager.getConnection("jdbc:sqlite:"+path);

                //System.out.println("sqlite 연결 완료");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return conn;
        }
    }


    public static void close() throws SQLException {
        if( conn != null ) {
            if( !conn.isClosed() ) {
                conn.close();
            }
        }
    }


}
