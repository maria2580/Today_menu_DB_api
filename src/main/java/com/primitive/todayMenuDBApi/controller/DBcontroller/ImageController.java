package com.primitive.todayMenuDBApi.controller.DBcontroller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

@RestController
@RequestMapping("")
public class ImageController {
    private final String uploadDir = "D:\\test\\images";
    private final String u = "D:\\\\test\\\\images";
    private DB_Connection_Data key = DB_Connection_Data.getInstance();

    @GetMapping( value = "images/lunch/{day}", produces = MediaType.IMAGE_JPEG_VALUE )
    public byte[] get_lunch_image(@PathVariable String day) throws IOException {
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
            if (filePath==null){
                filePath=uploadDir+"\\no_image.png";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        in = new FileInputStream(filePath);
        return IOUtils.toByteArray(in);
    }
    @GetMapping(value = "images/dinner/{day}", produces = MediaType.IMAGE_JPEG_VALUE )
    public byte[] get_dinner_image(@PathVariable String day) throws IOException{
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
            if (filePath==null){
                filePath=uploadDir+"\\no_image.png";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        in = new FileInputStream(filePath);
        return IOUtils.toByteArray(in);
    }


    @PostMapping("add/images/lunch/{day}")
    public void set_lunch_image(@PathVariable String day, @RequestParam String itemName, @RequestParam MultipartFile file){
        if (!file.isEmpty()) {
            final String suffix =file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String filename =day+suffix;
            String fullPath = uploadDir +"\\lunch\\"+ filename;
            String DBPath= u+"\\\\lunch\\\\"+filename;


    //멀티파트로 받은 변수를 로컬 경로에 저장
            if(file != null) {
                try{
                File uploadFile = new File(fullPath);
                FileCopyUtils.copy(file.getBytes(), uploadFile);
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

    @PostMapping("add/images/dinner/{day}")
    public void set_dinner_image(@PathVariable String day, @RequestParam String itemName, @RequestParam MultipartFile file){
        if (!file.isEmpty()) {
            final String suffix =file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String filename =day+suffix;
            String fullPath = uploadDir +"\\dinner\\"+ filename;
            String DBPath= u+"\\\\dinner\\\\"+filename;


            //멀티파트로 받은 변수를 로컬 경로에 저장
            if(file != null) {
                try{
                    File uploadFile = new File(fullPath);
                    FileCopyUtils.copy(file.getBytes(), uploadFile);
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
