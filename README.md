# LibraryManagementSystemGearUp
Project for GearUp

### Book Management
- Add new books.
- Remove existing books.
- Update book information.
- Search for books by different criteria (title, author, ISBN).
- 
### Member Management
- Register new members.
- Remove existing members.
- Update member information.
- Search for members by different criteria (name, ID).

### Loan Transaction
- Register a book loan.
- Register the return of a book.
- View a member's loan history.
- Check the availability of a book.

### Non-functional Requirements
- User Interface: Simple console for user interactions.
- Persistence: Use a MySQL database to store data about books, members, and transactions.
- Exception Handling: Properly handle all possible exceptions that may arise during system operations.
- Documentation: Well-documented code, clear instructions on how to set up and run the system.

### Tips and Best Practices Data Validation
- Ensure that all input data is validated to avoid errors and security issues.Exception Handling
- Handle all possible exceptions appropriately to prevent the application from failing unexpectedly.Modularity
- Keep the code modular and provide clear instructions on how to set up and run the application.Testing
- Perform thorough testing of all functionalities to ensure that the system works correctly in all possible scenarios.

## Design
### Conceptual model
Represents important objects and the relationships between them.
#### Nouns (Objects)
- *Library.*
- Book.
- Member.
- Loan.
  
#### Verbs (Classes)
- Add.
- Delete.
- Update.
- Search by.
  
- Register member.
- Delete member.
- Update member.
- Search by.

- Loan book.
- Return book.
- Watch history.
- Check for disponibility.

  While it might seem intuitive to have a *Library* class to represent the entire library system, this approach can lead to several design issues. I would violate the Single Responsibility Principle (SRP), I would have some issues with modularity, extending functionality, testing and maintaining my application. I had some issues trying to do my UML diagram so I went back to this part and I decided to delete a single overarching *Library* class.

### Designing my DB
By executing the following queries, you will be able to create your own DB.
Username and password are declared on `DatabaseConnection.java`.

#### My DB

```mysql
-- Create the DB
CREATE DATABASE DB_LIBRARY;

-- Use the DB we created 
USE DB_LIBRARY;
```
#### My tables

```mysql
-- Create table tbl_books
CREATE TABLE tbl_books (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  author VARCHAR(100) NOT NULL,
  isbn VARCHAR(13) NOT NULL,
  available BOOLEAN DEFAULT TRUE
);
```

```mysql
-- Create table tbl_members
CREATE TABLE tbl_members (
  id INT AUTO_INCREMENT PRIMARY KEY,
  member_name VARCHAR(100) NOT NULL,
  member_id VARCHAR(20) NOT NULL
);
```

```mysql
-- Create table tbl_loans
CREATE TABLE tbl_loans (
  id INT AUTO_INCREMENT PRIMARY KEY,
  book_id INT NOT NULL,
  member_id INT NOT NULL,
  loan_date DATE NOT NULL,
  return_date DATE,
  returned BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (book_id) REFERENCES tbl_books(id),
  FOREIGN KEY (member_id) REFERENCES tbl_members(id)
);
```

1. `tbl_books` table:
    - `id` (INT, AUTO_INCREMENT, PRIMARY KEY): Unique identifier for the book.
    - `title` (VARCHAR): Title of the book, marked as NOT NULL.
    - `author` (VARCHAR): Author of the book, marked as NOT NULL.
    - `isbn` (VARCHAR): ISBN of the book, marked as NOT NULL.
    - `available` (BOOLEAN, DEFAULT TRUE): Indicates the availability of the book, defaults to TRUE, so I do not need to restrict it with NOT NULL.

1. `tbl_members` table:
    - `id` (INT, AUTO_INCREMENT, PRIMARY KEY): Unique identifier for the member.
    - `member_name` (VARCHAR): Name of the member, marked as NOT NULL.
    - `member_id` (VARCHAR): ID of the member, marked as NOT NULL.

1. `tbl_loans` table:
    - `id` (INT, AUTO_INCREMENT, PRIMARY KEY): Unique identifier for the loan.
    - `member_id` (INT, FOREIGN KEY REFERENCES tbl_members(id)): Identifier of the member who made the loan, marked as NOT NULL.
    - `book_id` (INT, FOREIGN KEY REFERENCES tbl_books(id)): Identifier of the borrowed book, marked as NOT NULL.
    - `loan_date` (DATE): Date of the loan, marked as NOT NULL.
    - `return_date` (DATE): Date of return, marked as NOT NULL.
    - `returned` (BOOLEAN, DEFAULT FALSE): Indicates if the book has been returned, defaults to FALSE.

##### Queries
Getting the loan history of a member:
```sql
SELECT l.id, b.title, l.loan_date, l.return_date, l.returned
FROM tbl_loans l
JOIN tbl_books b ON l.book_id = b.id
WHERE l.member_id = ?;
```
<<<<<<< HEAD

###### Sets member_id column as null
This is just a workaround because I already created my DB and tables. Create tbl_members as explained below will be enough.
```sql
-- Set member_id as null
ALTER TABLE tbl_members
MODIFY COLUMN member_id VARCHAR(20) NULL;
```

###### New tbl_members
```sql
CREATE TABLE tbl_members (
  id INT AUTO_INCREMENT PRIMARY KEY,
  member_name VARCHAR(100) NOT NULL,
  member_id VARCHAR(20) NULL
);
```

##### Queries for inventory and members examples

###### Books
```sql
-- Cleans tbl_books
DELETE FROM tbl_books;
```

```sql
-- Resets id to 1
ALTER TABLE tbl_books AUTO_INCREMENT = 1;
```

```sql
-- Insert 10 books to manage an inventory
INSERT INTO tbl_books (title, author, isbn, available)
VALUES
  ('The Alchemist', 'Paulo Coelho', '9780062315007', TRUE),
  ('To Kill a Mockingbird', 'Harper Lee', '9780446310789', TRUE),
  ('1984', 'George Orwell', '9780451524935', TRUE),
  ('Pride and Prejudice', 'Jane Austen', '9780141439518', TRUE),
  ('The Catcher in the Rye', 'J.D. Salinger', '9780316769488', TRUE),
  ('One Hundred Years of Solitude', 'Gabriel Garcia Marquez', '9780060883287', TRUE),
  ('The Hitchhiker''s Guide to the Galaxy', 'Douglas Adams', '9780345391803', TRUE),
  ('Brave New World', 'Aldous Huxley', '9780060850524', TRUE),
  ('The Picture of Dorian Gray', 'Oscar Wilde', '9780141439570', TRUE),
  ('Frankenstein', 'Mary Shelley', '9780141439471', TRUE);
```

 ```sql
  -- Select books
  SELECT * FROM tbl_books; 
  ```

###### Members
```sql
-- Insert some members examples
INSERT INTO tbl_members (member_name, member_id)
VALUES
  ('Wyatt García', NULL),
  ('Kevin Varela', NULL),
  ('Juan Pérez', NULL),
  ('María Rodríguez', NULL),
  ('Carlos Gómez', NULL),
  ('Ana Martínez', NULL),
  ('Pedro Hernández', NULL),
  ('Emily Johnson', NULL),
  ('Michael Smith', NULL),
  ('Emma Brown', NULL),
  ('William Davis', NULL),
  ('Olivia Wilson', NULL);
```

```sql
-- Clean tbl_members
DELETE FROM tbl_members;
```

```sql
-- Resets id to 1
ALTER TABLE tbl_members AUTO_INCREMENT = 1;
```

```sql
-- Inserts some members
INSERT INTO tbl_members (member_name, member_id)
VALUES
  ('Wyatt García', NULL),
  ('Kevin Varela', NULL),
  ('Juan Pérez', NULL),
  ('María Rodríguez', NULL),
  ('Carlos Gómez', NULL),
  ('Ana Martínez', NULL),
  ('Pedro Hernández', NULL),
  ('Emily Johnson', NULL),
  ('Michael Smith', NULL),
  ('Emma Brown', NULL),
  ('William Davis', NULL),
  ('Olivia Wilson', NULL);
  ```
  
  ```sql
  -- Select members
  SELECT * FROM tbl_members; 
  ```
=======
>>>>>>> origin/main
