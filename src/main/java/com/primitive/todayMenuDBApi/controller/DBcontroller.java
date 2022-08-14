package com.primitive.todayMenuDBApi.controller;

import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.security.PublicKey;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.annotation.MultipartConfig;
import java.util.List;


@RestController
@RequestMapping("")
public class DBcontroller {
    //to configure DB setting replace string value.
    String DBname= "primitive_today_menu_db"; //replace your own DB name
    String DBuser="marin_admin";
    String DBpw="!2022primitive!";
    String URL ="jdbc:mysql://primitive-today-menu-db.car8huvgqumw.ap-northeast-2.rds.amazonaws.com:3306"+"?useSSL=False";

    @GetMapping
    public String testController(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, DBuser, DBpw);
            con.createStatement().execute("use "+DBname);

            con.createStatement().execute("create table if not exists comments (ID int, Content varchar(600), Date varchar(20));");
            con.createStatement().execute("create table if not exists likes (ID int, amount int, Date varchar(20));");
            con.createStatement().execute("create table if not exists dislikes (ID int, amount int, Date varchar(20));");

        }catch (Exception e ){
            e.printStackTrace();
        }

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
            Connection con = DriverManager.getConnection(URL, DBuser, DBpw);
            con.createStatement().execute("use "+DBname);


            Statement stmt1 = con.createStatement();
            ResultSet rs =stmt1.executeQuery("SELECT * from comments where Date = '"+day+"';");
            //System.out.println("쿼리문 실행 완료");

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
            Connection con = DriverManager.getConnection(URL, DBuser, DBpw);
            con.createStatement().execute("use "+DBname);

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
    @GetMapping("likes/{day}")
    public int get_like(@PathVariable String day){
        int amount=0;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, DBuser, DBpw);
            con.createStatement().execute("use "+DBname);
            ResultSet rs =con.createStatement().executeQuery(String.format("SELECT amount FROM likes where date='%s';",day));
            if (rs.next()){
                amount=rs.getInt(0);
            }else{
                amount=0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return amount;
    }
    @GetMapping("dislikes/{day}")
    public int get_dislike(@PathVariable String day){
        int amount=0;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, DBuser, DBpw);
            con.createStatement().execute("use "+DBname);
            ResultSet rs =con.createStatement().executeQuery(String.format("SELECT amount FROM dislikes where date='%s';",day));
            if (rs.next()){
                amount=rs.getInt(0);
            }else{
                amount=0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return amount;
    }
    @PostMapping("add/likes/{day}/plus")
    public void likes_plus(@PathVariable String day){
        try  {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, DBuser, DBpw);
            con.createStatement().execute("use "+DBname);

            Statement stmt = con.createStatement();
            ResultSet rs =stmt.executeQuery("SELECT ID FROM likes ORDER BY id DESC LIMIT 1;");

            rs.next();
            int a;
            try{
                a= rs.getInt("ID");
            }catch (Exception e){
                a=0;
            }



            System.out.println(a+"to check arrived value");
            int last_ID=a;

            String query = String.format("SELECT * FROM likes where date='%s';",day);
            ResultSet rs2 =stmt.executeQuery(query);
            int amount;
            if(rs2.next()){
                //DB에 값이 들어있는 경우
                try {
                    amount = rs2.getInt("amount");
                }catch (Exception e){
                    amount=0;
                }

                stmt.execute(String.format("update likes set amount=%d where date='%s';",amount+1,day));
            }else{
                //DB에 데이터 자체가 없는 경우
                amount=0;
                stmt.execute(String.format("insert likes (ID, amount, Date) values(%d,%d,'%s');",a+1,amount+1,day));
            }





        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @PostMapping("add/likes/{day}/minus")
    public void likes_minus(@PathVariable String day){
        try  {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, DBuser, DBpw);
            con.createStatement().execute("use "+DBname);


            Statement stmt = con.createStatement();
            ResultSet rs =stmt.executeQuery("SELECT ID FROM likes ORDER BY id DESC LIMIT 1;");

            rs.next();
            int a;
            try{
                a= rs.getInt("ID");}catch (Exception e){a=0;}
            System.out.println(a+"to check arrived value");
            int last_ID=a;

            String query = String.format("SELECT * FROM likes where date='%s';",day);
            ResultSet rs2 =stmt.executeQuery(query);
            int amount;


            if(rs2.next()){
                //DB에 값이 들어있는 경우
                try {
                    amount = rs2.getInt("amount");
                }catch (Exception e){
                    amount=0;
                }

                stmt.execute(String.format("update likes set amount=%d where date='%s';",amount-1,day));
            }else{
                //DB에 데이터 자체가 없는 경우
                amount=0;
                stmt.execute(String.format("insert likes (ID, amount, Date) values(%d,%d,'%s');",a+1,amount-1,day));

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("add/dislikes/{day}/plus")
    public void dislikes_plus(@PathVariable String day){
        try  {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, DBuser, DBpw);
            con.createStatement().execute("use "+DBname);

            Statement stmt = con.createStatement();
            ResultSet rs =stmt.executeQuery("SELECT ID FROM dislikes ORDER BY id DESC LIMIT 1;");

            rs.next();
            int a;
            try{
                a= rs.getInt("ID");}catch (Exception e){a=0;}
            System.out.println(a+"to check arrived value");
            int last_ID=a;

            String query = String.format("SELECT * FROM dislikes where date='%s';",day);
            ResultSet rs2 =stmt.executeQuery(query);
            int amount;
            if(rs2.next()){
                //DB에 값이 들어있는 경우
                try {
                    amount = rs2.getInt("amount");
                }catch (Exception e){
                    amount=0;
                }

                stmt.execute(String.format("update dislikes set amount=%d where date='%s';",amount+1,day));
            }else{
                //DB에 데이터 자체가 없는 경우
                amount=0;
                stmt.execute(String.format("insert dislikes (ID, amount, Date) values(%d,%d,'%s');",a+1,amount+1,day));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @PostMapping("add/dislikes/{day}/minus")
    public void dislikes_minus(@PathVariable String day){
        try  {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, DBuser, DBpw);
            con.createStatement().execute("use "+DBname);


            Statement stmt = con.createStatement();
            ResultSet rs =stmt.executeQuery("SELECT ID FROM dislikes ORDER BY id DESC LIMIT 1;");

            rs.next();
            int a;
            try{
                a= rs.getInt("ID");}catch (Exception e){a=0;}
            System.out.println(a+"to check arrived value");
            int last_ID=a;

            String query = String.format("SELECT * FROM dislikes where date='%s';",day);
            ResultSet rs2 =stmt.executeQuery(query);
            int amount;
            if(rs2.next()){
                //DB에 값이 들어있는 경우
                try {
                    amount = rs2.getInt("amount");
                }catch (Exception e){
                    amount=0;
                }

                stmt.execute(String.format("update dislikes set amount=%d where date='%s';",amount-1,day));
            }else{
                //DB에 데이터 자체가 없는 경우
                amount=0;
                stmt.execute(String.format("insert dislikes (ID, amount, Date) values(%d,%d,'%s');",a+1,amount-1,day));
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @GetMapping("images/lunch/{day}")
    public Image get_lunch_image(@PathVariable String day){
        Image img = null;
        return img;
    }
    @GetMapping("images/dinnner/{day}")
    public Image get_dinner_image(@PathVariable String day){

        Image img = null;
        return img;
    }


    @PostMapping("add/images/lunch/{day}")
    public void set_lunch_image(@PathVariable String day){

        return;
    }
    @PostMapping("add/images/dinner/{day}")
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
            Connection con = DriverManager.getConnection("jdbc:mysql://primitive-today-menu-db.car8huvgqumw.ap-northeast-2.rds.amazonaws.com:3306"+"?useSSL=False", DBuser, DBpw);
            con.createStatement().execute("use "+DBname);

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
