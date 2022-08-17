package com.primitive.todayMenuDBApi.controller;

import com.primitive.todayMenuDBApi.controller.DBcontroller.DB_Connection_Data;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.sql.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


@RestController
@RequestMapping("")
public class TestController {
    //to configure DB setting replace string value.
    private DB_Connection_Data key = DB_Connection_Data.getInstance();

    @GetMapping
    public String testController(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
            con.createStatement().execute("use "+key.getDBname());

            con.createStatement().execute("create table if not exists comments (ID int, Content varchar(600), Date varchar(20));");
            con.createStatement().execute("create table if not exists likes (ID int, amount int, Date varchar(20));");
            con.createStatement().execute("create table if not exists dislikes (ID int, amount int, Date varchar(20));");
            con.createStatement().execute("create table if not exists images (ID int, is_lunch boolean, Path varchar(100), Date varchar(20));");
            con.close();
        }catch (Exception e ){
            e.printStackTrace();
        }
        return "working";
    }
}
