package Utility;

import ConnectionHelper.ConnectionHelper;
import Model.Bank;

import javax.lang.model.util.ElementScanner6;
import java.sql.*;
import java.util.ArrayList;

//DAO CLASS
//DB RELATED OPERATION PERFORM HERE.
public class BankOperationClass implements BankInterface{
    private static Statement statement=null;
    private static ResultSet rs=null;
    private static Connection con= ConnectionHelper.createConnection();
    private  static String createAccountQuery="insert into bank values (?,?,?,?,?,?,?,?)";
    private static String totalBalance="SELECT total_balance FROM bank WHERE account_no = ? ORDER BY tx_id DESC LIMIT 1";
    private static String depositeQ="insert into bank (tx_id,account_no,deposite_amount,total_balance) values(?,?,?,?)";
    private static String withdrawQ="insert into bank (tx_id,account_no,withdraw_amount,total_balance) values(?,?,?,?)";
    private  static String selctAccountNo="select account_no from bank";
    private  static  String transferWithDrawwQuery="insert into bank values (?,?,?,?,?,?,?,?)";
    private  static String accountStatement="select * from bank where account_no=?";
  
    public boolean accountPresent(int accountNo){

        boolean status =false;
        try {
            statement= con.createStatement();
            rs= statement.executeQuery(selctAccountNo);

           while (rs.next()){
               if (accountNo==rs.getInt(1)){
                   status=true;
                   return  status;
               }
           }
           return status;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int createAccount(Bank b) {
        try {
            PreparedStatement pstmt =con.prepareStatement(createAccountQuery);//it is query compilation process
            pstmt.setInt(1,0);
            pstmt.setInt(2,b.getAccountNo());
            pstmt.setString(3,b.getAccountHolderName());
            pstmt.setDouble(4,b.getDepositAmount());
            pstmt.setDouble(5,0);
            pstmt.setInt(6,0);
            pstmt.setInt(7,0);
            pstmt.setDouble(8,b.getDepositAmount());

            int count=pstmt.executeUpdate();
            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int deposite(Bank b) {
        try {
            int  count;
            double totalBal;
                PreparedStatement preparedStatement1=con.prepareStatement(totalBalance);
                preparedStatement1.setInt(1,b.getAccountNo());
                ResultSet rs=preparedStatement1.executeQuery();

            PreparedStatement preparedStatement=con.prepareStatement(depositeQ);
               while (rs.next()){
                    totalBal=rs.getDouble(1);
                    totalBal=totalBal+b.getDepositAmount();
                 //  System.out.println(totalBal);
                   preparedStatement.setDouble(4,totalBal);
                }
            preparedStatement.setInt(1,0);
            preparedStatement.setInt(2,b.getAccountNo());
            preparedStatement.setDouble(3,b.getDepositAmount());

            count =preparedStatement.executeUpdate();
             return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int withdraw(Bank b4) {
        try {
            int  count;
            double totalBal;
            PreparedStatement  preparedStatement1 = con.prepareStatement(totalBalance);
            preparedStatement1.setInt(1,b4.getAccountNo());
            ResultSet rs=preparedStatement1.executeQuery();

            PreparedStatement preparedStatement=con.prepareStatement(withdrawQ);
            while (rs.next()){
                totalBal=rs.getDouble(1);

                if (totalBal<b4.getWithdrawAmount()){
                   count=0;
                   return count;
                }
                totalBal=totalBal-b4.getWithdrawAmount();
                preparedStatement.setDouble(4,totalBal);
            }
            preparedStatement.setInt(1,0);
            preparedStatement.setInt(2,b4.getAccountNo());
            preparedStatement.setDouble(3,b4.getWithdrawAmount());

             count=preparedStatement.executeUpdate();
            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public double displayBalance(Bank b5) {
        boolean status=accountPresent(b5.getAccountNo());
        if (status){
            try {
                PreparedStatement preparedStatement=con.prepareStatement(totalBalance);
                preparedStatement.setInt(1,b5.getAccountNo());
                rs=preparedStatement.executeQuery();
                while (rs.next()){
                    double balance=rs.getDouble(1);
                    return balance ;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            return 0;
        }
        return 0;
    }

    @Override
    public int transferAmountToAnotherAccount(Bank b) {
        boolean status=accountPresent(b.getToAccount());
        boolean status2=accountPresent(b.getFromAccount());

        if (status2 && status){
            try {
                PreparedStatement preparedStatement1=con.prepareStatement(totalBalance);
                preparedStatement1.setInt(1,b.getFromAccount());
                ResultSet rs=preparedStatement1.executeQuery();


                PreparedStatement preparedStatement3=con.prepareStatement(totalBalance);
                preparedStatement3.setInt(1,b.getToAccount());
                ResultSet rs1=preparedStatement3.executeQuery();

                while(rs.next() && rs1.next()){
                    if (rs.getDouble(1)>=b.getDepositAmount()){
                        //  double balanceFromSenderAccount=rs.getDouble(1);
                        PreparedStatement preparedStatement= con.prepareStatement(transferWithDrawwQuery);
                        preparedStatement.setInt(1,0);
                        preparedStatement.setInt(2,b.getFromAccount());
                        preparedStatement.setString(3,"-----");
                        preparedStatement.setDouble(4,0);
                        preparedStatement.setDouble(5,b.getDepositAmount());
                        preparedStatement.setInt(6,b.getToAccount());
                        preparedStatement.setInt(7,0);
                        preparedStatement.setDouble(8,rs.getDouble(1)-b.getDepositAmount());

                        //DEPOSITING TO ANOTHER ACCOUNT
                        PreparedStatement preparedStatement2= con.prepareStatement(transferWithDrawwQuery);
                        preparedStatement2.setInt(1,0);
                        preparedStatement2.setInt(2,b.getToAccount());
                        preparedStatement2.setString(3,"-----");
                        preparedStatement2.setDouble(4,b.getDepositAmount());
                        preparedStatement2.setDouble(5,0);
                        preparedStatement2.setInt(6,0);
                        preparedStatement2.setInt(7,b.getFromAccount());
                        preparedStatement2.setDouble(8,rs1.getDouble(1)+b.getDepositAmount());

                        int count= preparedStatement.executeUpdate();
                        int count1=preparedStatement2.executeUpdate();
                        return count+count1;
                    }else {
                        return -1;
                    }

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            return -2;
        }
       return 0;
    }


    public ArrayList<Bank> accountStatement(Bank b) {
        ArrayList<Bank> accStatement=new ArrayList<>();

        try {
            PreparedStatement preparedStatement= con.prepareStatement(accountStatement);
            preparedStatement.setInt(1,b.getAccountNo());

            rs=preparedStatement.executeQuery();
            Bank b1;
            while (rs.next()){
                 b1=new Bank();

                b1.setTxId(rs.getInt(1));
                b1.setAccountNo(rs.getInt(2));
                b1.setAccountHolderName(rs.getString(3));
                b1.setDepositAmount(rs.getDouble(4));
                b1.setWithdrawAmount(rs.getDouble(5));
                b1.setToAccount(rs.getInt(6));
                b1.setFromAccount(rs.getInt(7));
                b1.setTotalBalance(rs.getDouble(8));

                accStatement.add(b1);

            }
            return accStatement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
     //   return null;
    }
}
