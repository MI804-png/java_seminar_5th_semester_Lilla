@echo off
REM Vehicle Registration System - Windows Deployment Script
REM Authors: Mikhael Nabil Salama Rezk (IHUTSC), Szabo Lilla

echo === Vehicle Registration System Deployment Script ===
echo Authors: Mikhael Nabil Salama Rezk (IHUTSC), Szabo Lilla
echo Target: rivendell.nje.hu:9443
echo.

REM Check if Java is available
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

echo ✅ Java found
java -version 2>&1 | findstr "version"

REM Build the WAR file
echo.
echo 🔨 Building WAR file...
if exist "mvnw.cmd" (
    call mvnw.cmd clean package -DskipTests
) else (
    mvn clean package -DskipTests
)

REM Check if WAR file was created
if exist "target\ihutsc-se.war" (
    echo ✅ WAR file created successfully: target\ihutsc-se.war
    for %%A in (target\ihutsc-se.war) do echo 📊 File size: %%~zA bytes
) else (
    echo ❌ WAR file creation failed
    pause
    exit /b 1
)

echo.
echo 🚀 Deployment Instructions:
echo 1. Upload target\ihutsc-se.war to rivendell.nje.hu using WinSCP
echo 2. Copy to /opt/tomcat/webapps/ directory on the server
echo 3. Wait for auto-deployment (check Tomcat logs)
echo 4. Access: http://rivendell.nje.hu:9443/ihutsc-se/
echo.
echo 📝 Login credentials:
echo    Admin: admin / admin123
echo    User:  user / password
echo.
echo 🔗 API Base URL: http://rivendell.nje.hu:9443/ihutsc-se/api
echo.
echo ✨ Deployment ready! Good luck with your seminar presentation!
echo.
pause
