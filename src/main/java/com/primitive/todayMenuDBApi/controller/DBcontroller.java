package com.primitive.todayMenuDBApi.controller;

import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.List;


@RestController
@RequestMapping("")
public class DBcontroller {
    //to configure DB setting replace string value.
    String DBname= "primitive_comments_db"; //replace your own DB name
    String DBuser="root";
    String DBpw="!2022primitive!";





    @GetMapping
    public String testController(){
        return "working";
    }

    @GetMapping("comments/{day}")
    public String getcomments(@PathVariable String day){
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        // String day = sdf.format(new Date(now));

        JSONObject jsonObject = new JSONObject();
        JSONArray req_array = new JSONArray();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DBname+"?useSSL=False", DBuser, DBpw);
            System.out.println(con.getCatalog()+"getCatalog()");


            Statement stmt0 = con.createStatement();
            stmt0.execute("create table if not exists comments (ID int, Content varchar(600), Date varchar(20));");

            Statement stmt = con.createStatement();
            ResultSet rs =stmt.executeQuery("SELECT * from comments where Date = '"+day+"';");


            System.out.println("쿼리문 실행 완료");

            for (int i = 0 ; rs.next(); i++){
                req_array.add(rs.getString("Content"));

            }
            jsonObject.put("content",req_array);

            return jsonObject.toJSONString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toJSONString();
    }


    @PostMapping("add/comments/{date}")
    public String add_data(@RequestBody String content, @PathVariable String date){
        try  {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DBname+"?useSSL=False", DBuser, DBpw);

            System.out.println(con.getCatalog()+"getCatalog()");

            Statement stmt = con.createStatement();
            ResultSet rs =stmt.executeQuery("SELECT ID FROM comments ORDER BY id DESC LIMIT 1;");

            rs.next();
            int a;
            try{
                a= rs.getInt("ID");}catch (Exception e){a=0;}
            System.out.println(a+"to check arrived value");
            int last_ID=a;

            String query = String.format("insert into comments(ID, Content, Date) values(%d,'%s','%s');",(last_ID+1),content,date);
            stmt.executeUpdate(query);

        } catch (Exception e) {
           e.printStackTrace();
        }

        //alter table [테이블명] change [컬럼명] [변경할컬럼명] varchar(12);
        //출처: https://mcpaint.tistory.com/194 [MC빼인트와 함께:티스토리]


        return String.format("{ content : %s }",content);
    }

    @GetMapping("lunch_image/{day}")
    public Image get_lunch_image(@PathVariable String day){
        Image img = null;
        return img;
    }

    @GetMapping("dinner_image/{day}")
    public Image get_dinner_image(@PathVariable String day){
        Image img = null;
        return img;
    }


    @PostMapping("add/lunch_image/{day}")
    public void set_lunch_image(@PathVariable String day){


        return;
    }

    @PostMapping("add/dinner_image/{day}")
    public void set_dinner_image(@PathVariable String day){
        return;
    }
    
    
    
    
    @GetMapping("Suggestion/{day}")
    public String getrequest(@PathVariable String day){
        //기능 미구현
        return day;
    }
    
    @PostMapping("add/Suggestion/{date}")
    public String add_(@RequestBody String content, @PathVariable String date){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DBname+"?useSSL=False",DBuser,DBpw);

            Statement stmt0 = con.createStatement();
           stmt0.execute("create table if not exists claims (ID int, suggestion varchar(100), date varchar(20));");


            Statement stmt = con.createStatement();
            ResultSet rs =stmt.executeQuery("SELECT ID FROM claims ORDER BY id DESC LIMIT 1;");
            System.out.println("쿼리 실행");
            rs.next();
            int a;
            try{
                a=rs.getInt("ID");
                System.out.println("a= rs.getint()");
            }catch (Exception e){
                a=0;
                System.out.println("a=0처리 ");
            }
            String query = String.format("insert into claims(ID, suggestions, date) values(%d,'%s','%s');",a+1,content,date);
            stmt.executeUpdate(query);
            System.out.println("1");

        }catch(Exception e) {}
        return String.format("{content : %s}", content);
    }

}
