# Vehicle Registration System

A comprehensive Java Spring Boot web application for managing vehicle registrations, owners, and contact information.

## Project Information

- **Team Members**: Mikhael Nabil Salama Rezk (IHUTSC), Szabo Lilla
- **University**: John von Neumann University, Kecskemét
- **Course**: Java Applications Seminar
- **Technology Stack**: Java 17, Spring Boot 3.2.0, MySQL, Thymeleaf, Chart.js

## Features

### 1. Authentication System ✓
- User registration and login
- Role-based access control (Visitor, Registered, Admin)
- Spring Security integration
- Password encryption

### 2. Database Management ✓
- Person records with name, registration number, height
- Vehicle information (brand, color, registration)
- Phone numbers linked to persons
- MySQL database with JPA/Hibernate

### 3. Contact System ✓
- Contact form with server-side validation
- Message persistence to database
- Email and subject validation

### 4. Messages Management ✓ (Registered Users Only)
- View all contact messages in descending order
- Display sending timestamps
- Message filtering and search capabilities

### 5. Statistics & Charts ✓
- Interactive charts using Chart.js
- Vehicle distribution by brand and color
- Database statistics visualization

### 6. CRUD Operations ✓
- Complete Person management (Create, Read, Update, Delete)
- Form validation and error handling
- Data export capabilities

### 7. RESTful API ✓
- Full REST endpoints for persons and vehicles
- JSON data exchange
- API authentication required

### 8. Admin Panel ✓ (Admin Only)
- System dashboard
- User management interface
- Database statistics

### 9. Responsive Design ✓
- Modern CSS with responsive layout
- Mobile-friendly interface
- Professional theme implementation

## Database Schema

### Tables:
- `persons` - Personal information and registration numbers
- `vehicles` - Vehicle details linked by registration number
- `phones` - Phone numbers associated with persons
- `users` - Authentication and authorization
- `contact_messages` - Contact form submissions

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.0, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript, Chart.js
- **Database**: MySQL 8.0
- **Build Tool**: Maven
- **Deployment**: WAR file for Tomcat

## Installation & Setup

### Local Development
```bash
git clone <repository-url>
cd vehicle-registration-app
mvn spring-boot:run
```

### Production Deployment
```bash
mvn clean package
# Generates ihutsc-se.war in target/
# Deploy to rivendell.nje.hu:9443/opt/tomcat/webapps/
```

## Configuration

### Database Settings
- **Local**: `src/test/resources/application.properties`
- **Production**: `src/main/resources/application.properties`

### Production Database
- Host: rivendell.nje.hu
- Database: db208
- Username: studb208
- Port: 9443

## API Documentation

### Endpoints
- `GET /api/persons` - List all persons
- `POST /api/persons` - Create new person
- `PUT /api/persons/{id}` - Update person
- `DELETE /api/persons/{id}` - Delete person
- `GET /api/vehicles` - List all vehicles
- `GET /api/stats/vehicles-by-brand` - Vehicle brand statistics
- `GET /api/stats/vehicles-by-color` - Vehicle color statistics

### Authentication Required
All API endpoints require user authentication (Registered or Admin role).

## Testing

### cURL Examples
```bash
# Get all persons
curl -X GET "http://rivendell.nje.hu:9443/ihutsc-se/api/persons" \
  -H "Content-Type: application/json"

# Create new person
curl -X POST "http://rivendell.nje.hu:9443/ihutsc-se/api/persons" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","regnumber":"TEST01","height":175}'
```

## User Accounts

### Default Accounts
- **Admin**: admin / admin123 (Full access)
- **Test User**: user / user123 (Registered access)

### User Roles
- **Visitor**: Public pages only
- **Registered**: Access to messages and CRUD operations
- **Admin**: Full system access including admin panel

## Development Milestones

1. ✅ Initial project setup and database schema
2. ✅ Authentication and security implementation
3. ✅ Core CRUD operations and validation
4. ✅ REST API development
5. ✅ Frontend templates and responsive design
6. ✅ Chart.js integration and statistics
7. ✅ Admin panel and user management
8. ⏳ WAR deployment to production server
9. ⏳ Testing and documentation finalization

## Git Repository

**GitHub URL**: [To be provided]

### Commit History
- Initial project structure and configuration
- Database entities and repositories
- Authentication and security setup
- REST API implementation
- Frontend templates and styling

## License

This project is developed for educational purposes as part of the Java Applications Seminar course at John von Neumann University.

## Contact

For questions or support, please contact:
- Mikhael Nabil Salama Rezk - mikhael.rezk@nje.hu
- Szabo Lilla - lilla.szabo@nje.hu

---

© 2025 Vehicle Registration System - John von Neumann University
