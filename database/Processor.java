import java.util.Set;

public class Processor {

    public String getAction(String input) throws IncorrectQuerySyntaxException {
        String[] splitedInput = input.split(" ");
        String action = splitedInput[0].toLowerCase();

        Set<String> validActions = Set.of("select", "insert", "update", "delete");
        if (input.trim().length() == 0) {
            throw new IncorrectQuerySyntaxException("No query to execute");
        }

        if (("create".equals(action) && !"table".equalsIgnoreCase(splitedInput[1]))
                || !validActions.contains(action)) {
            throw new IncorrectQuerySyntaxException("Incorrect syntax near sql action.", input);
        }

        return action;
    }
}
