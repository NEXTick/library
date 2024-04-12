package library.controllers;

import library.models.Book;
import library.models.Person;
import jakarta.validation.Valid;
import library.services.BooksService;
import library.services.PeopleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;

    private final PeopleService peopleService;
    private final Logger logger;


    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;

        logger = Logger.getLogger(BooksController.class.getName());
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "sort_by_title", required = false) Boolean isSortByTitle,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage) {
        model.addAttribute("books", booksService.findAll(page, booksPerPage, isSortByTitle));
        logger.log(Level.INFO, "index");

        return "books/index";
    }

    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam(value = "search") String search) {
        model.addAttribute("foundedBooks", booksService.findBySearch(search));
        return "books/search";
    }


    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {

        Book book = booksService.findOne(id);

        model.addAttribute("book", book);

        Optional<Person> bookOwner = Optional.ofNullable(book.getOwner());

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", peopleService.findAll());

        logger.log(Level.INFO, "show by id");

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        logger.log(Level.INFO, "newBook " + book);
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {

        logger.log(Level.INFO, "create " + book);

        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Book book = booksService.findOne(id);
        logger.log(Level.INFO, "edit " + book);

        model.addAttribute("book", book);
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {
        logger.log(Level.INFO, "update " + book);

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        logger.log(Level.INFO, "delete " + booksService.findOne(id));
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        booksService.assign(id, person);
        return "redirect:/books/" + id;
    }

}
