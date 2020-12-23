package banking;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
public class UserInterface {
    private ArrayList<BankAccount> list;
    private Scanner scanner;
    private Database db;
    public UserInterface(Database db,Scanner scanner) {
        this.list = new ArrayList<>();
        this.db = db;
        this.scanner = scanner;
    }

    public void menu() {
        boolean exitFlag = true;
        while(exitFlag) {
            System.out.println("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");
            int choice = this.scanner.nextInt();
            switch (choice) {
                case 1:
                    BankAccount account = new BankAccount();
                    account.setAccountNumber();
                    account.setPin();
                    System.out.println("Your card has been created");
                    System.out.println("Your card number:");
                    System.out.println(account.getAccountNumber());
                    System.out.println("Your card PIN:");
                    System.out.println(account.getPin());
                    this.db.storeDataInDatabase(account.getAccountNumber(), account.getPin());
                    System.out.println("data stored in db");
                    this.list.add(account);
                    break;
                case 2:
                    System.out.println("Enter your card number:");
                    String accountNumber = this.scanner.next();
                    System.out.println("Enter your PIN:");
                    String pin = this.scanner.next();
                    boolean flag1 = db.isExist(accountNumber, pin);
                    if (flag1) {
                        System.out.println("You have successfully logged in!");
                        exitFlag = loggedIn(accountNumber);
                        break;
                    } else {
                        System.out.println("Wrong card number or PIN!");
                        break;
                    }
                case 0:
                    exitFlag = false;
            }
        }

        System.out.println("Bye!");

    }

    public boolean loggedIn(String account) {
        while (true) {
            String[] details = db.getData(account).split(" ");
            System.out.println("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit");
            int ch = this.scanner.nextInt();
            if (ch == 1) {
                System.out.println("Balance: " + details[3]);
            } else if (ch == 2) {
                System.out.println("Enter income:");
                int amount = this.scanner.nextInt();
                db.addIncome(details[1], amount);
                System.out.println("Income was added");
            } else if (ch == 0) {
                return false;
            } else if (ch == 3) {//transfering money to differnt account
                System.out.println("Transfer");
                System.out.print("Enter card number:");
                String accountNumber = this.scanner.next();
                if (details[1].equals(accountNumber)) {//Checking if the number enterd is same
                    System.out.println("You can't transfer money to the same account!");
                    continue;
                }
                boolean isCorrect = checkForLuhn(accountNumber);//applying luhn's algorithm for veriifying accoount number
                if (isCorrect) {
                    String s = db.getData(accountNumber);//checking if the account exist or not
                    if (s.equals("")) {
                        System.out.println("Such a card does not exist.");
                        continue;
                    }//Transfering money to differnt account.
                    System.out.println("Enter how much money you want to transfer:");
                    int mount = this.scanner.nextInt();
                    if (mount > Integer.parseInt(details[3])) {
                        System.out.println("Not enough money!");
                    } else {
                        db.transfer(details[1],accountNumber,mount);
                    }
                } else {
                    System.out.println("Probably you made mistake in the card number. Please try again!");
                }
            } else if(ch == 4) {//deleting an account
                db.delete(details[1]);
                System.out.println("The account has been closed!");
                return true;
            } else if (ch == 5) {
                System.out.println("You have successfully logged out!");
                return true;
            }
        }
    }

    public boolean checkForLuhn(String account) {
        int[] array = new int[16];
        for (int i = 0; i < 16; i++) {
            array[i] = Integer.parseInt(account.substring(i, i + 1));
        }

        for (int i = 0; i < array.length-1; i += 2) {
            array[i] *= 2;

        }
//        System.out.println(Arrays.toString(array));
        for (int i = 0; i < array.length-1; i+=2) {
            if (array[i] > 9) {
                array[i] -= 9;
            }
        }
        int sum = 0;
        for (int i = 0; i < array.length - 1; i++) {
            sum += array[i];
        }
//        System.out.println(sum);
        int rem = sum % 10;
        int val = 10 - rem;
//        System.out.println(val);
        if (array[15] == val) {
            return true;
        } else {
            return false;
        }

    }
}
