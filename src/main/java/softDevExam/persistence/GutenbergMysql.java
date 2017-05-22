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

	public static Connection getConnection() {
		Properties props = new Properties();
		Connection con = null;
		try {
			InputStream stream = GutenbergMysql.class.getResourceAsStream("/data/db.properties");
			props.load(stream);

			// load the Driver
			Class.forName(props.getProperty("MYSQL_DRIVER_CLASS"));

			// create the connection
			con = DriverManager.getConnection(props.getProperty("MYSQL_URL"), props.getProperty("MYSQL_USERNAME"),
					props.getProperty("DB_PASSWORD"));

		} catch (SQLException | IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}

	private void setDriver(String driver) {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Book> getBooksByCity(String city) {
		List<Book> books = new ArrayList();
		try (Connection conn = getConnection()) {
			PreparedStatement ps = conn.prepareStatement(getBooksByCityPS());
			ps.setString(1, city);
			ResultSet rs = ps.executeQuery(getBooksByCityPS());
			while (rs.next()) {
				books.add(new Book(rs.getString("id"), rs.getString("name")));
			}

		} catch (Exception e) {
			return null;
		}
		return books;
	}

	private String getBooksByCityPS() {
		return "SELECT * FROM books " + "JOIN book_author ON (book_author.bookId = books.id) "
				+ "JOIN authors ON (authors.id = book_author.authorId) "
				+ "WHERE EXISTS (SELECT 1 FROM book_city JOIN cities ON (cities.id = book_city.cityId) "
				+ "WHERE book_city.bookId = books.id AND cities.name = ?";
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
