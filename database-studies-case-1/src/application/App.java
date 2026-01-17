package application;

import java.util.Scanner;

import DAO.DepartmentDAO;
import connection.MySQLConnection;

public class App {
    public static void main(String[] args) throws Exception {
        try (var conn = MySQLConnection.getConnection()) {
            int sellerCount;
            String department;
            
            try (Scanner sc = new Scanner(System.in)){
                System.out.print("Inform the name of the depatment: ");
                department = sc.nextLine();
            }

            sellerCount = DepartmentDAO.sellerCount(conn, department);
            switch (sellerCount){
                case -1:
                    System.out.println("Error: There is no '" + department + "' department.");
                    break;
                case 0:
                    System.out.println("There are no sellers in the '" + department + "' department");
                    break;
                case 1:
                    System.out.println("There is " + sellerCount + " seller in the '" + department + "' department.");
                    break;
                default:
                    System.out.println("There are " + sellerCount + " sellers in the '" + department + "' department.");
                    break;
            }
            
            System.out.println();
            System.out.print(DepartmentDAO.sellerCountAll(conn));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}