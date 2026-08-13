package com.otakuma.migration.writer;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
public class FirestoreItemWriter<T> implements ItemWriter<T> {
    private static final Logger logger = LoggerFactory.getLogger(FirestoreItemWriter.class);
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;
    private static final int BATCH_SIZE = 20; // Firestore batch size limit is 500, we use a smaller value for safety
    private static final int WRITE_TIMEOUT_SECONDS = 30;
    
    private final Firestore firestore;
    private final String collectionName;

    @Override
    public void write(Chunk<? extends T> chunk) throws Exception {
        List<T> items = new ArrayList<>(chunk.getItems());
        List<T> failedItems = new ArrayList<>();
        Map<T, Exception> failures = new HashMap<>();
        
        // Process items in batches
        for (int i = 0; i < items.size(); i += BATCH_SIZE) {
            int end = Math.min(items.size(), i + BATCH_SIZE);
            List<T> batchItems = items.subList(i, end);
            
            int retryCount = 0;
            boolean success = false;
            Exception lastException = null;
            
            while (!success && retryCount < MAX_RETRIES) {
                WriteBatch batch = firestore.batch();
                Map<String, T> documentIdToItem = new HashMap<>();
                
                try {
                    // Add all items to the batch
                    for (T item : batchItems) {
                        try {
                            String documentId = getDocumentId(item);
                            batch.set(firestore.collection(collectionName).document(documentId), item);
                            documentIdToItem.put(documentId, item);
                        } catch (Exception e) {
                            logger.error("Failed to prepare item for batch: {}", item, e);
                            failedItems.add(item);
                            failures.put(item, e);
                        }
                    }
                    
                    // If we have items to write, commit the batch
                    if (!documentIdToItem.isEmpty()) {
                        ApiFuture<List<WriteResult>> future = batch.commit();
                        List<WriteResult> results = future.get(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                        
                        logger.info("Batch saved successfully! Saved {} items to collection {}",
                                results.size(), collectionName);
                        
                        // Log sample items (first 3)
                        List<String> documentIds = new ArrayList<>(documentIdToItem.keySet());
                        int sampleSize = Math.min(3, documentIds.size());
                        for (int j = 0; j < sampleSize; j++) {
                            logger.debug("Sample item saved: {} - {}", 
                                documentIds.get(j), 
                                documentIdToItem.get(documentIds.get(j)));
                        }
                    }
                    
                    success = true;
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    lastException = e;
                    retryCount++;
                    
                    if (retryCount < MAX_RETRIES) {
                        logger.warn("Failed to save batch, attempt {}/{}. Retrying in {} ms... Error: {}", 
                            retryCount, MAX_RETRIES, RETRY_DELAY_MS, e.getMessage());
                        TimeUnit.MILLISECONDS.sleep(RETRY_DELAY_MS * retryCount); // Increase backoff time with each retry
                    } else {
                        logger.error("Failed to save batch after {} retries", MAX_RETRIES, e);
                        // Add all items in the batch to failed items
                        failedItems.addAll(batchItems);
                        for (T item : batchItems) {
                            if (!failures.containsKey(item)) {
                                failures.put(item, e);
                            }
                        }
                    }
                }
            }
        }
        
        // Report failures
        if (!failedItems.isEmpty()) {
            logger.error("Failed to save {} out of {} items", failedItems.size(), items.size());
            for (Map.Entry<T, Exception> entry : failures.entrySet()) {
                logger.error("Failed item: {} - Error: {}", entry.getKey(), entry.getValue().getMessage());
            }
            
            // If all items failed, throw exception
            if (failedItems.size() == items.size()) {
                throw new RuntimeException("All items failed to write to Firestore");
            }
        }
    }

    private String getDocumentId(T item) {
        try {
            String className = item.getClass().getSimpleName();
            Method[] methods = item.getClass().getMethods();
            
            // First try to find getId method
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.equals("getId") || 
                    methodName.equals("getID")) {
                    Object result = method.invoke(item);
                    if (result != null) {
                        return String.valueOf(result);
                    }
                }
            }
            
            // If not found, look for a getter method that ends with "ID" or "Id"
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.startsWith("get") && 
                    (methodName.endsWith("ID") || methodName.endsWith("Id"))) {
                    Object result = method.invoke(item);
                    if (result != null) {
                        return String.valueOf(result);
                    }
                }
            }
            
            throw new RuntimeException("Could not find ID field for class: " + className);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get document ID for item: " + item, e);
        }
    }
}