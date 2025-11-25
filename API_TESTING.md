# API Testing Scripts for Vehicle Registration System

## Base Configuration
```bash
BASE_URL="http://rivendell.nje.hu:9443/ihutsc-se/api"
# For local testing: BASE_URL="http://localhost:8080/api"
```

## Person API Tests

### 1. Get All Persons
```bash
curl -X GET "${BASE_URL}/persons" \
  -H "Accept: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

### 2. Get Person by ID
```bash
curl -X GET "${BASE_URL}/persons/1" \
  -H "Accept: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

### 3. Create New Person
```bash
curl -X POST "${BASE_URL}/persons" \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -d '{
    "name": "API Test User",
    "regnumber": "API001",
    "height": 175
  }'
```

### 4. Update Person
```bash
curl -X PUT "${BASE_URL}/persons/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -d '{
    "name": "Updated Name",
    "regnumber": "ABC123",
    "height": 180
  }'
```

### 5. Delete Person
```bash
curl -X DELETE "${BASE_URL}/persons/5" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

## Vehicle API Tests

### 1. Get All Vehicles
```bash
curl -X GET "${BASE_URL}/vehicles" \
  -H "Accept: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

### 2. Get Vehicle by Registration Number
```bash
curl -X GET "${BASE_URL}/vehicles/ABC123" \
  -H "Accept: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

### 3. Create New Vehicle
```bash
curl -X POST "${BASE_URL}/vehicles" \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -d '{
    "regnum": "TEST01",
    "brand": "Toyota",
    "color": "silver"
  }'
```

### 4. Update Vehicle
```bash
curl -X PUT "${BASE_URL}/vehicles/TEST01" \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -d '{
    "regnum": "TEST01",
    "brand": "Honda",
    "color": "black"
  }'
```

### 5. Delete Vehicle
```bash
curl -X DELETE "${BASE_URL}/vehicles/TEST01" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

## Statistics API Tests

### 1. Vehicle Count by Brand
```bash
curl -X GET "${BASE_URL}/stats/vehicles-by-brand" \
  -H "Accept: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

### 2. Vehicle Count by Color
```bash
curl -X GET "${BASE_URL}/stats/vehicles-by-color" \
  -H "Accept: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

## Postman Collection

### Environment Variables
```json
{
  "name": "Vehicle Registration API",
  "values": [
    {
      "key": "baseUrl",
      "value": "http://rivendell.nje.hu:9443/ihutsc-se/api",
      "enabled": true
    },
    {
      "key": "username",
      "value": "admin",
      "enabled": true
    },
    {
      "key": "password", 
      "value": "admin123",
      "enabled": true
    }
  ]
}
```

### Request Collection
1. **Get Persons**: GET {{baseUrl}}/persons
2. **Create Person**: POST {{baseUrl}}/persons
3. **Update Person**: PUT {{baseUrl}}/persons/{{personId}}
4. **Delete Person**: DELETE {{baseUrl}}/persons/{{personId}}
5. **Get Vehicles**: GET {{baseUrl}}/vehicles
6. **Vehicle Stats**: GET {{baseUrl}}/stats/vehicles-by-brand

### Authentication Setup
- Type: Basic Auth
- Username: {{username}}
- Password: {{password}}

## Expected Responses

### Successful Person Creation (201 Created)
```json
{
  "id": 5,
  "name": "API Test User",
  "regnumber": "API001", 
  "height": 175,
  "phones": [],
  "vehicle": null
}
```

### Vehicle Statistics Response (200 OK)
```json
[
  ["Ford", 2],
  ["Skoda", 1],
  ["BMW", 1]
]
```

### Error Response (400 Bad Request)
```json
{
  "timestamp": "2025-11-25T10:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Registration number already exists",
  "path": "/api/persons"
}
```

## Testing Workflow

### 1. Basic Connectivity
```bash
# Test if API is accessible
curl -I "${BASE_URL}/persons"
# Should return 401 Unauthorized (authentication required)
```

### 2. Authentication Test
```bash
# Test with admin credentials
curl -X GET "${BASE_URL}/persons" \
  -u admin:admin123 \
  -H "Accept: application/json"
# Should return 200 OK with person list
```

### 3. Full CRUD Test
```bash
# Create -> Read -> Update -> Delete sequence
# 1. Create new person
# 2. Verify creation with GET
# 3. Update the person
# 4. Verify update with GET  
# 5. Delete the person
# 6. Verify deletion (404 Not Found)
```

### 4. Error Handling Test
```bash
# Test validation errors
curl -X POST "${BASE_URL}/persons" \
  -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "regnumber": "",
    "height": -1
  }'
# Should return 400 Bad Request with validation errors
```

## Performance Testing

### Load Test Example
```bash
# Simple load test with multiple concurrent requests
for i in {1..10}; do
  curl -X GET "${BASE_URL}/persons" -u admin:admin123 &
done
wait
echo "Load test completed"
```

## Documentation Screenshots

For the PDF documentation, include screenshots of:
1. **cURL command execution** with responses
2. **Postman interface** showing successful API calls
3. **Browser Network tab** showing API responses
4. **Database state** before and after API operations
5. **Error responses** for validation testing

Remember to test both local (localhost:8080) and production (rivendell.nje.hu:9443) environments!
