package com.otakuma.migration.config;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Configuration
@RequiredArgsConstructor
public class VerificationConfig {
    private static final Logger logger = LoggerFactory.getLogger(VerificationConfig.class);

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final Firestore firestore;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public Job verificationJob() {
        return new JobBuilder("verificationJob", jobRepository)
                .start(verificationStep())
                .build();
    }

    @Bean
    public Step verificationStep() {
        return new StepBuilder("verificationStep", jobRepository)
                .tasklet(verificationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet verificationTasklet() {
        return (contribution, chunkContext) -> {
            // Map of table names to collection names
            Map<String, String> tableToCollection = new HashMap<>();
            tableToCollection.put("admin", "admins");
            tableToCollection.put("admin_role", "admin_roles");
            tableToCollection.put("admin_droit", "admin_droits");
            tableToCollection.put("categorie", "categories");
            tableToCollection.put("theme", "themes");
            tableToCollection.put("produit", "products");
            tableToCollection.put("client", "clients");
            tableToCollection.put("commande", "commandes");
            tableToCollection.put("achatstock", "achatStocks");
            tableToCollection.put("sku", "skus");
            tableToCollection.put("attribut", "attributes");
            tableToCollection.put("fournisseur", "fournisseurs");

            // Map of tables to their primary key column
            Map<String, String> tableToPrimaryKey = new HashMap<>();
            tableToPrimaryKey.put("produit", "produitID");
            tableToPrimaryKey.put("commande", "commandeID");
            // Add other tables as needed

            boolean allMatch = true;
            // Verify each table/collection pair
            for (Map.Entry<String, String> entry : tableToCollection.entrySet()) {
                String tableName = entry.getKey();
                String collectionName = entry.getValue();

                // Get MySQL count as Long
                Long mysqlCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM " + tableName, Long.class);

                // Get Firestore count and convert to Long
                ApiFuture<QuerySnapshot> querySnapshot = firestore.collection(collectionName).get();
                int firestoreCount = 0;
                try {
                    firestoreCount = querySnapshot.get().size();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Error getting count from Firestore collection {}", collectionName, e);
                }

                logger.info("Table: {} -> MySQL count: {}, Firestore collection: {} -> count: {}", 
                    tableName, mysqlCount, collectionName, firestoreCount);

                // If counts don't match, investigate further for certain tables
                if ((long) mysqlCount != firestoreCount) {
                    allMatch = false;
                    logger.error("Count mismatch for {}: MySQL={}, Firestore={}, Difference={}", 
                        tableName, mysqlCount, firestoreCount, Math.abs((long) mysqlCount - firestoreCount));
                    
                    // For certain problematic tables, do deeper analysis
                    if (tableName.equals("produit") || tableName.equals("commande")) {
                        investigateMismatch(tableName, collectionName, tableToPrimaryKey.get(tableName));
                    }
                }
            }

            if (allMatch) {
                logger.info("Verification completed successfully! All record counts match.");
            } else {
                logger.warn("Verification found count mismatches. See logs for details.");
            }

            return RepeatStatus.FINISHED;
        };
    }
    
    /**
     * Investigates mismatches between MySQL and Firestore by comparing IDs
     */
    private void investigateMismatch(String tableName, String collectionName, String idColumn) {
        try {
            logger.info("Investigating mismatch for table {} / collection {}", tableName, collectionName);
            
            // Get all IDs from MySQL
            List<Long> mysqlIds = jdbcTemplate.queryForList(
                "SELECT " + idColumn + " FROM " + tableName, Long.class);
            Set<Long> mysqlIdSet = new HashSet<>(mysqlIds);
            
            // Get all document IDs from Firestore
            ApiFuture<QuerySnapshot> future = firestore.collection(collectionName).get();
            QuerySnapshot snapshot = future.get();
            Set<Long> firestoreIdSet = new HashSet<>();
            
            for (QueryDocumentSnapshot document : snapshot.getDocuments()) {
                try {
                    Object idObj = document.get("id");
                    if (idObj != null) {
                        if (idObj instanceof Long) {
                            firestoreIdSet.add((Long) idObj);
                        } else if (idObj instanceof Number) {
                            firestoreIdSet.add(((Number) idObj).longValue());
                        } else if (idObj instanceof String) {
                            firestoreIdSet.add(Long.parseLong((String) idObj));
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error parsing ID from document {}", document.getId(), e);
                }
            }
            
            // Find IDs in MySQL but not in Firestore
            Set<Long> missingInFirestore = new HashSet<>(mysqlIdSet);
            missingInFirestore.removeAll(firestoreIdSet);
            if (!missingInFirestore.isEmpty()) {
                logger.info("IDs in MySQL but missing in Firestore (first 10): {}", 
                    new ArrayList<>(missingInFirestore).subList(0, Math.min(10, missingInFirestore.size())));
                logger.info("Total missing in Firestore: {}", missingInFirestore.size());
            }
            
            // Find IDs in Firestore but not in MySQL
            Set<Long> extraInFirestore = new HashSet<>(firestoreIdSet);
            extraInFirestore.removeAll(mysqlIdSet);
            if (!extraInFirestore.isEmpty()) {
                logger.info("IDs in Firestore but missing in MySQL (first 10): {}", 
                    new ArrayList<>(extraInFirestore).subList(0, Math.min(10, extraInFirestore.size())));
                logger.info("Total extra in Firestore: {}", extraInFirestore.size());
                
                // Check a sample of these "extra" records to see what they contain
                if (!extraInFirestore.isEmpty()) {
                    Long sampleId = new ArrayList<>(extraInFirestore).get(0);
                    checkSampleDocument(collectionName, sampleId);
                }
            }
        } catch (Exception e) {
            logger.error("Error while investigating mismatch", e);
        }
    }
    
    /**
     * Check a sample document from Firestore to understand why it might exist there but not in MySQL
     */
    private void checkSampleDocument(String collectionName, Long id) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(collectionName)
                .whereEqualTo("id", id).get();
            QuerySnapshot snapshot = future.get();
            
            if (!snapshot.isEmpty()) {
                QueryDocumentSnapshot document = snapshot.getDocuments().get(0);
                logger.info("Sample 'extra' document in Firestore: {}", document.getData());
            }
        } catch (Exception e) {
            logger.error("Error checking sample document", e);
        }
    }
}
