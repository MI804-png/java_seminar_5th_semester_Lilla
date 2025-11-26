# Postman Testing Instructions

## Import the Collection

1. **Open Postman** (download from https://www.postman.com/downloads/ if not installed)
2. Click **Import** button (top left)
3. Click **Upload Files**
4. Select `Vehicle_Registration_API.postman_collection.json`
5. Click **Import**

## Authentication is Pre-configured

The collection has HTTP Basic Auth already set up:
- **Username**: `admin`
- **Password**: `admin123`

## Test the Following Endpoints

### 1. GET All Persons
- URL: `http://localhost:8080/api/persons`
- Expected: Array of all persons with their phone numbers

### 2. GET Person by ID
- URL: `http://localhost:8080/api/persons/1`
- Expected: Single person object (John Smith)

### 3. GET All Vehicles
- URL: `http://localhost:8080/api/vehicles`
- Expected: Array of all vehicles

### 4. GET Vehicle by Registration Number
- URL: `http://localhost:8080/api/vehicles/ABC123`
- Expected: Single vehicle object (Ford)

### 5. GET Vehicles by Brand Statistics
- URL: `http://localhost:8080/api/stats/vehicles-by-brand`
- Expected: Array of [brand, count] pairs

### 6. POST New Person (Optional)
- URL: `http://localhost:8080/api/persons`
- Body: JSON with regnumber, fname, lname, height
- Tests create functionality

### 7. POST New Vehicle (Optional)
- URL: `http://localhost:8080/api/vehicles`
- Body: JSON with regnum, brand, color
- Tests create functionality

## Screenshots to Capture

**For your PDF documentation, capture at least 2-3 screenshots showing:**

1. **GET request with response** - Show the full Postman window with:
   - Request URL in address bar
   - Authorization tab showing Basic Auth
   - Response body with JSON data
   - Status code (200 OK)

2. **Another GET request** - Different endpoint showing variety

3. **POST request (optional)** - If you test creating a new record

4. **Collection overview** - Show all endpoints in the left sidebar

## Tips

- Each request will show response time and size
- The **Body** tab shows the JSON response
- The **Status** shows `200 OK` for successful requests
- All authentication is automatic (inherited from collection)

## After Testing

Once you've tested and captured screenshots:
1. Save the screenshots for the PDF documentation
2. We'll create another Git commit documenting REST API testing completion
3. Then proceed to build the WAR file
