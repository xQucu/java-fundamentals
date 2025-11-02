import java.io.IOException;
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
        this.clearScreen();
        System.out.println(prompt);
        for (int i = 0; i < options.size(); i++) {
            String option = options.get(i);
            System.out.println((i + 1) + ": " + option);
        }
    }

    public void handleInput(List<String> options) {
        int input;
        try {
            input = Integer.parseInt(scanner.next());
        } catch (NumberFormatException _) {
            input = 0;
        }
        if (input > 0 && input <= options.size()) {
            this.choosenOption = options.get(input - 1);
        }
    }

    public void showLines(List<String> lines) {
        for (String line : lines) {
            System.out.println(line);
        }
    }

    public void waitForAnyInputToContinue() {
        try {
            System.in.read();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void clearScreen() {
        // System.out.print("\033[H\033[2J");
        // System.out.flush();
    }

    public String getChoice() {
        System.out.println(this.choosenOption);
        return this.choosenOption;
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showDissapearingMessage(String message) {
        clearScreen();
        System.out.println(message);
        try {
            Thread.sleep(Constants.DISSAPEARING_MESSAGE_WAIT_MILISECONDS);
        } catch (InterruptedException e) {
            System.err.println(e);
        } finally {
            this.clearScreen();
        }
    }
}
