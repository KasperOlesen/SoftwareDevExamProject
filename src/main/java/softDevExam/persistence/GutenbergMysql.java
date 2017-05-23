package softDevExam.persistence;

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

import javax.ejb.Stateless;

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
	public String getBooksByLocation(String location) {
		// TODO Auto-generated method stub
		return "";
	}

}
