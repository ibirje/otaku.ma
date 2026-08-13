package com.otakuma.migration.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirestoreConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(FirestoreConfig.class);
    
    @Value("${firestore.project-id}")
    private String projectId;
    
    @Value("${firestore.credentials-path}")
    private String credentialsPath;
    
    @Bean
    public Firestore firestore() throws IOException {
        GoogleCredentials credentials;
        
        // Try different locations for the credentials file
        try {
            // First check if it's an absolute path
            File credFile = new File(credentialsPath);
            if (credFile.exists()) {
                logger.info("Loading Firebase credentials from file system: {}", credentialsPath);
                credentials = GoogleCredentials.fromStream(new FileInputStream(credFile));
            } else {
                // Then check classpath
                logger.info("Loading Firebase credentials from classpath: {}", credentialsPath);
                credentials = GoogleCredentials.fromStream(
                    new ClassPathResource(credentialsPath).getInputStream()
                );
            }
        } catch (IOException e) {
            // Finally, try application default credentials
            logger.info("Trying application default credentials");
            credentials = GoogleCredentials.getApplicationDefault();
        }
        
        FirestoreOptions firestoreOptions =
            FirestoreOptions.getDefaultInstance().toBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build();
                
        logger.info("Firestore initialized with project ID: {}", projectId);
        return firestoreOptions.getService();
    }
}