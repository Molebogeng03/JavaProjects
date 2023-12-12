import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class User {
    private double balance;

    public User(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposit successful. New balance: " + balance);
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient funds. Withdrawal failed.");
            return false;
        } else {
            balance -= amount;
            System.out.println("Withdrawal successful. New balance: " + balance);
            return true;
        }
    }
}

class UserDatabase {
    private Map<Integer, User> users;

    public UserDatabase() {
        this.users = new HashMap<>();
    }

    public void addUser(int accountNumber, double initialBalance) {
        User user = new User(initialBalance);
        users.put(accountNumber, user);
    }

    public User getUser(int accountNumber) {
        return users.get(accountNumber);
    }
}

class ATM {
    private UserDatabase userDatabase;

    public ATM(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    public void deposit(int accountNumber, double amount) {
        User user = userDatabase.getUser(accountNumber);
        if (user != null) {
            user.deposit(amount);
        } else {
            System.out.println("User not found.");
        }
    }

    public void withdraw(int accountNumber, double amount) {
        User user = userDatabase.getUser(accountNumber);
        if (user != null) {
            user.withdraw(amount);
        } else {
            System.out.println("User not found.");
        }
    }

    public void checkBalance(int accountNumber) {
        User user = userDatabase.getUser(accountNumber);
        if (user != null) {
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Current balance: " + user.getBalance());
        } else {
            System.out.println("User not found.");
        }
    }
}

