package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.MySQLException;
import entities.Department;

public class DepartmentDAO {

    public static Department findByName(Connection conn, String name) throws MySQLException {
        // Returns a Department object if found by name, otherwise returns null.

        String sql = "SELECT * FROM department WHERE Name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    Department department = new Department(
                        rs.getInt("Id"),
                        rs.getString("Name")
                    );
                    return department;
                }
                return null;
            } catch (SQLException e) {
                throw new MySQLException("Could not execute the query.", e);
            }
        } catch (SQLException e) {
            throw new MySQLException("Could not prepare the statement.", e);
        }
    }

    public static Department findById(Connection conn, Integer id) throws MySQLException {
        // Returns a Department object if found by id, otherwise returns null.

        String sql = "SELECT * FROM department WHERE Id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    Department department = new Department(
                        rs.getInt("Id"),
                        rs.getString("Name")
                    );
                    return department;
                }
                return null;
            } catch (SQLException e) {
                throw new MySQLException("Could not execute the query.", e);
            }
        } catch (SQLException e) {
            throw new MySQLException("Could not prepare the statement.", e);
        }
    }

    public static Department insert(Connection conn, Department department) throws MySQLException {
        // Inserts a new department into the database and returns the department with the generated id.
        // Returns null if a department with the same name already exists.

        if (department == null) {
            throw new MySQLException("Department cannot be null.");
        }

        if (department.getName() == null || department.getName().trim().isEmpty()) {
            throw new MySQLException("Department name cannot be null or empty.");
        }

        if (findByName(conn, department.getName()) != null) {
            return null;
        }

        String sql = "INSERT INTO department (Name) VALUES (?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, department.getName());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new MySQLException("Inserting department failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    department.setId(generatedKeys.getInt(1));
                } else {
                    throw new MySQLException("Inserting department failed, no ID obtained.");
                }
            }

            return department;
        } catch (SQLException e) {
            throw new MySQLException("Could not prepare or execute the statement.", e);
        }
    }
    
    public static int sellerCount(Connection conn, String departmentName) throws MySQLException {
        // Returns the number of sellers in a given department.
        // Returns -1 if department doesn't exist

        String sql = """
        SELECT COUNT(seller.Id) as SellerCount
        FROM department
        LEFT JOIN seller ON seller.DepartmentId = department.Id
        WHERE department.Name = ?
        GROUP BY department.Id
        """;
                
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, departmentName);
            try (ResultSet rs = stmt.executeQuery()){
                if (!rs.next()){
                    return -1;
                }
                     
                return rs.getInt("SellerCount");
            } catch (SQLException e) {
            throw new MySQLException("Could not execute the query.", e);
            }
        } catch (SQLException e) {
            throw new MySQLException("Could not prepare the statement.", e);
        }
    }

    public static String sellerCountAll(Connection conn) throws MySQLException {
        // Returns a string listing all departments and their respective seller counts.

        StringBuilder sb = new StringBuilder();
        boolean hasResults = false;

        String sql = """
        SELECT department.Name, COUNT(seller.Id) as SellerCount
        FROM department
        LEFT JOIN seller ON seller.DepartmentId = department.Id
        GROUP BY department.Id
        """;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    hasResults = true;
                    sb.append(rs.getString("Name"));
                    sb.append(" - ");
                    sb.append(rs.getString("SellerCount"));
                    sb.append("\n");
                }

                return hasResults ? sb.toString() : "No departments found.";
            } catch (SQLException e) {
                throw new MySQLException("Could not excecute the query.", e);
            }
        } catch (SQLException e) {
            throw new MySQLException("Could not prepare the statement.", e);
        }
    }
}
