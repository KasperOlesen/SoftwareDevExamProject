package factories;

import softDevExam.controller.GutenbergController;
import softDevExam.persistence.GutenbergMysql;
import softDevExam.persistence.GutenbergNeo4J;

public class GutenbergControllerFactory {
    private GutenbergMysql sqlHandler;
    private GutenbergNeo4J noSqlHandler;

    public GutenbergControllerFactory() {

    }

    public GutenbergController create(String databaseType) {
        if (databaseType == "mysql") {
            return new GutenbergController(this.sqlHandler);
        }

        if (databaseType == "neo4j") {
            return new GutenbergController(this.noSqlHandler);
        }

        return null;
    }
}