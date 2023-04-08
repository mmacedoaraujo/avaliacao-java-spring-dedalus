package com.mmacedoaraujo.avaliacaojavaspringdedalus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails superUser = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("SUPERUSER")
                .build();

        return new InMemoryUserDetailsManager(superUser);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/users")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/users/paginated")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/users/**")).hasRole("SUPERUSER")
                        .anyRequest()
                        .authenticated()
                )
                .formLogin()
                .and()
                .httpBasic();

        return http.build();
    }

}
