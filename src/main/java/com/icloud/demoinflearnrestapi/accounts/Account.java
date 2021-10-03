package com.icloud.demoinflearnrestapi.accounts;


import lombok.*;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String email;
    private String password;

    //TODO 하나의 ENUM 타입이 아닌 여러개의 ENUM 타입인 경우
    @ElementCollection(fetch = EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;
}
