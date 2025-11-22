Dưới đây là **schema mẫu** cho dự án **SmartBank** — một ứng dụng ngân hàng số cơ bản (quản lý tài khoản, giao dịch, người dùng, thẻ, tiết kiệm…).
Bạn có thể copy SQL này chạy trực tiếp trong **MySQL**.

---

# ✅ **SMARTBANK – DATABASE SCHEMA (MySQL)**

## 1️⃣ **Tạo database**

```sql
CREATE DATABASE smartbank;
USE smartbank;
```

---

## 2️⃣ **Bảng users (Thông tin người dùng)**

Chứa thông tin khách hàng.

```sql
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 3️⃣ **Bảng accounts (Tài khoản ngân hàng)**

Một user có thể có nhiều tài khoản.

```sql
CREATE TABLE accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    account_type ENUM('SAVINGS', 'CHECKING') NOT NULL,
    balance DECIMAL(18,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

---

## 4️⃣ **Bảng transactions (Giao dịch)**

Lưu lại mọi giao dịch: chuyển tiền, nhận tiền, thanh toán…

```sql
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    type ENUM('DEPOSIT', 'WITHDRAW', 'TRANSFER_IN', 'TRANSFER_OUT') NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);
```

---

## 5️⃣ **Bảng transfers (Chuyển khoản)**

Lưu giao dịch chuyển khoản giữa 2 tài khoản.

```sql
CREATE TABLE transfers (
    transfer_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_account INT NOT NULL,
    receiver_account INT NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    message VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_account) REFERENCES accounts(account_id),
    FOREIGN KEY (receiver_account) REFERENCES accounts(account_id)
);
```

---

## 6️⃣ **Bảng cards (Thẻ ngân hàng)**

Quản lý thẻ ATM / Visa…

```sql
CREATE TABLE cards (
    card_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    card_number VARCHAR(20) UNIQUE NOT NULL,
    card_type ENUM('DEBIT', 'CREDIT') NOT NULL,
    expiry_date DATE NOT NULL,
    status ENUM('ACTIVE', 'LOCKED') DEFAULT 'ACTIVE',
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);
```

---

## 7️⃣ **Bảng savings (Tài khoản tiết kiệm)**

Gửi tiết kiệm, kỳ hạn, tiền lãi.

```sql
CREATE TABLE savings (
    saving_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    principal DECIMAL(18,2) NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL,
    term_months INT NOT NULL,
    start_date DATE NOT NULL,
    maturity_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

---

## 8️⃣ **Bảng logs (Audit logs)**

Theo dõi hoạt động.

```sql
CREATE TABLE audit_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    action VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

---


