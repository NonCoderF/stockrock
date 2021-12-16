package com.stockrock.analysis;

import com.stockrock.analysis.model.Request;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppController {

    @RequestMapping(value = "/terminal")
    public String terminal(){
        return "terminal";
    }


    @RequestMapping(value = "/chart")
    public String chart(){
        return "chart";
    }

    @RequestMapping(value = "/command", method = RequestMethod.POST)
    public @ResponseBody Request command(@RequestBody Request request){
        return request;
    }

}
