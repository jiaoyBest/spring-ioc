package com.matrix.service;

import com.matrix.annotation.MService;

/**
  * 这是一个类说明
  * @author (yu.jiao)
  * @date 2020/02/08
  */
@MService
public class DemoServiceImpl {

    public String testString(String key){
        return "当前传入得 key 值为：" + key ;
    }
}
