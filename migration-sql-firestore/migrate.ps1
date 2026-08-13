# Load environment variables
Write-Host "Setting environment variables..."
. .\set-env.ps1  # Using the PowerShell environment setup file

Write-Host "Starting migration..."
mvn spring-boot:run