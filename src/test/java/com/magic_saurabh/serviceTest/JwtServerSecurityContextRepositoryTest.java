package com.magic_saurabh.serviceTest;

import com.magic_saurabh.service.JwtAuthenticationManager;
import com.magic_saurabh.service.JwtServerSecurityContextRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class JwtServerSecurityContextRepositoryTest {

    @Mock
    private JwtAuthenticationManager authenticationManager;

    private JwtServerSecurityContextRepository securityContextRepository;

    private ServerWebExchange exchange;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityContextRepository = new JwtServerSecurityContextRepository(authenticationManager);
    }
    @Test
    void testLoadAuthorizationHeaderValid() {
        String token = "validToken";
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(token, token);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.just(authentication));
        Mono<SecurityContext> result = securityContextRepository.load(exchange);
        StepVerifier.create(result)
                .expectNextMatches(securityContext -> {
                    return securityContext instanceof SecurityContextImpl;
                })
                .verifyComplete();

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testLoadAuthorizationHeaderValidButAuthenticationFails() {
        String token = "invalidToken";
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.empty());
        Mono<SecurityContext> result = securityContextRepository.load(exchange);
        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
