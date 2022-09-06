package com.primitive.todayMenuDBApi.controller.DBcontroller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Base64;

@RestController
@RequestMapping("")
public class ImageController {
    String osName = System.getProperty("os.name").toLowerCase();
    private final String sep=File.separator;
    private final String uploadDir = (osName.contains("win")?"D:\\today_menu_api\\images":"/home/ec2-user/today_menu_api/images");//sep 이 / 인 경우 리눅스이며 구분자 중복 필요 없음
    private final String u = (osName.contains("win")?"D:\\\\today_menu_api\\\\images":"/home/ec2-user/today_menu_api/images");



    private DB_Connection_Data key = DB_Connection_Data.getInstance();

    @GetMapping( value = "images/lunch/{day}", produces = MediaType.IMAGE_JPEG_VALUE )
    public String get_lunch_image(@PathVariable String day) throws IOException {

        String filePath = null;//DB에서 받아온 경로가 저장될 변수
        FileInputStream in=null;//로컬에서 읽은 파일이 들어오는 변수
    //DB에서 경로 조회
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
            con.createStatement().execute("use " + key.getDBname());

            ResultSet rs =con.createStatement().executeQuery(String.format("select * from images where is_lunch = true and Date='%s';",day));

            if (rs.next()){
                filePath=rs.getString("Path");
            }
            con.close();
            if (filePath==null||filePath.equals("")){
                filePath=uploadDir+sep+"no_image.png";
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            in = new FileInputStream(filePath);
        }catch (Exception e){
            filePath=uploadDir+sep+"no_image.png";
            in = new FileInputStream(uploadDir+sep+"no_image.png");
        }
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return encodedString;
    }
    @GetMapping(value = "images/dinner/{day}", produces = MediaType.IMAGE_JPEG_VALUE )
    public String get_dinner_image(@PathVariable String day) throws IOException{

        String filePath = null;//DB에서 받아온 경로가 저장될 변수
        FileInputStream in=null;//로컬에서 읽은 파일이 들어오는 변수
    //DB에서 경로 조회
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
            con.createStatement().execute("use " + key.getDBname());

            ResultSet rs =con.createStatement().executeQuery(String.format("select * from images where is_lunch = false and Date='%s';",day));
            if (rs.next()){
                filePath=rs.getString("Path");
            }
            con.close();
            if (filePath==null||filePath.equals("")){
                filePath=uploadDir+sep+"no_image.png";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            in = new FileInputStream(filePath);
        }catch (Exception e){
            filePath=uploadDir+sep+"no_image.png";
            in = new FileInputStream(uploadDir+sep+"no_image.png");
        }
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return encodedString;
    }


    @PostMapping("add/images/lunch/{day}")//@modelattribute("key")
    public void set_lunch_image(@PathVariable String day, @RequestBody String encoded_image){
        if (!encoded_image.isEmpty()) {
            String filename =day+".png";
            String fullPath = uploadDir +sep+"lunch"+sep+ filename;
            String DBPath= u+sep+sep+"lunch"+sep+sep+filename;//윈도우인 경우 구분자가 \이기 때문에 구분자 중복 필요
            byte [] decoded_image =Base64.getDecoder().decode(encoded_image);//받은 이미지 디코딩 해서 로컬에 저장
    //멀티파트로 받은 변수를 로컬 경로에 저장
            if(encoded_image != null) {
                try{
                    if(!osName.contains("win")){Runtime.getRuntime().exec("chmod -R 777 " + uploadDir);}
                    File uploadFile = new File(fullPath);
                    FileCopyUtils.copy(decoded_image, uploadFile);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
    //저장한 경로를 DB 테이블에 저장
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
                con.createStatement().execute("use "+key.getDBname());

                int idx;
        //마지막 인덱스값 구해옴
                ResultSet rs =con.createStatement().executeQuery("SELECT ID FROM images ORDER BY id DESC LIMIT 1;");
                rs.next();
                try{
                    idx = rs.getInt("ID");
                }catch (Exception e) {
                    idx = 0;
                }
        //경로 저장
                String query = String.format("SELECT * FROM images where is_lunch=%b and date='%s';",true,day);
                ResultSet rs2 =con.createStatement().executeQuery(query);
                if(rs2.next()){
        //DB에 값이 이미 들어있는 경우 - 수정
                    con.createStatement().execute(String.format("update images set Path='%s' where is_lunch=%b and date='%s';",DBPath,true,day));
                }else{
        //DB에 데이터 자체가 없는 경우 - 열 추가
                    con.createStatement().execute(String.format("insert images (ID, is_lunch, Path, Date) values(%d,%b,'%s','%s');",idx+1,true,DBPath,day));
                }
                con.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return;
    }

    @PostMapping("add/images/dinner/{day}")
    public void set_dinner_image(@PathVariable String day, @RequestBody String encoded_image){
        if (!encoded_image.isEmpty()) {
            String filename =day+".png";
            String fullPath = uploadDir +sep+"dinner"+sep+ filename;
            String DBPath= u+sep+sep+"dinner"+sep+sep+filename;//윈도우인 경우 구분자가 \이기 때문에 구분자 중복 필요
            byte [] decoded_image =Base64.getDecoder().decode(encoded_image);//받은 이미지 디코딩 해서 로컬에 저장
            //멀티파트로 받은 변수를 로컬 경로에 저장
            if(encoded_image != null) {
                try{
                    if(!osName.contains("win")){Runtime.getRuntime().exec("chmod -R 777 " + uploadDir);}
                    File uploadFile = new File(fullPath);
                    FileCopyUtils.copy(decoded_image, uploadFile);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            //저장한 경로를 DB 테이블에 저장
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
                con.createStatement().execute("use "+key.getDBname());

                int idx;
                //마지막 인덱스값 구해옴
                ResultSet rs =con.createStatement().executeQuery("SELECT ID FROM images ORDER BY id DESC LIMIT 1;");
                rs.next();
                try{
                    idx = rs.getInt("ID");
                }catch (Exception e) {
                    idx = 0;
                }
                //경로 저장
                String query = String.format("SELECT * FROM images where is_lunch=%b and date='%s';",false,day);
                ResultSet rs2 =con.createStatement().executeQuery(query);
                if(rs2.next()){
                    //DB에 값이 이미 들어있는 경우 - 수정
                    con.createStatement().execute(String.format("update images set Path='%s' where is_lunch=%b and date='%s';",DBPath,false,day));
                }else{
                    //DB에 데이터 자체가 없는 경우 - 열 추가
                    con.createStatement().execute(String.format("insert images (ID, is_lunch, Path, Date) values(%d,%b,'%s','%s');",idx+1,false,DBPath,day));
                }
                con.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return;
    }



}
