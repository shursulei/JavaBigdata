import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.io.IOException;

public class DBConnect {
    private  static final File DB_PATH =new File("target/neo4j-db");
    private  static GraphDatabaseService graphDb;
    public DBConnect() throws IOException{
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        registerShutdownHook(graphDb);
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    public static void main(String[] args) {
        Transaction tx= graphDb.beginTx();
        Label label= Label.label("Person");
        Node first =graphDb.createNode(label);
        first.setProperty("name","first");
        Node second=graphDb.createNode(label);
        second.setProperty("name","second");
//        Relationship relationship =first.createRelationshipTo(second,RelTypes.KNOWS);
        System.out.println(first.getProperties("name"));
        System.out.println(second.getProperties("name"));


    }
}
