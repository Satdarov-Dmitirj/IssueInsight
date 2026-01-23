package com.example.demo.controller;
import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.service.AnalisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class AnalisController {
    AnalisService analisService;


    public AnalisController(AnalisService analisService){
        this.analisService = analisService;
    }
    @GetMapping
    public List<AnalisResultRespouns> getAllAnalysisTicket(@PathVariable Long id){
        List<AnalisResultRespouns>  resultRespouns = analisService.getTicketAnalysisById(id).
                stream().
                map(AnalisResultRespouns::fromEntity).
                collect(Collectors.toList());
        return resultRespouns; // повторить
    }
}
