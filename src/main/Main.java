
package main;


import controller.databaseChange;
import model.UserManager;

public class Main {

    public static void main(String[] args) {
//        new Controller.testing().print();
//        new view.AdminMenu().adminMenu(new controller.databaseChange().getAdmin(1));
//        new view.MainScreen();
       new view.OwnerMenu().ownerMenu();
        // UserManager.getInstance().setUser(new databaseChange().getAUser(2));
        // new view.AdminMenu().adminMenu();
    }
}
