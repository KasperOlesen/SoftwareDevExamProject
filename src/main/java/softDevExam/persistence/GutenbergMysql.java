package softDevExam.persistence;

import java.awt.Point;
import java.io.InputStream;
import java.sql.*;
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.driver = props.getProperty("MYSQL_DRIVER_CLASS");
		this.url = props.getProperty("MYSQL_URL");
		this.username = props.getProperty("MYSQL_USERNAME");
		this.password = props.getProperty("DB_PASSWORD");

	}

	public GutenbergMysql(String driver, String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.driver = driver;
	}

	public Connection getConnection() throws Exception {
		Class.forName(this.driver);
		return DriverManager.getConnection(this.url, this.username, this.password);
	}

	@Override
	public List<Book> getBooksByCity(String city) throws Exception {
		List<Book> resultList = new ArrayList<>();

		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(getBooksByCityPS())) {
				ps.setString(1, city);

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						resultList.add(new Book(rs.getString("id"), rs.getString("name")));
					}
				}
			}
		}

		return resultList;
	}

	@Override
	public List<City> getCitiesByBook(String book) throws Exception {
		List<City> resultList = new ArrayList<>();

		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(getCitiesByBookPS())) {
				ps.setString(1, book);

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Point p = (Point) rs.getObject("location");
						resultList.add(new City(rs.getString("name"), p.getX(), p.getY()));
					}
				}
			}
		}

		return resultList;
	}

	@Override
	public List<Book> getBooksAndCitysByAuthor(String author) throws Exception {
		List<Book> resultList = new ArrayList<>();

		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(getBooksAndCitysByAuthorPS())) {
				ps.setString(1, author);

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						resultList.add(new Book(rs.getString("id"), rs.getString("name")));
					}
				}
			}
		}

		return resultList;
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

	@Override
	public List<Book> getBooksByLocation(double longitude, double latitude) throws Exception {
		final int radiusInKilometers = 50;

		List<Book> books = new ArrayList<>();

		try (Connection conn = getConnection()) {
			// Since it not possible to fuck up numbers, we dont need to use parameters.. :D
			final String command = "SELECT 	books.* FROM cities " + "JOIN book_city ON (book_city.cityId = cities.id) "
					+ "JOIN books ON (books.id = book_city.bookId) " + "WHERE MBRCONTAINS(LINESTRING(POINT(" + longitude
					+ " + " + radiusInKilometers + " / (111.1 / COS(RADIANS(" + longitude + "))), " + "" + latitude
					+ " + " + radiusInKilometers + " / 111.1), " + "POINT(" + longitude + " - " + radiusInKilometers
					+ " / (111.1 / COS(RADIANS(" + latitude + "))), " + "" + latitude + " - " + radiusInKilometers
					+ " / 111.1)), " + "cities.location)";

			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(command);

			while (rs.next()) {
				books.add(new Book(rs.getString("id"), rs.getString("name")));
			}

		}

		return books;
	}

	private String getBooksAndCitysByAuthorPS() {
		return "SELECT * FROM authors " + "JOIN book_author ON (book_author.authorId = authors.id) "
				+ "JOIN books ON (books.id = book_author.bookId) " + "JOIN book_city ON (book_city.bookId = books.id) "
				+ "JOIN cities ON (book_city.cityId = cities.id) " + "WHERE authors.name = ?";
	}

}
