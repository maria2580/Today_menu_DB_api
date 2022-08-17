package com.primitive.todayMenuDBApi.controller.DBcontroller;

import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
@RequestMapping("")
public class Like_Controller {
    private DB_Connection_Data key = DB_Connection_Data.getInstance();

    @GetMapping("likes/{day}")
    public int get_like(@PathVariable String day){
        int amount=0;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
            con.createStatement().execute("use "+key.getDBname());
            ResultSet rs =con.createStatement().executeQuery(String.format("SELECT amount FROM likes where date='%s';",day));
            if (rs.next()){
                amount=rs.getInt("amount");
            }else{
                amount=0;
            }
            con.close();
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
            Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
            con.createStatement().execute("use "+key.getDBname());
            ResultSet rs =con.createStatement().executeQuery(String.format("SELECT amount FROM dislikes where date='%s';",day));
            if (rs.next()){
                amount=rs.getInt(0);
            }else{
                amount=0;
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return amount;
    }
    @PostMapping("add/likes/{day}/plus")
    public void likes_plus(@PathVariable String day){
        try  {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
            con.createStatement().execute("use "+key.getDBname());

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
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @PostMapping("add/likes/{day}/minus")
    public void likes_minus(@PathVariable String day){
        try  {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
            con.createStatement().execute("use "+key.getDBname());


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
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("add/dislikes/{day}/plus")
    public void dislikes_plus(@PathVariable String day){
        try  {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
            con.createStatement().execute("use "+key.getDBname());

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

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @PostMapping("add/dislikes/{day}/minus")
    public void dislikes_minus(@PathVariable String day){
        try  {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
            con.createStatement().execute("use "+key.getDBname());


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

            con.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
