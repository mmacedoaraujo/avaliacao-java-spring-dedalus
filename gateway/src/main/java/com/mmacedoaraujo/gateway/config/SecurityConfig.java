package com.mmacedoaraujo.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("SUPERUSER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> {
            exchanges.pathMatchers("/eureka").permitAll()
                    .pathMatchers("/users").permitAll()
                    .pathMatchers("/users/paginated").permitAll()
                    .pathMatchers("/users/**").hasRole("SUPERUSER")
                    .anyExchange()
                    .authenticated();
        }).httpBasic(withDefaults()).csrf().disable();
        return http.build();
    }

}
