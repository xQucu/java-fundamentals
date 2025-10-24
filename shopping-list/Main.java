import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        FileIOManager fm = new FileIOManager();
        Catalog catalog;
        try {
            catalog = new Catalog(fm);

        } catch (IOException e) {
            System.err.println("Problem with opening the file:" + e.getMessage());
            return;
        }
        UserList userList = new UserList(fm);
        MenuController menuController = new MenuController();

        menuController.start(catalog, userList);
    }
}
