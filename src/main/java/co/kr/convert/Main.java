package co.kr.convert;

import co.kr.convert.data.Mysql;
import co.kr.convert.data.Sqlite;
import co.kr.convert.data.Migration;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기

        Map<String,Object> map;
        String tableList[] = Migration.NAMES;
        Scanner sc = new Scanner(System.in);
        System.out.print("마이그레이션할 폴더명을 입력하세요 : ");
        String folderName = sc.next(); //fileName = "202210A";
        String[] filenames = getDirectoryFileNames(folderName);

        for(String fileName : filenames){
            fileName = fileName.replace(".db","");
            System.out.println("현재 작업중인 .db 파일 이름 : " + fileName);

            for(String tableName : tableList){
                //데이터 추출
                Sqlite sqlite = new Sqlite(folderName,fileName,tableName);
                map = sqlite.getData();

                //데이터 삽입
                Mysql mysql = new Mysql(map,fileName,tableName);
                mysql.setData();
            }
        }

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산

        System.out.print(secDiffTime > 60 ? Math.floorDiv(secDiffTime,60) + "분 " + Math.floorMod(secDiffTime,60) : secDiffTime);
        System.out.println("초가 소요되었습니다.");

    }

    public static String[] getDirectoryFileNames(String folderName){
        String DATA_DIRECTORY = "\\\\192.168.10.12\\share\\EMS-DB\\data\\paradox\\"+folderName+"\\";
        File dir = new File(DATA_DIRECTORY);
        FilenameFilter filter = (f, name) -> name.contains(".db");
        String[] filenames = dir.list(filter);

        return filenames;
    }

}

