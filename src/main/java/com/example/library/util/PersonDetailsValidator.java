package com.example.library.util;

import com.example.library.models.Person;
import com.example.library.services.PersonDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonDetailsValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    public PersonDetailsValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person)target;

        try {
            personDetailsService.loadUserByUsername(person.getName());
        } catch (UsernameNotFoundException ignored) {
            return;
        }

        errors.rejectValue("name", "", "Человек с таким именем уже зарегистрирован");
    }
}
