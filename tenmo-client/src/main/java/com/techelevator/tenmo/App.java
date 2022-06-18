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
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
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
            /*} else if (menuSelection == 5) {
                requestBucks();*/
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        BigDecimal balance = accountService.getBalance(currentUser);
        System.out.println("\n****************************");
        System.out.println("Current balance is: $" + balance);
        System.out.println("****************************");
        //testing commit
    }

    private void viewTransferHistory() {
        User[] users = transferService.listUsers(currentUser);
        Transfer[] transferHistory = transferService.transferHistory(currentUser.getUser().getId(), currentUser);
        System.out.println(transferHistory);
    }

    private void viewPendingRequests() {
    }

    private void sendBucks() {
        transferService = new TransferService();
        Transfer transfer = new Transfer();
        User[] users = transferService.listUsers(currentUser);
        if (users != null) {
            System.out.println("----------------------------------------------\n" +
                    "Users\n" +
                    "ID            Name\n" +
                    "----------------------------------------------");
            for (User user : users) {
                System.out.println(accountService.getAccountId(user.getId(), currentUser) + "          " + accountService.getUsername(user.getId(), currentUser));
            }
            System.out.println("----------------------------------------------");
        }

        console = new ConsoleService();

        Integer userTo = console.promptForInt("Enter the user ID who you would like to transfer to \n");
        Integer accountIdFrom = accountService.getAccountId(currentUser.getUser().getId(), currentUser);
        while (accountIdFrom.equals(userTo)) {
            userTo = console.promptForInt("Cmon now, no infinite money glitch\n");
            continue;
        }
        //while statement to make sure that you can't transfer to a random number
        //while(userTo){
        //}
        int amount = console.promptForInt("Enter amount you would like to transfer \n");
        while (accountService.getBalance(currentUser).intValue() < amount) {
            amount = console.promptForInt("Amount entered exceeds your balance, please enter a valid amount \n");
            continue;
        }
        Integer accountIdTo = userTo;

        transfer.setTransferTypeId(2);
        transfer.setTransferStatusId(2);
        transfer.setAccountFrom(accountIdFrom);
        transfer.setAccountTo(accountIdTo);
        transfer.setAmount(BigDecimal.valueOf(amount));

        transferService.addTransfer(transfer, accountIdFrom, accountIdTo, currentUser);
        BigDecimal balance = accountService.getBalance(currentUser);
        System.out.println("\nYour transfer has been completed, your new balance is: $" + balance);


    }

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}

