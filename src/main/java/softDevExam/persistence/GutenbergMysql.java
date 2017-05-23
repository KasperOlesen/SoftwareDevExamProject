package softDevExam.persistence;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import softDevExam.controller.GutenbergService;
import softDevExam.entity.Book;

public class GutenbergMysql implements GutenbergService {

	private final String url;
	private final String username;
	private final String password;
	private final String driver;

	public GutenbergMysql() {
		Properties props = new Properties();

		String url = null, username = null, password = null, driver = null;

		try {
			InputStream stream = GutenbergMysql.class.getResourceAsStream("/data/db.properties");
			props.load(stream);

			// load the Driver
			driver = props.getProperty("MYSQL_DRIVER_CLASS");
			url = props.getProperty("MYSQL_URL");
			username = props.getProperty("MYSQL_USERNAME");
			password = props.getProperty("DB_PASSWORD");

		} catch (Exception e) {
			e.printStackTrace();
		}

		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public GutenbergMysql(String driver, String url, String username, String password) {
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public Connection getConnection() throws Exception {
		try {
			Class.forName(this.driver);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());

		}

		return DriverManager.getConnection(this.url, this.username, this.password);
	}

	@Override
	public List<Book> getBooksByCity(String city) throws Exception {
		List<Book> books = new ArrayList<>();

		try (Connection conn = getConnection()) {
			PreparedStatement ps = conn.prepareStatement(getBooksByCityPS());
			ps.setString(1, city);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				books.add(new Book(rs.getString("id"), rs.getString("name")));
			}

		}

		return books;
	}

	private String getBooksByCityPS() {
		return "SELECT * FROM books JOIN book_author ON (book_author.bookId = books.id) "
				+ " JOIN authors ON (authors.id = book_author.authorId) "
				+ " WHERE EXISTS (SELECT 1 FROM book_city JOIN cities ON (cities.id = book_city.cityId) WHERE book_city.bookId = books.id AND cities.name = ?)";
	}

	@Override
	public String getCitiesByBook(String book) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getBooksAndCitysByAuthor(String author) {
		// TODO Auto-generated method stub
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

			System.out.println(command);

			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(command);

			while (rs.next()) {
				books.add(new Book(rs.getString("id"), rs.getString("name")));
			}

		}

		return books;
	}

}
