package application.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import application.services.DepartmentService;
import application.services.SellerService;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.DBException;

public class SellerMenu extends BaseMenu {

    public SellerMenu(DepartmentService departmentService, SellerService sellerService) {
        super(departmentService, sellerService);
    }

    @Override
    public MenuAction show(Scanner sc) throws DBException {
        MenuAction action = MenuAction.CONTINUE;
        String input;
        String menuOptions = """

        ================= SELLER MENU =================
        1. Search seller by ID
        2. Search seller by name
        3. Search seller by e-mail
        4. Show all sellers
        5. Show sellers in a department
        6. Insert new seller
        7. Update seller by ID
        8. Delete seller by ID

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
                    action = findByEmail(sc);
                    break;
                case "4":
                    action = findAll(sc);
                    break;
                case "5":
                    action = findByDepartment(sc);
                    break;
                case "6":
                    action = insert(sc);
                    break;
                case "7":
                    action = updateById(sc);
                    break;
                case "8":
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
        int sellerId;
        Seller seller;

        String menuOptions = """

            ============== FIND SELLER BY ID ==============
            B. Back to Seller Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);
            System.out.print("Inform the sellers's ID: ");
            input = sc.nextLine();

            if (input.trim().isEmpty()){
                System.out.println("Seller's ID cannot be empty. Please try again.");
                continue;
            }

            action = bOrQCheck(input);
            if (action != MenuAction.CONTINUE) {
                return action;
            }

            try {
                sellerId = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Please enter a valid integer.");
                continue;
            }

            if (sellerId < 1) {
                System.out.println("ID cannot be less then 1. Please try again.");
                continue;
            }

            seller = sellerService.findById(sellerId);

            if (seller == null) {
                System.out.println("There is no seller with ID = " + sellerId + ".");
            } else {
                System.out.println("Seller found: " + seller);
            }
        }
    }

    private MenuAction findByName(Scanner sc) throws DBException {
        MenuAction action;
        String input;
        String sellerName;
        Seller seller;

        String menuOptions = """

            ============= FIND SELLER BY NAME =============
            B. Back to Seller Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);
            System.out.print("Inform the seller's name: ");
            input = sc.nextLine();

            if (input.trim().isEmpty()) {
                System.out.println("Seller's name cannot be empty. Please try again.");
                continue;
            }

            action = bOrQCheck(input);            
            if (action != MenuAction.CONTINUE) {
                return action;
            }
            
            sellerName = fixName(input);
            seller = sellerService.findByName(sellerName);
            if (seller == null) {
                System.out.println("There is no seller by the name of '" + sellerName + "'.");
            } else {
                System.out.println("Seller found: " + seller);
            }
        }
    }

    private MenuAction findByEmail(Scanner sc) throws DBException {
        MenuAction action;
        String input;
        String email;
        Seller seller;

        String menuOptions = """

            ============ FIND SELLER BY E-MAIL ============
            B. Back to Seller Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);
            System.out.print("Inform the seller's e-mail: ");
            input = sc.nextLine();

            if (input.trim().isEmpty()) {
                System.out.println("Seller's e-mail cannot be empty. Please try again.");
                continue;
            }

            action = bOrQCheck(input);            
            if (action != MenuAction.CONTINUE) {
                return action;
            }
            
            email = input.trim().replace(" ", "");
            seller = sellerService.findByEmail(email);
            if (seller == null) {
                System.out.println("There is no seller with the e-mail '" + email + "'.");
            } else {
                System.out.println("Seller found: " + seller);
            }
        }
    }

    private MenuAction findAll (Scanner sc) throws DBException {
        List<Seller> sellers = sellerService.findAll();

        System.out.println("\n============= SHOWING ALL SELLERS =============\n");
        if (sellers.isEmpty()) {
            System.out.println("Seller table is empty.");
        } else {
            sellers.forEach(System.out::println);
        }

        return bOrQMenu(sc);
    }

    private MenuAction findByDepartment (Scanner sc) throws DBException {
        List<Seller> sellers = new ArrayList<>();
        MenuAction action;
        String input;
        String departmentName;
        Department department;

        String menuOptions = """

            ========== SHOW SELLERS BY DEPARTMENT ==========
            B. Back to Seller Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);
            System.out.print("Inform the department's name: ");
            input = sc.nextLine();

            if (input.trim().isEmpty()) {
                System.out.println("Department's name cannot be empty. Please try again.");
                continue;
            }

            action = bOrQCheck(input);            
            if (action != MenuAction.CONTINUE) {
                return action;
            }
            
            departmentName = fixName(input);
            department = departmentService.findByName(departmentName);
            if (department == null) {
                System.out.println("There is no '" + departmentName + "' department.");
                continue;
            } else {
                sellers = sellerService.findByDepartment(department);
            }

            if (sellers.isEmpty()) {
                System.out.println("There are no seller in the '" + departmentName + "' department.");
                continue;
            } else {
                System.out.println();
                System.out.println("Sellers in the '" + departmentName + "' department:");
                sellers.forEach(System.out::println);
                continue;
            }
        }
    }

    private MenuAction insert(Scanner sc) throws DBException {
        MenuAction action;
        String input;
        String sellerName;
        String email;
        LocalDate birthDate;
        BigDecimal baseSalary;
        String departmentName;
        Department department;
        Seller seller;

        String menuOptions = """

            ============== INSERT NEW SELLER ==============
            B. Back to Seller Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);

            while (true) {
                System.out.print("Inform the seller's name: ");
                input = sc.nextLine();
                
                if (input.trim().isEmpty()) {
                    System.out.println("Seller's name cannot be empty. Please try again.");
                    System.out.println();
                    continue;
                }
                
                action = bOrQCheck(input);            
                if (action != MenuAction.CONTINUE) {
                    return action;
                }
                
                sellerName = fixName(input);
                break;
            }

            while (true) {
                System.out.print("Inform the seller's e-mail: ");
                input = sc.nextLine();
                
                if (input.trim().isEmpty()) {
                    System.out.println("Seller's e-mail cannot be empty. Please try again.");
                    System.out.println();
                    continue;
                }
                
                action = bOrQCheck(input);            
                if (action != MenuAction.CONTINUE) {
                    return action;
                }
                
                email = input.trim().replace(" ", "");
                seller = sellerService.findByEmail(email);
                if (seller != null) {
                    System.out.println("There is already a seller with this e-mail: " + seller);
                    System.out.println();
                    continue;
                }

                break;
            }

            while (true) {
                System.out.print("Inform the seller's birth date (YYYY-MM-DD): ");
                input = sc.nextLine();
                
                if (input.trim().isEmpty()) {
                    System.out.println("Seller's birth date cannot be empty. Please try again.");
                    System.out.println();
                    continue;
                }
                
                action = bOrQCheck(input);            
                if (action != MenuAction.CONTINUE) {
                    return action;
                }

                try {
                    birthDate = LocalDate.parse(
                        input.trim()
                        .replace(" ", "")
                        .replace("/", "-")
                        .replace(".", "-")
                        .replace(",", "-"));
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter a valid date.");
                    System.out.println();
                    continue;
                }
            }

            while (true) {
                System.out.print("Inform the seller's base salary: ");
                input = sc.nextLine();
                
                if (input.trim().isEmpty()) {
                    System.out.println("Seller's base salary cannot be empty. Please try again.");
                    System.out.println();
                    continue;
                }
                
                action = bOrQCheck(input);            
                if (action != MenuAction.CONTINUE) {
                    return action;
                }

                try {
                    baseSalary = new BigDecimal(input
                        .trim()
                        .replace(" ", "")
                        .replace(",","."));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format. Please enter a valid salary number.");
                    System.out.println();
                    continue;
                }

                if (baseSalary.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Seller's base salary cannot be a negative number. Please try again.");
                    System.out.println();
                    continue;
                }

                break;
            }

            while (true) {
                System.out.print("Inform the name of the seller's department: ");
                input = sc.nextLine();
                
                if (input.trim().isEmpty()) {
                    System.out.println("Department's name cannot be empty. Please try again.");
                    System.out.println();
                    continue;
                }
                
                action = bOrQCheck(input);
                if (action != MenuAction.CONTINUE) {
                    return action;
                }
                
                departmentName = fixName(input);
                department = departmentService.findByName(departmentName);
                if (department == null) {
                    department = new Department(departmentName);
                }

                break;
            }

            if (department.getId() == null) {
                seller = sellerService.insertWithDepartment(department, new Seller( 
                    sellerName, 
                    email, 
                    birthDate, 
                    baseSalary, 
                    department));

                System.out.println("Seller and department successfully inserted:");
                System.out.println(seller);
            } else {
                seller = sellerService.insert(new Seller(
                    sellerName, 
                    email,
                    birthDate, 
                    baseSalary, 
                    department));
                
                System.out.println("Seller successfully inserted:");
                System.out.println(seller);
            }
        }
    }

    private MenuAction updateById(Scanner sc) throws DBException {
        MenuAction action;
        String input;
        int sellerId;
        Seller oldSeller;
        Seller newSeller;
        boolean updateNotComplete;
        boolean success;

        String menuOptions = """

            ============= UPDATE SELLER BY ID =============
            B. Back to Seller Menu
            Q. Quit
            """;

        String updateOprions = """

            What do you want to update?
            1 - Name
            2 - E-mail
            3 - Birth Date
            4 - Base Salary
            5 - Department

            0 - CONFIRM

            B. Back to Seller Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);
            System.out.print("Enter the seller's ID to update: ");
            input = sc.nextLine();

            if (input.trim().isEmpty()){
                System.out.println("Seller's ID cannot be empty. Please try again.");
                continue;
            }

            action = bOrQCheck(input);
            if (action != MenuAction.CONTINUE) {
                return action;
            }

            try {
                sellerId = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Please enter a valid integer.");
                continue;
            }

            
            if (sellerId < 1) {
                System.out.println("ID cannot be less then 1. Please try again.");
                continue;
            }

            oldSeller = sellerService.findById(sellerId);
            newSeller = new Seller(oldSeller);

            if (oldSeller == null) {
                System.out.println("There is no seller with ID = " + sellerId + ".");
                continue;
            } 

            System.out.println("Seller found: " + oldSeller);
            System.out.print("Are you sure you want to update it? (Y/N): ");
            input = sc.nextLine();

            if (input.equalsIgnoreCase("N") || input.equalsIgnoreCase("NO")) {
                System.out.println("Update canceled.");
                continue;
            } else if (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("YES")) {
                System.out.println("Invalid option. Update canceled.");
                continue;
            }

            updateNotComplete = true;
            while (updateNotComplete) {
                System.out.println();
                System.out.println(newSeller);
                System.out.println(updateOprions);
                System.out.print("Select an option: ");
                input = sc.nextLine();

                action = bOrQCheck(input);
                if (action != MenuAction.CONTINUE) {
                    return action;
                }
                
                switch (input.trim().toUpperCase()) {
                    case "0":
                        updateNotComplete = false;
                        break;
                    case "1":
                        action = nameUpdate(sc, newSeller);
                        break;
                    case "2":
                        action = emailUpdate(sc, newSeller);
                        break;
                    case "3":
                        action = birthDateUpdate(sc, newSeller);
                        break;
                    case "4":
                        action = baseSalaryUpdate(sc, newSeller);
                        break;
                    case "5":
                        action = departmentUpdate(sc, newSeller);
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

                if (action == MenuAction.EXIT) {
                    return MenuAction.EXIT;
                }
            }

            if (newSeller.getDepartment().getId() == null) {
                success = sellerService.updateByIdWithDepartment(newSeller.getDepartment(), newSeller);
            } else {
                success = sellerService.updateById(newSeller);
            }

            if (success) {
                System.out.println();
                System.out.println("Seller successfully updated.");
                System.out.println("From: " + oldSeller);
                System.out.println("To: " + newSeller);
            } else {
                System.out.println("Update failed. Please try again.");
            }
        }
    }

    private MenuAction deleteById(Scanner sc) throws DBException {
        MenuAction action;
        String input;
        int sellerId;
        Seller seller;

        String menuOptions = """

            ============= DELETE SELLER BY ID =============
            B. Back to Seller Menu
            Q. Quit
            """;

        while (true) {
            System.out.println(menuOptions);
            System.out.print("Enter the seller's ID to delete: ");
            input = sc.nextLine();

            action = bOrQCheck(input);
            if (action != MenuAction.CONTINUE) {    
                return action;
            }

            try {
                sellerId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Please enter a valid integer.");
                continue;
            }

            if (sellerId < 1){
                System.out.println("ID cannot be less then 1. Please try again.");
                continue;
            }

            seller = sellerService.findById(sellerId);
            if (seller == null) {
                System.out.println("Seller with ID = " + sellerId + " not found.");
                continue;
            }

            System.out.println();

            System.out.println("Seller found: " + seller);
            System.out.print("Are you sure you want to delete it? (Y/N): ");
            input = sc.nextLine();
            if (input.equalsIgnoreCase("N") || input.equalsIgnoreCase("NO")) {
                System.out.println("Deletion canceled.");
                continue;
            } else if (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("YES")) {
                System.out.println("Invalid option. Deletion canceled.");
                continue;
            }

            if (sellerService.deleteById(seller)) {
                System.out.println();
                System.out.println("Seller '" + seller.getName() + "' successfully deleted.");
            } else {
                System.out.println("Deletion failed. Please try again.");
            }
        }
    }


    private MenuAction bOrQMenu(Scanner sc) {
        String menuOptions = """
            
        ===============================================
        B. Back to Seller Menu
        Q. Quit
        """;
        
        System.out.println(menuOptions);
        System.out.print("Select an option: ");
        String input = sc.nextLine();
        
        MenuAction action = bOrQCheck(input);
        if (action == MenuAction.CONTINUE) {
            System.out.println("Invalid option. Returning to Seller Menu.");
            return MenuAction.BACK;
        }

        return action;
    }

    private MenuAction nameUpdate (Scanner sc, Seller seller) {
        MenuAction action;
        String input;
        String sellerName;

        while (true) {
            System.out.println();
            System.out.print("Inform the new seller's name: ");
            input = sc.nextLine();
            
            if (input.trim().isEmpty()) {
                System.out.println("Seller's name cannot be empty. Please try again.");
                continue;
            }

            action = bOrQCheck(input);
            if (action != MenuAction.CONTINUE) {
                return action;
            }
            
            sellerName = fixName(input);
            seller.setName(sellerName);

            return MenuAction.CONTINUE;
        }
    }

    private MenuAction emailUpdate (Scanner sc, Seller seller) {
        MenuAction action;
        String input;
        String email;
        Seller comparativeSeller;

        while (true) {
            System.out.println();
            System.out.print("Inform the new seller's e-mail: ");
            input = sc.nextLine();
            
            if (input.trim().isEmpty()) {
                System.out.println("Seller's e-mail cannot be empty. Please try again.");
                continue;
            }

            action = bOrQCheck(input);
            if (action != MenuAction.CONTINUE) {
                return action;
            }

            email = input.trim().replace(" ", "");
            comparativeSeller = sellerService.findByEmail(email);
            if (comparativeSeller != null) {
                System.out.println("There is already a seller with this e-mail: " + comparativeSeller);
                continue;
            }
            
            seller.setEmail(email);

            return MenuAction.CONTINUE;
        }
    }

    private MenuAction birthDateUpdate (Scanner sc, Seller seller) {
        MenuAction action;
        String input;
        LocalDate birthDate;

        while (true) {
            System.out.println();
            System.out.print("Inform the new seller's birth date (YYYY-MM-DD): ");
            input = sc.nextLine();
            
            if (input.trim().isEmpty()) {
                System.out.println("Seller's birth date cannot be empty. Please try again.");
                continue;
            }
            
            action = bOrQCheck(input);            
            if (action != MenuAction.CONTINUE) {
                return action;
            }
            
            try {
                birthDate = LocalDate.parse(
                    input.trim()
                    .replace(" ", "")
                    .replace("/", "-")
                    .replace(".", "-")
                    .replace(",", "-"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter a valid date.");
                continue;
            }

            seller.setBirthDate(birthDate);     

            return MenuAction.CONTINUE;
        }
    }

    private MenuAction baseSalaryUpdate (Scanner sc, Seller seller) {
        MenuAction action;
        String input;
        BigDecimal baseSalary;
        
        while (true) {
            System.out.println();
            System.out.print("Inform the seller's new base salary: ");
            input = sc.nextLine();
            
            if (input.trim().isEmpty()) {
                System.out.println("Seller's base salary cannot be empty. Please try again.");
                continue;
            }
            
            action = bOrQCheck(input);            
            if (action != MenuAction.CONTINUE) {
                return action;
            }
            
            try {
                baseSalary = new BigDecimal(input
                    .trim()
                    .replace(" ", "")
                    .replace(",","."));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid salary number.");
                continue;
            }
                
            if (baseSalary.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Seller's base salary cannot be a negative number. Please try again.");
                continue;
            }
                
            seller.setBaseSalary(baseSalary);
                
            return MenuAction.CONTINUE;
        }
    }

    private MenuAction departmentUpdate (Scanner sc, Seller seller) {
        MenuAction action;
        String input;
        String departmentName;
        Department department;
        
        while (true) {
            System.out.println();
            System.out.print("Inform the name of the new seller's department: ");
            input = sc.nextLine();
            
            if (input.trim().isEmpty()) {
                System.out.println("Department's name cannot be empty. Please try again.");
                continue;
            }
            
            action = bOrQCheck(input);
            if (action != MenuAction.CONTINUE) {
                return action;
            }
            
            departmentName = fixName(input);
            department = departmentService.findByName(departmentName);

            if (department == null) {
                seller.setDepartment(new Department(departmentName));;
            } else {
                seller.setDepartment(department);
            }

            return MenuAction.CONTINUE;        
        }
    }
}