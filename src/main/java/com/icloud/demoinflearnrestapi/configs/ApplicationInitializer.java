package com.icloud.demoinflearnrestapi.configs;

import com.icloud.demoinflearnrestapi.accounts.Account;
import com.icloud.demoinflearnrestapi.accounts.AccountRole;
import com.icloud.demoinflearnrestapi.accounts.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ApplicationInitializer implements ApplicationRunner {

    private final AccountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account account = Account.builder()
                .email("two_somang@icloud.com")
                .password("twosom")
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        accountService.saveAccount(account);
    }
}
