package com.example.library.controllers;


import com.example.library.models.Book;
import com.example.library.models.Person;
import com.example.library.services.BookService;
import com.example.library.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping("books")
public class BookController {
    private final BookService bookService;

    private final PeopleService peopleService;

    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookService.index());
        return "book/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.show(id));
        Optional<Person> owner = bookService.showOwner(id);

        if (owner.isPresent()) {
            model.addAttribute("owner", owner.get());
        }
        else {
            model.addAttribute("people", peopleService.index());
        }
        return "book/show";
    }

    @PatchMapping("/{id}/owner")
    public String setOwner(@PathVariable("id") int id,  @ModelAttribute("person") Person selectedPerson) {
        bookService.setOwner(id, selectedPerson.getId());
        return "redirect:/books/"+id;
    }

    @DeleteMapping("/{id}/owner")
    public String deleteOwner(@PathVariable("id") int id) {
        bookService.deleteOwner(id);
        return "redirect:/books/"+id;
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book/new";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "book/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.show(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }
}
