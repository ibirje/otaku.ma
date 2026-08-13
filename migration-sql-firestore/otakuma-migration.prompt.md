# Otakuma MySQL to Firestore Migration Project

<task>
Migrate data from a legacy MySQL e-commerce database to Google Cloud Firestore while maintaining data integrity and relationships.
</task>

<context>
This is a data migration project for an e-commerce platform specializing in anime and manga merchandise. The system needs to transition from a traditional MySQL database to a more scalable Firestore solution.
</context>

## Project Overview

<objective>
Convert relational data model to document-based structure while preserving:
- Data relationships
- Business logic
- Data integrity
</objective>

## Technical Stack

<dependencies>
- Spring Boot
- Spring Batch
- MySQL Connector
- Google Cloud Firestore
- Lombok
</dependencies>

## Core Components

<configuration>
1. Firebase Admin SDK:
   ```
   Location: src/main/resources/
   Type: Service Account Key (JSON)
   Purpose: Authentication and authorization
   ```

2. Database Configuration:
   ```properties
   # MySQL Configuration Template (Replace with actual values)
   spring.datasource.url=jdbc:mysql://${MYSQL_HOST}/${MYSQL_DATABASE}
   spring.datasource.username=${MYSQL_USERNAME}
   spring.datasource.password=${MYSQL_PASSWORD}
   spring.batch.jdbc.initialize-schema=always
   ```
</configuration>

## Project Structure

<components>
### Data Models
```
- AchatStock (Purchase Orders)
- Admin (Administrators)
- Product (Products)
- Client (Customers)
- Commande (Orders)
- SKU (Stock Keeping Units)
- Theme (Product Themes)
- Categorie (Categories)
```

### Processing Components
```
Readers/
├─ JdbcCursorItemReader implementations
└─ MySQL data extraction

Writers/
├─ FirestoreItemWriter
└─ Batch writing to Firestore
```

### Configuration
```
config/
├─ BatchConfig (Spring Batch job configuration)
├─ FirestoreConfig (Firestore connection)
└─ VerificationConfig (Data validation)
```
</components>

## Migration Statistics

<results>
Successfully Migrated Collections:
```
├─ Categories: 50 records
├─ Purchase Orders: 27 records
├─ Admin Roles: 7 records
├─ Attributes: 7 records
├─ Admins: 3 records
├─ Clients: 152 records
├─ Suppliers: 11 records
├─ Themes: 37 records
├─ Admin Rights: 40 records
└─ SKUs: 429 records
```

Data Discrepancies:
```
Products:
├─ MySQL: 360 records
└─ Firestore: 376 records

Orders:
├─ MySQL: 89 records
└─ Firestore: 106 records
```
</results>

## Migration Process

<steps>
1. Data Extraction:
   ```
   Source: MySQL tables
   Method: JdbcCursorItemReader
   Process: Sequential read with batch size optimization
   ```

2. Data Transformation:
   ```
   - Relational to document model conversion
   - Data type mapping
   - Relationship preservation
   ```

3. Data Loading:
   ```
   Destination: Firestore collections
   Method: Batch writing
   Validation: Count verification
   ```
</steps>

## Execution Guide

<execution>
1. Environment Setup:
   ```
   - Configure environment variables
   - Verify database connectivity
   - Check Firestore credentials
   ```

2. Run Migration:
   ```
   Command: mvn spring-boot:run
   Monitoring: Console output
   Verification: Automatic count validation
   ```
</execution>

## Dependencies

<dependencies_xml>
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-batch</artifactId>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <dependency>
        <groupId>com.google.cloud</groupId>
        <artifactId>google-cloud-firestore</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```
</dependencies_xml>

## Investigation Notes

<investigation>
Areas requiring further investigation:
1. Product count discrepancy (16 additional records in Firestore)
2. Order count discrepancy (17 additional records in Firestore)
3. Data validation implementation
4. Rollback mechanism
5. Automated testing suite
</investigation>

## LLM Instructions

<instructions>
When working with this project:
1. Never expose sensitive credentials
2. Use environment variables for configuration
3. Validate all data transformations
4. Check for data integrity after migration
5. Handle errors gracefully
</instructions>