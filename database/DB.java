import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DB {
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
            String table1Name = statementValues[2].trim();
            String table2Name = statementValues[3].trim();
            String onCondition = statementValues[4].trim();

            if (!tableExists(table1Name) || !tableExists(table2Name)) {
                throw new TableException("One of the tables does not exist", statement);
            }

            Table table1 = schema.getTables().get(table1Name);
            Table table2 = schema.getTables().get(table2Name);

            String[] onParts = onCondition.split("=");
            String leftSide = onParts[0].trim();
            String[] leftParts = leftSide.split("\\.");
            String leftTable = leftParts[0].trim();
            String leftColumn = leftParts[1].trim();

            String rightSide = onParts[1].trim();
            String[] rightParts = rightSide.split("\\.");
            String rightTable = rightParts[0].trim();
            String rightColumn = rightParts[1].trim();

            List<Map<String, Object>> joinedRows = performJoin(
                    table1, table2,
                    leftTable, leftColumn,
                    rightTable, rightColumn);

            List<String> columnsToDisplay = new ArrayList<>();
            if (columns.equals("*")) {
                for (Column col : table1.getColumns()) {
                    columnsToDisplay.add(table1Name + "." + col.getName());
                }
                for (Column col : table2.getColumns()) {
                    columnsToDisplay.add(table2Name + "." + col.getName());
                }
            } else {
                String[] columnArray = columns.split(",");
                for (String col : columnArray) {
                    columnsToDisplay.add(col.trim());
                }
            }

            IOmanager.displayResults(joinedRows, columnsToDisplay);
        } else {
            String columns = statementValues[1].trim();
            String tableName = statementValues[2].trim();
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
                    if (col.contains(".")) {
                        col = col.split("\\.")[1];
                    }
                    columnsToDisplay.add(col.trim());
                }
            }

            IOmanager.displayResults(rows, columnsToDisplay);
        }

    }

    private List<Map<String, Object>> performJoin(
            Table table1, Table table2,
            String leftTable, String leftColumn,
            String rightTable, String rightColumn) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> row1 : table1.getRows()) {
            for (Map<String, Object> row2 : table2.getRows()) {

                Object leftValue = leftTable.equals(table1.getName()) ? row1.get(leftColumn) : row2.get(leftColumn);
                Object rightValue = rightTable.equals(table2.getName()) ? row2.get(rightColumn) : row1.get(rightColumn);

                if (leftValue != null && leftValue.equals(rightValue)) {
                    Map<String, Object> joinedRow = new HashMap<>();

                    for (Map.Entry<String, Object> entry : row1.entrySet()) {
                        joinedRow.put(table1.getName() + "." + entry.getKey(), entry.getValue());
                    }

                    for (Map.Entry<String, Object> entry : row2.entrySet()) {
                        joinedRow.put(table2.getName() + "." + entry.getKey(), entry.getValue());
                    }

                    result.add(joinedRow);
                }
            }
        }

        return result;
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
