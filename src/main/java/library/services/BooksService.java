package library.services;

import library.models.Book;
import library.models.Person;
import library.repositories.BooksRepository;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(Integer page, Integer booksPerPage, Boolean isSortByTitle) {
        if (booksPerPage != null && isSortByTitle != null) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("title"))).getContent();
        } else if (isSortByTitle != null) {
            return booksRepository.findAll(Sort.by("title"));
        } else if (booksPerPage != null) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
        return booksRepository.findAll();
    }

    public Book findOne(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        updatedBook.setOwner(booksRepository.findById(id).get().getOwner());
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }


    public List<Book> findByOwner(Person person) {

        List<Book> books = booksRepository.findByOwner(person);

        books.forEach(book ->
        {
            System.out.println(book);
            if (new Date().getTime() - book.getWasTaken().getTime() > 30_000) {
                book.setExpired(true);
            }

        });
        for (Book book : books) {
            System.out.println(book);
        }
        return books;
    }

    @Transactional
    public void release(int id) {
        booksRepository.findById(id).ifPresent(book ->
        {
            book.setOwner(null);
            book.setWasTaken(null);
        });
    }

    @Transactional
    public void assign(int id, Person person) {
        booksRepository.findById(id).ifPresent(book ->
        {
            book.setOwner(person);
            book.setWasTaken(new Date());
        });
    }

    public List<Book> findBySearch(String search) {
        return booksRepository.findByTitleContainsIgnoreCase(search);
    }
}
