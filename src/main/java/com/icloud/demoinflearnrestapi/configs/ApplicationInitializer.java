package com.icloud.demoinflearnrestapi.configs;

import com.icloud.demoinflearnrestapi.accounts.AccountService;
import com.icloud.demoinflearnrestapi.common.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationInitializer implements ApplicationRunner {

    private final AccountService accountService;
    private final AppProperties appProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        accountService.saveAccount(appProperties.createAdminAccount());

        accountService.saveAccount(appProperties.createUserAccount());
    }
}
