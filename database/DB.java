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
        switch (action.toLowerCase()) {
            case "create" -> createTable(statement);
            case "select" -> select(statement);
            case "insert" -> insert(statement);
            case "update" -> update(statement);
            case "delete" -> delete(statement);
            default -> throw new IncorrectQuerySyntaxException("Incorrect syntax near action: " + action);
        }
    }

    private String removeTrailingSemicolon(String statement) {
        if (statement.endsWith(";")) {
            statement = statement.substring(0, statement.length() - 1);
        }
        return statement;
    }

    public void createTable(String statement) throws TableException, IncorrectQuerySyntaxException {
        String[] words = statement.split(" ");
        String tableName = words[2].trim();

        if (tableExists(tableName)) {
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
            columnType = setDataTypes(columnType);

            columns.add(new Column(columnName, columnType));
        }

        Table newTable = new Table(tableName, columns);
        schema.getTables().put(tableName, newTable);

        try {
            writeChangesToFile();
            System.out.println("Table created.");
        } catch (IOException e) {
            throw new TableException("Failed to save table to file: " + e.getMessage());
        }
    }

    private String setDataTypes(String columnType) throws IncorrectQuerySyntaxException {
        if (columnType.equalsIgnoreCase("text")) {
            columnType = "VARCHAR";
        } else if (columnType.equalsIgnoreCase("int")) {
            columnType = "INTEGER";
        } else if (columnType.equalsIgnoreCase("varchar")) {
            columnType = "VARCHAR";
        } else if (columnType.equalsIgnoreCase("integer")) {
            columnType = "INTEGER";
        } else {
            throw new IncorrectQuerySyntaxException("Incorrect type of data for column to be created");
        }
        return columnType;
    }

    private void select(String statement) throws TableException, IncorrectQuerySyntaxException {
        boolean hasJoin = statement.contains(" join ");
        String[] statementValues = statement.toLowerCase().split("select | from | join | on | where ");

        if (hasJoin) {
            if (statementValues.length < 5) {
                throw new IncorrectQuerySyntaxException("Incorrect syntax");
            }
            String columns = statementValues[1].trim();
            String table1Name = statementValues[2].trim();
            String table2Name = statementValues[3].trim();
            String onCondition = statementValues[4].trim();
            String whereCondition = statementValues.length > 5 ? statementValues[5].trim() : "";

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

            if (!whereCondition.isEmpty()) {
                List<Map<String, Object>> filteredRows = new ArrayList<>();
                for (Map<String, Object> row : joinedRows) {
                    if (evaluateWhereConditionForJoin(row, whereCondition, table1Name, table2Name)) {
                        filteredRows.add(row);
                    }
                }
                joinedRows = filteredRows;
            }

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
            if (statementValues.length < 3) {
                throw new IncorrectQuerySyntaxException("Incorrect syntax");
            }
            String columns = statementValues[1].trim();
            String tableName = statementValues[2].trim();
            String whereCondition = statementValues.length > 3 ? statementValues[3].trim() : "";

            if (!tableExists(tableName)) {
                throw new TableException("Table does not exist", statement);
            }

            Table table = schema.getTables().get(tableName);
            List<Map<String, Object>> rows = table.getRows();

            if (!whereCondition.isEmpty()) {
                List<Map<String, Object>> filteredRows = new ArrayList<>();
                for (Map<String, Object> row : rows) {
                    if (evaluateWhereCondition(row, whereCondition)) {
                        filteredRows.add(row);
                    }
                }
                rows = filteredRows;
            }

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

    private boolean evaluateWhereCondition(Map<String, Object> row, String whereCondition) {
        if (whereCondition == null || whereCondition.isEmpty()) {
            return true;
        }

        String[] conditionParts = whereCondition.split("=");
        if (conditionParts.length != 2) {
            return false;
        }

        String columnName = conditionParts[0].trim();
        String value = conditionParts[1].trim();

        if (value.startsWith("'") && value.endsWith("'")) {
            value = value.substring(1, value.length() - 1);
        }

        Object rowValue = row.get(columnName);
        if (rowValue == null) {
            return false;
        }

        if (rowValue instanceof Integer) {
            try {
                int intValue = Integer.parseInt(value);
                return rowValue.equals(intValue);
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return rowValue.toString().equals(value);
        }
    }

    private boolean evaluateWhereConditionForJoin(Map<String, Object> row, String whereCondition,
            String table1Name, String table2Name) {
        if (whereCondition == null || whereCondition.isEmpty()) {
            return true;
        }

        String[] conditionParts = whereCondition.split("=");
        if (conditionParts.length != 2) {
            return false;
        }

        String columnName = conditionParts[0].trim();
        String value = conditionParts[1].trim();

        if (!columnName.contains(".")) {
            return false;
        }

        if (value.startsWith("'") && value.endsWith("'")) {
            value = value.substring(1, value.length() - 1);
        }

        Object rowValue = row.get(columnName);
        if (rowValue == null) {
            return false;
        }

        if (rowValue instanceof Integer) {
            try {
                int intValue = Integer.parseInt(value);
                return rowValue.equals(intValue);
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return rowValue.toString().equals(value);
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

    private void insert(String statement) {
    }

    private void update(String statement) throws TableException, IncorrectQuerySyntaxException {
        String[] parts = statement.toLowerCase().split(" set ");
        if (parts.length != 2) {
            throw new IncorrectQuerySyntaxException("Invalid UPDATE syntax");
        }

        String tableName = parts[0].replaceFirst("update", "").trim();

        if (!tableExists(tableName)) {
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
            if (wherePart.isEmpty() || evaluateWhereCondition(row, wherePart)) {
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
            writeChangesToFile();
            System.out.println("Rows updated.");
        } catch (IOException e) {
            throw new TableException("Failed to save changes to file: " + e.getMessage());
        }
    }

    private void delete(String statement) throws TableException, IncorrectQuerySyntaxException {
        String[] parts = statement.toLowerCase().split(" from ");
        if (parts.length != 2) {
            throw new IncorrectQuerySyntaxException("Invalid DELETE syntax");
        }

        String tableName = parts[1].split(" where ")[0].trim();
        String wherePart = parts[1].contains(" where ") ? parts[1].split(" where ")[1].trim() : "";

        if (!tableExists(tableName)) {
            throw new TableException("Table does not exist");
        }

        Table table = schema.getTables().get(tableName);
        List<Map<String, Object>> rows = table.getRows();
        List<Map<String, Object>> rowsToRemove = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            if (wherePart.isEmpty() || evaluateWhereCondition(row, wherePart)) {
                rowsToRemove.add(row);
            }
        }

        rows.removeAll(rowsToRemove);

        try {
            writeChangesToFile();
            System.out.println("Rows deleted.");
        } catch (IOException e) {
            throw new TableException("Failed to save changes to file: ");
        }
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

        Set<String> validActions = Set.of("select", "insert", "update", "delete", "create");
        if (input.trim().length() == 0) {
            throw new IncorrectQuerySyntaxException("No query to execute");
        }

        if (("create".equalsIgnoreCase(action) && !"table".equalsIgnoreCase(splitedInput[1]))
                || !validActions.contains(action)) {
            throw new IncorrectQuerySyntaxException("Incorrect syntax near sql action.", input);
        }

        return action;
    }

}
