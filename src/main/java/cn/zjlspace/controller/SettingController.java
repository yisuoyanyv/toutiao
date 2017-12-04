package cn.zjlspace.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SettingController {
    @RequestMapping(value = {"/setting"})
    @ResponseBody
    public String setting(){
        return "Setting:OK";
    }
}
