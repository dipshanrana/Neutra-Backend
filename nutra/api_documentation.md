# Nutra App API Documentation

## Base URL
Assuming local development, the base URL is `http://localhost:8080`.

---

## Authentication Endpoints

### 1. User Login
- **Endpoint**: `POST /auth/login`
- **Description**: Authenticate a user and receive a JWT token.
- **Request Body**:
  ```json
  {
    "username": "johndoe",
    "password": "password123"
  }
  ```
- **Response** (200 OK):
  ```json
  {
    "id": 1,
    "username": "johndoe",
    "role": "CUSTOMER",
    "jwtToken": "eyJhbGciOiJIUzI1NiJ9..."
  }
  ```

### 2. User Signup
- **Endpoint**: `POST /auth/signup`
- **Description**: Register a new user and receive a JWT token. Automatically assigns the `CUSTOMER` role.
- **Request Body**:
  ```json
  {
    "username": "janedoe",
    "password": "password123"
  }
  ```
- **Response** (200 OK):
  ```json
  {
    "id": 2,
    "username": "janedoe",
    "role": "CUSTOMER",
    "jwtToken": "eyJhbGciOiJIUzI1NiJ9..."
  }
  ```

> **Note:** A static default Admin account is automatically seeded into the database upon the first startup. 
> - **Username:** `admin@admin.com`
> - **Password:** `admin123`

---

## Admin Endpoints
*Must be authenticated as an `ADMIN`.*

### 1. Create New Admin (Admin Only)
- **Endpoint**: `POST /admin/users/admin`
- **Description**: Create a new Administrator user. 
- **Request Body**:
  ```json
  {
    "username": "newadmin@admin.com",
    "password": "adminPassword123"
  }
  ```
- **Response** (201 Created):
  ```json
  {
    "id": 3,
    "username": "newadmin@admin.com",
    "role": "ADMIN"
  }
  ```

### 2. Get All Users (Admin Only)
- **Endpoint**: `GET /admin/users`
- **Description**: Retrieve a full list of all registered users (Customers and Admins).
- **Response** (200 OK): `List<User>`

---

## Category Endpoints

### 1. Create Category (Admin Only)
- **Endpoint**: `POST /categories`
- **Description**: Add a new category directly.
- **Request Body**:
  ```json
  {
    "name": "Supplements",
    "svg": "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"none\" viewBox=\"0 0 24 24\">...</svg>"
  }
  ```
- **Response** (201 Created): Returns the created Category object.

### 2. Get All Categories
- **Endpoint**: `GET /categories`
- **Description**: Retrieve all product categories and their full properties (like SVG strings).
- **Response** (200 OK): `List<Category>`

### 3. Get Category by ID
- **Endpoint**: `GET /categories/{id}`
- **Description**: Get category details by ID.
- **Response** (200 OK): Returns the matching Category object.

### 4. Update Category (Admin Only)
- **Endpoint**: `PUT /categories/{id}`
- **Description**: Update category properties like Name or SVG.
- **Request Body**: Updated `Category` object.
- **Response** (200 OK): Returns the updated Category object.

### 5. Delete Category (Admin Only)
- **Endpoint**: `DELETE /categories/{id}`
- **Description**: Delete a category.
- **Response** (204 No Content)

---

## Product Endpoints

### 1. Create Product (Admin Only)
- **Endpoint**: `POST /products`
- **Description**: Add a new product to the catalog. Note that if the submitted category doesn't exist, it will be automatically created using its Name.
- **Request Body**:
  ```json
  {
    "name": "Whey Protein",
    "category": {
      "name": "Supplements",
      "svg": "<svg>...</svg>"
    },
    "description": "High quality whey protein isolate.",
    "mp": 50.0,
    "sp": 40.0,
    "discount": 20.0,
    "images": [
      "https://example.com/image1.jpg",
      "https://example.com/image2.jpg"
    ]
  }
  ```
- **Response** (201 Created): Returns the created Product along with the fully assigned Category entity.

### 2. Get All Products
- **Endpoint**: `GET /products`
- **Description**: Retrieve a list of all products.
- **Response** (200 OK): `List<Product>`

### 3. Get Product Categories (Category Names Only)
- **Endpoint**: `GET /products/categories`
- **Description**: Retrieve a basic distinctive list of all product category names. **Use `GET /categories` for the full objects instead.**
- **Response** (200 OK): `List<String>`

### 4. Get Sample Products from Three Categories
- **Endpoint**: `GET /products/categories/sample-products`
- **Description**: Retrieves a list of up to three products, where each product belongs to a completely different category. This is useful for previewing varied items on the frontend home page.
- **Response** (200 OK): `List<Product>`

### 5. Get Random Products
- **Endpoint**: `GET /products/random`
- **Description**: Retrieve a list of products in a random order (useful for homepage displays).
- **Response** (200 OK): `List<Product>`

### 6. Get Product by ID
- **Endpoint**: `GET /products/{id}`
- **Description**: Retrieve detailed information about a specific product.
- **Response** (200 OK): `Product`

### 7. Update Product (Admin Only)
- **Endpoint**: `PUT /products/{id}`
- **Description**: Update an existing product. 
- **Request Body**: Updated `Product` object.
- **Response** (200 OK): `Product`

### 8. Delete Product (Admin Only)
- **Endpoint**: `DELETE /products/{id}`
- **Description**: Delete a specific product.
- **Response** (204 No Content)

---

## Search Endpoints

### 1. Search by Category Name
- **Endpoint**: `GET /products/search/category?category={categoryName}`
- **Description**: Retrieve all products belonging to a specific category. Matches the name of the assigned `Category` entity.
- **Response** (200 OK): `List<Product>`

### 2. Search by Name
- **Endpoint**: `GET /products/search/name?name={name}`
- **Description**: Retrieve products whose name contains the search term.
- **Response** (200 OK): `List<Product>`

### 3. Search by Price Range
- **Endpoint**: `GET /products/search/price?min={min}&max={max}`
- **Description**: Retrieve products with a selling price (`sp`) between the specified minimum and maximum values.
- **Response** (200 OK): `List<Product>`

---

## Global Exception Responses
In case of unhandled or Bad Request exceptions, the default error response structure will be:
```json
{
  "error": "Error message description"
}
```
