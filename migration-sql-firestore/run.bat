@echo off
setlocal

REM Load environment variables
call set-env.bat

if "%1"=="" (
    echo Usage: run.bat [full^|products^|orders^|verify]
    goto :end
)

if "%1"=="full" (
    echo Running full migration...
    mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=migrationJob"
) else if "%1"=="products" (
    echo Running products migration...
    mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=productMigrationJob"
) else if "%1"=="orders" (
    echo Running orders migration...
    mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=commandeMigrationJob"
) else if "%1"=="partial" (
    echo Running partial migration (products and orders)...
    mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=partialMigrationJob"
) else if "%1"=="verify" (
    echo Running verification...
    mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=verificationJob"
) else (
    echo Unknown command: %1
    echo Usage: run.bat [full^|products^|orders^|verify]
)

:end
endlocal