import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectHandler {
    private DatabaseSchema schema;
    private WhereConditionEvaluator whereEvaluator;
    private JoinProcessor joinProcessor;

    public SelectHandler(DatabaseSchema schema) {
        this.schema = schema;
        this.whereEvaluator = new WhereConditionEvaluator();
        this.joinProcessor = new JoinProcessor();
    }

    public void handle(String statement) throws TableException, IncorrectQuerySyntaxException {
        boolean hasJoin = statement.contains(" join ");

        if (hasJoin) {
            handleJoinQuery(statement);
        } else {
            handleSimpleQuery(statement);
        }
    }

    private void handleSimpleQuery(String statement) throws TableException, IncorrectQuerySyntaxException {
        String[] statementValues = statement.toLowerCase().split("select | from | where ");

        if (statementValues.length < 3) {
            throw new IncorrectQuerySyntaxException("Incorrect syntax");
        }
        String columns = statementValues[1].trim();
        String tableName = statementValues[2].trim();
        String whereCondition = statementValues.length > 3 ? statementValues[3].trim() : "";

        if (!schema.tableExists(tableName)) {
            throw new TableException("Table does not exist", statement);
        }

        Table table = schema.getTables().get(tableName);
        List<Map<String, Object>> rows = table.getRows();

        if (!whereCondition.isEmpty()) {
            List<Map<String, Object>> filteredRows = new ArrayList<>();
            for (Map<String, Object> row : rows) {
                if (whereEvaluator.evaluate(row, whereCondition)) {
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

    private void handleJoinQuery(String statement) throws TableException, IncorrectQuerySyntaxException {
        String[] statementValues = statement.toLowerCase().split("select | from | join | on | where ");

        if (statementValues.length < 5) {
            throw new IncorrectQuerySyntaxException("Incorrect syntax");
        }
        String columns = statementValues[1].trim();
        String table1Name = statementValues[2].trim();
        String table2Name = statementValues[3].trim();
        String onCondition = statementValues[4].trim();
        String whereCondition = statementValues.length > 5 ? statementValues[5].trim() : "";

        if (!schema.tableExists(table1Name) || !schema.tableExists(table2Name)) {
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

        List<Map<String, Object>> joinedRows = joinProcessor.performJoin(
                table1, table2,
                leftTable, leftColumn,
                rightTable, rightColumn);

        if (!whereCondition.isEmpty()) {
            List<Map<String, Object>> filteredRows = new ArrayList<>();
            for (Map<String, Object> row : joinedRows) {
                if (whereEvaluator.evaluateForJoin(row, whereCondition, table1Name, table2Name)) {
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
    }

}
