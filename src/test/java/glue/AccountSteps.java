package glue;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.junit.Assert.assertTrue;

import account.Account;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccountSteps {

    private Account account;
    private String statement;

    // Create a Logger instance
    private static final Logger logger = Logger.getLogger(AccountSteps.class.getName());

    static {
        // Set a console handler to format the log output
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.INFO); // Set log level
    }

    @Given("Account exists for Acc No. {string} with Name {string}")
    public void account_exists_for_acc_no_with_name(String accountNumber, String name) {
        logger.log(Level.INFO, "Creating an account for {0} with Account No. {1}", new Object[]{name, accountNumber});
        account = new Account(accountNumber, name);
    }

    @Given("deposits are made")
    public void deposits_are_made(DataTable dataTable) {
        logger.log(Level.INFO, "Deposits are being made.");
        dataTable.asLists().forEach(row -> {
            String depositType = row.get(0);  // e.g., INIT, DEP1, etc.
            double amount = Double.parseDouble(row.get(1));
            logger.log(Level.FINE, "Deposit: Type = {0}, Amount = {1}", new Object[]{depositType, amount});
            account.deposit(depositType, amount);
        });
    }

    @Given("withdrawls are made")
    public void withdrawals_are_made(DataTable dataTable) {
        logger.log(Level.INFO, "Withdrawals are being made.");
        dataTable.asLists().forEach(row -> {
            String withdrawalType = row.get(0);  // e.g., CHQ001
            double amount = Double.parseDouble(row.get(1));
            logger.log(Level.FINE, "Withdrawal: Type = {0}, Amount = {1}", new Object[]{withdrawalType, amount});
            account.withdraw(withdrawalType, amount);
        });
    }

    @When("statement is produced")
    public void statement_is_produced() {
        logger.log(Level.INFO, "Producing account statement.");
        statement = account.produceStatement();
    }

    @Then("statement includes {string}")
    public void statement_includes(String expectedText) {
        logger.log(Level.INFO, "Checking if the statement includes {0}", expectedText);
        assertTrue("Expected statement to include: " + expectedText, statement.contains(expectedText));
    }
}
