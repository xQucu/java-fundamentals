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
}
