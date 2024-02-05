package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.SessionService;

import java.util.Collection;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("class_active", "sessions");
        model.addAttribute("sessions", sessionService.findAll());
        return "sessions/list";
    }

    @GetMapping("/create/{id}")
    public String getById(Model model, @PathVariable int id) {
        var sessionDtoOptional = sessionService.findById(id);
        if (sessionDtoOptional.isEmpty()) {
            model.addAttribute("error", "Сеанс не найден");
            return "errors/404";
        }
        var row = sessionDtoOptional.get().getRowCount();
        Collection<Integer> rows = IntStream.rangeClosed(1, row).boxed().toList();
        var place = sessionDtoOptional.get().getPlaceCount();
        Collection<Integer> places = IntStream.rangeClosed(1, place).boxed().toList();
        model.addAttribute("sesion", sessionService.findById(id).get());
        model.addAttribute("rows", rows);
        model.addAttribute("places", places);
        return "tickets/buy";
    }

}
