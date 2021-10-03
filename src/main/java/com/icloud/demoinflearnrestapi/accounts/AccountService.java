package com.icloud.demoinflearnrestapi.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    public Account saveAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("there is no user " + email));

        Set<SimpleGrantedAuthority> authorities = account.getRoles()
                .stream()
                .map(AccountService::convertToRoleString)
                .collect(Collectors.toSet());

        return new User(account.getEmail(), account.getPassword(), authorities);
    }

    private static SimpleGrantedAuthority convertToRoleString(AccountRole role) {
        return new SimpleGrantedAuthority("ROLE_" + role.name());
    }
}
