import java.util.ArrayList;
import java.util.List;

public class UserList {
    // NOTE: this class can be also implemented holding quantities of each product
    private List<String> products;

    public UserList() {
        this.products = new ArrayList<>();
    }

    public void addProduct(String product) {
        this.products.add(product);
    }

    public List<String> getList() {
        return this.products;
    }

    public void removeProduct(String product) {
        this.products.remove(product);
    }

    public void saveListToFile(String fileName) {
        // TODO:saveListToFile with custom name from const
    }
}
