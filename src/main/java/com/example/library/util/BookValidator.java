package com.example.library.util;

import com.example.library.daos.BookDAO;
import com.example.library.models.Book;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    private final BookDAO dao;

    public BookValidator(BookDAO dao) {
        this.dao = dao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
