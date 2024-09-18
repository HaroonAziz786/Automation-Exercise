package account;

import java.util.ArrayList;
import java.util.List;

public class Account {

    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private List<String> transactions;

    // Constructor to create an account with account number and holder's name
    public Account(String accountNumber, String accountHolderName) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    // Method to handle deposits
    public void deposit(String code, double amount) {
        balance += amount;
        transactions.add(String.format("%s: %.2f", code, amount));
    }

    // Method to handle withdrawals
    public void withdraw(String code, double amount) {
        balance -= amount;
        transactions.add(String.format("%s: -%.2f", code, amount));
    }

    // Method to produce the statement
    public String produceStatement() {
        StringBuilder statement = new StringBuilder();
    
        // Add account details
        statement.append("Name: ").append(accountHolderName).append("\n");
        statement.append("Account: ").append(accountNumber).append("\n");
    
        // Add transaction details
        statement.append("Transactions:\n");
        for (String transaction : transactions) {
            statement.append(transaction).append("\n");
        }
    
        // Add balance
        statement.append("Balance: ").append(String.format("%.2f", balance));
    
        return statement.toString(); // Ensure a valid statement is returned
    }
    

    // Getters for account number and name (if needed for assertions)
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }
}
