import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Catalog {
    private HashMap<String, List<String>> products;

    public Catalog(FileIOManager fm) throws IOException {
        List<String> lines = fm.readLines(Constants.INPUT_FILE_NAME);
        this.products = new HashMap<>();

        // first line is ommited, because first line are headers
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] splitLine = line.split(",");
            if (splitLine.length < 2) {
                throw new IOException("Incorrect data format");
            }

            String category = splitLine[0].trim();
            String product = splitLine[1].trim();
            products.computeIfAbsent(category, _ -> new ArrayList<>()).add(product);

        }

    }

    public List<String> getCategories() {
        Set<String> categories = this.products.keySet();
        return new ArrayList<>(categories);

    }

    public List<String> getItemsFromCategory(String category) {
        return this.products.getOrDefault(category, new ArrayList<>());
    }

}
