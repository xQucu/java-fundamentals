import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Table {
    private String name;
    private List<Column> columns;
    private List<Map<String, Object>> rows;

    public Table() {
        this.rows = new ArrayList<>();
    }

    public Table(String name, List<Column> columns) {
        this.name = name;
        this.columns = columns;
        this.rows = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public Column getColumnByName(String columnName) {
        for (Column col : columns) {
            if (col.getName().equalsIgnoreCase(columnName)) {
                return col;
            }
        }
        return null;
    }
}
