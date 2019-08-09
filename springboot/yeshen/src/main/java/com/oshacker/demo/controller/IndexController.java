package com.oshacker.demo.controller;

import com.oshacker.demo.model.User;
import com.oshacker.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
public class IndexController {

    @Autowired
    private TestService testService;

    //1、如果是/，在浏览器访问时可以省略
    //2、这里的path=可以省略
    @RequestMapping(path={"/","/index"},method={RequestMethod.GET,RequestMethod.POST})
    //加@ResponseBody表示返回的是字符串，不加的话表示返回的是一个模板页面，
    //然后会去templates目录下找该文件
    @ResponseBody
    public String index(HttpSession session) {
        return "Q&A community"+session.getAttribute("msg")+testService.getMessage(1);
    }

    //路径参数与请求参数
    @RequestMapping(path={"/profile/{group}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("group") String group,//admin或user
                          @PathVariable("userId") int userId,
                          //@RequestParam不仅支持get请求，POST请求也OK
                          @RequestParam(value = "type",required = false) String type, //可以不传该参数
                          @RequestParam(value = "page",defaultValue ="1") int page) { //不传该参数默认为1
        return String.format("Profile Page of {%s},{%d},{%s},{%d}",group,userId,type,page);
    }

    @RequestMapping("/ftl")
    public String template(Model model) {
        model.addAttribute("name","zhangsan");

        model.addAttribute("boolVal",true);

        Date date=new Date();
        model.addAttribute("date",date);

        model.addAttribute("test","<h1>html原始内容</h1>");

        List<String> colors= Arrays.asList(new String[]{"red","green","blue"});
        model.addAttribute("colors",colors);

        Map<String,String> map=new HashMap<>();
        for (int i=0;i<4;i++) {
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("map",map);

        model.addAttribute("user",new User("zhangsan"));

        return "index";
    }

    @RequestMapping(path={"/request"},method={RequestMethod.GET})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session,
                          @CookieValue("JSESSIONID") String sessionId) {
        StringBuilder sb=new StringBuilder();
        sb.append(request.getMethod()+"<br>");
        sb.append(request.getQueryString()+"<br>");
        sb.append(request.getPathInfo()+"<br>");
        sb.append(request.getRequestURI()+"<br>");

        Enumeration<String> headerNames=request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name=headerNames.nextElement();
            sb.append(name+": "+request.getHeader(name)+"<br>");
        }

        Cookie[] cookies=request.getCookies();
        if (cookies!=null) {
            for (Cookie cookie:cookies) {
                sb.append("Cookie: "+cookie.getName()+"="+cookie.getValue()+"<br>");
            }
        }
        sb.append("CookieValue="+sessionId+"<br>");
        return sb.toString();
    }

    @RequestMapping(path={"/response"},method={RequestMethod.GET})
    @ResponseBody
    public void response(HttpServletResponse response,HttpSession session) {
        response.addHeader("nowcoderId","hello");
        response.addCookie(new Cookie("username","nowcoder"));
        try {
            response.getWriter().write("I love you");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //response.getOutputStream().write(byte b[], int off, int len);//写出图片到页面

        try {
            session.setAttribute("msg","jump from response");
            response.sendRedirect("/");//状态码是302
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(path={"/redirect1"},method={RequestMethod.GET})
    public String redirect1(HttpSession session) {
        session.setAttribute("msg","jump from redirect1");
        return "redirect:/";//状态码是302
    }

    @RequestMapping(path={"/redirect2/{status}"},method={RequestMethod.GET})
    public RedirectView redirect2(HttpSession session,
                                  @PathVariable("status") int status) {
        session.setAttribute("msg","jump from redirect2");
        RedirectView rw=new RedirectView("/",true);
        if (status==301) {//此时的状态码是301
            rw.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return rw;
    }

    @RequestMapping(path={"/admin"},method={RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("参数不对");
    }

    @ExceptionHandler() //出现异常，在这里进行异常处理
    @ResponseBody
    public String error(Exception e) {
        return "exception: "+e.getMessage();
    }


}
