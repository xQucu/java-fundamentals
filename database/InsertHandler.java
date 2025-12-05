import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InsertHandler {
    private DatabaseSchema schema;
    private String databaseFileName;

    public InsertHandler(DatabaseSchema schema, String databaseFileName) {
        this.schema = schema;
        this.databaseFileName = databaseFileName;
    }

    public void handle(String statement) throws TableException, IncorrectQuerySyntaxException {
        String[] parts = statement.toLowerCase().split(" into ");
        if (parts.length != 2) {
            throw new IncorrectQuerySyntaxException("Invalid INSERT syntax");
        }

        String tablePart = parts[1];
        String tableName = tablePart.split("\\(")[0].trim();

        if (!schema.tableExists(tableName)) {
            throw new TableException("Table does not exist");
        }

        int openParen = statement.indexOf("(");
        int closeParen = statement.indexOf(")");

        if (openParen == -1 || closeParen == -1) {
            throw new IncorrectQuerySyntaxException("Invalid syntax in INSERT statement");
        }

        String columnsStr = statement.substring(openParen + 1, closeParen).trim();
        String[] columns = columnsStr.split(",");

        int valuesIndex = statement.toLowerCase().indexOf("values");
        if (valuesIndex == -1) {
            throw new IncorrectQuerySyntaxException("Missing VALUES keyword in INSERT statement");
        }

        String valuesStr = statement.substring(statement.indexOf("(", valuesIndex) + 1,
                statement.indexOf(")", valuesIndex));
        String[] values = valuesStr.split(",");

        if (columns.length != values.length) {
            throw new IncorrectQuerySyntaxException("Number of columns and values do not match");
        }

        Table table = schema.getTables().get(tableName);
        Map<String, Object> newRow = new HashMap<>();

        for (int i = 0; i < columns.length; i++) {
            String columnName = columns[i].trim();
            String value = values[i].trim();

            Column col = table.getColumnByName(columnName);
            if (col == null) {
                throw new TableException("Column " + columnName + " does not exist in table " + tableName);
            }

            if (col.getType().equals("INTEGER")) {
                try {
                    newRow.put(columnName, Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    throw new IncorrectQuerySyntaxException("Invalid integer value for column " + columnName);
                }
            } else {
                if (value.startsWith("'") && value.endsWith("'")) {
                    value = value.substring(1, value.length() - 1);
                }
                newRow.put(columnName, value);
            }
        }

        table.getRows().add(newRow);

        try {
            FileManager.saveDatabase(databaseFileName, schema);
            System.out.println("Row inserted.");
        } catch (IOException e) {
            throw new TableException("Failed to save changes to file: " + e.getMessage());
        }
    }

}
