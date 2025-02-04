# Recipe Sharing Platform API Documentation

## Overview

The **Recipe Sharing Platform** allows users to share, manage, favorite, and search for recipes. It includes token-based authentication, CRUD operations for recipes, and image uploads. This document provides a comprehensive guide to the API endpoints and how to test them using Postman.

---

## Authentication

### Register User

- **Method**: `POST`
- **Endpoint**: `/auth/register`
- **Request Body**:
  ```json
  {
    "username": "user1",
    "password": "password123",
    "role": "USER"
  }
Response:
JSON

{
  "message": "User Registered Successfully"
}
Login User
Method: POST
Endpoint: /auth/login
Request Body:
JSON

{
  "username": "user1",
  "password": "password123"
}
Response:
JSON

{
  "token": "your_jwt_token"
}
Recipe API
Create Recipe
Method: POST
Endpoint: /api/recipes
Request Header:
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json  // Important!
Request Body:
JSON

{
  "name": "Italian Spaghetti",
  "ingredients": ["spaghetti", "tomato sauce", "basil"],
  "steps": ["Boil water", "Cook spaghetti", "Prepare sauce"],
  "tags": ["Italian", "Pasta"]
}
Response:
JSON

{
  "id": 1,
  "name": "Italian Spaghetti",
  "ingredients": ["spaghetti", "tomato sauce", "basil"],
  "steps": ["Boil water", "Cook spaghetti", "Prepare sauce"],
  "tags": ["Italian", "Pasta"],
  "imageUrl": "/uploads/your-image.jpg"
}
Get All Recipes (Paginated)
Method: GET
Endpoint: /api/recipes
Request Parameters:
page (default: 0)
size (default: 10)
Response:
JSON

[
  {
    "id": 1,
    "name": "Italian Spaghetti",
    "ingredients": ["spaghetti", "tomato sauce", "basil"],
    "steps": ["Boil water", "Cook spaghetti", "Prepare sauce"],
    "tags": ["Italian", "Pasta"],
    "imageUrl": "/uploads/your-image.jpg"
  }
]
Search Recipes
Method: GET
Endpoint: /api/recipes/search
Request Parameters:
ingredient (optional)
tag (optional)
Response: (Same structure as Get All Recipes)
Favorite Recipe API
Favorite a Recipe
Method: POST
Endpoint: /api/recipes/{recipeId}/favorite
Request Header:
Authorization: Bearer <JWT_TOKEN>
Response:
JSON

{
  "message": "Recipe favorited successfully."
}
Get All Favorited Recipes
Method: GET
Endpoint: /api/recipes/favorites
Request Header:
Authorization: Bearer <JWT_TOKEN>
Response: (Same structure as Get All Recipes)
Image Upload
Upload Recipe Image
Method: POST
Endpoint: /api/recipes/{recipeId}/upload-image
Request Header:
Authorization: Bearer <JWT_TOKEN>
Form Data:
file: (multipart file)
Response:
JSON

{
  "message": "Image uploaded successfully: /uploads/your-image.jpg"
}
Error Handling
The API uses standard HTTP status codes and JSON responses for error handling. Examples:

400 Bad Request:
JSON

{
  "error": "Invalid Request",
  "message": "The provided data is invalid."
}
401 Unauthorized:
JSON

{
  "error": "Unauthorized",
  "message": "Authentication is required to access this resource."
}
403 Forbidden:
JSON

{
  "error": "Forbidden",
  "message": "You do not have permission to perform this action."
}
404 Not Found:
JSON

{
  "error": "Not Found",
  "message": "The resource you requested could not be found."
}
Notes
Authentication: All requests, except for register and login, require a valid JWT token in the Authorization header.
Roles: (Add specific role information if applicable. E.g., "Users with the ROLE_ADMIN can access all endpoints.")
Image Upload: The uploaded images are stored in the /uploads directory on the server.
Technologies Used
Spring Boot (Backend)
JWT (Authentication and Authorization)
MySQL (Database)
Postman (API Testing)
How to Test Using Postman
This section provides examples of how to use Postman to test the API endpoints.  Remember to replace placeholders like {recipeId} and <your_jwt_token> with actual values.

1. Register a User
Method: POST
URL: http://localhost:7800/auth/register
Body: (JSON - see above)
2. Login User
Method: POST
URL: http://localhost:7800/auth/login
Body: (JSON - see above)
3. Favorite a Recipe
Method: POST
URL: http://localhost:7800/api/recipes/{recipeId}/favorite
Headers:
Authorization: Bearer <your_jwt_token>
4. Upload Recipe Image
Method: POST
URL: http://localhost:7800/api/recipes/{recipeId}/upload-image
Headers:
Authorization: Bearer <your_jwt_token>
Body: (Form Data - select a file)
