import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteHandler {
    private DatabaseSchema schema;
    private String databaseFileName;
    private WhereConditionEvaluator whereEvaluator;

    public DeleteHandler(DatabaseSchema schema, String databaseFileName) {
        this.schema = schema;
        this.databaseFileName = databaseFileName;
        this.whereEvaluator = new WhereConditionEvaluator();
    }

    public void handle(String statement) throws TableException, IncorrectQuerySyntaxException {
        String[] parts = statement.toLowerCase().split(" from ");
        if (parts.length != 2) {
            throw new IncorrectQuerySyntaxException("Invalid DELETE syntax");
        }

        String tableName = parts[1].split(" where ")[0].trim();
        String wherePart = parts[1].contains(" where ") ? parts[1].split(" where ")[1].trim() : "";

        if (!schema.tableExists(tableName)) {
            throw new TableException("Table does not exist");
        }

        Table table = schema.getTables().get(tableName);
        List<Map<String, Object>> rows = table.getRows();
        List<Map<String, Object>> rowsToRemove = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            if (wherePart.isEmpty() || whereEvaluator.evaluate(row, wherePart)) {
                rowsToRemove.add(row);
            }
        }

        rows.removeAll(rowsToRemove);

        try {
            FileManager.saveDatabase(databaseFileName, schema);
            System.out.println("Rows deleted.");
        } catch (IOException e) {
            throw new TableException("Failed to save changes to file: ");
        }
    }

}
