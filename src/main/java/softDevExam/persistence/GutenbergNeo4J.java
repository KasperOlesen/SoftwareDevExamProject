package softDevExam.persistence;

import java.sql.SQLException;
import java.util.*;

import softDevExam.controller.GutenbergService;
import softDevExam.entity.Book;
import softDevExam.entity.City;

import org.neo4j.driver.v1.*;

public class GutenbergNeo4J implements GutenbergService {

	private final String url;
	private final AuthToken token;

	public GutenbergNeo4J() {
		// TODO: Read from properties file
		this("bolt://localhost:7687", AuthTokens.basic("neo4j", "123123qwe"));
	}

	public GutenbergNeo4J(String url, AuthToken token) {
		this.url = url;
		this.token = token;
	}

	@Override
	public List<Book> getBooksByCity(String city) throws Exception {
		List<Book> books = new ArrayList<>();

		try (Driver driver = GraphDatabase.driver(this.url, this.token)) {
			try (Session session = driver.session()) {
				String command = "MATCH (c:City {name: $cityName})<-[m:MENTIONS]-(b:Book)<-[r:HAS_WRITTEN]-(a:Author) "
						+ "RETURN b;";

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("cityName", city);

				StatementResult response = session.run(command, parameters);
				for (Record record : response.list()) {
					books.add(new Book(record.get("id").asString(), record.get("name").asString()));
				}
			}
		}

		return books;
	}

	@Override
	public List<City> getCitiesByBook(String book) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> getBooksAndCitysByAuthor(String author) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> getBooksByLocation(double longitude, double latitude) throws Exception {
		List<Book> books = new ArrayList<>();

		try (Driver driver = GraphDatabase.driver(this.url, this.token)) {
			try (Session session = driver.session()) {
				String command = "MATCH (b:Book)-[:MENTIONS]-(a:City) "
						+ "WHERE distance(point(a), point({ longitude: $longitude, latitude: $latitude }))/1000 <= 50 "
						+ "RETURN b;";

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("latitude", latitude);
				parameters.put("longitude", longitude);

				StatementResult response = session.run(command, parameters);
				for (Record record : response.list()) {
					books.add(new Book(record.get("id").asString(), record.get("name").asString()));
				}
			}
		}

		return books;
	}
}
