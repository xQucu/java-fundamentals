import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserList {
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

    public void saveListToFile(String fileName) throws IOException {
        this.fm.writeLines(Constants.OUTPUT_FILE_NAME, this.products);
    }
}
