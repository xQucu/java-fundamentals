import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateTableHandler {
    private DatabaseSchema schema;
    private String databaseFileName;
    private QueryParser parser;

    public CreateTableHandler(DatabaseSchema schema, String databaseFileName) {
        this.schema = schema;
        this.databaseFileName = databaseFileName;
        this.parser = new QueryParser();
    }

    public void handle(String statement) throws TableException, IncorrectQuerySyntaxException {
        String[] words = statement.split(" ");
        String tableName = words[2].trim();

        if (schema.tableExists(tableName)) {
            throw new TableException("Table already exists", statement);
        }

        int openParen = statement.indexOf("(");
        int closeParen = statement.lastIndexOf(")");

        if (openParen == -1 || closeParen == -1) {
            throw new TableException("Invalid syntax", statement);
        }

        String columnsStr = statement.substring(openParen + 1, closeParen);
        String[] columnDefs = columnsStr.split(",");

        List<Column> columns = new ArrayList<>();

        for (String colDef : columnDefs) {
            String[] parts = colDef.trim().split(" ");

            if (parts.length < 2) {
                throw new TableException("Invalid column definition: " + colDef, statement);
            }

            String columnName = parts[0].trim();
            String columnType = parts[1].trim();
            columnType = parser.normalizeDataType(columnType);

            columns.add(new Column(columnName, columnType));
        }

        Table newTable = new Table(tableName, columns);
        schema.getTables().put(tableName, newTable);

        try {
            FileManager.saveDatabase(databaseFileName, schema);
            System.out.println("Table created.");
        } catch (IOException e) {
            throw new TableException("Failed to save table to file: " + e.getMessage());
        }
    }
}
