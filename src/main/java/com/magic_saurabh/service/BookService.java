package com.magic_saurabh.service;

import com.magic_saurabh.exception.BookNotFoundException;
import com.magic_saurabh.model.Book;

import com.magic_saurabh.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;

    }

    public Flux<Book> getAllBooks() {
        return bookRepository.findAll();

    }

    public Mono<Book> getBookById(String id) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new BookNotFoundException("This id" + id + " is not present in database")))
                .flatMap(book->bookRepository.findById(id));
    }

    public Mono<Book> createBook(Book book) {
        book.setCreatedDate(new Date());
        book.setModifiedDate(new Date());
        return bookRepository.save(book);

    }

    public Mono<Book> updateBook(String id, Book updatedBook) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new BookNotFoundException("This id " + id + "not store in database")))
                .flatMap(existingBook -> {
                    existingBook.setTitle(updatedBook.getTitle());
                    existingBook.setAuthor(updatedBook.getAuthor());
                    existingBook.setPublicationYear(updatedBook.getPublicationYear());
                    existingBook.setModifiedDate(new Date());
                    return bookRepository.save(existingBook);
                });

    }

    public Mono<Void> deleteBook(String id) {
        return bookRepository.deleteById(id)
                .switchIfEmpty(Mono.error(new BookNotFoundException("This id" + id + " is not present in database")));
    }
}