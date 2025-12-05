import java.util.Set;

public class QueryParser {

    public String normalize(String statement) {
        statement = statement.toLowerCase();
        return removeTrailingSemicolon(statement);
    }

    public String getAction(String input) throws IncorrectQuerySyntaxException {
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

    public String normalizeDataType(String columnType) throws IncorrectQuerySyntaxException {
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

    private String removeTrailingSemicolon(String statement) {
        if (statement.endsWith(";")) {
            statement = statement.substring(0, statement.length() - 1);
        }
        return statement;
    }
}
