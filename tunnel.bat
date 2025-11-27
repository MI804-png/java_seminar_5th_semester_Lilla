@echo off
REM SSH Tunnel to access Tomcat via localhost
echo ========================================
echo Creating SSH Tunnel to rivendell.nje.hu
echo ========================================
echo.
echo This will forward port 9443 to your local machine.
echo After tunnel is established, access your app at:
echo.
echo   http://localhost:9443/ihutsc-se/
echo.
echo Press Ctrl+C to close the tunnel when done.
echo.
echo You will be prompted for password: abc123456
echo.

ssh -L 9443:localhost:9443 student208@rivendell.nje.hu

pause
