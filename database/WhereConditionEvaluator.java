import java.util.Map;

public class WhereConditionEvaluator {

    public boolean evaluate(Map<String, Object> row, String whereCondition) {
        if (whereCondition == null || whereCondition.isEmpty()) {
            return true;
        }

        String[] conditionParts = whereCondition.split("=");
        if (conditionParts.length != 2) {
            return false;
        }

        String columnName = conditionParts[0].trim();
        String value = conditionParts[1].trim();

        if (value.startsWith("'") && value.endsWith("'")) {
            value = value.substring(1, value.length() - 1);
        }

        Object rowValue = row.get(columnName);
        if (rowValue == null) {
            return false;
        }

        if (rowValue instanceof Integer) {
            try {
                int intValue = Integer.parseInt(value);
                return rowValue.equals(intValue);
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return rowValue.toString().equalsIgnoreCase(value);
        }
    }

    public boolean evaluateForJoin(Map<String, Object> row, String whereCondition,
            String table1Name, String table2Name) {
        if (whereCondition == null || whereCondition.isEmpty()) {
            return true;
        }

        String[] conditionParts = whereCondition.split("=");
        if (conditionParts.length != 2) {
            return false;
        }

        String columnName = conditionParts[0].trim();
        String value = conditionParts[1].trim();

        if (!columnName.contains(".")) {
            return false;
        }

        if (value.startsWith("'") && value.endsWith("'")) {
            value = value.substring(1, value.length() - 1);
        }

        Object rowValue = row.get(columnName);
        if (rowValue == null) {
            return false;
        }

        if (rowValue instanceof Integer) {
            try {
                int intValue = Integer.parseInt(value);
                return rowValue.equals(intValue);
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return rowValue.toString().equalsIgnoreCase(value);
        }
    }
}
