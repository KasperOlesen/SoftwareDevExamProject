package Migration;

import java.io.*;

import org.neo4j.driver.v1.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NeoBookMigrate {

    private final String url;
    private final AuthToken token;
    private final IBookIdentifierProvider bookIdentifierProvider;

    public NeoBookMigrate(String url, AuthToken token, IBookIdentifierProvider bookIdentifierProvider) {
        this.url = url;
        this.token = token;
        this.bookIdentifierProvider = bookIdentifierProvider;
    }

    public void performMigration(String filePath) throws Exception, IOException {
        // Read citites
        final String dir = System.getProperty("user.dir");
        final String path = dir + filePath;

        performMigration(new FileInputStream(path));
    }

    public void performMigration(InputStream stream) throws Exception, IOException {
        Object[] strCommandList = createMigration(new InputStreamReader(stream, "UTF8"));

        // Since the commands are ordered in the way that they need to be inserted
        // will should loop over them, and execute the queries in that order, so 
        // data will not be missing when creating relations

        Set<String> authors = (Set<String>) strCommandList[0];

        try (PrintWriter writer = new PrintWriter(System.getProperty("user.dir") + "/neo/authors.csv", "UTF8")) {
            writer.println("id:ID,name,:LABEL");

            int i = 1;
            for (String author : authors) {
                writer.println(
                        "\"" + author.replace("\"", "\\\"") + "\",\"" + author.replace("\"", "\\\"") + "\",Author");
                i++;
            }
        }

        List<BookDescription> books = (List<BookDescription>) strCommandList[1];

        try (PrintWriter writer = new PrintWriter(System.getProperty("user.dir") + "/neo/books.csv", "UTF8")) {
            writer.println("id:ID,title,:LABEL");

            int i = 1;
            for (BookDescription book : books) {
                writer.println("\"" + book.id + "\",\"" + book.name.replace("\"", "\\\"") + "\",Book");
                i++;
            }
        }

        try (PrintWriter writer = new PrintWriter(System.getProperty("user.dir") + "/neo/author_book.csv", "UTF8")) {
            writer.println(":START_ID,:END_ID,:TYPE");

            int i = 1;
            for (BookDescription book : books) {
                String author = book.authors[0];

                writer.println("\"" + author.replace("\"", "\\\"") + "\",\"" + book.id + "\",HAS_WRITTEN");
                i++;
            }
        }

        Map<String, Set<String>> cities = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(System.getProperty("user.dir") + "/data/cities.csv"), "UTF8"))) {
            String line = reader.readLine();

            while (line != null) {
                String[] parts = line.split(",");

                String id = parts[0];
                String name = parts[1].replace("'", "`");

                Set<String> set;
                if (cities.containsKey(name)) {
                    set = cities.get(name);
                } else {
                    set = new HashSet<String>();
                }

                set.add(id);

                cities.put(name, set);

                line = reader.readLine();
            }
        }

        try (PrintWriter writer = new PrintWriter(System.getProperty("user.dir") + "/neo/book_city.csv", "UTF8")) {
            writer.println(":START_ID,:END_ID,:TYPE");

            int i = 1;
            for (BookDescription book : books) {
                for (String city : book.cities) {
                    if (cities.containsKey(city)) {
                        for (String cityId : cities.get(city)) {
                            String part = "\"" + book.id + "\",\"" + cityId + "\",MENTIONS";
                            writer.println(part);
                        }
                    }
                }
                i++;
            }
        }

        // for (String strCommands : strCommandList) {
        //     Collection<String> commands = Arrays.asList(strCommands.split("\n\n"));

        //     AtomicInteger index = new AtomicInteger();

        //     // Loop over the commands in paralllel
        //     commands.parallelStream().forEach(command -> {
        //         Driver driver = GraphDatabase.driver(this.url, this.token);

        //         // Fire the command against the DB< 
        //         String[] subCommands = command.split("\n");

        //         try (Session session = driver.session()) {
        //             session.run(subCommands[0]);
        //         }

        //         // We split every command up
        //         if (subCommands.length > 1) {
        //             Arrays.asList(Arrays.copyOfRange(subCommands, 1, subCommands.length)).parallelStream()
        //                     .forEach(subCommand -> {
        //                         try (Session session = driver.session()) {
        //                             session.run(subCommand);
        //                         } catch (Exception ex) {
        //                             System.out.println("Could not fire \"" + command + "\" - " + ex.getMessage());
        //                         }
        //                     });
        //         }

        //         System.out.println("Executed: " + index.getAndIncrement());
        //     });
        // }

        // System.out.println(dir);
    }

    public Object[] createMigration(InputStreamReader readerStream) throws Exception, IOException {
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

        return new Object[] { authors, books };
    }

    public StringBuilder createAuthorsCommands(Set<String> authors) {
        StringBuilder strBuilder = new StringBuilder();

        List<String> parts = new ArrayList<>();
        for (String author : authors) {
            parts.add("CREATE (a:Author { name: '" + author.replace("'", "\\'") + "'})");
        }

        String sql = String.join(";\n ", parts);
        strBuilder.append(sql);
        strBuilder.append("\n\n");

        return strBuilder;
    }

    public StringBuilder createBookCommands(List<BookDescription> books) {
        StringBuilder strBuilder = new StringBuilder();

        for (BookDescription book : books) {
            StringBuilder commandBuilder = new StringBuilder();

            String author = book.authors[0];
            commandBuilder.append("MATCH (a:Author {name: '" + author.replace("'", "\\'") + "'}) CREATE (b:Book {id: '"
                    + book.id + "', title: '" + book.name.replace("'", "\\'") + "'})<-[:HAS_WRITTEN]-(a);\n");

            // Lets add some references to the cities
            List<String> cityParts = new ArrayList<>();
            for (String city : book.cities) {
                cityParts.add("MATCH (b:Book {id: '" + book.id + "'}), (c:City {name: '" + city.replace("'", "\\'")
                        + "'}) CREATE (b)-[r:MENTIONS]->(c)");
            }

            commandBuilder.append(String.join(";\n ", cityParts) + ";");

            strBuilder.append(commandBuilder);
            strBuilder.append("\n\n");
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