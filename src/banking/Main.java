package banking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:sqlite:" + args[1];
        System.out.println(url);
        Database db = new Database(url);
        Scanner scanner = new Scanner(System.in);
        UserInterface user = new UserInterface(db,scanner);
        user.menu();
    }
}