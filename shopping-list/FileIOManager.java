import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileIOManager {

    public List<String> readLines(String fileName) throws IOException {
        Path filePath = Paths.get(fileName);

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        return lines;
    }
}
