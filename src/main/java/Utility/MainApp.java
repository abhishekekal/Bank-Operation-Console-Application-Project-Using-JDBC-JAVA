package Utility;

import Model.Bank;


import java.util.ArrayList;
import java.util.Scanner;



public class MainApp {

    private static Scanner sc=new Scanner(System.in);
    private static BankInterface bankInterface=new BankOperationClass();//OBJECT CREATED TO CALL BANKOPERATIONCLASS METHODS
    public static void main(String[] args) {
        operation();
    }
    private static void operation(){
        boolean status=true;

        while (status){
            System.out.println("======================");
            System.out.println("1 : CREATE ACCOUNT");
            System.out.println("2 : PERFORM TRANSACTION ");
            System.out.println("3 : EXIT  ");
            System.out.println("======================");
            System.out.print("ENTER CHOICE : ");
            int choice= sc.nextInt();

            if (choice==1){
               createAccountMethod();
            }else if (choice==2){
                performOperation();
            }else if (choice==3){
                break;
            }else {
                System.out.println("INVALID CHOICE");
            }
        }
    }
    private static void createAccountMethod(){
        System.out.print("ENTER ACCOUNT NO: ");
        int accountNo= sc.nextInt();
        System.out.print("ENETR ACCOUNT HOLDER NAME : ");
        String accountHolderName= sc.next();
        System.out.print("ENTER INITIAL BALANCE : ");
        double initialBalance= sc.nextDouble();

        if (initialBalance>=5000){//initial balance should be 5000 or more
            Bank b1=new Bank();
            b1.setAccountNo(accountNo);
            b1.setAccountHolderName(accountHolderName);
            b1.setDepositAmount(initialBalance);

           int count= bankInterface.createAccount(b1);//CALLING TO DOA CLASS METHOD

           if (count>0){
               System.out.println("------------------------------");
               System.out.println("ACCOUNT CREATED SUCCESSFULLY");
               System.out.println("------------------------------");
           } else {
               System.out.println("SOMETHING WENT WRONG !!!");
           }
        }else {
            System.out.println("INVALID INITIAL AMOUNT (initial amt should be <=5000)");
        }
    }
    private static void performOperation(){
        boolean status=true;
        while (status) {
            System.out.println("==============================");
            System.out.println("1: DEPOSIT");
            System.out.println("2: WITHDRAW");
            System.out.println("3: DISPLAY ACCOUNT STATEMENT");
            System.out.println("4: DISPLAY BALANCE");
            System.out.println("5: TRANSFER TO ACCOUNT");
            System.out.println("6: EXIT");
            System.out.println("==============================");
            Integer choice = sc.nextInt();
            switch (choice) {
                case 1:
                    deposite();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    accountStatement();
                    break;
                case 4:
                    displayBalance();
                    break;
                case 5:
                    tranferToAccount();
                    break;
                case 6:
                    status = false;
                    break;
                default:
                    System.out.println("INVALID CHOICE !!!");
            }
        }
    }

    private static void accountStatement() {
        System.out.println("ENTER ACCOUNT NUMBER : ");
        int accountNumber=sc.nextInt();

        Bank b=new Bank();
        b.setAccountNo(accountNumber);

         ArrayList<Bank> data= bankInterface.accountStatement(b);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");

        System.out.println("TX_ID     NAME     ACCOUNT NO    DEPOSIT AMOUNT      WithdrawAmount      FromAccount      ToAccount      TotalBalance");
        for (Bank b1 :data) {
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
            System.out.println(b1.getTxId()+"\t\t"+b1.getAccountHolderName()+"\t\t"+b1.getAccountNo()+"\t\t"+b1.getDepositAmount()+"\t\t\t"+b1.getWithdrawAmount()+"\t\t\t"+b1.getFromAccount()+"\t\t\t"+b1.getToAccount()+""+b1.getTotalBalance());

        }
         System.out.println("------------------------------------------------------------------------------------------------------------------------------------2");
    }

    private static void tranferToAccount() {
        System.out.println("ENTER FROM ACCOUNT(SENDER) : ");
        int senderAccountNumnber= sc.nextInt();
        System.out.println("ENTER TO ACCOUNT(RECEIVER)");
        int receiverAccountNumber= sc.nextInt();
        System.out.println("ENTER TRANSFER AMOUNT: ");
        double tranasferAmount= sc.nextDouble();

        Bank b6=new Bank();
        b6.setFromAccount(senderAccountNumnber);
        b6.setToAccount(receiverAccountNumber);
        b6.setDepositAmount(tranasferAmount);

        int count=bankInterface.transferAmountToAnotherAccount(b6);
        System.out.println("-------------------------------------");
        if (count>0){
            System.out.println(count+" transfer successfully");
        }else if (count==0){
            System.out.println("something went wrong ");
        }else if (count==-1){
            System.out.println("INSUFFICIENT BALANCE");
        }else if (count==-2){
            System.out.println("ACCOUNT NUMBER NOT PRESENT");
        }
        System.out.println("-------------------------------------");

    }

    private static void displayBalance() {
        System.out.println("ENTER ACCOUNT NO: ");
        int accountNo= sc.nextInt();

        Bank b5=new Bank();

        b5.setAccountNo(accountNo);
        double balance=bankInterface.displayBalance(b5);
        if (balance>0){
            System.out.println("-------------------------");
            System.out.println("TOTAL BALANCE :"+balance);
            System.out.println("-------------------------");

        }else if (balance==0){
            System.out.println("ACCOUNT NOT PRESENT!!!");
        }

    }

    private static void withdraw() {
        System.out.print("ENTER ACCOUNT NO: ");
        int accountNo= sc.nextInt();
        System.out.print("ENTER AMOUNT: ");
        double amount= sc.nextDouble();
        if (amount>0){
            Bank b4 =new Bank();
            b4.setAccountNo(accountNo);
            b4.setWithdrawAmount(amount);

            int count= bankInterface.withdraw(b4);

            if (count>0){
                System.out.println("withdraw successfully");
            }else if (count==0){
                System.out.println("S0METHING WENT WRONG");
            }
        }else {
            System.out.println("AMOUNT SHOULD BE >0");
        }
    }

    private static void deposite() {
        System.out.println("ENTER ACCOUNT NO: ");
        int accountNumber= sc.nextInt();
        System.out.println("ENTER AMOUNT :");
        double amount = sc.nextDouble();

        if (amount>0){
            Bank b3=new Bank();
            b3.setAccountNo(accountNumber);
            b3.setDepositAmount(amount);

            int count= bankInterface.deposite(b3);
            if (count>0){
                System.out.println("deposit successfully");
            }else {
                System.out.println("deposit went wrong !!!");
            }

        }else {
            System.out.println("INVALID BALANCE !!!");
        }
    }

}
