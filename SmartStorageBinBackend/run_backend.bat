@echo off
echo ========================================================
echo     Smart Storage Bin - Spring Boot Backend Launcher
echo ========================================================
echo Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo Java 17 or higher is required to run the backend.
    pause
    exit /b 1
)

echo Starting Spring Boot Backend application on port 8080...
mvn spring-boot:run
pause
