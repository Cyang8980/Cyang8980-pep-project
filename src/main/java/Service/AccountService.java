package Service;


import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;
    
    
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /**
     * get all accounts
     * @return list of all accounts
     */

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    /**
     * add account to database
     * @param Account account
     * @return the account added
     */

    public Account addAccount(Account account) {
        return accountDAO.insertAccount(account);
    }
    
    /**
     * get account based on username and password
     * @param String username
     * @param String password
     * @return the account that was matched
     */

    public Account login (String username, String password) {
        return accountDAO.AccountExists(username, password);
    }
}
