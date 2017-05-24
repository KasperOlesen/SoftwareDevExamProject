package softDevExam.persistence;

import java.sql.SQLException;
import java.io.InputStream;
import java.util.*;

import softDevExam.controller.GutenbergService;
import softDevExam.entity.*;

import org.neo4j.driver.v1.*;

public class GutenbergNeo4J implements GutenbergService {

	private final String url;
	private final AuthToken token;

	public GutenbergNeo4J() {
		Properties props = new Properties();

		try {
			InputStream stream = GutenbergMysql.class.getResourceAsStream("db.properties");
			props.load(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.url = props.getProperty("NEO4J_URL");
		this.token = AuthTokens.basic(props.getProperty("NEO4J_USERNAME"), props.getProperty("NEO4J_PASSWORD"));
	}

	public GutenbergNeo4J(String url, AuthToken token) {
		this.url = url;
		this.token = token;
	}

	@Override
	public List<Book> getBooksByCity(String city) throws Exception {
		Map<String, Book> bookLookup = new HashMap<>();

		try (Driver driver = GraphDatabase.driver(this.url, this.token)) {
			try (Session session = driver.session()) {
				final String command = "MATCH (a:Author)-[r:HAS_WRITTEN]->(b:Book)-[m:MENTIONS]->(c:City  { name: $cityName }) "
						+ "RETURN b, a;";

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("cityName", city);

				StatementResult response = session.run(command, parameters);
				for (Record record : response.list()) {
					Value book = record.get("b");

					String bookId = book.get("id").asString();

					Book bookResult;

					if (bookLookup.containsKey(bookId)) {
						bookResult = bookLookup.get(bookId);
					} else {
						Value author = record.get("a");

						bookResult = new Book(bookId, book.get("title").asString(),
								new Author(author.get("name").asString()));
						bookLookup.put(bookResult.getId(), bookResult);
					}

					// Value c = record.get("c");
					// bookResult.getCities().add(new City(c.get("name").asString(), c.get("latitude").asDouble(),
					// 		c.get("longitude").asDouble()));
				}
			}
		}

		return new ArrayList<Book>(bookLookup.values());
	}

	@Override
	public List<City> getCitiesByBook(String book) {
		List<City> resultList = new ArrayList<>();

		try (Driver driver = GraphDatabase.driver(this.url, this.token)) {
			try (Session session = driver.session()) {
				final String command = "MATCH (b:Book {title: $bookName})-[m:MENTIONS]->(c:City) RETURN c;";

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("bookName", book);

				StatementResult response = session.run(command, parameters);
				for (Record record : response.list()) {
					Value c = record.get("c");
					resultList.add(new City(c.get("name").asString(), c.get("latitude").asDouble(),
							c.get("longitude").asDouble()));
				}
			}
		}

		return resultList;
	}

	@Override
	public List<Book> getBooksAndCitysByAuthor(String author) {
		Map<String, Book> bookLookup = new HashMap<>();

		try (Driver driver = GraphDatabase.driver(this.url, this.token)) {
			try (Session session = driver.session()) {
				final String command = "MATCH (a:Author)-[r:HAS_WRITTEN]->(b:Book)-[m:MENTIONS]->(c:City) "
						+ "WHERE a.name = $authorName " + "RETURN b, c, a;";

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("authorName", author);

				StatementResult response = session.run(command, parameters);
				for (Record record : response.list()) {
					Value book = record.get("b");

					String bookId = book.get("id").asString();

					Book bookResult;

					if (bookLookup.containsKey(bookId)) {
						bookResult = bookLookup.get(bookId);
					} else {
						Value a = record.get("a");

						bookResult = new Book(bookId, book.get("title").asString(),
								new Author(a.get("name").asString()));
						bookLookup.put(bookResult.getId(), bookResult);
					}

					Value c = record.get("c");
					bookResult.getCities().add(new City(c.get("name").asString(), c.get("latitude").asDouble(),
							c.get("longitude").asDouble()));
				}
			}
		}

		return new ArrayList<Book>(bookLookup.values());
	}

	@Override
	public List<Book> getBooksByLocation(double longitude, double latitude) throws Exception {
		Map<String, Book> bookLookup = new HashMap<>();

		try (Driver driver = GraphDatabase.driver(this.url, this.token)) {
			try (Session session = driver.session()) {
				final String command = "MATCH (a:Author)-[r:HAS_WRITTEN]->(b:Book)-[m:MENTIONS]->(c:City) "
						+ "WHERE distance(point(c), point({ longitude: $longitude, latitude: $latitude  }))/1000 <= 50 "
						+ "RETURN b, c, a;";

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("latitude", latitude);
				parameters.put("longitude", longitude);

				StatementResult response = session.run(command, parameters);
				for (Record record : response.list()) {
					Value book = record.get("b");

					String bookId = book.get("id").asString();

					Book bookResult;

					if (bookLookup.containsKey(bookId)) {
						bookResult = bookLookup.get(bookId);
					} else {
						Value author = record.get("a");

						bookResult = new Book(bookId, book.get("title").asString(),
								new Author(author.get("name").asString()));
						bookLookup.put(bookResult.getId(), bookResult);
					}

					Value c = record.get("c");
					bookResult.getCities().add(new City(c.get("name").asString(), c.get("latitude").asDouble(),
							c.get("longitude").asDouble()));
				}
			}
		}

		return new ArrayList<Book>(bookLookup.values());
	}
}
