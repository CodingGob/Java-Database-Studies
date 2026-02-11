package application.services;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import model.dao.DepartmentDao;
import model.entities.Department;
import model.exceptions.DBException;


public class DepartmentService extends BaseService {
    private final DepartmentDao departmentDao;

    public DepartmentService(Connection conn, DepartmentDao departmentDao) {
        super(conn);
        this.departmentDao = departmentDao;
    }


    public Department insert(Department obj) throws DBException {
        return departmentDao.insert(obj);
    }

    public boolean updateById(Department obj) throws DBException {
        return departmentDao.updateById(obj);
    }

    public boolean deleteById(Department obj) throws DBException {
        return departmentDao.deleteById(obj);
    }

    public Department findById(int id) throws DBException {
        return departmentDao.findById(id);
    }

    public Department findByName(String name) throws DBException {
        return departmentDao.findByName(name);
    }

    public List<Department> findAll() throws DBException {
        return departmentDao.findAll();
    }

    public int countSellersByName(String name) throws DBException {
        return departmentDao.countSellersByName(name);
    }    

    public Map<Department, Integer> countAllSellers() throws DBException {
        return departmentDao.countAllSellers();
    }
}