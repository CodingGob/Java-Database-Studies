package application.services;

import java.sql.Connection;
import java.util.List;

import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.DBException;

public class SellerService extends BaseService {
    private final SellerDao sellerDao;
    private final DepartmentDao departmentDao;

    public SellerService(Connection conn, SellerDao sellerDao, DepartmentDao departmentDao) {
        super(conn);
        this.sellerDao = sellerDao;
        this.departmentDao = departmentDao;
    }


    public Seller insertWithDepartment(Department department, Seller seller) throws DBException {
        try {
            beginTransaction();

            seller.setDepartment(departmentDao.insert(department));
            seller = sellerDao.insert(seller);

            commit();

            return seller;
        } catch (Exception e) {
            rollback();
            throw new DBException("Transaction failed.", e);
        } finally {
            endTransaction();
        }
    }

    public Seller insert(Seller obj) throws DBException {
        return sellerDao.insert(obj);
    }

    public boolean updateById(Seller obj) throws DBException {
        return sellerDao.updateById(obj);
    }

    public boolean updateByIdWithDepartment(Department department, Seller seller) throws DBException {
        try {
            beginTransaction();

            seller.setDepartment(departmentDao.insert(department));
            boolean success = sellerDao.updateById(seller);

            commit();

            return success;
        } catch (Exception e) {
            rollback();

            return false;
        } finally {
            endTransaction();
        }
    }

    public boolean deleteById(Seller obj) throws DBException {
        return sellerDao.deleteById(obj);
    }

    public Seller findById(int id) throws DBException {
        return sellerDao.findById(id);
    }

    public Seller findByName(String name) throws DBException {
        return sellerDao.findByName(name);
    }

    public Seller findByEmail(String email) throws DBException {
        return sellerDao.findByEmail(email);
    }

    public List<Seller> findAll() throws DBException {
        return sellerDao.findAll();
    }

    public List<Seller> findByDepartment(Department obj) throws DBException {
        return sellerDao.findByDepartment(obj);
    }
}