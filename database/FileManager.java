import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static DatabaseSchema loadDatabase(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        if (!Files.exists(path) || Files.size(path) == 0) {
            return new DatabaseSchema();
        }

        byte[] jsonData = Files.readAllBytes(path);
        return objectMapper.readValue(jsonData, DatabaseSchema.class);
    }

    public static void saveDatabase(String filePath, DatabaseSchema schema) throws IOException {
        Path path = Paths.get(filePath);

        String jsonString = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(schema);

        Files.write(path, jsonString.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}
