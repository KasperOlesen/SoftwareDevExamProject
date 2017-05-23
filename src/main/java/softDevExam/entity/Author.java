package softDevExam.entity;

import java.util.List;

public class Author {
    private final String name;
    private final List<Book> books;

    public Author(String name, List<Book> books) {
        this.name = name;
        this.books = books;
    }

    public String getName() {
        return this.name;
    }

    public List<Book> getBooks() {
        return this.books;
    }
}