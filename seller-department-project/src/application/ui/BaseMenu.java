package application.ui;

import java.util.Scanner;

import model.dao.DepartmentDao;
import model.dao.SellerDao;

public abstract class BaseMenu {
    protected DepartmentDao departmentDao;
    protected SellerDao sellerDao;

    public BaseMenu(DepartmentDao departmentDao, SellerDao sellerDao){
        this.departmentDao = departmentDao;
        this.sellerDao = sellerDao;
    }

    public abstract MenuAction show(Scanner sc);

    protected String fixName(String name) {
        StringBuilder fixedName = new StringBuilder();
        String[] parts = name.split(" ");

        for (String part : parts) {
            switch (part.length()) {
                case 0:
                    break;
                case 1:
                    part = part.toUpperCase();
                    fixedName.append(part + " ");
                    break;           
                default:
                    part = part.toUpperCase().charAt(0) + part.substring(1).toLowerCase();
                    fixedName.append(part + " ");
                    break;
            }
        }
        
        return fixedName.toString().trim();
    }

    protected MenuAction bOrQCheck(String input) {
        switch (input.toUpperCase().trim()) {
            case "B", "BACK" -> { return MenuAction.BACK; }
            case "Q", "QUIT" -> { return MenuAction.EXIT; }
            default -> { return MenuAction.CONTINUE; }
        }
    }

    protected MenuAction yOrNCheck(String input) {
        switch (input.toUpperCase().trim()) {
            case "Y","YES" -> { return MenuAction.CONTINUE; }
            case "N","NO" -> { return MenuAction.BACK; }
            case "Q","QUIT" -> { return MenuAction.EXIT; }
            default -> { return MenuAction.INVALID; }
        }
    }
}