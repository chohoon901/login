package io.playdata.login;

import io.playdata.login.model.Account;
import io.playdata.login.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    //join
    public void join(Account account) {
        accountRepository.save(account);
    }

    //login
    public Account login(String loginID, String password) {
        return accountRepository.findByLoginIDAndPassword(loginID, password);
    }
}
