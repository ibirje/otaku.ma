#!/bin/bash

# Load environment variables
source ./set-env.sh

if [ -z "$1" ]; then
    echo "Usage: ./run.sh [full|products|orders|partial|verify]"
    exit 1
fi

case $1 in
    "full")
        echo "Running full migration..."
        mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=migrationJob"
        ;;
    "products")
        echo "Running products migration..."
        mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=productMigrationJob"
        ;;
    "orders")
        echo "Running orders migration..."
        mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=commandeMigrationJob"
        ;;
    "partial")
        echo "Running partial migration (products and orders)..."
        mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=partialMigrationJob"
        ;;
    "verify")
        echo "Running verification..."
        mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=verificationJob"
        ;;
    *)
        echo "Unknown command: $1"
        echo "Usage: ./run.sh [full|products|orders|partial|verify]"
        exit 1
        ;;
esac