package com.example.library.util;

import com.example.library.daos.PersonDAO;
import com.example.library.models.Person;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private PersonDAO dao;

    public PersonValidator(PersonDAO dao) {
        this.dao = dao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
