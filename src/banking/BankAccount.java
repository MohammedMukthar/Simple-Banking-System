package banking;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BankAccount {
    private long accountNumber;
    private String pin;
    private int balance;

    public BankAccount() {
        this.accountNumber = 4000000000000000L;
        this.pin = "";
        this.balance = 0;
    }


    public void setAccountNumber() {
        long num = ThreadLocalRandom.current().nextLong(000000000l, 999999999l) * 10;
        this.accountNumber += num;
        luhnsAlgorithm();
    }
    // luhns Algorithm Implementation
    public void luhnsAlgorithm() {
        String temp = Long.toString(this.accountNumber);
        int[] array = new int[16];
        for (int i = 0; i < 16; i++) {
            array[i] = Integer.parseInt(temp.substring(i, i + 1));
        }
        for (int i = 0; i < array.length-1; i += 2) {
            array[i] *= 2;

        }
        for (int i = 0; i < array.length-1; i+=2) {
            if (array[i] > 9) {
                array[i] -= 9;
            }
        }
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        int rem = sum % 10;
        int val = 10 - rem;
        if (val != 10) {
            this.accountNumber += val;
        } else {
            this.accountNumber += 0;
        }

    }

    public String getAccountNumber() {
        return Long.toString(this.accountNumber);
    }

    public void setPin() {
        Random random = new Random();
        int range = (9999 - 0000 + 1);
        int pin = random.nextInt(range);
        String p = Integer.toString(pin);
        if  (p.length() < 4) {
            int count = p.length();
            while (count != 4) {
                p = "0" + p;
                count++;
            }
        }
        this.pin = p;
    }

    public String getPin() {
        return this.pin;
    }

    public void setBalance(int balance) {
        this.balance += balance;
    }

    public int getBalance() {
        return this.balance;
    }
}
