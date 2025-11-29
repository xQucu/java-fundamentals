
public class DBApp {
    public static void main(String[] args) {
        IOmanager iom = new IOmanager();
        Processor processor = new Processor();
        boolean running = true;
        DB db = new DB(Consts.dbFileName);
        System.out.println(Consts.greetMessage);

        while (running) {
            String input = iom.handleInput();
            String action = "";

            try {
                action = processor.getAction(input);
                db.exec(input, action);
            } catch (IncorrectQuerySyntaxException e) {
                System.out.println(e.getMessage());
            } catch (TableExistsException e) {
                System.out.println(e.getMessage());
            }

        }

    }
}
