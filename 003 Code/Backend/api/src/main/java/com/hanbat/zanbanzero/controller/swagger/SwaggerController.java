package com.hanbat.zanbanzero.controller.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {
    @GetMapping("/apis")
    public String swagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
