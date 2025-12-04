import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class IOmanager {

    private Scanner scanner;

    public IOmanager() {
        Scanner scanner = new Scanner(System.in);
        this.scanner = scanner;
    }

    public String handleInput() {
        String input = "";
        try {
            input = scanner.nextLine().trim();
        } catch (Exception e) {
            return "";
        }
        return input;

    }

    public void print(String message) {
        System.out.println(message);
    }

    public static void displayResults(List<Map<String, Object>> rows, List<String> columns) {
        if (rows.isEmpty()) {
            System.out.println("No rows found.");
            return;
        }

        System.out.println();
        for (String col : columns) {
            System.out.printf("%-20s", col);
        }
        System.out.println();

        for (String _ : columns) {
            System.out.print("--------------------");
        }
        System.out.println();

        for (Map<String, Object> row : rows) {
            for (String col : columns) {
                Object value = row.get(col);
                String displayValue = (value != null) ? value.toString() : "NULL";
                System.out.printf("%-20s", displayValue);
            }
            System.out.println();
        }

        System.out.println("\n" + rows.size() + " row(s) returned.");
    }
}
