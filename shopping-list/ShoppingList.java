import java.io.IOException;

public class ShoppingList {
    public static void main(String[] args) {
        FileIOManager fm = new FileIOManager();
        Catalog catalog;
        try {
            catalog = new Catalog(fm);
            UserList userList = new UserList(fm);
            MenuController menuController = new MenuController();

            menuController.init(catalog, userList);
        } catch (IOException e) {
            System.err.println("Problem with opening the file:" + e.getMessage());
        }
    }
}
