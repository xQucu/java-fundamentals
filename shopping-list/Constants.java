import java.util.List;

public class Constants {
    public static final String INPUT_FILE_NAME = "data.txt";
    public static final String MAIN_MENU_PROMPT = "Choose action by entering corresponding number and clicking enter";
    public static final List<String> MAIN_MENU = List.of("Add product", "Remove product", "Show list", "Save list",
            "Quit");

    public static final String ADD_PRODUCT_CATEGORIES_MENU = "Select category by entering corresponding number and clicking enter or choose to go back";
    public static final String ADD_PRODUCT_FINAL_MENU = "Select item to add by entering corresponding number and clicking enter or choose to go back";
    public static final String GOODBYE_MESSAGE = "Goodbye";
    public static final String GO_BACK_OPTION = "Go back";
    public static final String OUTPUT_FILE_NAME = "output.txt";
    public static final int DISSAPEARING_MESSAGE_WAIT_MILISECONDS = 2000;
    public static final String ON_ADD_MESSAGE = "Item was sucesfully added !";
    public static final String SHOW_LIST_MESSAGE = "The list of product you have chosen is below, to continue press enter";
    public static final String EMPTY_SHOW_LIST_MESSAGE = "Looks like there is nothing here yet, press enter to continue";
    public static final String SUCESSFULL_LIST_SAVE = "Your list was sucesfully saved, press enter to continue";
    public static final String FAILED_LIST_SAVE = "Saving your list failed, press enter to continue";
    public static final String REMOVE_ITEM_MENU = "Select item by entering corresponding number and clicking enter or choose to go back";
    public static final String INCORRECT_INPUT_WARNING = "Choice incorrect, please try again.";
}
