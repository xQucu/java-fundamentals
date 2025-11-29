
public class TableExistsException extends Exception {
    private String query;

    public TableExistsException(String message, String query) {
        this.query = query;
        super(message);
    }

    public TableExistsException(String message) {
        super(message);
    }

    public String getQuery() {
        return query;
    }

}
