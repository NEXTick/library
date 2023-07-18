package com.example.library.services;

import com.example.library.models.Book;
import com.example.library.models.Person;
import com.example.library.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public List<Book> showPersonBooks(int id) {
        Optional<Person> p = peopleRepository.findById(id);
        if (p.isPresent()) {
            Hibernate.initialize(p.get().getBooks());
            p.get().getBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if (diffInMillies > 864_000_000) {
                    book.setExpired(true);
                }
            });

            return p.get().getBooks();
        }
        return Collections.emptyList();
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(Person person) {
        peopleRepository.save(person);
    }

    public void delete(int id) {
        peopleRepository.findById(id).ifPresent(peopleRepository::delete);
    }
}
