package model.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.DBException;
import model.exceptions.DBIntegrityException;
import model.exceptions.UniqueViolationException;
import model.exceptions.ValidationException;

public class SellerDaoJDBC implements SellerDao {
    private final Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }


    private String sqlSelect(String column) {
        if (column == null) {
            return "SELECT " 
                    + "s.Id AS SellerId, " 
                    + "s.Name AS SellerName, "
                    + "s.Email, "
                    + "s.BirthDate, "
                    + "s.BaseSalary, "
                    + "s.DepartmentId, "
                    + "d.Name AS DepartmentName "
                + "FROM seller AS s "
                + "LEFT JOIN department AS d "
                    + "ON s.DepartmentId = d.Id";
        }

        return "SELECT " 
                + "s.Id AS SellerId, " 
                + "s.Name AS SellerName, "
                + "s.Email, "
                + "s.BirthDate, "
                + "s.BaseSalary, "
                + "s.DepartmentId, "
                + "d.Name AS DepartmentName "
            + "FROM seller AS s "
            + "LEFT JOIN department AS d "
                + "ON s.DepartmentId = d.Id "
            + "WHERE " + column + " = ?";
    }
    
    private String sqlUpdate(String column) {
        return "UPDATE "
        + "seller SET "
            + "Name = ?, "
            + "Email = ?, "
            + "BirthDate = ?, "
            + "BaseSalary = ?, "
            + "DepartmentId = ? "
        + "WHERE " + column + " = ?";
    }
        
    private String sqlDelete(String column) {
        return "DELETE FROM seller WHERE " + column + " = ?";
    } 
    
    private String sqlInsert() {
        return "INSERT "
        + "INTO seller ("
            + "Name, "
            + "Email, "
            + "BirthDate, "
            + "BaseSalary, "
            + "DepartmentId) "
        + "VALUES (?, ?, ?, ?, ?)";
    }


    private void validateObj(Seller obj) throws DBException {
        if (obj == null) {
            throw new ValidationException("Seller cannot be null.");
        }

        if (obj.getId() == null || obj.getId() < 1) {
            throw new ValidationException("Seller's Id cannot be null or less then 1.");
        }

        if (obj.getName() == null || obj.getName().trim().isEmpty()) {
            throw new ValidationException("Seller's name cannot be null or empty.");
        }

        if (obj.getEmail() == null || obj.getEmail().trim().isEmpty()) {
            throw new ValidationException("Seller's email cannot be null or empty.");
        }

        if (obj.getBirthDate() == null) {
            throw new ValidationException("Seller's birth date cannot be null or empty.");
        }

        if (obj.getBaseSalary() == null || obj.getBaseSalary().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Seller's base salary cannot be null or less than 0.");
        }

        if (obj.getDepartment() == null || obj.getDepartment().getId() == null || obj.getDepartment().getId() < 1) {
            throw new ValidationException("Seller's department cannot be null and must have a valid id.");
        }
    }

    private void validateObjData(Seller obj) throws DBException {
        if (obj == null) {
            throw new ValidationException("Seller cannot be null.");
        }

        if (obj.getName() == null || obj.getName().trim().isEmpty()) {
            throw new ValidationException("Seller's name cannot be null or empty.");
        }

        if (obj.getEmail() == null || obj.getEmail().trim().isEmpty()) {
            throw new ValidationException("Seller's email cannot be null or empty.");
        }

        if (obj.getBirthDate() == null) {
            throw new ValidationException("Seller's birth date cannot be null or empty.");
        }

        if (obj.getBaseSalary() == null || obj.getBaseSalary().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Seller's base salary cannot be null or less than 0.");
        }

        if (obj.getDepartment() == null || obj.getDepartment().getId() == null || obj.getDepartment().getId() < 1) {
            throw new ValidationException("Seller's department cannot be null and must have a valid id.");
        }
    }

    private Seller creatSeller(ResultSet rs) throws DBException {
        try {

            Department department = new Department(
                rs.getInt("DepartmentId"),
                rs.getString("DepartmentName"));
                
            return new Seller(
                rs.getInt("SellerId"), 
                rs.getString("SellerName"), 
                rs.getString("Email"), 
                rs.getDate("BirthDate").toLocalDate(), 
                rs.getBigDecimal("BaseSalary"), 
                department);
        } catch (SQLException e) {
            throw new DBException("Could not create new seller.", e);
        }
    }

    private void setFullStatement(PreparedStatement stmt, Seller obj) throws DBException {
        try {
            stmt.setString(1, obj.getName());
            stmt.setString(2, obj.getEmail());
            stmt.setDate(3, java.sql.Date.valueOf(obj.getBirthDate()));
            stmt.setBigDecimal(4, obj.getBaseSalary());
            stmt.setInt(5, obj.getDepartment().getId());
        } catch (SQLException e) {
            throw new DBException("Could not set statement parameters.", e);
        }
    }


    @Override
    public Seller insert(Seller obj) throws DBException {
        validateObjData(obj);

        try (PreparedStatement stmt = conn.prepareStatement(sqlInsert(), PreparedStatement.RETURN_GENERATED_KEYS)) {
            setFullStatement(stmt, obj);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (!keys.next()){
                    throw new DBException("No ID generated for department.");
                }
                
                return new Seller(
                    keys.getInt(1), 
                    obj.getName(),
                    obj.getEmail(),
                    obj.getBirthDate(),
                    obj.getBaseSalary(),
                    obj.getDepartment());
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
    public boolean updateById(Seller obj) throws DBException {
        validateObj(obj);

        try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate("Id"))) {
            setFullStatement(stmt, obj);
            stmt.setInt(6, obj.getId());
            
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
    public boolean deleteById(Seller obj) throws DBException {
        validateObj(obj);
        
        try (PreparedStatement stmt = conn.prepareStatement(sqlDelete("Id"))) {
            stmt.setInt(1, obj.getId());

            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451 || "23000".equals(e.getSQLState())){
                throw new DBIntegrityException(
                    "Foreign key violation: key is still associated with other entities.", e);
            }
            
            throw new DBException("Could not execute seller delete.", e);
        }
    }

    @Override
    public Seller findById(int id) throws DBException {
        if (id < 1) {
            throw new ValidationException("Seller's department id cannot be less 1.");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sqlSelect("s.Id"))) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return creatSeller(rs);
            }
        } catch (SQLException e) {
            throw new DBException("Could not execute seller find by id.", e);
        }
    }

    @Override
    public Seller findByName(String name) throws DBException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Seller's name cannot be null or empty.");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sqlSelect("s.Name"))) {
            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return creatSeller(rs);
            }
        } catch (SQLException e) {
            throw new DBException("Could not execute seller find by name.", e);
        }
    }

    @Override
    public Seller findByEmail(String email) throws DBException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Seller's email cannot be null or empty.");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sqlSelect("s.Email"))) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return creatSeller(rs);
            }
        } catch (SQLException e) {
            throw new DBException("Could not execute seller find by email.", e);
        }
    }

    @Override
    public List<Seller> findAll() throws DBException {
        List <Seller> sellers = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sqlSelect(null))) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Seller seller = creatSeller(rs);
                    sellers.add(seller);
                }
            }

            return sellers;
        } catch (SQLException e) {
            throw new DBException("Could not execute seller find all.", e);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department obj) throws DBException {
        if (obj == null) {
            throw new ValidationException("Department cannot be null.");
        }

        if (obj.getName() == null || obj.getName().trim().isEmpty()) {
            throw new ValidationException("Department's name cannot be null or empty.");
        }

        List <Seller> sellers = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sqlSelect("d.Name"))) {
            stmt.setString(1, obj.getName());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Seller seller = creatSeller(rs);
                    sellers.add(seller);
                }
            }
            
            return sellers;
        } catch (SQLException e) {
            throw new DBException("Could not execute seller find by department.", e);
        }
    }
}