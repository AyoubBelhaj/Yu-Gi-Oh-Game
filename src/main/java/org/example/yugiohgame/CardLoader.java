package org.example.yugiohgame;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CardLoader {
    private static final String DATABASE_NAME = "yugioh";
    private static final String COLLECTION_NAME = "decks";

    public static List<Card> loadDeckFromDatabase(String deckId) {
        ArrayList<Card> cards = new ArrayList<>();
        String uri = "mongodb://localhost:27017";
        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applicationName("yugioh")
                .applyToConnectionPoolSettings(builder -> builder.maxWaitTime(1000, TimeUnit.MILLISECONDS))
                .build();

        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document deckDoc = collection.find(new Document("_id", new ObjectId(deckId))).first();
            if (deckDoc != null) {
                List<Document> deckCards = (List<Document>) deckDoc.get("deck");
                for (Document doc : deckCards) {
                    String name = doc.getString("name");
                    String desc = doc.getString("desc");
                    int attack = doc.getInteger("atk", 0);
                    int defense = doc.getInteger("def", 0);
                    List<Document> cardImages = (List<Document>) doc.get("card_images");
                    String imageUrl = cardImages != null && !cardImages.isEmpty() ? cardImages.get(0).getString("image_url") : "";

                    cards.add(new BaseCard(name, attack, defense, desc, imageUrl));
                }
            } else {
                System.out.println("No deck found with the specified ID.");
            }
        } catch (Exception e) {
            System.out.println("Error loading deck: " + e.getMessage());
        }

        return cards;
    }
}
