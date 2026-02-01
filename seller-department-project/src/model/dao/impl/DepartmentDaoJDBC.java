package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.dao.DepartmentDao;
import model.entities.Department;
import model.exceptions.DBException;
import model.exceptions.DBIntegrityException;
import model.exceptions.UniqueViolationException;
import model.exceptions.ValidationException;

public class DepartmentDaoJDBC implements DepartmentDao {
    private final Connection conn;
    
    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    
    private String sqlSelect(String column) {
        if (column == null) {
            return "SELECT " 
                    + "Id," 
                    + "Name "
                + "FROM department"; 
        }

        return "SELECT " 
                + "Id," 
                + "Name "
            + "FROM department " 
            + "WHERE " + column + " = ?";
    }
    
    private String sqlUpdate(String column) {
        return "UPDATE department SET Name = ? WHERE " + column + " = ?";
    }
        
    private String sqlDelete(String column) {
        return "DELETE FROM department WHERE " + column + " = ?";
    } 
    
    private String sqlInsert() {
        return "INSERT INTO department (Name) VALUES (?)";
    }

    private String sqlSellerCount(String column) {
        return "SELECT "
                + "COUNT(s.Id) AS SellerCount "
            + "FROM department AS d "
            + "LEFT JOIN seller AS s "
            + "ON s.DepartmentId = d.Id "
            + "WHERE d." + column + " = ? "
            + "GROUP BY d.Id";
    }


    private void validate(Department obj) throws DBException {
        if (obj == null) {
            throw new ValidationException("Department cannot be null.");
        }

        if (obj.getId() == null || obj.getId() < 1) {
            throw new ValidationException("Id cannot be null or less then 1.");
        }

        if (obj.getName() == null || obj.getName().trim().isEmpty()) {
            throw new ValidationException("Department's name cannot be null or empty.");
        }
    }
    
    @Override
    public Department insert(Department obj) throws DBException {
        if (obj == null) {
            throw new ValidationException("Department cannot be null.");
        }

        if (obj.getName() == null || obj.getName().trim().isEmpty()) {
            throw new ValidationException("Department's name cannot be null or empty.");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sqlInsert(), PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, obj.getName());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (!keys.next()){
                    throw new DBException("No ID generated for department.");
                }
                
                return new Department(keys.getInt(1), obj.getName());
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || "23000".equals(e.getSQLState())) {
                throw new UniqueViolationException(
                    "Unique violation: other rows already have these unique values.", e);
            }
            throw new DBException("Could not execute department insert.", e);
        }
    }

    @Override
    public boolean updateById(Department obj) throws DBException {
        this.validate(obj);

        try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate("Id"))) {
            stmt.setString(1, obj.getName());
            stmt.setInt(2, obj.getId());

            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || "23000".equals(e.getSQLState())) {
                throw new UniqueViolationException(
                    "Unique violation: other rows already have these unique values.", e);
            }

            throw new DBException("Could not execute department update.", e);
        }
    }

    @Override
    public boolean deleteById(Department obj) throws DBException {
        this.validate(obj);
        
        try (PreparedStatement stmt = conn.prepareStatement(sqlDelete("Id"))) {
            stmt.setInt(1, obj.getId());

            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451 || "23000".equals(e.getSQLState())){
                throw new DBIntegrityException(
                    "Foreign key violation: key is still associated with other entities.", e);
            }
            
            throw new DBException("Could not execute department delete.", e);
        }
    }
            
    @Override
    public Department findById(int id) throws DBException {
        if (id < 1) {
            throw new ValidationException("Id cannot be null or less then 1.");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sqlSelect("Id"))) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new Department(
                    rs.getInt("Id"), 
                    rs.getString("Name"));
            }
        } catch (SQLException e) {
            throw new DBException("Could not execute department select by id.", e);
        }
    }

    @Override
    public Department findByName(String name) throws DBException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Department's name cannot be null or empty.");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sqlSelect("Name"))) {
            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new Department(
                    rs.getInt("Id"), 
                    rs.getString("Name"));
            }
        } catch (SQLException e) {
            throw new DBException("Could not execute department select by name.", e);
        }
    }

    @Override
    public List<Department> findAll() throws DBException {
        List<Department> departments = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sqlSelect(null))) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    departments.add(new Department(
                        rs.getInt("Id"), 
                        rs.getString("Name")));
                }

                return departments;
            }
        } catch (SQLException e) {
            throw new DBException("Could not execute department select all.", e);
        }
    }

    @Override
    public int countSellers(String name) throws DBException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Department's name cannot be null or empty.");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sqlSellerCount("Name"))) {
            stmt.setString(1, name);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return -1;
                }

                return rs.getInt("SellerCount");
            }

        } catch (SQLException e) {
            throw new DBException("Could not execute department count Sellers.", e);
        }
    }
}