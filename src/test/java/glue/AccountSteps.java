package glue;

import account.Account;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class AccountSteps {

    private Account account;
    private String statement;

    @Given("Account exists for Acc No. {string} with Name {string}")
    public void account_exists_for_acc_no_with_name(String accountNumber, String name) {
        account = new Account(accountNumber, name);
    }

    @Given("deposits are made")
    public void deposits_are_made(DataTable dataTable) {
        dataTable.asLists().forEach(row -> {
            String depositType = row.get(0);  // e.g., INIT, DEP1, etc.
            double amount = Double.parseDouble(row.get(1));
            account.deposit(depositType, amount);
        });
    }

    @Given("withdrawls are made")
    public void withdrawals_are_made(DataTable dataTable) {
        dataTable.asLists().forEach(row -> {
            String withdrawalType = row.get(0);  // e.g., CHQ001
            double amount = Double.parseDouble(row.get(1));
            account.withdraw(withdrawalType, amount);
        });
    }

    @When("statement is produced")
    public void statement_is_produced() {
        statement = account.produceStatement();
    }

    @Then("statement includes {string}")
    public void statement_includes(String expectedText) {
        assertTrue("Expected statement to include: " + expectedText, statement.contains(expectedText));
    }
}
