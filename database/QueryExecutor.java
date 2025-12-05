public class QueryExecutor {
    private DatabaseSchema schema;
    private String databaseFileName;
    private CreateTableHandler createHandler;
    private SelectHandler selectHandler;
    private InsertHandler insertHandler;
    private UpdateHandler updateHandler;
    private DeleteHandler deleteHandler;

    public QueryExecutor(DatabaseSchema schema, String databaseFileName) {
        this.schema = schema;
        this.databaseFileName = databaseFileName;
        this.createHandler = new CreateTableHandler(schema, databaseFileName);
        this.selectHandler = new SelectHandler(schema);
        this.insertHandler = new InsertHandler(schema, databaseFileName);
        this.updateHandler = new UpdateHandler(schema, databaseFileName);
        this.deleteHandler = new DeleteHandler(schema, databaseFileName);
    }

    public void execute(String action, String statement)
            throws IncorrectQuerySyntaxException, TableException {
        switch (action.toLowerCase()) {
            case "create" -> createHandler.handle(statement);
            case "select" -> selectHandler.handle(statement);
            case "insert" -> insertHandler.handle(statement);
            case "update" -> updateHandler.handle(statement);
            case "delete" -> deleteHandler.handle(statement);
            default -> throw new IncorrectQuerySyntaxException("Incorrect syntax near action: " + action);
        }
    }
}
