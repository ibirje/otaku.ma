# MySQL to Firestore Migration Tool

This tool migrates data from a MySQL database to Google Firestore for the Otakuma project.

## Setup

1. **Configure Environment Variables**:
   - Run `set-env.bat` (Windows) or `source set-env.ps1` (PowerShell) to set the required environment variables
   - Environment variables include database credentials, Firestore configuration, and performance settings

2. **Firebase Credentials**:
   - Place your Firebase service account key in `src/main/resources/firebase-credentials.json`
   - Make sure this file is in the `.gitignore` to prevent committing sensitive information

3. **Build the Project**:
   ```
   mvn clean package
   ```

## Usage

1. **Run the Migration**:
   - For full migration: `mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=migrationJob"`
   - For partial migration (only products and orders): `mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=partialMigrationJob"`

2. **Verify the Migration**:
   - Run verification job: `mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=verificationJob"`
   - Check logs for any discrepancies in record counts or data integrity issues

3. **Performance Tuning**:
   - Modify these environment variables to tune performance:
     - `DB_MAX_POOL_SIZE`: Database connection pool size (default: 10)
     - `DB_MIN_IDLE`: Minimum idle connections (default: 5)
     - `BATCH_CHUNK_SIZE`: Number of records processed in a chunk (default: 10)
     - `BATCH_THREAD_POOL_SIZE`: Number of concurrent threads (default: 4)

## Data Entities

The following entities are migrated:
- Admins, Admin Roles, Admin Rights
- Categories and Themes
- Products and Attributes
- Clients
- Orders
- Suppliers
- Stock Purchases
- SKUs (Stock Keeping Units)

## Troubleshooting

### Count Mismatches
If verification shows count mismatches between MySQL and Firestore:
- Check the detailed logs for specific IDs that are missing or extra
- Examine both databases for data inconsistencies
- Run the partial migration job to re-migrate specific entities

### Connection Issues
- Ensure your VPN is active if connecting to a remote database
- Check that your Firebase credentials are valid and have proper permissions

### Performance Issues
- Reduce the chunk size for large tables
- Increase thread pool size on machines with more CPU cores
- Monitor memory usage and adjust JVM heap size if needed