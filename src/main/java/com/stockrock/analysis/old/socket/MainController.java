package com.stockrock.analysis.old.socket;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

//    @Autowired
//    private UserService userService;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("surname", "Ahmed");
        return "hello";
    }

    @RequestMapping(value = "/sock")
    public String sock() {
        return "sock";
    }

    @RequestMapping(value = "/async")
    public String asyncTask(){

        System.out.println("Currently Executing thread name - " + Thread.currentThread().getName());

//        Future<User> userFuture = userService.createUserWithThreadPoolExecutor();


        return "hello";
    }


}
