package com.example.library.controllers;

import com.example.library.daos.PersonDAO;
import com.example.library.models.Person;
import com.example.library.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("people")
public class PeopleController {

    private final PersonDAO dao;
    private final PersonValidator personValidator;

    public PeopleController(PersonDAO dao, PersonValidator personValidator) {
        this.dao = dao;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", dao.index());
        return "index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", dao.show(id));
        return "show";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "new";
        }
        dao.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", dao.show(id));
        return "edit";
    }

    @PatchMapping("/{id}")
    public String update(Model model, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        dao.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        dao.delete(id);
        return "redirect:/people";
    }
}
