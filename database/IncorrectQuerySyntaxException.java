
public class IncorrectQuerySyntaxException extends Exception {
    private String query;

    public IncorrectQuerySyntaxException(String message, String query) {
        this.query = query;
        super(message);
    }

    public IncorrectQuerySyntaxException(String message) {
        super(message);
    }

    public String getQuery() {
        return query;
    }

}
