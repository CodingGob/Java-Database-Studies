package model.dao;


import java.util.List;
import java.util.Map;

import model.entities.Department;

public interface DepartmentDao {
	Department insert(Department obj);
	boolean updateById(Department obj);
	boolean deleteById(Department obj);
	Department findById(int id);
	Department findByName(String name);
	List<Department> findAll();
	int countSellersByName(String name);
	Map<Department, Integer> countAllSellers();
}