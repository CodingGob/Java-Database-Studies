package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	Seller insert(Seller obj);
	boolean updateById(Seller obj);
	boolean deleteById(Seller obj);
	Seller findById(int id);
	Department findByName(String name);
	Department findByEmail(String email);
	List<Seller> findAll();
	List<Seller> findByDepartment(Department department);
}