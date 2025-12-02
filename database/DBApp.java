import java.io.IOException;

public class DBApp {
    public static void main(String[] args) {
        IOmanager iom = new IOmanager();
        boolean running = true;
        DB db;
        try {
            db = new DB(Consts.dbFileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println(Consts.greetMessage);

        while (running) {
            String input = iom.handleInput();

            try {
                db.exec(input);
            } catch (IncorrectQuerySyntaxException e) {
                System.out.println(e.getMessage());
            } catch (TableException e) {
                System.out.println(e.getMessage());
            }

        }

    }
}
