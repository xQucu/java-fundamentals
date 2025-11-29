public class DB {
    // varchar, text
    private String databaseFileName;

    public DB(String databaseFileName) {
        this.databaseFileName = databaseFileName;
    }

    public void exec(String statement, String action) throws IncorrectQuerySyntaxException, TableExistsException {
        String[] splitStatement = statement.toLowerCase().split(" ");
        switch (action.toLowerCase()) {
            case "create" -> createTable(statement);
            case "select" -> select(splitStatement);
            case "insert" -> insert(splitStatement);
            case "update" -> update(splitStatement);
            case "delete" -> delete(splitStatement);
            default -> throw new IncorrectQuerySyntaxException("Incorrect syntax near action: " + action);
        }
    }

    public void createTable(String statement) throws TableExistsException {
        String tableName = statement.split(" ")[2];
        if (tableExists(tableName)) {
            throw new TableExistsException("Table name provided in query already exists.");
        }
        String[] columns = statement.split("[()]")[1].split(",");

    }

    private void select(String[] splitStatement) {

    }

    private void insert(String[] splitStatement) {
    }

    private void update(String[] splitStatement) {
    }

    private void delete(String[] splitStatement) {
    }

    private boolean tableExists(String tableName) {
        return false;
    }

    private void writeChangesToFile() {

    }

}
