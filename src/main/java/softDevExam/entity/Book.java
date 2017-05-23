package softDevExam.entity;

import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;
import java.util.ArrayList;

@XmlRootElement
public class Book {
	private String id;
	private String name;
	private Author author;
	private List<City> cities = new ArrayList<>();

	public Book() {
	}

	public Book(String id, String name, Author author) {
		this.id = id;
		this.name = name;
		this.author = author;
	}

	public List<City> getCities() {
		return this.cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public Author getAuthor() {
		return this.author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
