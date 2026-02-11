package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	Seller insert(Seller obj);
	boolean updateById(Seller obj);
	boolean deleteById(Seller obj);
	Seller findById(int id);
	Seller findByName(String name);
	Seller findByEmail(String email);
	List<Seller> findAll();
	List<Seller> findByDepartment(Department obj);
}