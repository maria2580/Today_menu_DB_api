package com.primitive.todayMenuDBApi.controller.DBcontroller;


import com.primitive.todayMenuDBApi.DB_Connection_Data;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
@RequestMapping("")
public class SuggestionController {
    private DB_Connection_Data key = DB_Connection_Data.getInstance();

    @GetMapping("Suggestion/{day}")
    public String getrequest(@PathVariable String day){
        //기능 미구현
        return day;
    }

    @PostMapping("add/Suggestion/{date}")
    public String add_(@RequestBody String content, @PathVariable String date){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
            con.createStatement().execute("use "+key.getDBname());

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
            con.close();
        }catch(Exception e) {}
        return String.format("{content : %s}", content);
    }
}
