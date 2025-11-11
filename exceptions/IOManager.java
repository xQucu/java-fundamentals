import java.util.ArrayList;
import java.util.Scanner;

public class IOManager {

    private Scanner scanner;

    public IOManager() {
        Scanner scanner = new Scanner(System.in);
        this.scanner = scanner;
    }

    public ArrayList<Integer> handleInput() {
        String input = "";
        input = scanner.nextLine().trim();

        String[] separatedValues = input.split(",");

        ArrayList<Integer> separatedNumbers = new ArrayList<Integer>();

        for (String StringVal : separatedValues) {
            try {
                int numVal = Integer.parseInt(StringVal);
                separatedNumbers.add(numVal);
            } catch (NumberFormatException _) {
                // ignore
            }
        }

        return separatedNumbers;
    }

    public void print(String message) {
        System.out.println(message);
    }
}
