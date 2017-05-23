package softDevExam.persistence;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import softDevExam.controller.GutenbergService;
import softDevExam.entity.Book;
import softDevExam.entity.City;

public class GutenbergMysql implements GutenbergService {

	private String url;
	private String username;
	private String password;
	private String driver;

	public GutenbergMysql() {
		Properties props = new Properties();

		try {
			InputStream stream = GutenbergMysql.class.getResourceAsStream("/data/db.properties");
			props.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.driver = props.getProperty("MYSQL_DRIVER_CLASS");
		this.url = props.getProperty("MYSQL_URL");
		this.username = props.getProperty("MYSQL_USERNAME");
		this.password = props.getProperty("DB_PASSWORD");

	}

	public GutenbergMysql(String url, String username, String password, String driver) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.driver = driver;
	}

	public Connection getConnection() {
		try {
			Class.forName(this.driver);
			return DriverManager.getConnection(this.url, this.username, this.password);
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Book> getBooksByCity(String city) throws SQLException {
		List<Book> resultList = new ArrayList<>();

		ResultSet rs = getResults(city, getBooksByCityPS());
		while (rs.next()) {
			resultList.add(new Book(rs.getString("id"), rs.getString("name")));
		}

		return resultList;
	}

	@Override
	public List<City> getCitiesByBook(String book) throws SQLException {
		List<City> resultList = new ArrayList<>();
		ResultSet rs = getResults(book, getCitiesByBookPS());
		while (rs.next()) {
			Point p = (Point) rs.getObject("location");
			resultList.add(new City(rs.getString("name"), p.getX(), p.getY()));
		}

		return resultList;
	}

	@Override
	public List<Book> getBooksAndCitysByAuthor(String author) throws SQLException {
		List<Book> resultList = new ArrayList<>();

		ResultSet rs = getResults(author, getBooksAndCitysByAuthorPS());
		while (rs.next()) {
			resultList.add(new Book(rs.getString("id"), rs.getString("name")));
		}

		return resultList;
	}

	@Override
	public List<Book> getBooksByLocation(double latitude, double longitude) throws Exception {
		List<Book> resultList = new ArrayList<>();
		return resultList;

	}

	private ResultSet getResults(String param, String sql) {

		try (Connection conn = getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, param);
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	private String getBooksByCityPS() {
		return "SELECT * FROM books JOIN book_author ON (book_author.bookId = books.id) "
				+ " JOIN authors ON (authors.id = book_author.authorId) "
				+ " WHERE EXISTS (SELECT 1 FROM book_city JOIN cities ON (cities.id = book_city.cityId) WHERE book_city.bookId = books.id AND cities.name = ?)";
	}

	private String getCitiesByBookPS() {
		return "SELECT * FROM books " + "JOIN book_city ON (book_city.bookId = books.id) "
				+ "JOIN cities ON (cities.id = book_city.cityId) " + "WHERE books.name = ?";
	}

	private String getBooksByLocationPS() {
		return "";
	}

	private String getBooksAndCitysByAuthorPS() {
		return "SELECT * FROM authors " + "JOIN book_author ON (book_author.authorId = authors.id) "
				+ "JOIN books ON (books.id = book_author.bookId) " + "JOIN book_city ON (book_city.bookId = books.id) "
				+ "JOIN cities ON (book_city.cityId = cities.id) " + "WHERE authors.name = ?";
	}

}
