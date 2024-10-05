package Utility;

import Model.Bank;

import java.util.ArrayList;

public interface BankInterface {

    int createAccount(Bank b);

    int deposite(Bank b);

    int withdraw(Bank b4);

    double displayBalance(Bank b5);
    //int transferAmountToAnotherAccount();

    int transferAmountToAnotherAccount(Bank b);

    ArrayList<Bank> accountStatement(Bank b);
}
