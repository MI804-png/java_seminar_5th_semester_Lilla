# Build and Deployment Guide

## Production Build

**Build Date**: November 27, 2025  
**Build Status**: ✅ SUCCESS  
**Build Time**: 01:11 min

### WAR File Details

- **Filename**: `ihutsc-se.war`
- **Size**: 56,750,942 bytes (56.7 MB)
- **Location**: `target/ihutsc-se.war`
- **Type**: Spring Boot executable WAR with embedded Tomcat

### Build Command

```bash
mvn clean package -DskipTests
```

### Build Configuration

- **Spring Boot Version**: 2.7.18
- **Java Version**: 17
- **Maven Version**: 3.9.5
- **Packaging**: WAR (Web Application Archive)

### Included Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Thymeleaf
- Spring Boot Starter Validation
- MySQL Connector/J
- Thymeleaf Extras Spring Security 5
- Jackson Databind (JSON serialization)
- Bootstrap 5 & jQuery (frontend)

## Deployment Instructions

### Option 1: Run Locally with Maven

```bash
mvn spring-boot:run
```

Application will start on: http://localhost:8080

### Option 2: Run WAR File Directly

```bash
java -jar target/ihutsc-se.war
```

### Option 3: Deploy to Linux Server

1. Transfer WAR file to server:
   ```bash
   scp target/ihutsc-se.war user@server:/path/to/deployment/
   ```

2. On the server, run:
   ```bash
   java -jar ihutsc-se.war
   ```

3. Configure as systemd service for automatic startup (optional)

### Database Configuration

Ensure MySQL database is configured with:
- **Database Name**: `vehiclereg`
- **Username**: `root`
- **Password**: (as configured in application.properties)
- **Port**: 3306

The application will automatically create tables on first startup using Hibernate DDL.

## Application Access

### Web Interface
- **URL**: http://localhost:8080
- **Admin User**: admin / admin123
- **Regular User**: user / user123

### REST API Endpoints
- **Base URL**: http://localhost:8080/api
- **Authentication**: HTTP Basic Auth
- **Endpoints**:
  - GET `/api/persons` - List all persons
  - GET `/api/persons/{id}` - Get person by ID
  - GET `/api/vehicles` - List all vehicles
  - GET `/api/vehicles/{regnum}` - Get vehicle by registration number
  - GET `/api/stats/vehicles-by-brand` - Get vehicle count by brand
  - POST `/api/persons` - Create new person
  - POST `/api/vehicles` - Create new vehicle

## Testing Results

### cURL Testing
✅ All 5 GET endpoints tested successfully  
✅ JSON responses clean (no circular references)  
✅ Results documented in API_TEST_RESULTS.txt

### Postman Testing
✅ Comprehensive collection created with 7 endpoints  
✅ HTTP Basic Auth pre-configured  
✅ Published workspace: https://www.postman.com/insertcheesyline

## Technical Stack

- **Backend**: Java 17, Spring Boot 2.7.18
- **Frontend**: Thymeleaf, Bootstrap 5, jQuery
- **Database**: MySQL 8.0, JPA/Hibernate
- **Security**: Spring Security with HTTP Basic Auth
- **Build Tool**: Maven 3.9.5
- **Server**: Embedded Apache Tomcat 9.0.83

## Version Control

**Repository**: java_seminar_5th_semester_Lilla  
**Owner**: MI804-png  
**Branch**: main  
**Total Commits**: 5

### Commit History
1. Initial project setup with Spring Boot configuration
2. Add HTTP Basic Authentication and Security
3. Fix JSON circular reference in REST API entities
4. Complete REST API testing with Postman collection
5. Build production WAR file for deployment

## Next Steps

1. ✅ Build production WAR file - COMPLETED
2. ⏳ Create comprehensive PDF documentation (15+ pages)
3. ⏳ Include screenshots of all 14 requirements
4. ⏳ Push final commits to GitHub
5. ⏳ Deploy to Linux server (optional)

## Support

For issues or questions, refer to:
- README.md - Project overview
- API_TESTING.md - API endpoint documentation
- POSTMAN_TESTING_GUIDE.md - Postman setup instructions
- DEPLOYMENT.md - Deployment details
