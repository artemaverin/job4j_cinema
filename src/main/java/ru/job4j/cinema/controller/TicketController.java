package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.TicketService;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/result")
    public String create(@ModelAttribute Ticket ticket, Model model) {
        try {
            ticketService.save(ticket);
            return "tickets/success";
        } catch (Exception e) {
            model.addAttribute("message", "Билет на эти места уже забронирован");
            return "tickets/reject";
        }
    }
}
