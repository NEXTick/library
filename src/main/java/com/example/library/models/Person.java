package com.example.library.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;


@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 50, message = "Uncorrected name")
    @Column(name = "name")
    private String name;
    @Min(value = 1850, message = "Year of birthday can be since 1850")
    @Column(name = "year_of_birthday", nullable = false)
    private int yearOfBirthday;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person(int id, String name, int yearOfBirthday) {
        this.id = id;
        this.name = name;
        this.yearOfBirthday = yearOfBirthday;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfBirthday() {
        return yearOfBirthday;
    }

    public void setYearOfBirthday(int yearOfBirthday) {
        this.yearOfBirthday = yearOfBirthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
