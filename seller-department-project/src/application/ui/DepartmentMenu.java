package application.ui;

import java.util.List;
import java.util.Scanner;

import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.exceptions.DBException;

public class DepartmentMenu extends BaseMenu {
    public DepartmentMenu(DepartmentDao departmentDao, SellerDao sellerDao){
        super(departmentDao, sellerDao);
    }

    @Override
    public MenuAction show(Scanner sc) throws DBException {
        MenuAction action = MenuAction.CONTINUE;
        String input;
        String menuOptions = """

        =============== DEPARTMENT MENU ===============
        1. Search department by ID
        2. Search department by Name
        3. Show all departments
        4. Count sellers in a department
        5. Insert new department
        6. Update department
        7. Delete department

        B. Back to Main Menu
        Q. Quit
        """;

        while (action != MenuAction.EXIT) {
            System.out.println(menuOptions);
            System.out.print("Select an option: ");
            input = sc.nextLine();

            switch (input.toUpperCase().trim()) {
                case "1":
                    action = findById(sc);
                    break;
                case "2":
                    action = findByName(sc);
                    break;
                case "3":
                    action = findAll(sc);
                    break;
                case "4":
                    action = countSellers(sc);
                    break;
                case "5":
                    action = insert(sc);
                    break;
                case "6":
                    action = updateById(sc);
                    break;
                case "7":
                    action = deleteById(sc);
                    break;
                case "B":
                case "BACK":
                    return MenuAction.BACK;
                case "Q":
                case "QUIT":
                    return MenuAction.EXIT;
                default:
                    System.out.println("Invalid option. Please try again.");
                    continue;
            }
        }

        return action;
    }


    private MenuAction findById(Scanner sc) throws DBException {
        MenuAction action;
        String input;
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
            input = sc.nextLine();

            if (input.trim().isEmpty()){
                System.out.println("Department ID cannot be empty. Please try again.");
                continue;
            }

            action = bOrQCheck(input);
            if (action != MenuAction.CONTINUE) {
                return action;
            }

            try {
                departmentId = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Please enter a valid integer.");
                continue;
            }

            if (departmentId < 1) {
                System.out.println("ID cannot be less then 1. Please try again.");
                continue;
            }

            department = departmentDao.findById(departmentId);

            if (department == null) {
                System.out.println("There is no department with ID = " + departmentId + ".");
            } else {
                System.out.println("Department found: " + department);
            }
        }
    }

    private MenuAction findByName(Scanner sc) throws DBException {
        MenuAction action;
        String input;
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
            input = sc.nextLine();

            if (input.trim().isEmpty()) {
                System.out.println("Department name cannot be empty. Please try again.");
                continue;
            }

            action = bOrQCheck(input);            
            if (action != MenuAction.CONTINUE) {
                return action;
            }
            
            departmentName = fixName(input);
            department = departmentDao.findByName(departmentName);
            if (department == null) {
                System.out.println("There is no '" + departmentName + "' department.");
            } else {
                System.out.println("Department found: " + department);
            }
        }
    }

    private MenuAction findAll(Scanner sc) throws DBException {
        System.out.println("\n========= SHOWING ALL DEPARTMENTS =========");

        List<Department> departments = departmentDao.findAll();
        if (departments.isEmpty()) {
            System.out.println("Department table is empty.");
        } else {            
            for (Department department : departments) {
                System.out.println(department);
            }
        }

        return bOrQMenu(sc);
    }

    private MenuAction countSellers(Scanner sc) throws DBException {
        MenuAction action;
        String input;
        String departmentName;
        int sellerCount;

        String menuOptions = """

            ========= DEPARTMENT COUNT SELLERS =========
            B. Back to Department Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);

            System.out.print("Inform the department's name: ");
            input = sc.nextLine();

            if (input.trim().isEmpty()){
                System.out.println("Department name cannot be empty. Please try again.");
                continue;
            }
            
            action = bOrQCheck(input);
            if (action != MenuAction.CONTINUE) {
                return action;
            }
            
            departmentName = fixName(input);
            sellerCount = departmentDao.countSellers(departmentName);
            if (sellerCount == -1) {
                System.out.println("There is no '" + departmentName + "' department.");
            } else {
                System.out.println("Number of sellers in the '" + departmentName + "' department = " + sellerCount);
            }
        }
    }

    private MenuAction insert(Scanner sc) throws DBException {
        MenuAction action;
        String departmentName;
        Department department;
        String input;

        String menuOptions = """

            =========== INSERT NEW DEPARTMENT ===========
            B. Back to Department Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);
            System.out.print("Inform the name of the department: ");
            input = sc.nextLine();

            if (input.trim().isEmpty()) {
                System.out.println("Department name cannot be empty. Please try again.");
                continue;
            }

            action = bOrQCheck(input);
            if (action != MenuAction.CONTINUE) {
                return action;
            }
            
            departmentName = fixName(input);
            department = departmentDao.findByName(departmentName);
            if (department != null) {
                System.out.println("Insertion canceled. There is already a department by this name: " + department);
            } else {
                department = departmentDao.insert(new Department(null, departmentName));
                System.out.println("Department successfully inserted: " + department);
            }
        }
    }

    private MenuAction updateById(Scanner sc) throws DBException {
        MenuAction action;
        String input;
        int departmentId;
        String newName;
        Department oldDepartment;
        Department newDepartment;
        Department comparativeDepartment;

        String menuOptions = """

            ========= UPDATE DEPARTMENT BY ID =========
            B. Back to Department Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);
            System.out.print("Enter the department's ID to update: ");
            input = sc.nextLine();

            if (input.trim().isEmpty()){
                System.out.println("Department ID cannot be empty. Please try again.");
                continue;
            }

            action = bOrQCheck(input);
            if (action != MenuAction.CONTINUE) {    
                return action;
            }

            try {
                departmentId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Please enter a valid integer.");
                continue;
            }

            if (departmentId < 1){
                System.out.println("ID cannot be less then 1. Please try again.");
                continue;
            }

            oldDepartment = departmentDao.findById(departmentId);
            if (oldDepartment == null) {
                System.out.println("There is no department with ID = " + departmentId + ".");
                continue;
            }

            System.out.println();
            System.out.println("Department's current data: " + oldDepartment);
            System.out.print("Are you sure you want to update it? (Y/N): ");
            input = sc.nextLine();

            if (input.equalsIgnoreCase("N") || input.equalsIgnoreCase("NO")) {
                System.out.println("Update canceled.");
                continue;
            } else if (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("YES")) {
                System.out.println("Invalid option. Update canceled.");
                continue;
            }

            System.out.println();
            System.out.print("Enter the new name for the department: ");
            input = sc.nextLine();

            if (input.trim().isEmpty()) {
                System.out.println("Department name cannot be empty. Please try again.");
                continue;
            }

            action = bOrQCheck(input);
            if (action != MenuAction.CONTINUE) {
                return action;
            }

            newName = fixName(input);
            comparativeDepartment = departmentDao.findByName(newName);
            if (comparativeDepartment != null) {
                System.out.println("Update canceled. There is already a department by this name: " + comparativeDepartment);
                continue;
            }
            
            newDepartment = new Department(oldDepartment.getId(), newName);
            if (departmentDao.updateById(newDepartment)) {
                System.out.println();
                System.out.println("Department successfully updated.");
                System.out.println("From: " + oldDepartment);
                System.out.println("To: " + newDepartment);
            }
        }
    }

    private MenuAction deleteById(Scanner sc) throws DBException {
        MenuAction action;
        String input;
        int departmentId;
        Department department;
        int sellerCount;

        String menuOptions = """

            ========= DELETE DEPARTMENT BY ID =========
            B. Back to Department Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);
            System.out.print("Enter the department's ID to delete: ");
            input = sc.nextLine();

            action = bOrQCheck(input);
            if (action != MenuAction.CONTINUE) {    
                return action;
            }

            try {
                departmentId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Please enter a valid integer.");
                continue;
            }

            if (departmentId < 1){
                System.out.println("ID cannot be less then 1. Please try again.");
                continue;
            }

            department = departmentDao.findById(departmentId);
            if (department == null) {
                System.out.println("Department with ID = " + departmentId + " not found.");
                continue;
            }

            System.out.println();
            
            sellerCount = departmentDao.countSellers(department.getName());
            if (sellerCount > 0) {
                System.out.println("Number of sellers in the '" + department.getName() + "' department = " + sellerCount);
                System.out.println("Deletion canceled. Department cannot be removed while there are still sellers attributed to it.");
                continue;
            } 

            System.out.println("Department found: " + department);
            System.out.print("Are you sure you want to delete it? (Y/N): ");
            input = sc.nextLine();
            if (input.equalsIgnoreCase("N") || input.equalsIgnoreCase("NO")) {
                System.out.println("Deletion canceled.");
                continue;
            } else if (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("YES")) {
                System.out.println("Invalid option. Deletion canceled.");
                continue;
            }

            if (departmentDao.deleteById(department)) {
                System.out.println();
                System.out.println("Department '" + department.getName() + "' successfully deleted.");
            }
        }
    }


    private MenuAction bOrQMenu(Scanner sc) {
        String menuOptions = """
            
        ===========================================
        B. Back to Department Menu
        Q. Quit
        """;
        
        System.out.println(menuOptions);
        System.out.print("Select an option: ");
        String input = sc.nextLine();
        
        MenuAction action = bOrQCheck(input);
        if (action == MenuAction.CONTINUE) {
            System.out.println("Invalid option. Returning to Department Menu.");
            return MenuAction.BACK;
        }

        return action;
    }
}

