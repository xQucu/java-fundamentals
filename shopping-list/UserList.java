import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserList {
    // NOTE: this could also be also implemented holding quantities of each product
    private List<String> products;
    private FileIOManager fm;

    public UserList(FileIOManager fm) {
        this.products = new ArrayList<>();
        this.fm = fm;
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

    public boolean saveListToFile(String fileName) {
        try {
            this.fm.writeLines(Constants.OUTPUT_FILE_NAME, this.products);
        } catch (IOException _) {
            return false;
        }
        return true;
    }
}
