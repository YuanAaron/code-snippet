package com.oshacker.demo.controller;

import com.oshacker.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SettingController {

    @Autowired
    private TestService testService;

    @RequestMapping(path={"/setting"},method={RequestMethod.GET})
    @ResponseBody
    public String setting() {
        return "setting OK! "+testService.getMessage(2);
    }
}
