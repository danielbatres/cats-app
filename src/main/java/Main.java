import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int optionMenu = -1;
        String[] buttons = {"1. See cats", "2. See favourites", "3. Exit"};

        do {
            // Main menu
            String option = (String) JOptionPane.showInputDialog(null, "Java Cats", "Main menu", JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);

            // Validate wich option the user selects

            for (int i = 0; i < buttons.length; i++) {
                if (option.equals(buttons[i])) {
                    optionMenu = i;
                }
            }

            switch (optionMenu) {
                case 0:
                    CatService.seeCats();
                    break;
                case 1:
                    Cats cat = new Cats();
                    CatService.seeFavourites(cat.getApiKey());
                    break;
                default:
                    break;
            }
        } while (optionMenu != 1);
    }
}
