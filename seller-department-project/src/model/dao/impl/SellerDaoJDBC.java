package model.dao.impl;

import java.sql.Connection;
import java.util.List;

import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.DBException;

public class SellerDaoJDBC implements SellerDao {
    private final Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Seller insert(Seller obj) throws DBException {
        // TODO Auto-generated method stub
    }

    @Override
    public Seller update(Seller obj) throws DBException {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean delete(Integer id) throws DBException {
        // TODO Auto-generated method stub
    }

    @Override
    public Seller findById(int id) throws DBException {
        // TODO Auto-generated method stub
    }

    @Override
    public Department findByName(String name) throws DBException {
        // TODO Auto-generated method stub
    }

    @Override
    public Department findByEmail(String email) throws DBException {
        // TODO Auto-generated method stub
    }

    @Override
    public List<Seller> findAll() throws DBException {
        // TODO Auto-generated method stub
    }

    @Override
    public List<Seller> findByDepartment(Department department) throws DBException {
        // TODO Auto-generated method stub
    }
}
