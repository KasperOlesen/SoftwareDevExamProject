package Migration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.*;

public class BookMigrate {
    private final String tableName;
    private final IBookIdentifierProvider bookIdentifierProvider;

    public BookMigrate(String tableName, IBookIdentifierProvider bookIdentifierProvider) {
        this.tableName = tableName;
        this.bookIdentifierProvider = bookIdentifierProvider;
    }

    public void performMigration() throws IOException {
        // Read citites
        final String dir = System.getProperty("user.dir");
        final String path = dir + "/data/allformatted.csv";

        try (FileInputStream stream = new FileInputStream(path)) {
            String commands = createMigration(new InputStreamReader(stream));

            // Fire away the commands
        }

        // System.out.println(dir);
    }

    public String createMigration(InputStreamReader readerStream) throws IOException {
        List<BookDescription> books = new LinkedList<>();
        Set<String> authors = new HashSet<String>();
        Set<String> cities = new HashSet<String>();

        try (BufferedReader reader = new BufferedReader(readerStream)) {
            String line = reader.readLine();

            while (line != null) {
                // We split by '#' because we can! ;') 
                String[] parts = line.split("#");

                // Map the data
                String bookName = parts[0];
                String[] bookAuthors = parts[1].split(","); // authors are splited by ','
                String[] bookCities = parts[2].split(","); // cities are splited by ','

                // Loop over all the authors of the book, if it is not already added, then add it
                // to the list
                for (String bookAuthor : bookAuthors) {
                    if (!authors.contains(bookAuthor)) {
                        authors.add(bookAuthor);
                    }
                }

                // Loop over all the cities which is included the book, if it is not already added, then add it
                // to the list
                for (String bookCity : bookCities) {
                    if (!cities.contains(bookCity)) {
                        cities.add(bookCity);
                    }
                }

                // Finally we should add the book BookDescription
                books.add(new BookDescription(bookIdentifierProvider.getNextIdentifier(), bookName, bookAuthors, bookCities));

                line = reader.readLine();
            }
        }

        // Now we have all the books and cities, so we can create the commands;
        StringBuilder strBuilder = new StringBuilder();

        // We start by creating all the authors;
        strBuilder.append(createAuthorsCommands(authors));

        // Next we will loop over all the books and create relations
        strBuilder.append(createBookCommands(books));

        return strBuilder.toString();
    }

    public StringBuilder createAuthorsCommands(Set<String> authors) {
        StringBuilder strBuilder = new StringBuilder();

        for (String author : authors) {
            String sql = "INSERT INTO Author (name) VALUES ('" + author.replace("'", "\\'") + "');\n";

            strBuilder.append(sql);
        }

        return strBuilder;
    }

    public StringBuilder createBookCommands(List<BookDescription> books) {
        StringBuilder strBuilder = new StringBuilder();

        for (BookDescription book : books) {
            StringBuilder commandBuilder = new StringBuilder();

            // Start by creating the book
            commandBuilder.append("INSERT INTO Books (id, name) VALUES ('" + book.id + "', '" + book.name.replace("'", "\\'") + "');\n");

            // Lets add some references to the author
            for (String author : book.authors) {
                commandBuilder.append("INSERT INTO book_author (bookId, authorId) SELECT '" + book.id
                        + "', author.id FROM Authors WHERE Name = '" + author.replace("'", "\\'") + "';\n");
            }

            // Lets add some references to the cities
            for (String city : book.cities) {
                commandBuilder.append("INSERT INTO book_city (bookId, cityId) SELECT '" + book.id
                        + "', city.id FROM Cities WHERE Name = '" + city.replace("'", "\\'") + "';\n");
            }

            strBuilder.append(commandBuilder);
        }

        return strBuilder;
    }

    public class BookDescription {
        UUID id;
        String name;
        String[] authors;
        String[] cities;

        public BookDescription(UUID id, String name, String[] authors, String[] cities) {
            this.id = id;
            this.name = name;
            this.authors = authors;
            this.cities = cities;
        }
    }
}