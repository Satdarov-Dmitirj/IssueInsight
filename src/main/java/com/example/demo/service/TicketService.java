package com.example.demo.service;

import com.example.demo.repository.AnalisRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;

@AllArgsConstructor
public class TicketService {
    private TicketRepository ticketRepository;
    private CategoryRepository categoryRepository; //добавление, удаление, управление пользоватеелм
    private AnalisRepository analisRepository;

    public TicketService createTicket(){
    }


}
