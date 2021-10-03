package com.icloud.demoinflearnrestapi.common;

import com.icloud.demoinflearnrestapi.accounts.Account;
import com.icloud.demoinflearnrestapi.accounts.AccountRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Component
@ConfigurationProperties(prefix = "my-app")
@Getter
@Setter
public class AppProperties {

    @NotEmpty
    private String adminUsername;

    @NotEmpty
    private String adminPassword;

    @NotEmpty
    private String userUsername;

    @NotEmpty
    private String userPassword;

    @NotEmpty
    private String clientId;

    @NotEmpty
    private String clientSecret;


    public Account createAdminAccount() {
        return Account.builder()
                .email(getAdminUsername())
                .password(getAdminPassword())
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
    }


    public Account createUserAccount() {
        return Account.builder()
                .email(getUserUsername())
                .password(getUserPassword())
                .roles(Set.of(AccountRole.USER))
                .build();
    }
}
