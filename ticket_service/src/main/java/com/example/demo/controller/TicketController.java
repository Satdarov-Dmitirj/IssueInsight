
package com.example.demo.controller;

import com.example.demo.dto.TicketCreateRequest;
import com.example.demo.dto.TicketDto;
import com.example.demo.entity.TicketStatus;
import com.example.demo.repository.TicketServiceInterface;
import com.example.demo.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket Controller", description = "Управление тикетами")
public class TicketController {
    private final TicketServiceInterface ticketService;
    private final MailService mailService;

    @Operation(summary = "Создать новый тикет")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Тикет создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@RequestBody TicketCreateRequest request) {
        TicketDto ticket = ticketService.createTicket(request);

        // Отправляем уведомление пользователю
        mailService.sendEmail(
                request.getEmail(),
                "Тикет #" + ticket.getId() + " создан",
                "<h2>Ваш тикет создан</h2>" +
                        "<p>Тикет #" + ticket.getId() + " успешно создан.</p>" +
                        "<p>Тема: " + ticket.getSubject() + "</p>" +
                        "<p>Статус: " + ticket.getStatus() + "</p>"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @Operation(summary = "Получить тикет по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тикет найден"),
            @ApiResponse(responseCode = "404", description = "Тикет не найден")
    })
    @GetMapping("/by-id")
    public TicketDto getTicket(@Parameter(description = "ID тикета", example = "1") @RequestParam Long id) {
        return ticketService.getTicketById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    @Operation(summary = "Получить все тикеты")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список тикетов получен")
    })
    @GetMapping
    public List<TicketDto> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @Operation(summary = "Изменить статус тикета")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус изменён"),
            @ApiResponse(responseCode = "404", description = "Тикет не найден")
    })
    @PutMapping("/change-status")
    public TicketDto changeStatus(@RequestParam Long id, @RequestParam TicketStatus status) {
        TicketDto ticket = ticketService.changeTicketStatus(id, status);

        // Отправляем уведомление пользователю
        mailService.sendTicketNotification(
                ticket.getCustomerEmail(),
                String.valueOf(ticket.getId()),
                status.toString(),
                null
        );

        return ticket;
    }

    @Operation(summary = "Закрыть тикет")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тикет закрыт"),
            @ApiResponse(responseCode = "404", description = "Тикет не найден")
    })
    @PutMapping("/close")
    public TicketDto closeTicket(@RequestParam Long id, @RequestParam String resolution) {
        TicketDto ticket = ticketService.closeTicket(id, resolution);

        // Отправляем уведомление пользователю
        mailService.sendTicketNotification(
                ticket.getCustomerEmail(),
                String.valueOf(ticket.getId()),
                "CLOSED",
                resolution
        );

        return ticket;
    }

    @Operation(summary = "Получить тикеты по статусу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список получен")
    })
    @GetMapping("/by-status")
    public List<TicketDto> getByStatus(@RequestParam TicketStatus status) {
        return ticketService.getTicketsByStatus(status);
    }

    @Operation(summary = "Получить тикеты по категории")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список получен")
    })
    @GetMapping("/by-category")
    public List<TicketDto> getByCategory(@RequestParam Long categoryId) {
        return ticketService.getTicketsByCategory(categoryId);
    }
}