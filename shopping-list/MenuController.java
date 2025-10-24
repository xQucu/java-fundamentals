import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuController {
    private String menuName;

    public MenuController() {
        this.menuName = "Main Menu";
    }

    public void start(Catalog catalog, UserList userList) {
        // add item
        // remove item
        // show list
        // quit

        Scanner scanner = new Scanner(System.in);
        ConsoleIOManager ConsoleIOManager = new ConsoleIOManager(scanner);

        boolean running = true;
        while (running) {
            if (this.menuName.equals("Main Menu")) {

                ConsoleIOManager.displayMenu(Constants.MAIN_MENU_PROMPT, Constants.MAIN_MENU);
                ConsoleIOManager.handleInput(Constants.MAIN_MENU);
                this.menuName = ConsoleIOManager.getChoice();

            } else if (this.menuName.equals("Add product")) {

                List<String> menuItems = catalog.getCategories();
                menuItems.add(Constants.GO_BACK_OPTION);
                ConsoleIOManager.displayMenu(Constants.ADD_PRODUCT_CATEGORIES_MENU, menuItems);
                ConsoleIOManager.handleInput(menuItems);
                String newMenuName = ConsoleIOManager.getChoice();
                if (newMenuName.equals(Constants.GO_BACK_OPTION)) {
                    this.menuName = "Main Menu";
                } else if (catalog.getCategories().contains(newMenuName)) {
                    this.menuName = newMenuName;
                }

            } else if (this.menuName.equals("Remove product")) {

            } else if (catalog.getCategories().contains(this.menuName)) {

                List<String> menuItems = new ArrayList<>(catalog.getItemsFromCategory(this.menuName));
                menuItems.add(Constants.GO_BACK_OPTION);
                ConsoleIOManager.displayMenu(Constants.ADD_PRODUCT_FINAL_MENU, menuItems);
                ConsoleIOManager.handleInput(menuItems);
                String newMenuName = ConsoleIOManager.getChoice();
                if (newMenuName.equals(Constants.GO_BACK_OPTION)) {
                    this.menuName = "Main Menu";
                } else if (catalog.getItemsFromCategory(this.menuName).contains(newMenuName)) {
                    userList.addProduct(newMenuName);
                    ConsoleIOManager.showDissapearingMessage(Constants.ON_ADD_MESSAGE);
                }

            } else if (this.menuName.equals("Show list")) {
                List<String> productsList = userList.getList();
                if (productsList.size() > 0) {
                    ConsoleIOManager.showMessage(Constants.SHOW_LIST_MESSAGE);
                    ConsoleIOManager.showLines(productsList);
                } else {
                    ConsoleIOManager.showMessage(Constants.EMPTY_SHOW_LIST_MESSAGE);
                }
                ConsoleIOManager.waitForAnyInputToContinue();
                this.menuName = "Main Menu";

            } else if (this.menuName.equals("Save list")) {
                boolean ok = userList.saveListToFile(Constants.OUTPUT_FILE_NAME);
                if (ok) {
                    ConsoleIOManager.showMessage(Constants.SUCESSFULL_LIST_SAVE);
                } else {
                    ConsoleIOManager.showMessage(Constants.FAILED_LIST_SAVE);
                }
                ConsoleIOManager.waitForAnyInputToContinue();
                this.menuName = "Main Menu";

            } else if (this.menuName.equals("Quit")) {

                ConsoleIOManager.showMessage(Constants.GOODBYE_MESSAGE);

                running = false;
            }
        }
    }

}
