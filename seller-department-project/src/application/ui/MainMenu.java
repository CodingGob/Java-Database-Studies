package application.ui;

import java.util.Scanner;

import application.services.DepartmentService;
import application.services.SellerService;
import model.exceptions.DBException;

public class MainMenu extends BaseMenu {

    public MainMenu(DepartmentService departmentService, SellerService sellerService) {
        super(departmentService, sellerService);
    }

    @Override
    public MenuAction show(Scanner sc) throws DBException {
        MenuAction action = MenuAction.CONTINUE;
        String input;
        String menuOptions = """

            ================== MAIN MENU ==================
            1. Department Menu
            2. Seller Menu

            Q. Quit
            """;

        while(action != MenuAction.EXIT) {
            System.out.println(menuOptions);
            System.out.print("Select an option: ");
            input = sc.nextLine();

            switch (input.toUpperCase().trim()) {
                case "1":
                    action = new DepartmentMenu(departmentService, sellerService).show(sc);
                    break;
                case "2":
                    action = new SellerMenu(departmentService, sellerService).show(sc);
                    break;
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
}