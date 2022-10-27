package co.kr.convert;

import co.kr.convert.data.Mysql;
import co.kr.convert.data.Sqlite;
import co.kr.convert.db.ConnectionSingleton;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        //System.out.print("마이그레이션할 FILE명을 입력하세요 : ");
        //String fileName = sc.next();
        String fileName = "202210A";
        Map<String,Object> map;

        //ecod20완료
        //ecod20s완료
        //ecod21완료
        //ecod40완료
        //ecod40s완료

        //데이터 추출
        Sqlite sqlite = new Sqlite();
        map = sqlite.getData(fileName);

        //데이터 삽입
        Mysql mysql = new Mysql(map);
        mysql.setData();

    }
}