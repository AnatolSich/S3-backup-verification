package download;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.extern.apachecommons.CommonsLog;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Properties;

@CommonsLog
public class MongoDBClient {

    private final MongoCollection<Document> bodCollection;


    public MongoDBClient(Properties appProps) {
        this.bodCollection = new MongoClient(
                appProps.getProperty("upstox.verification.mongodb.host"),
                Integer.parseInt(appProps.getProperty("upstox.verification.mongodb.port")))
                    .getDatabase(appProps.getProperty("upstox.verification.mongodb.database"))
                    .getCollection("entire_bod");
    }

    public ArrayList<Document> getBodCollection() {
        ArrayList<Document> list = bodCollection.find().into(new ArrayList<>());
        log.info("(Additional info) Bod collection is downloaded");
        return list;
    }
}
