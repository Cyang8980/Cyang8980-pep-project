package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    
    /**
     * create a method to get a List of all accoutns
     * @return an arraylist of all accounts that exist
     */

    public List<Account> getAllAccounts() {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    /**
     * get an account based on account ID
     * @param int account ID
     * @return an account object if account exists else null
     */

    public Account getAccountWithID(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setInt method here.
            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    /**
     * create an account given account information
     * if account exists return null
     * password and username have constraints (password length > 4, username has to be unique)
     * @param Account account
     * @return the new generated account object if account exists else null
     */
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            if (usernameExists(connection, account.getUsername())) {
                System.out.println("Username already exists");
                return null;
            }
            
            // Write SQL logic here to insert the account record
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            // Set username and password
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                if (account.getUsername() != "" && account.getPassword().length() > 4) {
                    int generated_account_id = (int) pkeyResultSet.getLong(1);
                    return new Account(generated_account_id, account.getUsername(), account.getPassword());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    /**
     * checks if username already exists
     * @param Connection connection
     * @param String username
     * @return true if username exists, else false
     */
    private boolean usernameExists(Connection connection, String username) {
        try {
            String sql = "SELECT COUNT(*) FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    /**
     * checks if account exists
     * @param String password
     * @param String username
     * @return true if username exists, else false
     */
    
    public Account AccountExists(String username, String password) {
        try{
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
            int account_id = resultSet.getInt("account_id");
            return new Account(account_id, username, password);
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
