/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Config;

/**
 *
 * @author flori
 */

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import java.util.Arrays;

/**
 * Conexión a MongoDB
 * Patrón Singleton
 */
public class MongoDBConnection {
    private static MongoDBConnection instance;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private DatabaseConfig config;
    
    private MongoDBConnection() {
        try {
            config = DatabaseConfig.getInstance();
            
            // Configuración del cliente MongoDB
            MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                    builder.hosts(Arrays.asList(
                        new ServerAddress(config.getMongoHost(), config.getMongoPort())
                    ))
                )
                .build();
            
            this.mongoClient = MongoClients.create(settings);
            this.database = mongoClient.getDatabase(config.getMongoDatabase());
            
            System.out.println("✓ Conexión MongoDB establecida");
        } catch (Exception e) {
            System.err.println("✗ Error al conectar con MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static synchronized MongoDBConnection getInstance() {
        if (instance == null) {
            instance = new MongoDBConnection();
        }
        return instance;
    }
    
    public MongoDatabase getDatabase() {
        return database;
    }
    
    public MongoClient getMongoClient() {
        return mongoClient;
    }
    
    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("✓ Conexión MongoDB cerrada");
        }
    }
}
