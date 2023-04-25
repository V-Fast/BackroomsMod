package com.lumination.backrooms.utils;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.UUID;

public class ModMongo {
    public static final String username = "Lumaa";
    public static final String password = "N@th@n49630";
    public static final String uri = "mongodb://%s:%s@127.0.0.1:27017".formatted(username, password);

    public static MongoDatabase db;
    public static MongoCollection<Document> collection;
    public static boolean connected = false;

    public static void init(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            db = mongoClient.getDatabase("MinecraftMods");
            collection = db.getCollection("backrooms");
            connected = true;
        }
    }

    private static void check() {
        if (connected) {
            collection.find();
        }
    }

    public static void countUp(UUID playerUuid) {
        if (connected) {
            Document document = new Document().append("played", playerUuid);
        }
    }
}
