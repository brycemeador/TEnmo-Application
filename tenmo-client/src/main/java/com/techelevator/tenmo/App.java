package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AccountService accountService = new AccountService();
    private TransferService transferService = new TransferService();

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.loginPause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("\nRegistration successful. You can now login.");
        } else {
            consoleService.registrationError();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.loginError();
        } else {
            consoleService.printWelcome();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {

            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                System.out.println("\nThank you for using TEnmo, have a great day!");
                continue;
            } else {
                System.out.println("\nInvalid Selection");
            }
            consoleService.pause();
        }
    }

    //Allows for Strings to be formatted
    private String leftpad(String text, int length) {
        return String.format("%-" + length + "." + length + "s", text);
    }

    //Displays the current user's balance
    private void viewCurrentBalance() {
        BigDecimal balance = accountService.getBalance(currentUser);
        System.out.println("\n****************************" +
                "\nCurrent balance is: $" + balance +
                "\n****************************");
    }

    //Displays the user's transfer history, listing the transfer ID, who it was from/to, and the amount of the transfer.
    //User can then select a transfer using the transfer ID and it will display the full details about it.
    private void viewTransferHistory() {
        transferService = new TransferService();
        Transfer[] transferHistory = transferService.transferHistory(accountService.getAccountId(currentUser.getUser().getId(), currentUser), currentUser);
        User[] users = transferService.listUsers(currentUser);
        String username = null;

        //Checks that the users transfer history exist
        if (transferHistory.length == 0) {
            System.out.println("\n 🚫 No transfers to display 🚫");
        } else {
            System.out.println("-------------------------------------\n" +
                    "Transfer History\n" +
                    "ID         From/To         Amount\n" +
                    "-------------------------------------");

            for (Transfer transfer : transferHistory) {
                if (users != null) {
                    for (User user : users) {
                        if (accountService.getAccountId(user.getId(), currentUser).equals(transfer.getAccountTo()) && !currentUser.getUser().getId().equals(user.getId())) {
                            username = user.getUsername();
                        }
                        if (accountService.getAccountId(user.getId(), currentUser).equals(transfer.getAccountFrom()) && !currentUser.getUser().getId().equals(user.getId())) {
                            username = user.getUsername();
                        }
                    }
                    System.out.print(transfer.getTransferID() + "       ");
                    if (accountService.getAccountId(currentUser.getUser().getId(), currentUser).equals(transfer.getAccountFrom())) {
                        System.out.print("To:   " + leftpad(username, 10) + "$" + transfer.getAmount() + "\n");
                    } else {
                        System.out.print("From: " + leftpad(username, 10) + "$" + transfer.getAmount() + "\n");
                    }
                }
            }

            //Allows you to select a transfer based on transfer ID and it will then show the details of the selected transfer
            System.out.println("-------------------------------------");
            console = new ConsoleService();
            Integer transferSelection = console.promptForInt("Please enter transfer ID to view details or press any number to return\n");
            for (Transfer transfer : transferHistory) {
                if (transferSelection.equals(transfer.getTransferID())) {
                    Transfer selectedTransfer = transferService.transferDetails(transferSelection);
                    System.out.println("----------------------------\n" +
                            "Transfer Details\n" +
                            "----------------------------");
                    System.out.println(leftpad("Transfer ID:", 18) + selectedTransfer.getTransferID());

                    int toUser = selectedTransfer.getAccountTo();
                    int toUser1 = Integer.parseInt(accountService.getUserId(toUser, currentUser));
                    int fromUser = selectedTransfer.getAccountFrom();
                    int fromUser1 = Integer.parseInt(accountService.getUserId(fromUser, currentUser));

                    if (selectedTransfer.getAccountFrom().equals(accountService.getAccountId(currentUser.getUser().getId(), currentUser))) {
                        System.out.println(leftpad("From:", 18) + currentUser.getUser().getUsername());
                        System.out.println(leftpad("To:", 18) + accountService.getUsername(toUser1, currentUser));
                    } else {
                        System.out.println(leftpad("From:", 18) + accountService.getUsername(fromUser1, currentUser));
                        System.out.println(leftpad("To:", 18) + currentUser.getUser().getUsername());
                    }

                    if (selectedTransfer.getTransferTypeId().equals(2)) {
                        System.out.println(leftpad("Type:", 18) + "Send");
                    } else {
                        System.out.println(leftpad("Type:", 18) + "Received");
                    }

                    System.out.println(leftpad("Status:", 18) + "Approved");
                    System.out.println(leftpad("Amount:", 18) + "$" + transfer.getAmount());
                }
            }
        }
    }

    private void viewPendingRequests() {
        consoleService.nextUpdate();
    }

    //Allows user to transfer $$$ to another user
    private void sendBucks() {
        transferService = new TransferService();
        Transfer transfer = new Transfer();
        User[] users = transferService.listUsers(currentUser);

        //Verifies there are other users and if so, displays their account IDs and their usernames
        if (users != null) {
            System.out.println("-------------------------------------\n" +
                    "Users\n" +
                    "ID            Name\n" +
                    "-------------------------------------");
            for (User user : users) {
                System.out.println(accountService.getAccountId(user.getId(), currentUser) + "          " + accountService.getUsername(user.getId(), currentUser));
            }
            System.out.println("-------------------------------------");
        }

        console = new ConsoleService();

        Integer userTo = console.promptForInt("Enter the user ID who you would like to transfer to \n");
        Integer accountIdFrom = accountService.getAccountId(currentUser.getUser().getId(), currentUser);

        //Checks that you aren't transferring to yourself
        while (accountIdFrom.equals(userTo)) {
            userTo = console.promptForInt("Cmon now, no infinite money glitch\n");
            continue;
        }

        BigDecimal amount = console.promptForBigDecimal("Enter amount you would like to transfer \n");

        //Checks that the transfer amount doesn't exceed user's balance
        while (accountService.getBalance(currentUser).intValue() < amount.intValue()) {
            amount = console.promptForBigDecimal("Amount entered exceeds your balance, please enter a valid amount \n");
            continue;
        }

        //Checks that transfer is greater than $0
        if (0.01 >= amount.intValue()) {
            amount = console.promptForBigDecimal("Transfer must be greater than 0 \n");
        }

        Integer accountIdTo = userTo;

        transfer.setTransferTypeId(2);
        transfer.setTransferStatusId(2);
        transfer.setAccountFrom(accountIdFrom);   //sets the data in SQL
        transfer.setAccountTo(accountIdTo);
        transfer.setAmount(amount);

        transferService.addTransfer(transfer, accountIdFrom, accountIdTo, currentUser);
        BigDecimal balance = accountService.getBalance(currentUser);
        System.out.println("\nYour transfer has been completed, your new balance is: $" + balance);
    }

    private void requestBucks() {
        consoleService.nextUpdate();
    }

}

