package com.otakuma.migration.config;

import com.google.cloud.firestore.Firestore;
import com.otakuma.migration.model.*;
import com.otakuma.migration.reader.*;
import com.otakuma.migration.writer.FirestoreItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final Firestore firestore;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ProductReader productReader;
    private final AdminReader adminReader;
    private final AdminRoleReader adminRoleReader;
    private final AdminDroitReader adminDroitReader;
    private final CategorieReader categorieReader;
    private final ThemeReader themeReader;
    private final ClientReader clientReader;
    private final CommandeReader commandeReader;
    private final AchatStockReader achatStockReader;
    private final SKUReader skuReader;
    private final AttributReader attributReader;
    private final FournisseurReader fournisseurReader;
    
    @Value("${batch.chunk-size:100}")
    private int chunkSize;
    
    @Value("${batch.thread-pool-size:4}")
    private int threadPoolSize;
    
    @Value("${batch.max-thread-pool-size:8}")
    private int maxThreadPoolSize;
    
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolSize);
        executor.setMaxPoolSize(maxThreadPoolSize);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("migration-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
    
    @Bean
    @StepScope
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[] {"processedItemCount", "skippedItemCount", "failedItemCount"});
        return listener;
    }

    @Bean
    public FirestoreItemWriter<Product> productWriter() {
        return new FirestoreItemWriter<>(firestore, "products");
    }

    @Bean
    public FirestoreItemWriter<Admin> adminWriter() {
        return new FirestoreItemWriter<>(firestore, "admins");
    }

    @Bean
    public FirestoreItemWriter<AdminRole> adminRoleWriter() {
        return new FirestoreItemWriter<>(firestore, "admin_roles");
    }

    @Bean
    public FirestoreItemWriter<AdminDroit> adminDroitWriter() {
        return new FirestoreItemWriter<>(firestore, "admin_droits");
    }

    @Bean
    public FirestoreItemWriter<Categorie> categorieWriter() {
        return new FirestoreItemWriter<>(firestore, "categories");
    }

    @Bean
    public FirestoreItemWriter<Theme> themeWriter() {
        return new FirestoreItemWriter<>(firestore, "themes");
    }

    @Bean
    public FirestoreItemWriter<Client> clientWriter() {
        return new FirestoreItemWriter<>(firestore, "clients");
    }

    @Bean
    public FirestoreItemWriter<Commande> commandeWriter() {
        return new FirestoreItemWriter<>(firestore, "commandes");
    }

    @Bean
    public FirestoreItemWriter<AchatStock> achatStockWriter() {
        return new FirestoreItemWriter<>(firestore, "achatStocks");
    }

    @Bean
    public FirestoreItemWriter<SKU> skuWriter() {
        return new FirestoreItemWriter<>(firestore, "skus");
    }

    @Bean
    public FirestoreItemWriter<Attribut> attributWriter() {
        return new FirestoreItemWriter<>(firestore, "attributes");
    }

    @Bean
    public FirestoreItemWriter<Fournisseur> fournisseurWriter() {
        return new FirestoreItemWriter<>(firestore, "fournisseurs");
    }

    @Bean
    public Step adminRoleMigrationStep() throws Exception {
        return new StepBuilder("adminRoleMigrationStep", jobRepository)
                .<AdminRole, AdminRole>chunk(chunkSize, transactionManager)
                .reader(adminRoleReader.reader())
                .writer(adminRoleWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(threadPoolSize)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step adminDroitMigrationStep() throws Exception {
        return new StepBuilder("adminDroitMigrationStep", jobRepository)
                .<AdminDroit, AdminDroit>chunk(chunkSize, transactionManager)
                .reader(adminDroitReader.reader())
                .writer(adminDroitWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(threadPoolSize)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step adminMigrationStep() throws Exception {
        return new StepBuilder("adminMigrationStep", jobRepository)
                .<Admin, Admin>chunk(chunkSize, transactionManager)
                .reader(adminReader.reader())
                .writer(adminWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(threadPoolSize)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step categorieMigrationStep() throws Exception {
        return new StepBuilder("categorieMigrationStep", jobRepository)
                .<Categorie, Categorie>chunk(chunkSize, transactionManager)
                .reader(categorieReader.reader())
                .writer(categorieWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(threadPoolSize)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step themeMigrationStep() throws Exception {
        return new StepBuilder("themeMigrationStep", jobRepository)
                .<Theme, Theme>chunk(chunkSize, transactionManager)
                .reader(themeReader.reader())
                .writer(themeWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(threadPoolSize)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step productMigrationStep() throws Exception {
        return new StepBuilder("productMigrationStep", jobRepository)
                .<Product, Product>chunk(20, transactionManager) // Smaller chunk size for products with lots of data
                .reader(productReader.reader())
                .writer(productWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(threadPoolSize)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step clientMigrationStep() throws Exception {
        return new StepBuilder("clientMigrationStep", jobRepository)
                .<Client, Client>chunk(chunkSize, transactionManager)
                .reader(clientReader.reader())
                .writer(clientWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(threadPoolSize)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step commandeMigrationStep() throws Exception {
        return new StepBuilder("commandeMigrationStep", jobRepository)
                .<Commande, Commande>chunk(20, transactionManager) 
                .reader(commandeReader.reader())
                .writer(commandeWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(threadPoolSize)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step achatStockMigrationStep() throws Exception {
        return new StepBuilder("achatStockMigrationStep", jobRepository)
                .<AchatStock, AchatStock>chunk(20, transactionManager) 
                .reader(achatStockReader.reader())
                .writer(achatStockWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(threadPoolSize)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step skuMigrationStep() throws Exception {
        return new StepBuilder("skuMigrationStep", jobRepository)
                .<SKU, SKU>chunk(chunkSize, transactionManager)
                .reader(skuReader.reader())
                .writer(skuWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(threadPoolSize)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step attributMigrationStep() throws Exception {
        return new StepBuilder("attributMigrationStep", jobRepository)
                .<Attribut, Attribut>chunk(chunkSize, transactionManager)
                .reader(attributReader.reader())
                .writer(attributWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(threadPoolSize)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step fournisseurMigrationStep() throws Exception {
        return new StepBuilder("fournisseurMigrationStep", jobRepository)
                .<Fournisseur, Fournisseur>chunk(chunkSize, transactionManager)
                .reader(fournisseurReader.reader())
                .writer(fournisseurWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(threadPoolSize)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Flow adminRoleAndDroitFlow() throws Exception {
        return new FlowBuilder<Flow>("adminRoleAndDroitFlow")
                .split(taskExecutor())
                .add(
                    new FlowBuilder<Flow>("adminRoleFlow")
                        .start(adminRoleMigrationStep())
                        .build(),
                    new FlowBuilder<Flow>("adminDroitFlow")
                        .start(adminDroitMigrationStep())
                        .build()
                )
                .build();
    }

    @Bean
    public Flow categorieThemeFlow() throws Exception {
        return new FlowBuilder<Flow>("categorieThemeFlow")
                .split(taskExecutor())
                .add(
                    new FlowBuilder<Flow>("categorieFlow")
                        .start(categorieMigrationStep())
                        .build(),
                    new FlowBuilder<Flow>("themeFlow")
                        .start(themeMigrationStep())
                        .build()
                )
                .build();
    }

    @Bean
    public Flow clientAndCommandeFlow() throws Exception {
        return new FlowBuilder<Flow>("clientAndCommandeFlow")
                .split(taskExecutor())
                .add(
                    new FlowBuilder<Flow>("clientFlow")
                        .start(clientMigrationStep())
                        .build(),
                    new FlowBuilder<Flow>("commandeFlow")
                        .start(commandeMigrationStep())
                        .build()
                )
                .build();
    }

    @Bean
    public Flow inventoryFlow() throws Exception {
        return new FlowBuilder<Flow>("inventoryFlow")
                .start(fournisseurMigrationStep())
                .next(new FlowBuilder<Flow>("stockFlow")
                    .split(taskExecutor())
                    .add(
                        new FlowBuilder<Flow>("skuFlow")
                            .start(skuMigrationStep())
                            .build(),
                        new FlowBuilder<Flow>("achatStockFlow")
                            .start(achatStockMigrationStep())
                            .build()
                    )
                    .build())
                .build();
    }

    @Bean
    public Flow productAndAttributeFlow() throws Exception {
        return new FlowBuilder<Flow>("productAndAttributeFlow")
                .split(taskExecutor())
                .add(
                    new FlowBuilder<Flow>("productFlow")
                        .start(productMigrationStep())
                        .build(),
                    new FlowBuilder<Flow>("attributFlow")
                        .start(attributMigrationStep())
                        .build()
                )
                .build();
    }

    @Bean
    public Job migrationJob() throws Exception {
        return new JobBuilder("migrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(adminRoleAndDroitFlow())
                .next(adminMigrationStep())
                .next(categorieThemeFlow())
                .next(productAndAttributeFlow())
                .next(clientAndCommandeFlow())
                .next(inventoryFlow())
                .end()
                .build();
    }

    @Bean
    public Job partialMigrationJob() throws Exception {
        return new JobBuilder("partialMigrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(productMigrationStep())
                .next(commandeMigrationStep())
                .build();
    }

    // Add a synchronous task executor for job launcher
    @Bean
    public TaskExecutor jobLauncherTaskExecutor() {
        return new TaskExecutor() {
            @Override
            public void execute(Runnable task) {
                task.run();
            }
        };
    }

    // Configure a synchronous job launcher
    @Bean
    public JobLauncher syncJobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(jobLauncherTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}