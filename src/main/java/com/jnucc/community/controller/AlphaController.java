package com.jnucc.community.controller;

import com.jnucc.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/alpha")
public class AlphaController {

    private final AlphaService alphaService;

    @Autowired
    public AlphaController(AlphaService alphaService) {
        this.alphaService = alphaService;
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello, world";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public String register(String name, String passwd) {
        return name + ": " + passwd;
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public String user(Model model) {
        model.addAttribute("name", "张三");
        model.addAttribute("age", "25");
        return "/alpha/user";
    }

    @RequestMapping(value = "info", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> info() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "张三");
        return map;
    }
}
