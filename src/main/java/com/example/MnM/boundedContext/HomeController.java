package com.example.MnM.boundedContext;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String showMain() {
        return "main";
    }

    @GetMapping("/about")
    public String showAbout(){
        return "about";
    }
}
