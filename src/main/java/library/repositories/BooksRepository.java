package library.repositories;

import library.models.Book;
import library.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwner(Person owner);

    List<Book> findByTitleContainsIgnoreCase(String search);

}
