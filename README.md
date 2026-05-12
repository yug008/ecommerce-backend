# 🛒 ShopEase - Ecommerce Backend

A production-ready REST API for a full-stack ecommerce application built with Spring Boot, secured with JWT authentication, and integrated with Razorpay payment gateway and Cloudinary image storage.

## 🔗 Links
- **Live Site:** https://ecommerce-frontend-orcin-omega.vercel.app
- **Backend Repo:** https://github.com/yug008/ecommerce-backend
- **Frontend Repo:** https://github.com/yug008/ecommerce-frontend

## 🛠 Tech Stack

| Technology | Purpose |
|------------|---------|
| Spring Boot 3.2 | REST API Framework |
| Spring Security | Authentication & Authorization |
| JWT (JSON Web Token) | Stateless Authentication |
| PostgreSQL (Neon) | Cloud Database |
| Spring Data JPA + Hibernate | ORM & Database Layer |
| Razorpay | Payment Gateway |
| Cloudinary | Cloud Image Storage |
| Docker | Containerization |
| Render | Backend Deployment |
| Vercel | Frontend Deployment |

## ✨ Features

- 🔐 JWT Authentication with Role Based Access Control (USER/ADMIN)
- 🛍️ Product Management with Cloudinary Image Upload
- 🛒 Cart Management (Add, Remove, Clear)
- 📦 Order Placement with Automatic Stock Decrement
- 💳 Razorpay Payment Integration with HMAC Signature Verification
- 👨‍💼 Admin Panel (Manage Products & Orders)
- ☁️ Cloud PostgreSQL with Neon
- 🐳 Dockerized for easy deployment

## 📌 API Endpoints

### 🔑 Auth
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/auth/register | Public |
| POST | /api/auth/login | Public |

### 📦 Products
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | /api/products | Public |
| GET | /api/products/{id} | Public |
| GET | /api/products/search?name= | Public |
| GET | /api/products/category?category= | Public |
| POST | /api/products/add | Admin |
| PUT | /api/products/{id} | Admin |
| DELETE | /api/products/{id} | Admin |

### 🛒 Cart
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/cart/add?productId=&quantity= | User |
| GET | /api/cart | User |
| DELETE | /api/cart/remove?id= | User |
| DELETE | /api/cart/clear | User |

### 📋 Orders
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/orders/place | User |
| GET | /api/orders/my-orders | User |
| GET | /api/orders/{id} | User |
| GET | /api/orders/all | Admin |
| PUT | /api/orders/{id}/status | Admin |

### 💳 Payment
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/payment/create/{orderId} | User |
| POST | /api/payment/verify | User |
| GET | /api/payment/key | Public |

## 💳 Payment Flow
1. User places order → status PENDING
2. Backend creates Razorpay order → returns razorpayOrderId
3. Frontend opens Razorpay checkout popup
4. User pays → Razorpay returns paymentId + signature
5. Backend verifies HMAC signature using secret key
6. Order status updated to PAID in database

## 🗄 Database Schema
users        → id, name, email, password, role, created_at

products     → id, name, description, price, stock, category, image_url, created_at

carts        → id, user_id

cart_items   → id, cart_id, product_id, quantity

orders       → id, user_id, status, total_amount, address, razorpay_order_id, razorpay_payment_id, created_at

order_items  → id, order_id, product_id, quantity, price

## ⚙️ Environment Variables

```env
# Database (Neon PostgreSQL)
DB_URL=jdbc:postgresql://your-neon-url/ecommerce_db
DB_USERNAME=your_username
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_secret_key

# Razorpay
RAZORPAY_KEY_ID=rzp_test_xxxxx
RAZORPAY_KEY_SECRET=your_secret

# Cloudinary
CLOUDINARY_CLOUD_NAME=your_cloud
CLOUDINARY_API_KEY=your_key
CLOUDINARY_API_SECRET=your_secret
```

## 🚀 Run Locally

```bash
# Clone the repo
git clone https://github.com/yourusername/ecommerce-backend.git

# Navigate to project
cd ecommerce-backend

# Add your application.yaml with local credentials

# Run with Maven
./mvnw spring-boot:run

# Or with Docker
docker build -t ecommerce-backend .
docker run -p 8080:8080 ecommerce-backend
```

## 👨‍💻 Author
**Yug Mehta**
- GitHub: [@yug008](https://github.com/yug008)
