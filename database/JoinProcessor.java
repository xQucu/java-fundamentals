import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinProcessor {

    public List<Map<String, Object>> performJoin(
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
}
