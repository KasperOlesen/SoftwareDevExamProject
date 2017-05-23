package softDevExam.entity;

public class Author {
	private final String name;

	public Author() {
		this.name = null;
	}

	public Author(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}