package application;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import DAO.DepartmentDAO;
import connection.MySQLConnection;
import connection.MySQLException;
import entities.Department;
import util.MenuAction;

public class App {
    public static void main(String[] args) throws Exception {
        try (
            Scanner sc = new Scanner(System.in);
            Connection conn = MySQLConnection.getConnection()
        ) {
            System.out.println("Company Management System 0.0.1");
            MenuAction action = mainMenu(sc, conn);
            if (action == MenuAction.EXIT) {
                System.out.println("\nClosing the application...");
            }
        } catch (MySQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static String fixName(String name) {
        String[] parts = name.split(" ");
        StringBuilder fixedName = new StringBuilder();

        for (String part : parts) {
            switch (part.length()) {
                case 0:
                    break;
                case 1:
                    part = part.toUpperCase();
                    fixedName.append(part);
                    break;            
                default:
                    part = part.toUpperCase().charAt(0) + part.substring(1).toLowerCase();
                    fixedName.append(part);
                    break;
            }
            fixedName.append(" ");
        }

        fixedName.deleteCharAt(fixedName.length() - 1); // Remove last space
        return fixedName.toString();
    }

    public static MenuAction mainMenu(Scanner sc, Connection conn) throws MySQLException {
        String menuOptions = """

                ================= MAIN MENU =================
                1. Department Menu
                2. Seller Menu

                Q. Quit
                """;

        while (true) {
            System.out.println(menuOptions);
            System.out.print("Select an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    if (departmentMenu(sc, conn) == MenuAction.EXIT) {
                        return MenuAction.EXIT;
                    }
                    break;
                case "2":
                    // sellerMenu(sc, conn); // Implement sellerMenu similarly
                    System.out.println("Seller Menu is under construction.");
                    break;
                case "Q":
                case "q":
                    return MenuAction.EXIT;
                default:
                    System.out.println("Invalid option. Please try again.");
            }            
        }
    }

    public static MenuAction departmentMenu(Scanner sc, Connection conn) throws MySQLException {
        String menuOptions = """

                =============== DEPARTMENT MENU ===============
                1. Search department by ID
                2. Search department by Name
                3. Show all departments
                4. Count sellers in given department
                5. Count all sellers in the company by department
                6. Insert new department
                7. Update by ID
                9. Update by Name
                8. Delete by ID
                10. Delete by Name

                B. Back to Main Menu
                Q. Quit
                """;

        while (true) {
            System.out.println(menuOptions);
            System.out.print("Select an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    if (depFindById(sc, conn) == MenuAction.EXIT) {
                        return MenuAction.EXIT;
                    }
                    break;
                case "2":
                    if (depFindByName(sc, conn) == MenuAction.EXIT) {
                        return MenuAction.EXIT;
                    }
                    break;
                case "3":
                    if (depShowAll(sc, conn) == MenuAction.EXIT) {
                        return MenuAction.EXIT;
                    }
                    break;
                case "4":
                    if (depSellerCount(sc, conn) == MenuAction.EXIT) {
                        return MenuAction.EXIT;
                    }
                    break;
                case "5":
                    if (depSellerCountAll(sc, conn) == MenuAction.EXIT) {
                        return MenuAction.EXIT;
                    }
                    break;
                case "6":
                    if (depInsert(sc, conn) == MenuAction.EXIT) {
                        return MenuAction.EXIT;
                    }
                    break;
                case "7":
                    // Implement updateById functionality
                    break;
                case "8":
                    // Implement deleteById functionality
                    break;
                case "9":
                    // Implement updateByName functionality
                    break;
                case "10":
                    // Implement deleteByName functionality
                    break;
                case "B":
                case "b":
                    return MenuAction.BACK;
                case "Q":
                case "q":
                    return MenuAction.EXIT;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
    }

    public static MenuAction depFindById (Scanner sc, Connection conn) throws MySQLException {
        int departmentId;
        Department department;

        String menuOptions = """

            ========= FIND DEPARTMENT BY ID =========
            B. Back to Department Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);

            System.out.print("Inform the department's ID: ");
            String input = sc.nextLine();

            if (input.toUpperCase().equals("Q")) {
                return MenuAction.EXIT;
            }

            if (input.toUpperCase().equals("B")) {
                return MenuAction.BACK;
            }

            try {
                departmentId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Please enter a valid integer.");
                continue;
            }

            department = DepartmentDAO.findById(conn, departmentId);
            if (department == null) {
                System.out.println("There is no department with ID = " + departmentId + ".");
            } else {
                System.out.println("Department with ID = " + departmentId + " found, name = '" + department.getName() + "'.");
            }
            System.out.println();
        }
    }

    public static MenuAction depFindByName (Scanner sc, Connection conn) throws MySQLException {
        String departmentName;
        Department department;

        String menuOptions = """

            ======== FIND DEPARTMENT BY NAME ========
            B. Back to Department Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);

            System.out.print("Inform the department's name: ");
            departmentName = sc.nextLine();
            departmentName = fixName(departmentName);

            if (departmentName.equals("Q")) {
                return MenuAction.EXIT;
            }

            if (departmentName.equals("B")) {
                return MenuAction.BACK;
            }

            
            department = DepartmentDAO.findByName(conn, departmentName);
            if (department == null) {
                System.out.println("There is no '" + departmentName + "' department.");
            } else {
                System.out.println("Department '" + department.getName() + "' found, ID = " + department.getId() + ".");
            }
            System.out.println();
        }
    }

    public static MenuAction depShowAll(Scanner sc, Connection conn) throws MySQLException {
        System.out.println("\n========= SHOWING ALL DEPARTMENTS =========");

        List<Department> departments = DepartmentDAO.showAll(conn);
        if (departments.isEmpty()) {
            System.out.println("No departments found.");
        } else {            
            for (Department department : departments) {
                System.out.println("ID = " + department.getId() + ", Name = '" + department.getName() + "'");
            }
        }

        String menuOptions = """

            ===========================================
            B. Back to Department Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);
            System.out.print("Select an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "B":
                case "b":
                    return MenuAction.BACK;
                case "Q":
                case "q":
                    return MenuAction.EXIT;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public static MenuAction depSellerCount(Scanner sc, Connection conn) throws MySQLException {
        String departmentName;
        int sellerCount;

        String menuOptions = """

            ========= DEPARTMENT SELLER COUNT =========
            B. Back to Department Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);

            System.out.print("Inform the department's name: ");
            departmentName = sc.nextLine();
            departmentName = fixName(departmentName);

            if (departmentName.equals("Q")) {
                return MenuAction.EXIT;
            }

            if (departmentName.equals("B")) {
                return MenuAction.BACK;
            }

            sellerCount = DepartmentDAO.sellerCount(conn, departmentName);
            switch (sellerCount){
                case -1:
                    System.out.println("There is no '" + departmentName + "' department.");
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
        }
    }

    public static MenuAction depSellerCountAll(Scanner sc, Connection conn) throws MySQLException {
        System.out.println("\n======== SELLER COUNT BY DEPARTMENT ========");
        
        Map<String, Integer> result = DepartmentDAO.sellerCountAll(conn);
        if (result.isEmpty()) {
            System.out.println("No departments found.");
        } else {
            for (Map.Entry<String, Integer> entry : result.entrySet()) {
                System.out.println("'" + entry.getKey() + "', Seller Count = " + entry.getValue());
            }
        }

        String menuOptions = """

            ============================================
            B. Back to Department Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);
            System.out.print("Select an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "B":
                case "b":
                    return MenuAction.BACK;
                case "Q":
                case "q":
                    return MenuAction.EXIT;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public static MenuAction depInsert(Scanner sc, Connection conn) throws MySQLException {
        String departmentName;
        Department department;

        String menuOptions = """

            =========== INSERT NEW DEPARTMENT ===========
            B. Back to Department Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);

            System.out.print("Inform the name of the department: ");
            departmentName = sc.nextLine();
            departmentName = fixName(departmentName);

            if (departmentName.equals("Q")) {
                return MenuAction.EXIT;
            }

            if (departmentName.equals("B")) {
                return MenuAction.BACK;
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
    }
}