@echo off
REM Automated deployment script for rivendell.nje.hu
echo ======================================
echo Uploading WAR to Tomcat Server
echo ======================================
echo.

set WAR_FILE=c:\java_seminar\java_seminar\vehicle-registration-app\target\ihutsc-se.war
set USERNAME=student208
set HOST=rivendell.nje.hu
set REMOTE_PATH=/opt/tomcat/webapps/

echo WAR File: %WAR_FILE%
echo Server: %HOST%
echo Username: %USERNAME%
echo.
echo Uploading... (You will be prompted for password: abc123456)
echo.

scp -P 22 "%WAR_FILE%" %USERNAME%@%HOST%:%REMOTE_PATH%

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ======================================
    echo Upload successful!
    echo ======================================
    echo.
    echo Your application should be deploying now.
    echo Check: http://rivendell.nje.hu:9443/ihutsc-se/
    echo.
) else (
    echo.
    echo ======================================
    echo Upload failed!
    echo ======================================
    echo.
    echo Please check:
    echo 1. Password is correct: abc123456
    echo 2. You have permissions to /opt/tomcat/webapps/
    echo 3. Server is accessible
    echo.
)

pause
