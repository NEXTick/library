package com.example.library.daos;

import com.example.library.models.Book;
import com.example.library.models.Person;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book show(int id) {
        return jdbcTemplate
                .query("SELECT * FROM Book WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public void save(Book book) {

        jdbcTemplate.update("INSERT INTO Book (title, author, year) VALUES ( ?, ?, ? )"
                , book.getTitle(), book.getAuthor(), book.getYear());
    }

    public void setOwner(int bookId, long personId) {
        jdbcTemplate.update("INSERT INTO Person_Book VALUES (?,?)"
                , personId, bookId);
    }

    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE Book SET title=?, author=?, year=? WHERE id=?",
                updatedBook.getTitle(), updatedBook.getAuthor(), updatedBook.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
    }

    public Optional<Person> showOwner(int bookId) {
        return jdbcTemplate.query("SELECT Person.* " +
                "FROM Person JOIN Person_Book on Person.id = Person_Book.person_id " +
                "JOIN Book on Person_Book.book_id = Book.id where Person_Book.book_id = ?", new Object[]{bookId}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public void deleteOwner(int bookId) {
        jdbcTemplate.update("DELETE FROM Person_Book WHERE book_id=?", bookId);
    }
}
