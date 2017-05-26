/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Migration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import Migration.CreateRelationshipsNeo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.neo4j.driver.v1.AuthToken;
import static org.hamcrest.CoreMatchers.is;

/**
 *
 * @author Kasper
 */
public class RelationshipsNeoTest {
    
    public RelationshipsNeoTest() {
    }
    

    @Test
    public void createMigrationTest() throws FileNotFoundException, UnsupportedEncodingException, IOException{
        CreateRelationshipsNeo crn = new CreateRelationshipsNeo("test", new AuthToken() {});
        
        String[] test = {"MATCH (b:Book {id: '1'}), (c:City {id: '5'}) CREATE (b)-[r:MENTIONS]->(c)", "MATCH (b:Book {id: '1'}), (c:City {id: '40'}) CREATE (b)-[r:MENTIONS]->(c)", "MATCH (b:Book {id: '1'}), (c:City {id: '55'}) CREATE (b)-[r:MENTIONS]->(c)", "MATCH (b:Book {id: '1'}), (c:City {id: '500'}) CREATE (b)-[r:MENTIONS]->(c)", "MATCH (b:Book {id: '2'}), (c:City {id: '0'}) CREATE (b)-[r:MENTIONS]->(c)", "MATCH (b:Book {id: '2'}), (c:City {id: '9'}) CREATE (b)-[r:MENTIONS]->(c)", "MATCH (b:Book {id: '2'}), (c:City {id: '900'}) CREATE (b)-[r:MENTIONS]->(c)", "MATCH (b:Book {id: '2'}), (c:City {id: '5418'}) CREATE (b)-[r:MENTIONS]->(c)", "MATCH (b:Book {id: '2'}), (c:City {id: '1'}) CREATE (b)-[r:MENTIONS]->(c)"};
        
        String[] res = crn.createMigration(new InputStreamReader(new FileInputStream("data/test-data/book_city.txt"), "UTF8")).split("\n");
        
        assertThat(res, is(test));
    }
}
