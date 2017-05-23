package Migration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.sql.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.text.StrBuilder;

public class BookMigrate {
    
    private final String url;
    private final String username;
    private final String password;
    private final IBookIdentifierProvider bookIdentifierProvider;

    public BookMigrate(String url, String username, String password, IBookIdentifierProvider bookIdentifierProvider) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.bookIdentifierProvider = bookIdentifierProvider;
    }

    public void performMigration(String filePath) throws Exception, IOException {
        // Read citites
        final String dir = System.getProperty("user.dir");
        final String path = dir + filePath;

        try (FileInputStream stream = new FileInputStream(path)) {
            String[] strCommandList = createMigration(new InputStreamReader(stream, "UTF8"));

            // Since the commands are ordered in the way that they need to be inserted
            // will should loop over them, and execute the queries in that order, so 
            // data will not be missing when creating relations

            for (String strCommands : strCommandList) {
                Collection<String> commands = Arrays.asList(strCommands.split("\n\n"));

                AtomicInteger index = new AtomicInteger();

                // Loop over the commands in paralllel
                commands.parallelStream().forEach(command -> {
                    // Fire the command against the DB< 
                    try (Connection con = DriverManager.getConnection(this.url, this.username, this.password)) {
                        String[] subCommands = command.split("\n");

                        try (Statement st = con.createStatement()) {
                            st.execute(subCommands[0]);

                            System.out.println(subCommands[0]);
                            System.out.println("Execute command!- " + index.getAndIncrement());
                        }

                        // We split every command up
                        if (subCommands.length > 1) {
                            Arrays.asList(Arrays.copyOfRange(subCommands, 1, subCommands.length)).parallelStream()
                                    .forEach(subCommand -> {
                                        try (Statement st = con.createStatement()) {
                                            st.execute(subCommand);
                                        } catch (SQLException ex) {
                                            System.out
                                                    .println("Could not fire \"" + command + "\" - " + ex.getMessage());
                                        }
                                    });
                        }
                    } catch (SQLException ex) {
                        System.out.println("Could not fire \"" + command + "\" - " + ex.getMessage());
                    }
                });
            }
        }

        // System.out.println(dir);
    }

    public String[] createMigration(InputStreamReader readerStream) throws Exception, IOException {
        List<BookDescription> books = new LinkedList<>();
        Set<String> authors = new HashSet<String>();
        Set<String> cities = new HashSet<String>();

        try (BufferedReader reader = new BufferedReader(readerStream)) {
            String line = reader.readLine();

            while (line != null) {
                if (line.trim() == "") {
                    line = reader.readLine();
                    continue;
                }

                // We split by '#' because we can! ;') 
                String[] parts = line.split("#");

                if (parts.length != 3) {
                    line = reader.readLine();
                    continue;
                }

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
                books.add(new BookDescription(bookIdentifierProvider.getNextIdentifier(), bookName, bookAuthors,
                        bookCities));

                line = reader.readLine();
            }
        }

        return new String[] { createAuthorsCommands(authors).toString(), createBookCommands(books).toString() };
    }

    public StringBuilder createAuthorsCommands(Set<String> authors) {
        StringBuilder strBuilder = new StringBuilder();

        for (String author : authors) {
            String sql = "INSERT INTO authors (name) VALUES ('" + author.replace("'", "\\'") + "');\n\n";

            strBuilder.append(sql);
        }

        return strBuilder;
    }

    public StringBuilder createBookCommands(List<BookDescription> books) {
        StringBuilder strBuilder = new StringBuilder();

        for (BookDescription book : books) {
            StringBuilder commandBuilder = new StringBuilder();

            // Start by creating the book
            commandBuilder.append("INSERT INTO books (id, name) VALUES ('" + book.id + "', '"
                    + book.name.replace("'", "\\'") + "');\n");

            // Lets add some references to the author
            for (String author : book.authors) {
                commandBuilder.append("INSERT INTO book_author (bookId, authorId) SELECT '" + book.id
                        + "', authors.id FROM authors WHERE authors.name = '" + author.replace("'", "\\'") + "';\n");
            }

            // Lets add some references to the cities
            for (String city : book.cities) {
                commandBuilder.append("INSERT INTO book_city (bookId, cityId) SELECT '" + book.id
                        + "', cities.id FROM cities WHERE cities.name = '" + city.replace("'", "\\'") + "';\n");
            }

            strBuilder.append(commandBuilder);
            strBuilder.append("\n");
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