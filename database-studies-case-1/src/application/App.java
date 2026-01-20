package application;

import java.util.Scanner;

import DAO.DepartmentDAO;
import connection.MySQLConnection;
import entities.Department;

public class App {
    public static void main(String[] args) throws Exception {
        try (var conn = MySQLConnection.getConnection()) {
            Scanner sc = new Scanner(System.in);
            String departmentName;
            int sellerCount;
            int departmentId;
            Department department;
            
            // Testing sellerCount() from DepartmentDAO
            System.out.println("========= TESTING sellerCount() FROM DEPARTMENTDAO =========");
            System.out.println("Enter 'q' to stop.");
            System.out.println();
            departmentName = "";
            while (true) {
                System.out.print("Inform the name of the department: ");
                departmentName = sc.nextLine();
                if (departmentName.toLowerCase().equals("q")) {
                    break;
                }
                sellerCount = DepartmentDAO.sellerCount(conn, departmentName);
                switch (sellerCount){
                    case -1:
                        System.out.println("Error: There is no '" + departmentName + "' department.");
                        break;
                    case 0:
                        System.out.println("There are no sellers in the '" + departmentName + "' department");
                        break;
                    case 1:
                        System.out.println("There is " + sellerCount + " seller in the '" + departmentName + "' department.");
                        break;
                    default:
                        System.out.println("There are " + sellerCount + " sellers in the '" + departmentName + "' department.");
                        break;
                    }
                System.out.println();
            }

            // Testing sellerCountAll() from DepartmentDAO
            System.out.println("========= TESTING sellerCountAll() FROM DEPARTMENTDAO =========");
            System.out.println();
            System.out.println("Total number of sellers in the company: ");
            System.out.print(DepartmentDAO.sellerCountAll(conn));
            System.out.println();

            // Testing findByName() from DepartmentDAO
            System.out.println("========= TESTING findByName() FROM DEPARTMENTDAO =========");
            System.out.println("Enter 'q' to stop.");
            System.out.println();
            departmentName = "";
            while (true) {
                System.out.print("Inform the name of the department: ");
                departmentName = sc.nextLine();
                if (departmentName.toLowerCase().equals("q")) {
                    break;
                }
                department = DepartmentDAO.findByName(conn, departmentName);
                if (department == null) {
                    System.out.println("There is no '" + departmentName + "' department.");
                    System.out.println();
                } else {
                    System.out.println("Department '" + department.getName() + "' found, id = " + department.getId() + ".");
                }
                System.out.println();
            }

            // Testing findById() from DepartmentDAO
            System.out.println("========= TESTING findById() FROM DEPARTMENTDAO =========");
            System.out.println("Enter 0 to stop.");
            System.out.println();
            departmentId = -1;
            while (true) {
                System.out.print("Inform the id of the department: ");
                departmentId = sc.nextInt();
                if (departmentId == 0) {
                    break;
                }
                department = DepartmentDAO.findById(conn, departmentId);
                if (department == null) {
                    System.out.println("There is no department with id = " + departmentId + ".");
                } else {
                    System.out.println("Department with id = " + departmentId + " found, name = '" + department.getName() + "'.");
                }
                System.out.println();
            }
            
            sc.nextLine();
            
            // Testing insert() from DepartmentDAO
            System.out.println("========= TESTING insert() FROM DEPARTMENTDAO =========");
            System.out.println("Enter 'q' to stop.");
            System.out.println();
            departmentName = "";
            while (true) {
                System.out.print("Inform the name of the department: ");
                departmentName = sc.nextLine();
                if (departmentName.toLowerCase().equals("q")) {
                    break;
                }
                if (departmentName.length() > 1) {
                    departmentName = departmentName.toUpperCase().charAt(0) + departmentName.substring(1).toLowerCase();
                } else if (departmentName.length() == 1) {
                    departmentName = departmentName.toUpperCase();
                }
                department = new Department(departmentName);
                department = DepartmentDAO.insert(conn, department);
                if (department == null) {
                    System.out.println("Department '" + departmentName + "' already exists.");
                } else {
                    System.out.println("Department '" + department.getName() + "' inserted with id = " + department.getId() + "'.");
                }
                System.out.println();
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}