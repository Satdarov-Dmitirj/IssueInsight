package com.example.demo.htmlController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/analysisProfile") //http://localhost:8080/analysisProfile
@Controller()
public class AnalysisControllerPage {

    @GetMapping
    public String getPageAnalysis(){
        return "test.html";
    }
}
