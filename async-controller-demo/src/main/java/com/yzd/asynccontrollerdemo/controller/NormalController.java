package com.yzd.asynccontrollerdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/***
 * 同步请求处理
 */
@RestController
@RequestMapping("normal")
public class NormalController {
    @RequestMapping("test")
    public String test(){
        return "normal/test:hello-world";
    }
}
