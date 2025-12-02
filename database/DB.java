import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DB {
    // varchar, text
    private String databaseFileName;
    private DatabaseSchema schema;

    public DB(String databaseFileName) throws IOException {
        this.databaseFileName = databaseFileName;
        loadFromFile();
    }

    public void exec(String statement) throws IncorrectQuerySyntaxException, TableException {
        String action = this.getAction(statement);

        statement = statement.toLowerCase();
        statement = removeTrailingSemicolon(statement);
        String[] splitStatement = statement.toLowerCase().split(" ");
        switch (action.toLowerCase()) {
            case "create" -> createTable(statement);
            case "select" -> select(statement);
            case "insert" -> insert(splitStatement);
            case "update" -> update(splitStatement);
            case "delete" -> delete(splitStatement);
            default -> throw new IncorrectQuerySyntaxException("Incorrect syntax near action: " + action);
        }
    }

    private String removeTrailingSemicolon(String statement) {
        if (statement.endsWith(";")) {
            statement = statement.substring(0, statement.length() - 1);
        }
        return statement;
    }

    public void createTable(String statement) throws TableException {
        // String tableName = statement.split(" ")[2];
        // if (tableExists(tableName)) {
        // throw new TableExistsException("Table name provided in query already
        // exists.");
        // }
        // String[] columns = statement.split("[()]")[1].split(",");

    }

    private void select(String statement) throws TableException {
        boolean hasJoin = statement.contains(" join ");
        String[] statementValues = statement.toLowerCase().split("select | from | join | on | where ");

        if (hasJoin) {
            String columns = statementValues[1].trim();
            String table1 = statementValues[2].trim();
            String table2 = statementValues[3].trim();
            String onCondition = statementValues[4].trim();
        } else {
            String columns = statementValues[1].trim();
            String tableName = statementValues[2].trim();
            System.out.println(columns);
            if (!tableExists(tableName)) {
                throw new TableException("Table does not exist", statement);
            }

            Table table = schema.getTables().get(tableName);
            List<Map<String, Object>> rows = table.getRows();

            List<String> columnsToDisplay = new ArrayList<>();
            if (columns.equals("*")) {
                for (Column col : table.getColumns()) {
                    columnsToDisplay.add(col.getName());
                }
            } else {
                String[] columnArray = columns.split(",");
                for (String col : columnArray) {
                    columnsToDisplay.add(col.trim());
                }
            }

            IOmanager.displayResults(rows, columnsToDisplay);
        }

    }

    private void insert(String[] splitStatement) {
    }

    private void update(String[] splitStatement) {
    }

    private void delete(String[] splitStatement) {
    }

    private boolean tableExists(String tableName) {
        return schema.getTables().containsKey(tableName);
    }

    private void loadFromFile() throws IOException {
        this.schema = FileManager.loadDatabase(databaseFileName);
    }

    private void writeChangesToFile() throws IOException {
        FileManager.saveDatabase(databaseFileName, schema);

    }

    private String getAction(String input) throws IncorrectQuerySyntaxException {
        String[] splitedInput = input.split(" ");
        String action = splitedInput[0].toLowerCase();

        Set<String> validActions = Set.of("select", "insert", "update", "delete");
        if (input.trim().length() == 0) {
            throw new IncorrectQuerySyntaxException("No query to execute");
        }

        if (("create".equals(action) && !"table".equalsIgnoreCase(splitedInput[1]))
                || !validActions.contains(action)) {
            throw new IncorrectQuerySyntaxException("Incorrect syntax near sql action.", input);
        }

        return action;
    }

}
