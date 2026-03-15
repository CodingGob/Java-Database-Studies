# Java Database Studies

This repository focuses on learning how Java interacts with Relational Databases (MySQL) using native JDBC, without the use of frameworks like Spring or Hibernate. The goal was to master the Data Access Object (DAO) pattern, manual connection management, and transaction handling.

### Project Structure

The repository is organized into three evolutionary stages:

1. **database-studies-basics** A fundamental introduction to JDBC. It covers the basics of establishing a connection and implementing a simple `UserDAO` for basic CRUD operations.

2. **database-studies-case-1** A more complex implementation featuring a CLI (Command Line Interface) menu.
   - Introduction of `DepartmentDAO` and `SellerDAO`.
   - Custom exception handling for database errors (`MySQLException` and `DBIntegrityException`).
   - Implementation of logic-heavy queries, such as counting entities across departments.

3. **seller-department-project** The most refined version of the studies, focusing on architecture and decoupling:
   - **DAO Pattern**: Uses interfaces and a `DaoFactory` to hide implementation details.
   - **Service Layer**: Introduction of a Service layer to handle business logic and manage database transactions (manual `autoCommit` control).
   - **UI Separation**: A structured CLI organized into specific menu classes (`DepartmentMenu`, `SellerMenu`) inheriting from a base menu structure.

### Key Concepts Mastered
- **JDBC Core**: Connection, Statement, PreparedStatement, and ResultSet.
- **DAO Pattern**: Decoupling database logic from business logic using interfaces.
- **Transaction Management**: Manually handling commit and rollback.
- **Exception Handling**: Translating SQL errors into meaningful custom domain exceptions.
- **Composition**: Handling relationships between entities (e.g., Sellers belonging to Departments) at the JDBC level.

### Technologies
- **Java**: JDK 17
- **Database**: MySQL
- **IDE**: VS Code
