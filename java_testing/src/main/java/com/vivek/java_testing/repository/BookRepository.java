package com.vivek.java_testing.repository;

import com.vivek.java_testing.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book> findByBookNameContainsIgnoreCase(String bookName);

    List<Book> findByAuthor_AuthorNameContains(String authorName);

    @Query("select b from Book b where upper(b.author.authorName) like upper(concat('%', ?1, '%'))")
    List<Book> findByAuthor_AuthorNameContainsIgnoreCase(String authorName);



}
