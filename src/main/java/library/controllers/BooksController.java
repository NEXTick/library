package library.controllers;

import library.dao.BookDAO;
import library.dao.PersonDAO;
import library.models.Book;
import library.models.Person;
import jakarta.validation.Valid;
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

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    private final Logger logger;


    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        logger = Logger.getLogger(BooksController.class.getName());
    }

    @GetMapping()
    public String index(Model model) {

        model.addAttribute("books", bookDAO.index());
        logger.log(Level.INFO, "index");

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {

        model.addAttribute("book", bookDAO.show(id).orElse(null));

        Optional<Person> bookOwner = bookDAO.getBookOwner(id);

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", personDAO.index());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        logger.log(Level.INFO, "newBook");
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {

        logger.log(Level.INFO, "create " + book);

        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        logger.log(Level.INFO, "edit " + bookDAO.show(id));

        model.addAttribute("book", bookDAO.show(id).orElse(null));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {
        logger.log(Level.INFO, "update " + book);

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        logger.log(Level.INFO, "delete " + bookDAO.show(id));
        bookDAO.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookDAO.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookDAO.assign(id, person);
        return "redirect:/books/" + id;
    }

}
