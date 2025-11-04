import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuController {
    private String currentState;
    private boolean running;

    public MenuController() {
        this.currentState = "Main Menu";
        this.running = true;
    }

    public void init(Catalog catalog, UserList userList) {

        Scanner scanner = new Scanner(System.in);
        ConsoleIOManager ConsoleIOManager = new ConsoleIOManager(scanner);

        while (this.running) {

            switch (this.currentState) {

                case Constants.MAIN_MENU_NAME: {
                    mainMenu(ConsoleIOManager);
                    break;
                }

                case Constants.ADD_PRODUCTS_MENU_NAME: {
                    addProductMenu(catalog, ConsoleIOManager);
                    break;
                }

                case Constants.REMOVE_PRODUCTS_MENU_NAME: {
                    removeProductMenu(userList, ConsoleIOManager);
                    break;
                }

                case Constants.SHOW_LIST_MENU_NAME: {
                    productListMenu(userList, ConsoleIOManager);
                    break;
                }

                case Constants.SAVE_LIST_MENU_NAME: {
                    saveListMenu(userList, ConsoleIOManager);
                    break;
                }

                case Constants.QUIT_NAME: {
                    quit(ConsoleIOManager);
                    break;
                }

                default: {
                    categoriesMenu(catalog, userList, ConsoleIOManager);
                    break;
                }

            }
        }
    }

    private void quit(ConsoleIOManager ConsoleIOManager) {
        ConsoleIOManager.showMessage(Constants.GOODBYE_MESSAGE);

        this.running = false;
    }

    private void saveListMenu(UserList userList, ConsoleIOManager ConsoleIOManager) {
        try {
            ConsoleIOManager.clearScreen();
            userList.saveListToFile(Constants.OUTPUT_FILE_NAME);
            ConsoleIOManager.showMessage(Constants.SUCESSFULL_LIST_SAVE);
        } catch (IOException e) {
            ConsoleIOManager.showMessage(Constants.FAILED_LIST_SAVE);
        } finally {
            ConsoleIOManager.waitForAnyInputToContinue();
            this.currentState = Constants.MAIN_MENU_NAME;
        }
    }

    private void productListMenu(UserList userList, ConsoleIOManager ConsoleIOManager) {
        List<String> productsList = userList.getList();
        ConsoleIOManager.clearScreen();
        if (productsList.size() > 0) {
            ConsoleIOManager.displayMenu(Constants.SHOW_LIST_MESSAGE, productsList);
        } else {
            ConsoleIOManager.showMessage(Constants.EMPTY_SHOW_LIST_MESSAGE);
        }
        ConsoleIOManager.waitForAnyInputToContinue();
        this.currentState = Constants.MAIN_MENU_NAME;
    }

    private void categoriesMenu(Catalog catalog, UserList userList, ConsoleIOManager ConsoleIOManager) {
        List<String> menuItems = new ArrayList<>(catalog.getCategoryByName(this.currentState).getProductNames());
        menuItems.add(Constants.GO_BACK_OPTION);
        ConsoleIOManager.displayMenu(Constants.ADD_PRODUCT_FINAL_MENU, menuItems);
        ConsoleIOManager.handleInput(menuItems);
        String newMenuName = ConsoleIOManager.getChoice();
        if (newMenuName.equals(Constants.GO_BACK_OPTION)) {
            this.currentState = Constants.MAIN_MENU_NAME;
        } else if (catalog.getCategoryByName(this.currentState).getProductNames().contains(newMenuName)) {
            userList.addProduct(newMenuName);
            ConsoleIOManager.showDissapearingMessage(Constants.ON_ADD_MESSAGE);
        }
    }

    private void removeProductMenu(UserList userList, ConsoleIOManager ConsoleIOManager) {
        List<String> menuItems = new ArrayList<>(userList.getList());
        if (menuItems.size() > 0) {
            menuItems.add(Constants.GO_BACK_OPTION);
            ConsoleIOManager.displayMenu(Constants.REMOVE_ITEM_MENU, menuItems);
            ConsoleIOManager.handleInput(menuItems);
            String choice = ConsoleIOManager.getChoice();
            if (choice.equals(Constants.GO_BACK_OPTION)) {
                this.currentState = Constants.MAIN_MENU_NAME;
            } else if (userList.getList().contains(choice)) {
                userList.removeProduct(choice);
            }
        } else {
            ConsoleIOManager.clearScreen();
            ConsoleIOManager.showMessage(Constants.EMPTY_SHOW_LIST_MESSAGE);
            ConsoleIOManager.waitForAnyInputToContinue();
            this.currentState = Constants.MAIN_MENU_NAME;
        }
    }

    private void addProductMenu(Catalog catalog, ConsoleIOManager ConsoleIOManager) {
        List<String> menuItems = catalog.getCategories();
        menuItems.add(Constants.GO_BACK_OPTION);
        ConsoleIOManager.displayMenu(Constants.ADD_PRODUCT_CATEGORIES_MENU, menuItems);
        ConsoleIOManager.handleInput(menuItems);
        String newMenuName = ConsoleIOManager.getChoice();
        if (newMenuName.equals(Constants.GO_BACK_OPTION)) {
            this.currentState = Constants.MAIN_MENU_NAME;
        } else if (catalog.getCategories().contains(newMenuName)) {
            this.currentState = newMenuName;
        }
    }

    private void mainMenu(ConsoleIOManager ConsoleIOManager) {
        ConsoleIOManager.displayMenu(Constants.MAIN_MENU_PROMPT, Constants.MAIN_MENU_OPTIONS);
        ConsoleIOManager.handleInput(Constants.MAIN_MENU_OPTIONS);
        this.currentState = ConsoleIOManager.getChoice();
    }

}
