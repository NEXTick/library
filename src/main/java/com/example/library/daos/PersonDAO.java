package com.example.library.daos;

import com.example.library.models.Book;
import com.example.library.models.Person;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate
                .query("SELECT * FROM Person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {

        jdbcTemplate.update("INSERT INTO Person (name, yearOfBirthday) VALUES ( ?, ? )"
                , person.getName(), person.getYearOfBirthday());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Person SET name=?, yearOfBirthday=? WHERE id=?",
                updatedPerson.getName(), updatedPerson.getYearOfBirthday(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }

    public List<Book> showPersonBooks(int id) {
        return jdbcTemplate.query("SELECT Book.title, Book.author, Book.year " +
                "FROM Book JOIN Person_Book on Book.id = Person_Book.book_id " +
                "JOIN Person on Person_Book.person_id = Person.id where person_id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class)).stream().toList();

    }
}
