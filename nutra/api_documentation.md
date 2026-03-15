# Nutra Backend — API Endpoints Documentation

> **Base URL:** `http://209.126.86.149:8079`
> **Base Currency:** `USD` (All product prices are entered in USD)
>
> All endpoints below are relative to this base URL.
> For example, `POST /auth/login` means `POST http://209.126.86.149:8079/auth/login`

                ---

                ## 🔑 Auto-Seeded Data

A default dataset is created on first startup if the database is empty:

### 1. Admin Credentials
| Field    | Value            | Role  |
|----------|------------------|-------|
| Username | `admin@admin.com`| ADMIN |
| Password | `admin123`       |       |

### 2. Test Customer Accounts
Ten accounts are seeded from `user1@example.com` to `user10@example.com` with the password `password123`.

### 3. Store Content
The seeder also creates:
*   **5 Categories**: Supplements, Vitamins, Lifestyle, Equipment, Bundles.
*   **10 Products**: Samples spread across categories with mock images.
*   **3 Blogs** & **3 Information Pages**.

                ---

                ## 🔐 Authentication Headers

                For endpoints marked **🔒 ADMIN**, you must include a JWT token in the request header:

                ```
                Authorization: Bearer <your-jwt-token>
                ```

                You can obtain a token by calling the **Login** or **Signup** endpoints.

                ---

                ---

                ## 1. Authentication Endpoints

                ### 1.1 Admin Login

                | Property    | Value                |
                |-------------|----------------------|
                | **Method**  | `POST`               |
                | **URL**     | `/auth/admin/login`  |
                | **Auth**    | None                 |

                > Only users with the **ADMIN** role can log in through this endpoint. Attempting to log in with a CUSTOMER account will return an error.

                **Request Body:**
                ```json
                {
                  "username": "admin@admin.com",
                  "password": "admin123"
                }
                ```

                **Success Response (200 OK):**
                ```json
                {
                  "userId": "1",
                  "username": "admin@admin.com",
                  "JwtToken": "eyJhbGciOiJIUzI1NiJ9..."
                }
                ```

                **Error Response (400 Bad Request):**
                ```json
                {
                  "error": "Access denied. Not an admin account."
                }
                ```

                ---

                ### 1.2 User Login

                | Property    | Value                |
                |-------------|----------------------|
                | **Method**  | `POST`               |
                | **URL**     | `/auth/user/login`   |
                | **Auth**    | None                 |

                > Only users with the **CUSTOMER** role can log in through this endpoint. Attempting to log in with an ADMIN account will return an error.

                **Request Body:**
                ```json
                {
                  "username": "user1@example.com",
                  "password": "password123"
                }
                ```

                **Success Response (200 OK):**
                ```json
                {
                  "userId": "2",
                  "username": "user1@example.com",
                  "JwtToken": "eyJhbGciOiJIUzI1NiJ9..."
                }
                ```

                **Error Response (400 Bad Request):**
                ```json
                {
                  "error": "Access denied. Not a customer account."
                }
                ```

                ---

                ### 1.3 Signup

                | Property    | Value                |
                |-------------|----------------------|
                | **Method**  | `POST`               |
                | **URL**     | `/auth/signup`       |
                | **Auth**    | None                 |

                > Signup always creates a **CUSTOMER** role user. Admins can only be created via the Admin endpoint.

                **Request Body:**
                ```json
                {
                  "username": "janedoe",
                  "password": "password123"
                }


                **Success Response (200 OK):**
                ```json
                {
                  "userId": "3",
                  "username": "janedoe",
                  "JwtToken": "eyJhbGciOiJIUzI1NiJ9..."
                }
                ```

                ---

                ---

                ## 2. Admin Endpoints

                > All admin endpoints require **🔒 ADMIN** role JWT token.

                ### 2.1 Create New Admin

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `POST`                 |
                | **URL**     | `/admin/users/admin`   |
                | **Auth**    | 🔒 ADMIN              |

                **Request Body:**
                ```json
                {
                  "username": "newadmin@admin.com",
                  "password": "securePassword123"
                }
                ```

                **Success Response (201 Created):**
                ```json
                {
                  "id": 3,
                  "username": "newadmin@admin.com",
                  "role": "ADMIN"
                }
                ```

                ---

                ### 2.2 Get All Registered Users

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `GET`                  |
                | **URL**     | `/admin/users`         |
                | **Auth**    | 🔒 ADMIN              |

                **Success Response (200 OK):**
                ```json
                [
                  {
                    "id": 1,
                    "username": "admin@admin.com",
                    "role": "ADMIN"
                  },
                  {
                    "id": 2,
                    "username": "johndoe",
                    "role": "CUSTOMER"
                  }
                ]
                ```

                ### 2.3 Deactivate User

                | Property    | Value                                |
                |-------------|--------------------------------------|
                | **Method**  | `PATCH`                              |
                | **URL**     | `/admin/users/{id}/deactivate`       |
                | **Auth**    | 🔒 ADMIN                            |

                > Disables a user account. Deactivated users cannot log in.

                **Success Response (200 OK):** Updated user object with `"active": false`.

                ---

                ### 2.4 Activate User

                | Property    | Value                                |
                |-------------|--------------------------------------|
                | **Method**  | `PATCH`                              |
                | **URL**     | `/admin/users/{id}/activate`         |
                | **Auth**    | 🔒 ADMIN                            |

                > Re-enables a previously deactivated user account.

                **Success Response (200 OK):** Updated user object with `"active": true`.

                ---

                ### 2.5 Delete User

                | Property    | Value                                |
                |-------------|--------------------------------------|
                | **Method**  | `DELETE`                             |
                | **URL**     | `/admin/users/{id}`                  |
                | **Auth**    | 🔒 ADMIN                            |

                > Permanently removes a user from the system. This action is irreversible.

                **Success Response (204 No Content):** Empty body.

                ---

                ---


                ## 3. Category Endpoints

                ### 3.1 Create Category

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `POST`                 |
                | **URL**     | `/categories`          |
                | **Auth**    | 🔒 ADMIN              |
                | **Content-Type** | `multipart/form-data` |

                **Request Parts:**
                1.  **`category` (application/json)**:
                    ```json
                    {
                      "name": "Supplements",
                      "svg": "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'><path d='M12 2L2 7l10 5 10-5-10-5z'/></svg>",
                      "badge": "NEW",
                      "shortDescription": "High‑quality supplement category"
                    }
                    ```
                2.  **`image` (File)**: Optional image file for the category.

                **Success Response (201 Created):**
                ```json
                {
                  "id": 1,
                  "name": "Supplements",
                  "svg": "<svg>...</svg>",
                  "badge": "NEW",
                  "shortDescription": "High-quality supplement category",
                  "image": "/uploads/supplements.png"
                }
                ```

                ---

                ### 3.2 Get All Categories

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `GET`                  |
                | **URL**     | `/categories`          |
                | **Auth**    | None (Public)          |

                **Success Response (200 OK):**
                ```json
                [
                  {
                    "id": 1,
                    "name": "Supplements",
                    "svg": "<svg>...</svg>"
                  },
                  {
                    "id": 2,
                    "name": "Vitamins",
                    "svg": "<svg>...</svg>"
                  }
                ]
                ```

                ---

                ### 3.3 Get Category by ID

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `GET`                  |
                | **URL**     | `/categories/{id}`     |
                | **Auth**    | None (Public)          |

                **Success Response (200 OK):**
                ```json
                {
                  "id": 1,
                  "name": "Supplements",
                  "svg": "<svg>...</svg>"
                }
                ```

                **Error Response (404):**
                ```json
                {
                  "error": "Category not found with id: 99"
                }
                ```

                ---

                ### 3.4 Update Category

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `PUT`                  |
                | **URL**     | `/categories/{id}`     |
                | **Auth**    | 🔒 ADMIN              |
                | **Content-Type** | `multipart/form-data` |

                **Request Parts:** Same as **Create Category (3.1)**. Include the `category` JSON with the new `badge` and `shortDescription` fields and an optional `image` file.


                **Success Response (200 OK):** Updated `Category` object.

                ---

                ### 3.5 Delete Category

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `DELETE`               |
                | **URL**     | `/categories/{id}`     |
                | **Auth**    | 🔒 ADMIN              |

                **Success Response (204 No Content):** Empty body.

                ---

                ---

                ## 4. Product Endpoints

                ### 4.1 Create Product

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `POST`                 |
                | **URL**     | `/products`            |
                | **Auth**    | 🔒 ADMIN              |
                | **Content-Type** | `multipart/form-data` |

                > If the category name doesn't exist, it is automatically created.
                > **Note:** All product prices (SP/MP) are stored and managed in **USD**.

                **Request Parts (Multipart):**

                1.  **`product` (Content-Type: `application/json`)**:
                    ```json
                    {
                      "name": "Whey Protein Isolate",
                      "link": "https://example.com/products/whey-protein",
                      "category": {
                        "name": "Supplements",
                        "svg": "<svg>...</svg>"
                      },
                      "description": "Premium whey protein isolate for lean muscle growth.",
                      "singleProductMp": 3500.0,
                      "singleProductSp": 2800.0,
                      "twoProductMp": 7000.0,
                      "twoProductSp": 5000.0,
                      "threeProductMp": 10500.0,
                      "threeProductSp": 7000.0,
                      "badge": "HOT",
                      "categoryBadge": "1 BEST SELLER",
                      "benefitsParagraph": "Our premium whey protein isolate is designed for athletes who demand the best. It provides a clean source of protein that is rapidly absorbed by the body.",
                      "benefits": [
                        {
                          "svg": "<svg>...</svg>",
                          "benefitDescription": "Rapid absorption for quick recovery"
                        },
                        {
                          "svg": "<svg>...</svg>",
                          "benefitDescription": "High bioavailability and purity"
                        }
                      ],
                      "servingSize": "1 Scoop (30g)",
                      "capsulesPerContainer": "30 Scoops",
                      "supplementFacts": [
                        {
                          "nutrientName": "Whey Protein Isolate",
                          "amountPerServing": "25g",
                          "amount": "83%"
                        }
                      ],
                      "reviews": [
                        {
                          "username": "john_lifter",
                          "stars": 5,
                          "comment": "Great taste and mixes well!"
                        }
                      ],
                      "freebies": [
                        "Free Shaker",
                        "Free Shipping over $50"
                      ],
                      "howToUse": [
                        "Mix one scoop with 250ml of water",
                        "Drink immediately after workout"
                      ],
                      "faqs": [
                        {
                          "question": "Is this product suitable for vegans?",
                          "answer": "Yes, it is plant-based."
                        }
                      ]
                    }
                    ```
                2.  **`featuredImages`**: `List<File>` (Exactly 2 files)
                3.  **`singleProductImage`**: `File` (Optional)
                4.  **`twoProductImage`**: `File` (Optional)
                5.  **`threeProductImage`**: `File` (Optional)

                **Frontend Implementation Example (JavaScript):**
                ```javascript
                const formData = new FormData();

                // 1. Add product data as a JSON blob
                const productBlob = new Blob([JSON.stringify(productData)], { type: 'application/json' });
                formData.append('product', productBlob);

                // 2. Add files
                featuredImagesArray.forEach(file => formData.append('featuredImages', file));
                formData.append('singleProductImage', fileInput1.files[0]);
                formData.append('twoProductImage', fileInput2.files[0]);
                formData.append('threeProductImage', fileInput3.files[0]);

                // 3. Send request
                fetch('http://209.126.86.149:8079/products', {
                  method: 'POST',
                  headers: { 'Authorization': 'Bearer ' + token },
                  body: formData // Content-Type is set automatically by the browser
                });
                ```

                **Success Response (201 Created):**
                ```json
                {
                  "id": 1,
                  "name": "Whey Protein Isolate",
                  "link": "https://example.com/products/whey-protein",
                  "category": {
                    "id": 1,
                    "name": "Supplements",
                    "svg": "<svg>...</svg>"
                  },
                  "featuredImages": [
                    "/uploads/featured1.png",
                    "/uploads/featured2.png"
                  ],
                  "singleProductImage": "/uploads/single.png",
                  "twoProductImage": "/uploads/two.png",
                  "threeProductImage": "/uploads/three.png",
                  "badge": "HOT",
                  "categoryBadge": "1 BEST SELLER",
                  "benefitsParagraph": "Our premium whey protein isolate is designed for athletes who demand the best...",
                  "benefits": [
                    {
                      "svg": "<svg>...</svg>",
                      "benefitDescription": "Rapid absorption for quick recovery"
                    },
                    {
                      "svg": "<svg>...</svg>",
                      "benefitDescription": "High bioavailability and purity"
                    }
                  ],
                  "servingSize": "1 Scoop (30g)",
                  "capsulesPerContainer": "30 Scoops",
                  "supplementFacts": [
                    {
                      "nutrientName": "Whey Protein Isolate",
                      "amountPerServing": "25g",
                      "amount": "83%"
                    }
                  ],
                  "reviews": [
                    {
                      "id": 1,
                      "username": "john_lifter",
                      "stars": 5,
                      "comment": "Great taste and mixes well!"
                    }
                  ],
                  "freebies": [
                    "Free Shaker",
                    "Free Shipping over $50"
                  ],
                  "howToUse": [
                    "Mix one scoop with 250ml of water",
                    "Drink immediately after workout"
                  ],
                  "faqs": [
                    {
                      "question": "Is this product suitable for vegans?",
                      "answer": "Yes, it is plant-based."
                    }
                  ]
                }
                ```

                ---

                ### 4.2 Bulk Create Products

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `POST`                 |
                | **URL**     | `/products/bulk`       |
                | **Auth**    | 🔒 ADMIN              |
                | **Content-Type** | `application/json` |

                > Saves multiple products in a single request. If a category name doesn't exist, it is automatically created. 
                > **Note:** Unlike the single product endpoint, this does not support file uploads.

                **Request Body (List of Product Objects):**
                ```json
                [
                  {
                    "name": "Whey Protein Isolate",
                    "link": "https://example.com/products/whey-protein",
                    "category": { "name": "Supplements" },
                    "description": "Premium whey protein isolate...",
                    "singleProductMp": 3500.0,
                    "singleProductSp": 2800.0
                  },
                  {
                    "name": "Vitamin D3",
                    "category": { "name": "Vitamins" },
                    "description": "Essential vitamin for bone health...",
                    "singleProductMp": 1200.0,
                    "singleProductSp": 950.0
                  }
                ]
                ```

                **Success Response (201 Created):** `List<Product>` (The assigned IDs and full objects)

                ---

                ### 4.3 Get All Products

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `GET`                  |
                | **URL**     | `/products`            |
                | **Auth**    | None (Public)          |

                **Success Response (200 OK):** `List<Product>`

                ---

                ### 4.4 Get Product by ID

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `GET`                  |
                | **URL**     | `/products/{id}`       |
                | **Auth**    | None (Public)          |

                **Success Response (200 OK):** Full `Product` object (as shown in 4.1 response).

                **Error Response (404):**
                ```json
                {
                  "error": "Product not found with id: 99"
                }
                ```

                ---

                ### 4.5 Update Product

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `PUT`                  |
                | **URL**     | `/products/{id}`       |
                | **Auth**    | 🔒 ADMIN              |
                | **Content-Type** | `multipart/form-data` |

                **Request Parts:** Same structure as **Create Product (4.1)**. Include the `product` JSON and any new `MultipartFile` images to update. If an image part is not included, the existing image will be kept.

                **Success Response (200 OK):** Updated `Product` object.

                ---

                ### 4.6 Delete Product

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `DELETE`               |
                | **URL**     | `/products/{id}`       |
                | **Auth**    | 🔒 ADMIN              |

                **Success Response (204 No Content):** Empty body.

                ---

                ### 4.7 Get All Category Names

                | Property    | Value                       |
                |-------------|------------------------------|
                | **Method**  | `GET`                        |
                | **URL**     | `/products/categories`       |
                | **Auth**    | None (Public)                |

                > Returns only the names. For full category objects, use `GET /categories`.

                **Success Response (200 OK):**
                ```json
                ["Supplements", "Vitamins", "Equipment"]
                ```

                ---

                ### 4.8 Get Sample Products from 3 Random Categories

                | Property    | Value                                    |
                |-------------|------------------------------------------|
                | **Method**  | `GET`                                    |
                | **URL**     | `/products/categories/sample-products`   |
                | **Auth**    | None (Public)                            |

                > Returns one product from each of up to 3 randomly selected categories. Useful for homepage previews.

                **Success Response (200 OK):** `List<Product>` (max 3 items)

                ---

                ### 4.9 Get All Products (Random Order)

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `GET`                  |
                | **URL**     | `/products/random`     |
                | **Auth**    | None (Public)          |

                **Success Response (200 OK):** `List<Product>` in shuffled order.

                ---

                ---

                ## 5. Product Search Endpoints

                ### 5.1 Search by Category Name

                | Property        | Value                                          |
                |-----------------|------------------------------------------------|
                | **Method**      | `GET`                                          |
                | **URL**         | `/products/search/category?category={name}`    |
                | **Auth**        | None (Public)                                  |
                | **Query Param** | `category` — category name (case-insensitive)  |

                **Example:** `GET /products/search/category?category=Supplements`

                **Success Response (200 OK):** `List<Product>`

                ---

                ### 5.2 Search by Product Name

                | Property        | Value                                          |
                |-----------------|------------------------------------------------|
                | **Method**      | `GET`                                          |
                | **URL**         | `/products/search/name?name={searchTerm}`      |
                | **Auth**        | None (Public)                                  |
                | **Query Param** | `name` — partial name match (case-insensitive) |

                **Example:** `GET /products/search/name?name=whey`

                **Success Response (200 OK):** `List<Product>`

                ---

                ### 5.3 Search by Price Range

                | Property         | Value                                            |
                |------------------|--------------------------------------------------|
                | **Method**       | `GET`                                            |
                | **URL**          | `/products/search/price?min={min}&max={max}`     |
                | **Auth**         | None (Public)                                    |
                | **Query Params** | `min` — minimum selling price (Double)           |
                |                  | `max` — maximum selling price (Double)           |

                **Example:** `GET /products/search/price?min=1000&max=3000`

                **Success Response (200 OK):** `List<Product>`

                ---

                ### 5.4 Search by Global Badge

                | Property        | Value                                          |
                |-----------------|------------------------------------------------|
                | **Method**      | `GET`                                          |
                | **URL**         | `/products/search/badge?badge={badgeName}`     |
                | **Auth**        | None (Public)                                  |
                | **Query Param** | `badge` — global badge name (e.g. HOT, NEW)    |

                **Example:** `GET /products/search/badge?badge=HOT`

                **Success Response (200 OK):** `List<Product>`

                ---

                ### 5.5 Search by Category Badge

                | Property        | Value                                                  |
                |-----------------|--------------------------------------------------------|
                | **Method**      | `GET`                                                  |
                | **URL**         | `/products/search/category-badge?badge={B}&category={C}`|
                | **Auth**        | None (Public)                                          |
                | **Query Params**| `badge` — e.g. "1 BEST SELLER"                        |
                |                 | `category` — e.g. "Supplements"                       |

                **Example:** `GET /products/search/category-badge?badge=1+BEST+SELLER&category=Supplements`

                **Success Response (200 OK):** `List<Product>`

                ---

                ---

                ## 6. Blog Endpoints

                ### 6.1 Create Blog

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `POST`                 |
                | **URL**     | `/blogs`               |
                | **Auth**    | 🔒 ADMIN              |

                > A blog can be linked to one or more products via their IDs. This endpoint consumes `multipart/form-data`.

                **Request Parts:**
                1.  `blog`: A JSON blob containing the blog details (title, content, author, relatedProducts).
                2.  `image`: (Optional) The blog's feature image file.

                **JSON Part Example (`blog`):**
                ```json
                {
                  "title": "Top 5 Supplements for Beginners",
                  "content": "Here are the top supplements every beginner should consider when starting their fitness journey...",
                  "author": "Admin",
                  "category": {
                    "name": "Supplements"
                  },
                  "relatedProducts": [
                    { "id": 1 },
                    { "id": 3 }
                  ]
                }
                ```

                **Success Response (201 Created):**
                ```json
                {
                  "id": 1,
                  "title": "Top 5 Supplements for Beginners",
                  "content": "Here are the top supplements every beginner should consider...",
                  "author": "Admin",
                  "image": "/uploads/blog1.png",
                  "category": {
                    "id": 1,
                    "name": "Supplements",
                    "svg": "<svg>...</svg>"
                  },
                  "relatedProducts": [ ... ]
                }
                ```

                ---

                ### 6.2 Get All Blogs

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `GET`                  |
                | **URL**     | `/blogs`               |
                | **Auth**    | None (Public)          |

                **Success Response (200 OK):** `List<Blog>`

                ---

                ### 6.3 Get Blog by ID

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `GET`                  |
                | **URL**     | `/blogs/{id}`          |
                | **Auth**    | None (Public)          |

                **Success Response (200 OK):** Full `Blog` object with related products.

                **Error Response (404):**
                ```json
                {
                  "error": "Blog not found with ID 99"
                }
                ```

                ---

                ### 6.4 Update Blog

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `PUT`                  |
                | **URL**     | `/blogs/{id}`          |
                | **Auth**    | 🔒 ADMIN              |

                **Request Parts:** Same as **Create Blog (6.1)**.

                **Success Response (200 OK):** Updated `Blog` object.

                ---

                ### 6.5 Delete Blog

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `DELETE`               |
                | **URL**     | `/blogs/{id}`          |
                | **Auth**    | 🔒 ADMIN              |

                **Success Response (204 No Content):** Empty body.

                ---

                ---

                ## 7. Information Endpoints

                ### 7.1 Create Information

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `POST`                 |
                | **URL**     | `/information`         |
                | **Auth**    | 🔒 ADMIN              |

                > Can optionally be linked to a category. This endpoint consumes `multipart/form-data`.

                **Request Parts:**
                1.  `information`: A JSON blob containing the information details (title, content, category).
                2.  `image`: (Optional) A single image file for this information section.

                **JSON Part Example (`information`):**
                ```json
                {
                  "title": "About Our Supplements",
                  "content": "We source our supplements from the finest manufacturers worldwide. Every product goes through rigorous testing...",
                  "category": {
                    "id": 1
                  }
                }
                ```

                **Success Response (201 Created):**
                ```json
                {
                  "id": 1,
                  "title": "About Our Supplements",
                  "content": "We source our supplements from the finest manufacturers worldwide...",
                  "image": "/uploads/info1.png",
                  "category": {
                    "id": 1,
                    "name": "Supplements",
                    "svg": "<svg>...</svg>"
                  }
                }
                ```

                ---

                ### 7.2 Get All Information

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `GET`                  |
                | **URL**     | `/information`         |
                | **Auth**    | None (Public)          |

                **Success Response (200 OK):** `List<Information>`

                ---

                ### 7.3 Get Information by ID

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `GET`                  |
                | **URL**     | `/information/{id}`    |
                | **Auth**    | None (Public)          |

                **Success Response (200 OK):** Full `Information` object.

                **Error Response (404):**
                ```json
                {
                  "error": "Information not found with ID 99"
                }
                ```

                ---

                ### 7.4 Update Information

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `PUT`                  |
                | **URL**     | `/information/{id}`    |
                | **Auth**    | 🔒 ADMIN              |

                **Request Parts:** Same as **Create Information (7.1)**.

                **Success Response (200 OK):** Updated `Information` object.

                ---

                ### 7.5 Delete Information

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `DELETE`               |
                | **URL**     | `/information/{id}`    |
                | **Auth**    | 🔒 ADMIN              |

                **Success Response (204 No Content):** Empty body.

                ---

                ---

                ## 8. Analytics Endpoints

                ### 8.1 Record a Initial Visit

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `POST`                 |
                | **URL**     | `/analytics/record`    |
                | **Auth**    | None (Public)          |

                **Request Body:** None required

                **Success Response (200 OK):**
                ```json
                {
                  "message": "Visit recorded"
                }
                ```

                ---

                ### 8.2 Get Analytics Statistics

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `GET`                  |
                | **URL**     | `/analytics/stats`     |
                | **Auth**    | None (Public)          |

                **Success Response (200 OK):**
                ```json
                {
                  "byCountry": [
                    {
                      "country": "Unknown",
                      "clicks": 1
                    }
                  ],
                  "totalVisits": 1
                }
                ```

                ---

                ---

                ## 9. Currency Endpoints

                > Publicly accessible endpoints for real-time exchange rates and price conversions.

                ### 9.1 Get Latest Rates

                | Property    | Value                  |
                |-------------|------------------------|
                | **Method**  | `GET`                  |
                | **URL**     | `/currency/rates`      |
                | **Auth**    | None (Public)          |

                > Returns all available exchange rates with **USD** as the base currency.

                **Success Response (200 OK):**
                ```json
                {
                  "amount": 1.0,
                  "base": "USD",
                  "date": "2026-03-11",
                  "rates": {
                    "INR": 91.96,
                    "EUR": 0.94,
                    "GBP": 0.78,
                    "JPY": 151.2
                  }
                }
                ```

                ---

                ### 9.2 Convert Single Amount

                | Property        | Value                                   |
                |-----------------|-----------------------------------------|
                | **Method**      | `GET`                                   |
                | **URL**         | `/currency/convert?amount=X&currency=Y` |
                | **Auth**        | None (Public)                           |
                | **Query Params**| `amount` (default 1.0)                  |
                |                 | `currency` (e.g. INR, EUR)              |

                **Success Response (200 OK):**
                ```json
                {
                  "base": "USD",
                  "targetCurrency": "INR",
                  "originalAmount": 49.99,
                  "exchangeRate": 91.96,
                  "convertedAmount": 4597.52,
                  "date": "2026-03-11"
                }
                ```

                ---

                ### 9.3 Convert All Product Prices

                | Property        | Value                                   |
                |-----------------|-----------------------------------------|
                | **Method**      | `GET`                                   |
                | **URL**         | `/currency/product/{id}?currency=INR` |
                | **Auth**        | None (Public)                           |

                > Fetches a product and converts all 6 price fields (SP/MP for all pack sizes) to the target currency.

                **Success Response (200 OK):**
                ```json
                {
                  "productId": 1,
                  "productName": "Nutra Omega 3",
                  "baseCurrency": "USD",
                  "targetCurrency": "INR",
                  "exchangeRate": 91.96,
                  "date": "2026-03-11",
                  "singleProductMp": 4597.52,
                  "singleProductSp": 3677.52,
                  "twoProductMp": 8349.52,
                  "twoProductSp": 6897.52,
                  "threeProductMp": 11977.52,
                  "threeProductSp": 9677.52
                }
                ```

                ---

                ---

                ## 10. Error Responses

                All errors follow a consistent JSON format:

                ```json
                {
                  "error": "Error message description"
                }
                ```

                | Exception Type               | HTTP Status         |
                |------------------------------|---------------------|
                | `ResourceNotFoundException`  | `404 Not Found`     |
                | `RuntimeException`           | `400 Bad Request`   |
                | `Exception` (generic)        | `500 Internal Error`|
                | Unauthorized / No Token      | `401 Unauthorized`  |
                | Forbidden / Wrong Role       | `403 Forbidden`     |

                ---

                ## 11. Quick Reference Table

                | #  | Method   | Endpoint                                 | Auth         | Description                              |
                |----|----------|------------------------------------------|--------------|------------------------------------------|
                | 1  | `POST`   | `/auth/admin/login`                      | None         | Admin login — validates ADMIN role       |
                | 2  | `POST`   | `/auth/user/login`                       | None         | User login — validates CUSTOMER role     |
                | 3  | `POST`   | `/auth/signup`                           | None         | Register as Customer                     |
                | 4  | `POST`   | `/admin/users/admin`                     | 🔒 ADMIN    | Create a new Admin user                  |
                | 5  | `GET`    | `/admin/users`                           | 🔒 ADMIN    | List all users                           |
                | 6  | `PATCH`  | `/admin/users/{id}/deactivate`           | 🔒 ADMIN    | Deactivate user login (reversible)       |
                | 7  | `PATCH`  | `/admin/users/{id}/activate`             | 🔒 ADMIN    | Reactivate user login                    |
                | 8  | `DELETE` | `/admin/users/{id}`                      | 🔒 ADMIN    | Permanently delete user account          |
                | 9  | `POST`   | `/categories`                            | 🔒 ADMIN    | Create a category                        |
                | 10 | `GET`    | `/categories`                            | None         | Get all categories                       |
                | 11 | `GET`    | `/categories/{id}`                       | None         | Get category by ID                       |
                | 12 | `PUT`    | `/categories/{id}`                       | 🔒 ADMIN    | Update a category                        |
                | 13 | `DELETE` | `/categories/{id}`                       | 🔒 ADMIN    | Delete a category                        |
                | 14 | `POST`   | `/products`                              | 🔒 ADMIN    | Create a product                         |
                | 15 | `GET`    | `/products`                              | None         | Get all products                         |
                | 16 | `GET`    | `/products/{id}`                         | None         | Get product by ID                        |
                | 17 | `PUT`    | `/products/{id}`                         | 🔒 ADMIN    | Update a product                         |
                | 18 | `DELETE` | `/products/{id}`                         | 🔒 ADMIN    | Delete a product                         |
                | 19 | `GET`    | `/products/categories`                   | None         | Get all category names                   |
                | 20 | `GET`    | `/products/categories/sample-products`   | None         | Get 1 product from 3 random categories   |
                | 21 | `GET`    | `/products/random`                       | None         | Get all products (random order)          |
                | 22 | `GET`    | `/products/search/category?category=X`   | None         | Search products by category              |
                | 23 | `GET`    | `/products/search/name?name=X`           | None         | Search products by name                  |
                | 24 | `GET`    | `/products/search/price?min=X&max=Y`     | None         | Search products by price range           |
                | 25 | `POST`   | `/blogs`                                 | 🔒 ADMIN    | Create a blog post                       |
                | 26 | `GET`    | `/blogs`                                 | None         | Get all blog posts                       |
                | 27 | `GET`    | `/blogs/{id}`                            | None         | Get blog by ID                           |
                | 28 | `PUT`    | `/blogs/{id}`                            | 🔒 ADMIN    | Update a blog post                       |
                | 29 | `DELETE` | `/blogs/{id}`                            | 🔒 ADMIN    | Delete a blog post                       |
                | 30 | `POST`   | `/information`                           | 🔒 ADMIN    | Create an information page               |
                | 31 | `GET`    | `/information`                           | None         | Get all information pages                |
                | 32 | `GET`    | `/information/{id}`                      | None         | Get information by ID                    |
                | 33 | `PUT`    | `/information/{id}`                      | 🔒 ADMIN    | Update an information page               |
                | 34 | `DELETE` | `/information/{id}`                      | 🔒 ADMIN    | Delete an information page               |
                | 35 | `POST`   | `/analytics/record`                      | None         | Record a new visit based on IP           |
                | 36 | `GET`    | `/analytics/stats`                       | None         | Get analytics statistics                 |
                | 37 | `GET`    | `/currency/rates`                        | None         | Get all latest rates (base USD)          |
                | 38 | `GET`    | `/currency/convert`                      | None         | Convert single USD amount                |
                | 39 | `GET`    | `/currency/product/{id}`                 | None         | Convert all product prices (one call)    |
                | 40 | `GET`    | `/blogs/category/{name}`                 | None         | Get blogs by category name               |
                | 41 | `GET`    | `/information/category/{name}`           | None         | Get info pages by category name          |
                | 42 | `GET`    | `/products/search/badge`                 | None         | Search products by global badge          |
                | 43 | `GET`    | `/products/search/category-badge`        | None         | Search products by category-specific badge|
                ---

                ---

                ### 12. Working with Images (Frontend Guide)

#### 🚀 How it Works
1.  **Upload**: Send images as binary `MultipartFile` in your `POST`/`PUT` requests.
2.  **Storage**: The server saves the file to disk and stores a relative path (e.g., `/uploads/uuid_filename.png`) in the database.
3.  **Display**: The server serves these files as static resources. You can directly use the returned URL in an `<img>` tag:
    `<img src="http://209.126.86.149:8079/uploads/xxxx.png" />`

#### 9.1 Uploading Images (Multipart/Form-Data)
When creating or updating a product, images must be sent as binary files using the `FormData` browser API.

**Steps:**
1.  Construct a `FormData` object.
2.  Attach the product JSON as a `Blob` with type `application/json` (exclude image fields).
3.  Attach individual files for `singleProductImage`, `twoProductImage`, etc.
4.  Attach multiple files for `featuredImages` parts.

```javascript
const formData = new FormData();
const token = "YOUR_JWT_TOKEN";

// 1. Build the product data (text fields only)
const productData = { 
  name: "New Vitamin", 
  category: { name: "Vitamins" },
  description: "High quality vitamins...",
  singleProductMp: 50.0,
  singleProductSp: 45.0,
  // ... rest of the product fields
};

// 2. Append JSON as a Blob
formData.append('product', new Blob([JSON.stringify(productData)], { type: 'application/json' }));

// 3. Append individual image files
formData.append('singleProductImage', fileInput1.files[0]); 
formData.append('twoProductImage', fileInput2.files[0]);
formData.append('threeProductImage', fileInput3.files[0]);

// 4. Append multiple files for featuredImages (exactly 2 required)
formData.append('featuredImages', featuredInput.files[0]);
formData.append('featuredImages', featuredInput.files[1]);

// 5. Send via fetch/axios (Note: NO /api prefix)
const response = await fetch('http://209.126.86.149:8079/products', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`
    // Browser automatically sets Content-Type for FormData
  },
  body: formData
});
```
                ```

                ### 9.2 Displaying Images (Fetching)
The backend now returns simple relative URL strings for all image fields. These files are served as static resources from the `/uploads/` directory on the server.

**Example Response:**
```json
{
  "name": "Whey Protein",
  "singleProductImage": "/uploads/a1b2-c3d4-e5f6-image.png",
  "featuredImages": [
    "/uploads/f7g8-h9i0-image.png",
    "/uploads/j1k2-l3m4-image.png"
  ]
}
```

**In your React/Frontend Component:**
```javascript
// Prepend the Base URL to the relative path
const BASE_URL = "http://209.126.86.149:8079";

return (
  <div>
    <h1>{product.name}</h1>
    
    {/* Use the URL directly with the base URL! */}
    <img src={`${BASE_URL}${product.singleProductImage}`} alt="Main" />

    {/* Displaying featured images list */}
    {product.featuredImages.map((src, index) => (
      <img key={index} src={`${BASE_URL}${src}`} alt={`Featured ${index}`} />
    ))}
  </div>
);
```

**🚀 Key Performance Advantages:**
*   **Browser Caching**: Images are now cached by the browser, reducing repeated data transfer.
*   **Smaller Payloads**: JSON responses are tiny because they only contain path strings.
*   **Scalability**: The database stays small and fast since images are stored on the filesystem.
*   **Zero Prefixes**: No Base64 data URLs - just clean static file serving.
*   **Automatic Cleanup**: Deleting an entity (Product/Category/Blog) also automatically deletes its images from the disk.

