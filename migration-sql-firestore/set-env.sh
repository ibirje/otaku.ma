#!/bin/bash

# Database connection settings
export MYSQL_URL="jdbc:mysql://35.232.167.9/otaku_ma"
export MYSQL_USERNAME="root"
export MYSQL_PASSWORD="msakhen#600#"

# Firebase configuration
export FIREBASE_PROJECT_ID="otakuma-v2"
export FIREBASE_CREDENTIALS_PATH="firebase-credentials.json"

# Performance tuning
export DB_MAX_POOL_SIZE="10"
export DB_MIN_IDLE="5"
export BATCH_CHUNK_SIZE="10"
export BATCH_THREAD_POOL_SIZE="4"
export BATCH_MAX_THREAD_POOL_SIZE="8"

# Logging configuration
export SHOW_SQL="false"
export BATCH_LOG_LEVEL="INFO"
export APP_LOG_LEVEL="INFO"

echo "Environment variables set successfully!"
echo "Database: $MYSQL_URL"
echo "Firebase Project: $FIREBASE_PROJECT_ID"
echo "Thread Pool Size: $BATCH_THREAD_POOL_SIZE"