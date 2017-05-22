package softDevExam.factories;

import softDevExam.controller.GutenbergController;
import softDevExam.persistence.GutenbergMysql;
import softDevExam.persistence.GutenbergNeo4J;

public class GutenbergControllerFactory {

	public GutenbergController create(String databaseType) {
		if (databaseType.equalsIgnoreCase("mysql")) {
			return new GutenbergController(new GutenbergMysql());
		}

		if (databaseType.equalsIgnoreCase("neo4j")) {
			return new GutenbergController(new GutenbergNeo4J());
		}

		return null;
	}
}