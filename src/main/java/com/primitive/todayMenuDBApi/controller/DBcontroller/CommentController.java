package com.primitive.todayMenuDBApi.controller.DBcontroller;

import com.primitive.todayMenuDBApi.controller.DB_Connection_Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("")
public class CommentController {
    private DB_Connection_Data key = DB_Connection_Data.getInstance();

    @GetMapping("comments/{day}")
    public String getcomments(@PathVariable String day){
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        // String day = sdf.format(new Date(now));

        JSONObject jsonObject = new JSONObject();
        JSONArray req_array = new JSONArray();
        JSONArray date_array= new JSONArray();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
            con.createStatement().execute("use "+key.getDBname());


            Statement stmt1 = con.createStatement();
            ResultSet rs =stmt1.executeQuery("SELECT * from comments where Date = '"+day+"';");
            //System.out.println("쿼리문 실행 완료");

            for (int i = 0 ; rs.next(); i++){
                req_array.add(rs.getString("Content"));
                date_array.add(rs.getString("Date_written"));

            }
            con.close();

            jsonObject.put("content",req_array);
            jsonObject.put("date_written",date_array);

            return jsonObject.toJSONString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toJSONString();
    }


    @PostMapping("add/comments/{date}")
    public String add_data(@RequestBody String content, @PathVariable String date){
        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String written_time = sdf.format(new Date(now));
        try  {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(key.getURL(), key.getDBuser(), key.getDBpw());
            con.createStatement().execute("use "+key.getDBname());

            Statement stmt = con.createStatement();
            ResultSet rs =stmt.executeQuery("SELECT ID FROM comments ORDER BY id DESC LIMIT 1;");
            rs.next();
            int a;
            try{
                a= rs.getInt("ID");}catch (Exception e){a=0;}

            System.out.println(a+"to check arrived value");
            int last_ID=a;

            String query = String.format("insert into comments(ID, Content, Date, Date_written) values(%d,'%s','%s','%s');",(last_ID+1),content,date,written_time);
            stmt.executeUpdate(query);
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //alter table [테이블명] change [컬럼명] [변경할컬럼명] varchar(12);
        //출처: https://mcpaint.tistory.com/194 [MC빼인트와 함께:티스토리]


        return String.format("{ content : %s }",content);
    }
}
