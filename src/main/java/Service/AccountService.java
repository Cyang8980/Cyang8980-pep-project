package Service;


import Model.Account;
import DAO.AccountDAO;

import java.sql.SQLException;
import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public Account addAccount(Account account) {
        return accountDAO.insertAccount(account);
    }
    public Account login (String username, String password) {
        return accountDAO.AccountExists(username, password);
    }
}
