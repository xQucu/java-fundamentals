import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateHandler {
    private DatabaseSchema schema;
    private String databaseFileName;
    private WhereConditionEvaluator whereEvaluator;

    public UpdateHandler(DatabaseSchema schema, String databaseFileName) {
        this.schema = schema;
        this.databaseFileName = databaseFileName;
        this.whereEvaluator = new WhereConditionEvaluator();
    }

    public void handle(String statement) throws TableException, IncorrectQuerySyntaxException {
        String[] parts = statement.toLowerCase().split(" set ");
        if (parts.length != 2) {
            throw new IncorrectQuerySyntaxException("Invalid UPDATE syntax");
        }

        String tableName = parts[0].replaceFirst("update", "").trim();

        if (!schema.tableExists(tableName)) {
            throw new TableException("Table does not exist");
        }

        Table table = schema.getTables().get(tableName);

        String setPart = parts[1];
        String wherePart = "";
        if (setPart.contains(" where ")) {
            wherePart = setPart.split(" where ")[1].trim();
            setPart = setPart.split(" where ")[0].trim();
        }

        String[] assignments = setPart.split(",");
        Map<String, String> updates = new HashMap<>();
        for (String assign : assignments) {
            String[] keyValue = assign.split("=");
            if (keyValue.length != 2) {
                throw new IncorrectQuerySyntaxException("Invalid SET syntax in UPDATE statement");
            }
            updates.put(keyValue[0].trim(), keyValue[1].trim());
        }

        List<Map<String, Object>> rows = table.getRows();
        for (Map<String, Object> row : rows) {
            if (wherePart.isEmpty() || whereEvaluator.evaluate(row, wherePart)) {
                for (Map.Entry<String, String> entry : updates.entrySet()) {
                    Column col = table.getColumnByName(entry.getKey());
                    if (col == null) {
                        throw new TableException("Column " + entry.getKey() + " does not exist in table " + tableName);
                    }
                    String value = entry.getValue();
                    if (col.getType().equals("INTEGER")) {
                        try {
                            row.put(col.getName(), Integer.parseInt(value));
                        } catch (NumberFormatException e) {
                            throw new IncorrectQuerySyntaxException(
                                    "Invalid integer value for column " + col.getName());
                        }
                    } else {
                        if (value.startsWith("'") && value.endsWith("'")) {
                            value = value.substring(1, value.length() - 1);
                        }
                        row.put(col.getName(), value);
                    }
                }
            }
        }

        try {
            FileManager.saveDatabase(databaseFileName, schema);
            System.out.println("Rows updated.");
        } catch (IOException e) {
            throw new TableException("Failed to save changes to file: " + e.getMessage());
        }
    }

}
