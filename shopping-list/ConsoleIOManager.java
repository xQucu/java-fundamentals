import java.util.List;
import java.util.Scanner;

public class ConsoleIOManager {
    private String choosenOption;
    private Scanner scanner;

    public ConsoleIOManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public ConsoleIOManager() {
        this.choosenOption = null;
    }

    public void displayMenu(String prompt, List<String> options) {
        // System.out.print("\033[H\033[2J");
        // System.out.flush();
        System.out.println(prompt);
        for (int i = 0; i < options.size(); i++) {
            String option = options.get(i);
            System.out.println((i + 1) + ": " + option);
        }
    }

    public void handleInput(List<String> options) {
        // FIXME: proper handling of incorrect inputs
        int input = Integer.parseInt(scanner.next());
        if (input > 0 && input <= options.size()) {
            this.choosenOption = options.get(input - 1);
        }
    }

    public String getChoice() {
        return this.choosenOption;
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
