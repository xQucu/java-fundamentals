
public class TableException extends Exception {
    private String query;

    public TableException(String message, String query) {
        this.query = query;
        super(message);
    }

    public TableException(String message) {
        super(message);
    }

    public String getQuery() {
        return query;
    }

}
