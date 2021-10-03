package com.icloud.demoinflearnrestapi.accounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static com.icloud.demoinflearnrestapi.accounts.AccountRole.ADMIN;
import static com.icloud.demoinflearnrestapi.accounts.AccountRole.USER;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        this.accountRepository.deleteAll();
    }


    @DisplayName("사용자 이름으로 계정 가져오기")
    @Test
    void findByUsername() throws Exception {
        // GIVEN
        String username = "two_somang@icloud.com";
        String password = "twosom";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(ADMIN, USER))
                .build();

        accountService.saveAccount(account);

        // WHEN
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // THEN
        assertTrue(passwordEncoder.matches(password, userDetails.getPassword()));
    }

    @DisplayName("사용자 이름으로 계정 가져오기 실패")
    @Test
    void findByUsernameFail() throws Exception {
        String username = "random@email.com";
        UsernameNotFoundException failTest = assertThrows(UsernameNotFoundException.class, () -> {
            accountService.loadUserByUsername(username);
            fail("supposed to be failed");
        });
        String errorMessage = failTest.getMessage();
        assertEquals("there is no user " + username, errorMessage);

    }

}