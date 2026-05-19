package com.example.demo.entity;

public enum TicketStatus {
    OPEN("Открыто"), IN_PROGRESS("В работе"), RESOLVED("Решено"), CLOSED("Закрыт");

    String userName;

    TicketStatus(String status){
        this.userName = status;
    }

    public String toString(){
        return "name" + userName;
    }
}
