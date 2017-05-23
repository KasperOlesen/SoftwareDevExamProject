package softDevExam.entity;

import java.util.List;

public class Book {

	private String name;
	private List<City> cities;
	private Author author;

	public Book(String name, List<City> cities, Author author) {
		this.name = name;
		this.cities = cities;
		this.author = author;
	}

	public Book(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<City> getCities() {
		return cities;
	}

	public Author getAuthor() {
		return author;
	}

}
