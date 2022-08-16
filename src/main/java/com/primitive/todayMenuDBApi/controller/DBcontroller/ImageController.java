package com.primitive.todayMenuDBApi.controller.DBcontroller;

import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping("")
public class ImageController {
    private DB_Connection_Data key = DB_Connection_Data.getInstance();

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
}
