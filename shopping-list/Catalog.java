import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Catalog {
    private HashMap<String, Category> categories;

    public Catalog(FileIOManager fm) throws IOException {
        List<String> lines = fm.readLines(Constants.INPUT_FILE_NAME);
        this.categories = new HashMap<>();

        for (int i = Constants.FIRST_LINE_TO_READ_FROM - 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] splitLine = line.split(",");
            if (splitLine.length < 2) {
                throw new IOException("Incorrect data format");
            }

            String categoryName = splitLine[0].trim();
            String productName = splitLine[1].trim();
            Product product = new Product(productName);

            Category category = categories.computeIfAbsent(categoryName, name -> new Category(name));

            category.addProduct(product);

        }

    }

    public List<String> getCategories() {
        Set<String> categories = this.categories.keySet();
        return new ArrayList<>(categories);

    }

    public Category getCategoryByName(String name) {
        return this.categories.get(name);
    }

}
