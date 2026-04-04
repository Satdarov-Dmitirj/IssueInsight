package ru.natification.mainup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationDto {
    @JsonProperty("to")
    private String to;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("body")
    private String body;

    @JsonProperty("type")
    private String type; // "TICKET_CREATED", "STATUS_CHANGED", "TICKET_CLOSED"

    @JsonProperty("ticketId")
    private String ticketId;

    @JsonProperty("ticketStatus")
    private String ticketStatus;

    @JsonProperty("resolution")
    private String resolution;
}