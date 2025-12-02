import java.util.HashMap;
import java.util.Map;

public class DatabaseSchema {
    private Map<String, Table> tables;

    public DatabaseSchema() {
        this.tables = new HashMap<>();
    }

    public Map<String, Table> getTables() {
        return tables;
    }

    public void setTables(Map<String, Table> tables) {
        this.tables = tables;
    }
}
