package com.madadipouya.elasticsearch.springdata.example.service.impl;

import com.madadipouya.elasticsearch.springdata.BookElasticsearchContainer;
import com.madadipouya.elasticsearch.springdata.example.model.Book;
import com.madadipouya.elasticsearch.springdata.example.service.BookService;
import com.madadipouya.elasticsearch.springdata.example.service.exception.BookNotFoundException;
import com.madadipouya.elasticsearch.springdata.example.service.exception.DuplicateIsbnException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DefaultBookServiceIT {

    @Autowired
    private BookService bookService;

    @Autowired
    ElasticsearchTemplate template;

    @Container
    private static ElasticsearchContainer elasticsearchContainer = new BookElasticsearchContainer();

    @BeforeAll
    static void setUp() {
        elasticsearchContainer.start();
    }

    @BeforeEach
    void testIsContainerRunning() {
        assertTrue(elasticsearchContainer.isRunning());
        recreateIndex();
    }

    @Test
    void testGetBookByIsbn() throws DuplicateIsbnException {
        bookService.create(createBook("12 rules for life", "Jordan Peterson", 2018, "978-0345816023"));
        Optional<Book> result = bookService.getByIsbn("978-0345816023");
        assertTrue(result.isPresent());
        Book createdBook = result.get();
        assertNotNull(createdBook);
        assertEquals("12 rules for life", createdBook.getTitle());
        assertEquals("Jordan Peterson", createdBook.getAuthorName());
        assertEquals(2018, createdBook.getPublicationYear());
        assertEquals("978-0345816023", createdBook.getIsbn());
    }

    @Test
    void testGetAllBooks() throws DuplicateIsbnException {
        bookService.create(createBook("12 rules for life", "Jordan Peterson", 2018, "978-0345816023"));
        bookService.create(createBook("The Cathedral and the Bazaar", "Eric Raymond", 1999, "9780596106386"));
        List<Book> books = bookService.getAll();

        assertNotNull(books);
        assertEquals(2, books.size());
    }

    @Test
    void testFindByAuthor() throws DuplicateIsbnException {
        bookService.create(createBook("12 rules for life", "Jordan Peterson", 2018, "978-0345816023"));
        bookService.create(createBook("Maps of Meaning", "Jordan Peterson", 1999, "9781280407253"));

        List<Book> books = bookService.findByAuthor("Jordan Peterson");

        assertNotNull(books);
        assertEquals(2, books.size());
    }

    @Test
    void testFindByTitleAndAuthor() throws DuplicateIsbnException {
        bookService.create(createBook("12 rules for life", "Jordan Peterson", 2018, "978-0345816023"));
        bookService.create(createBook("Rules or not rules?", "Jordan Miller", 2010, "978128000000"));
        bookService.create(createBook("Poor economy", "Jordan Miller", 2006, "9781280789000"));
        bookService.create(createBook("The Cathedral and the Bazaar", "Eric Raymond", 1999, "9780596106386"));

        List<Book> books = bookService.findByTitleAndAuthor("rules", "jordan");

        assertNotNull(books);
        assertEquals(2, books.size());
    }

    @Test
    void testCreateBook() throws DuplicateIsbnException {
        Book createdBook = bookService.create(createBook("12 rules for life", "Jordan Peterson", 2018, "978-0345816023"));
        assertNotNull(createdBook);
        assertNotNull(createdBook.getId());
        assertEquals("12 rules for life", createdBook.getTitle());
        assertEquals("Jordan Peterson", createdBook.getAuthorName());
        assertEquals(2018, createdBook.getPublicationYear());
        assertEquals("978-0345816023", createdBook.getIsbn());
    }

    @Test
    void testCreateBookWithDuplicateISBNThrowsException() throws DuplicateIsbnException {
        Book createdBook = bookService.create(createBook("12 rules for life", "Jordan Peterson", 2018, "978-0345816023"));
        assertNotNull(createdBook);
        assertThrows(DuplicateIsbnException.class, () -> {
            bookService.create(createBook("Test title", "Test author", 2010, "978-0345816023"));
        });
    }

    @Test
    void testDeleteBookById() throws DuplicateIsbnException {
        Book createdBook = bookService.create(createBook("12 rules for life", "Jordan Peterson", 2018, "978-0345816023"));

        assertNotNull(createdBook);
        assertNotNull(createdBook.getId());

        bookService.deleteById(createdBook.getId());
        List<Book> books = bookService.findByAuthor("Jordan Peterson");

        assertTrue(books.isEmpty());
    }

    @Test
    void testUpdateBook() throws DuplicateIsbnException, BookNotFoundException {
        Book bookToUpdate = bookService.create(createBook("12 rules for life", "Jordan Peterson", 2000, "978-0345816023"));

        assertNotNull(bookToUpdate);
        assertNotNull(bookToUpdate.getId());

        bookToUpdate.setPublicationYear(2018);
        Book updatedBook = bookService.update(bookToUpdate.getId(), bookToUpdate);

        assertNotNull(updatedBook);
        assertNotNull(updatedBook.getId());
        assertEquals("12 rules for life", updatedBook.getTitle());
        assertEquals("Jordan Peterson", updatedBook.getAuthorName());
        assertEquals(2018, updatedBook.getPublicationYear());
        assertEquals("978-0345816023", updatedBook.getIsbn());
    }

    @Test
    void testUpdateBookThrowsExceptionIfCannotFindBook() {
        Book updatedBook = createBook("12 rules for life", "Jordan Peterson", 2000, "978-0345816023");

        assertThrows(BookNotFoundException.class, () -> {
            bookService.update("1A2B3C", updatedBook);
        });
    }

    private Book createBook(String title, String authorName, int publicationYear, String isbn) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthorName(authorName);
        book.setPublicationYear(publicationYear);
        book.setIsbn(isbn);
        return book;
    }

    private void recreateIndex() {
        if (template.indexExists(Book.class)) {
            template.deleteIndex(Book.class);
            template.createIndex(Book.class);
        }
    }

    @AfterAll
    static void destroy() {
        elasticsearchContainer.stop();
    }
}
