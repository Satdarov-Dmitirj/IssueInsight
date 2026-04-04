package ru.dmitrij.service;

import com.example.demo.dto.TicketCreateRequest;
import com.example.demo.dto.TicketDto;
import com.example.demo.entity.*;
import com.example.demo.repository.AnalisRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TicketRepository;
import com.example.demo.service.AnalisService;
import com.example.demo.service.TicketService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AnalisRepository analisRepository;

    @Mock
    private AnalisService analisService;

    @InjectMocks
    private TicketService ticketService;

    private TicketCreateRequest request;
    private Ticket ticket;
    private Category category;
    private AnalysisResult analysisResult;

    @BeforeEach
    void setUp() {
        request = new TicketCreateRequest();
        request.setSubject("Проблема с входом");
        request.setDescription("Не могу войти");
        request.setEmail("user@mail.com");

        category = new Category();
        category.setId(1L);
        category.setName("Авторизация");

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setSubject("Проблема с входом");
        ticket.setDescription("Не могу войти");
        ticket.setCustomerEmail("user@mail.com");
        ticket.setTicketStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());

        analysisResult = new AnalysisResult();
        analysisResult.setDetectedCause("Авторизация");
        analysisResult.setAnaliseScore(90.0);
    }

    @Test
    public void createTicket_ShouldReturnDto() {
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> {
            Ticket t = inv.getArgument(0);
            if (t.getId() == null) t.setId(1L);
            return t;
        });
        when(analisService.analysisTicket(any(Ticket.class))).thenReturn(analysisResult);
        when(categoryRepository.findByName("Авторизация")).thenReturn(Optional.of(category));

        TicketDto result = ticketService.createTicket(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("Авторизация", result.getCategoryName());
    }

    @Test
    public void getTicketById_ShouldReturnTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Optional<TicketDto> result = ticketService.getTicketById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1L, result.get().getId());
    }

    @Test
    public void getAllTickets_ShouldReturnList() {
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket));

        List<TicketDto> result = ticketService.getAllTickets();

        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void changeTicketStatus_ShouldUpdate() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        TicketDto result = ticketService.changeTicketStatus(1L, TicketStatus.IN_PROGRESS);

        Assertions.assertEquals(TicketStatus.IN_PROGRESS, result.getStatus());
    }

    @Test
    public void closeTicket_ShouldClose() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        TicketDto result = ticketService.closeTicket(1L, "Решено");

        Assertions.assertEquals(TicketStatus.CLOSED, result.getStatus());
        Assertions.assertTrue(result.getDescription().contains("Решение: Решено"));
    }

    @Test
    public void getTicketsByStatus_ShouldFilter() {
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket));

        List<TicketDto> result = ticketService.getTicketsByStatus(TicketStatus.OPEN);

        Assertions.assertEquals(1, result.size());
    }
}