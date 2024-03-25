package library.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("library")
public class HelloController {
    @GetMapping()
    public String hello() {
        return "hello";
    }
}
