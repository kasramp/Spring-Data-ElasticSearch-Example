package com.madadipouya.elasticsearch.springdata.example.service.impl;

import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.match;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import com.madadipouya.elasticsearch.springdata.example.model.Book;
import com.madadipouya.elasticsearch.springdata.example.repository.BookRepository;
import com.madadipouya.elasticsearch.springdata.example.service.BookService;
import com.madadipouya.elasticsearch.springdata.example.service.exception.BookNotFoundException;
import com.madadipouya.elasticsearch.springdata.example.service.exception.DuplicateIsbnException;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;

@Service
public class DefaultBookService implements BookService {

    private final BookRepository bookRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;

    public DefaultBookService(BookRepository bookRepository, ElasticsearchTemplate elasticsearchTemplate) {
        this.bookRepository = bookRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Optional<Book> getByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll()
            .forEach(books::add);
        return books;
    }

    @Override
    public List<Book> findByAuthor(String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        var criteria = QueryBuilders.bool(builder -> builder.must(
            match(queryAuthor -> queryAuthor.field("authorName").query(author)),
            match(queryTitle -> queryTitle.field("title").query(title))
        ));

        return elasticsearchTemplate.search(NativeQuery.builder().withQuery(criteria).build(), Book.class)
            .stream().map(SearchHit::getContent).toList();
    }

    @Override
    public Book create(Book book) throws DuplicateIsbnException {
        if (getByIsbn(book.getIsbn()).isEmpty()) {
            return bookRepository.save(book);
        }
        throw new DuplicateIsbnException(String.format("The provided ISBN: %s already exists. Use update instead!", book.getIsbn()));
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book update(String id, Book book) throws BookNotFoundException {
        Book oldBook = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("There is not book associated with the given id"));
        oldBook.setIsbn(book.getIsbn());
        oldBook.setAuthorName(book.getAuthorName());
        oldBook.setPublicationYear(book.getPublicationYear());
        oldBook.setTitle(book.getTitle());
        return bookRepository.save(oldBook);
    }
}
