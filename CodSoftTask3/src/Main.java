
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the ATM!");

        UserDatabase userDatabase = new UserDatabase();
        ATM atm = new ATM(userDatabase);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Create Account");
            System.out.println("2. Log In");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter your name:");
                    String userName = scanner.next();

                    // Generate a unique account number (you can implement this based on your requirements)
                    int accountNumber = (int) (Math.random() * 1000000);
                    System.out.println("Your account has been created. Your account number is: " + accountNumber);

                    System.out.println("Enter initial balance:");
                    double initialBalance = scanner.nextDouble();

                    userDatabase.addUser(accountNumber, initialBalance);
                    break;
                case 2:
                    System.out.println("Enter your account number:");
                    int enteredAccountNumber = scanner.nextInt();

                    if (userDatabase.getUser(enteredAccountNumber) == null) {
                        System.out.println("User not found. Please enter a valid account number.");
                        continue;
                    }

                    System.out.println("\nChoose an option:");
                    System.out.println("1. Deposit");
                    System.out.println("2. Withdraw");
                    System.out.println("3. Check Balance");
                    System.out.println("4. Exit");

                    int subChoice = scanner.nextInt();

                    switch (subChoice) {
                        case 1:
                            System.out.println("Enter deposit amount:");
                            double depositAmount = scanner.nextDouble();
                            atm.deposit(enteredAccountNumber, depositAmount);
                            break;
                        case 2:
                            System.out.println("Enter withdrawal amount:");
                            double withdrawalAmount = scanner.nextDouble();
                            atm.withdraw(enteredAccountNumber, withdrawalAmount);
                            break;
                        case 3:
                            atm.checkBalance(enteredAccountNumber);
                            break;
                        case 4:
                            System.out.println("Exiting. Thank you!");
                            scanner.close();
                            System.exit(0);
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                    break;
                case 3:
                    System.out.println("Exiting. Thank you!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
