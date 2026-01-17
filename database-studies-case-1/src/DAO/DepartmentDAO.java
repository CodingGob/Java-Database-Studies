package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.MySQLException;

public class DepartmentDAO {
    
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
