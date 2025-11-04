import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserList {
    private List<Product> products;
    private FileIOManager fm;

    public UserList(FileIOManager fm) {
        this.products = new ArrayList<>();
        this.fm = fm;
    }

    public void addProduct(String product) {
        this.products.add(new Product(product));
    }

    public List<String> getListOfProducts() {
        List<String> productNames = new ArrayList<>();
        for (Product product : this.products) {
            productNames.add(product.getName());
        }
        return productNames;
    }

    public void removeProduct(String productToRemove) {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.getName().equalsIgnoreCase(productToRemove)) {
                products.remove(i);
                System.out.println("Removed product: " + productToRemove);
                return;
            }
        }
    }

    public void saveListToFile(String fileName) throws IOException {
        this.fm.writeLines(Constants.OUTPUT_FILE_NAME, this.getListOfProducts());
    }

}
