package application;

import java.sql.Connection;
import java.util.Scanner;

import application.services.DepartmentService;
import application.services.SellerService;
import application.ui.MainMenu;
import db.MySQLConnection;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;


public class App {
    public static void main(String[] args) throws Exception {
        try (
            Connection conn = MySQLConnection.getConnection();
            Scanner sc = new Scanner(System.in)
        ) {
            DepartmentDao departmentDao = DaoFactory.createDepartmentDao(conn);
            SellerDao sellerDao = DaoFactory.createSellerDao(conn);

            DepartmentService departmentService = new DepartmentService(conn, departmentDao);
            SellerService sellerService = new SellerService(conn, sellerDao, departmentDao);

            System.out.println();
            System.out.println("Company Management System 0.0.1");
            new MainMenu(departmentService, sellerService).show(sc);

            System.out.println("\nClosing the application...");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}