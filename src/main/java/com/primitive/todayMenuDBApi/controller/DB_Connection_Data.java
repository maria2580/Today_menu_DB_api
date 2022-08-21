package com.primitive.todayMenuDBApi.controller;

public class DB_Connection_Data {
    private String DBname= "primitive_today_menu_db"; //replace your own DB name
    private String DBuser="marin_admin";
    private String DBpw="!2022primitive!";
    private String URL ="jdbc:mysql://primitive-today-menu-db.car8huvgqumw.ap-northeast-2.rds.amazonaws.com:3306"+"?useSSL=False";
    private static DB_Connection_Data instance;

    private DB_Connection_Data(){
        super();
    }

    public static synchronized DB_Connection_Data getInstance() {
        if (instance == null) { instance = new DB_Connection_Data();}
        return instance;
    }

    public String getDBname() {
        return DBname;
    }

    public String getDBuser() {
        return DBuser;
    }

    public String getDBpw() {
        return DBpw;
    }

    public String getURL() {
        return URL;
    }
}
