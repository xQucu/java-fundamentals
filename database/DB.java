import java.io.IOException;

public class DB {
    private DatabaseSchema schema;
    private QueryParser parser;
    private QueryExecutor executor;

    public DB(String databaseFileName) throws IOException {
        this.schema = FileManager.loadDatabase(databaseFileName);
        this.parser = new QueryParser();
        this.executor = new QueryExecutor(schema, databaseFileName);
    }

    public void exec(String statement) throws IncorrectQuerySyntaxException, TableException {
        String action = parser.getAction(statement);
        statement = parser.normalize(statement);
        executor.execute(action, statement);
    }
}
