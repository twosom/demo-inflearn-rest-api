package com.icloud.demoinflearnrestapi.configs;

import com.icloud.demoinflearnrestapi.accounts.Account;
import com.icloud.demoinflearnrestapi.accounts.AccountService;
import com.icloud.demoinflearnrestapi.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.util.Set;

import static com.icloud.demoinflearnrestapi.accounts.AccountRole.ADMIN;
import static com.icloud.demoinflearnrestapi.accounts.AccountRole.USER;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;


    @DisplayName("인증 토큰 발급")
    @Test
    void authToken() throws Exception {
        // GIVEN
        String username = "twosom@icloud.com";
        String password = "twosom";

        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(ADMIN, USER))
                .build();

        this.accountService.saveAccount(account);

        String clientId = "myApp";
        String clientSecret = "pass";

        //TODO 기본적으로 인증 서버가 등록이 되면 /oauth/token 이라는 요청을 처리할 수 있는 핸들러가 적용됨.
        this.mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(clientId, clientSecret))
                        .param("username", username)
                        .param("password", password)
                        .param("grant_type", "password")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document(
                        "get-oauth-token",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Http Basic 방식으로 clientId, clientSecret 을 실어서 보냅니다.")
                        ),
                        requestParameters(
                                parameterWithName("username").description("인증받을 사용자의 아이디입니다."),
                                parameterWithName("password").description("인증받을 사용자의 비밀번호입니다."),
                                parameterWithName("grant_type").description("Oauth 인증을 받을 타입을 지정합니다.")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CACHE_CONTROL).description("인증 정보에 대한 캐싱을 방지하는 헤더입니다."),
                                headerWithName(HttpHeaders.PRAGMA).description("인증 정보에 대한 캐싱을 방지하는 헤더입니다."),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 타입을 나타내는 헤더입니다."),
                                headerWithName("X-Content-Type-Options").description("크로스사이트 스크립트를 방어하는 헤더입니다."),
                                headerWithName("X-Xss-Protection").description("크로스사이트 스크립트를 방어하는 헤더입니다."),
                                headerWithName("X-Frame-Options").description("크로스사이트 스크립트를 방어하는 헤더입니다.")
                        ),
                        responseFields(
                                fieldWithPath("access_token").description("Oauth 토큰의 AccessToken 입니다."),
                                fieldWithPath("token_type").description("Oauth 토큰의 타입을 나타냅니다."),
                                fieldWithPath("refresh_token").description("Oauth 토큰의 RefreshToken 입니다."),
                                fieldWithPath("expires_in").description("Oauth 토큰의 만료 시간입니다."),
                                fieldWithPath("scope").description("Oauth 토큰의 스코프를 나타냅니다.")
                        )
                ))
        ;
    }

}