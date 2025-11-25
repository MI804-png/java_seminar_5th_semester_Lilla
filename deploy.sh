#!/bin/bash
# Vehicle Registration System - Deployment Script
# Authors: Mikhael Nabil Salama Rezk (IHUTSC), Szabo Lilla

echo "=== Vehicle Registration System Deployment Script ==="
echo "Authors: Mikhael Nabil Salama Rezk (IHUTSC), Szabo Lilla"
echo "Target: rivendell.nje.hu:9443"
echo ""

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH"
    echo "Please install Java 17 or higher"
    exit 1
fi

echo "✅ Java found: $(java -version 2>&1 | head -n 1)"

# Build the WAR file
echo ""
echo "🔨 Building WAR file..."
if command -v mvn &> /dev/null; then
    mvn clean package -DskipTests
elif [[ -f "./mvnw" ]]; then
    ./mvnw clean package -DskipTests
else
    echo "❌ Neither Maven nor Maven Wrapper found"
    exit 1
fi

# Check if WAR file was created
if [[ -f "target/ihutsc-se.war" ]]; then
    echo "✅ WAR file created successfully: target/ihutsc-se.war"
    echo "📊 File size: $(du -h target/ihutsc-se.war | cut -f1)"
else
    echo "❌ WAR file creation failed"
    exit 1
fi

echo ""
echo "🚀 Deployment Instructions:"
echo "1. Upload target/ihutsc-se.war to rivendell.nje.hu using WinSCP/SCP"
echo "2. Copy to /opt/tomcat/webapps/ directory on the server"
echo "3. Wait for auto-deployment (check Tomcat logs)"
echo "4. Access: http://rivendell.nje.hu:9443/ihutsc-se/"
echo ""
echo "📝 Login credentials:"
echo "   Admin: admin / admin123"
echo "   User:  user / password"
echo ""
echo "🔗 API Base URL: http://rivendell.nje.hu:9443/ihutsc-se/api"
echo ""
echo "✨ Deployment ready! Good luck with your seminar presentation!"
