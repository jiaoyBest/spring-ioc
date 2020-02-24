package com.matrix.controller;

import com.matrix.annotation.MAitowrited;
import com.matrix.annotation.MController;
import com.matrix.annotation.MRequestMapping;
import com.matrix.service.DemoServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
  * 这是一个类说明
  * @author (yu.jiao)
  * @date 2020/02/08
  */
@MController
@MRequestMapping("/test")
public class TestController {

    @MAitowrited
    private DemoServiceImpl demoService ;

    @MRequestMapping("/test")
    public void get(HttpServletRequest request , HttpServletResponse response){
        String key = request.getParameter("key");
        String result = demoService.testString(key);
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
