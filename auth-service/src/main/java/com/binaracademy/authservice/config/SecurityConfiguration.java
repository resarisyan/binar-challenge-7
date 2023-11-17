package com.binaracademy.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.binaracademy.authservice.enumeration.EnumPermission.ADMIN_READ;
import static com.binaracademy.authservice.enumeration.EnumPermission.ADMIN_DELETE;
import static com.binaracademy.authservice.enumeration.EnumPermission.CUSTOMER_CREATE;
import static com.binaracademy.authservice.enumeration.EnumPermission.CUSTOMER_READ;
import static com.binaracademy.authservice.enumeration.EnumPermission.CUSTOMER_UPDATE;
import static com.binaracademy.authservice.enumeration.EnumPermission.MERCHANT_CREATE;
import static com.binaracademy.authservice.enumeration.EnumPermission.MERCHANT_READ;
import static com.binaracademy.authservice.enumeration.EnumPermission.MERCHANT_UPDATE;
import static com.binaracademy.authservice.enumeration.EnumPermission.MERCHANT_DELETE;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private static final String[] WHITE_LIST_URL = {
            "/auth/**",
            "/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/docs"
    };
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private static final String URL_PRODUCT = "/v1/product/**";
    private static final String URL_MERCHANT = "/v1/merchant/**";
    private static final String URL_CART = "/v1/cart/**";
    private static final String URL_ORDER = "/v1/order/**";
    private static final String URL_USER = "/v1/user/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers(GET, URL_PRODUCT).hasAnyAuthority(CUSTOMER_READ.getPermission(), MERCHANT_READ.getPermission())
                                .requestMatchers(POST, URL_PRODUCT).hasAnyAuthority(MERCHANT_CREATE.getPermission())
                                .requestMatchers(PUT, URL_PRODUCT).hasAnyAuthority(MERCHANT_UPDATE.getPermission())
                                .requestMatchers(DELETE, URL_PRODUCT).hasAnyAuthority(MERCHANT_DELETE.getPermission())
                                .requestMatchers(GET, URL_MERCHANT).hasAnyAuthority(CUSTOMER_READ.getPermission(), MERCHANT_READ.getPermission())
                                .requestMatchers(POST, URL_MERCHANT).hasAnyAuthority(MERCHANT_CREATE.getPermission())
                                .requestMatchers(PUT, URL_MERCHANT).hasAnyAuthority(MERCHANT_UPDATE.getPermission())
                                .requestMatchers(POST, URL_CART).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(POST, URL_ORDER).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(GET, URL_ORDER).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(PUT, URL_USER).hasAnyAuthority(ADMIN_READ.getPermission(), CUSTOMER_UPDATE.getPermission(), MERCHANT_UPDATE.getPermission())
                                .requestMatchers(DELETE, URL_USER).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling(
                        exception -> exception
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .addLogoutHandler(logoutHandler)
                );
        return http.build();
    }
}


