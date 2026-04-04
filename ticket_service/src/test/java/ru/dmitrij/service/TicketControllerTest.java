package ru.dmitrij.service;

import com.example.demo.controller.TicketController;
import com.example.demo.dto.TicketCreateRequest;
import com.example.demo.dto.TicketDto;
import com.example.demo.entity.TicketStatus;
import com.example.demo.service.EmailNotificationSendlerService;
import com.example.demo.service.TicketService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private EmailNotificationSendlerService emailNotificationSender;

    @InjectMocks
    private TicketController ticketController;

    private TicketDto ticketDto;
    private TicketCreateRequest request;

    @BeforeEach
    void setUp() {
        ticketDto = new TicketDto(
                1L, "Тест", "Описание", "user@mail.com",
                TicketStatus.OPEN, LocalDateTime.now(), null, "Категория"
        );

        request = new TicketCreateRequest();
        request.setSubject("Тест");
        request.setDescription("Описание");
        request.setEmail("user@mail.com");
    }

    @Test
    public void createTicket_ShouldReturnCreated() {
        when(ticketService.createTicket(any(TicketCreateRequest.class))).thenReturn(ticketDto);
        doNothing().when(emailNotificationSender).sendTicketCreatedNotification(anyString(), anyString(), anyString(), anyString());

        ResponseEntity<TicketDto> response = ticketController.createTicket(request);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(1L, response.getBody().getId());
        verify(emailNotificationSender).sendTicketCreatedNotification(eq("user@mail.com"), eq("1"), anyString(), anyString());
    }

    @Test
    public void getTicket_ShouldReturnDto() {
        when(ticketService.getTicketById(1L)).thenReturn(Optional.of(ticketDto));

        TicketDto result = ticketController.getTicket(1L);

        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    public void changeStatus_ShouldNotify() {
        when(ticketService.changeTicketStatus(1L, TicketStatus.IN_PROGRESS)).thenReturn(ticketDto);
        doNothing().when(emailNotificationSender).sendStatusChangeNotification(anyString(), anyString(), anyString());

        TicketDto result = ticketController.changeStatus(1L, TicketStatus.IN_PROGRESS);

        Assertions.assertNotNull(result);
        verify(emailNotificationSender).sendStatusChangeNotification(anyString(), anyString(), anyString());
    }

    @Test
    public void closeTicket_ShouldNotify() {
        when(ticketService.closeTicket(1L, "Решение")).thenReturn(ticketDto);
        doNothing().when(emailNotificationSender).sendTicketClosedNotification(anyString(), anyString(), anyString());

        TicketDto result = ticketController.closeTicket(1L, "Решение");

        Assertions.assertNotNull(result);
        verify(emailNotificationSender).sendTicketClosedNotification(anyString(), anyString(), anyString());
    }
}