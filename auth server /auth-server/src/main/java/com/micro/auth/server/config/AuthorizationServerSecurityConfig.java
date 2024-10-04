package com.micro.auth.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.micro.auth.server.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class AuthorizationServerSecurityConfig {

        private final CustomUserDetailsService userDetailsService;

        public AuthorizationServerSecurityConfig(CustomUserDetailsService userDetailsService) {
                this.userDetailsService = userDetailsService;
        }

        @Bean
        public AuthenticationManager authenticationManager() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setUserDetailsService(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder());
                return new ProviderManager(provider);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        @Order(1)
        public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
                OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
                http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                                .oidc(Customizer.withDefaults()); // Enable OpenID Connect 1.0
                http
                                .exceptionHandling((exceptions) -> exceptions
                                                .defaultAuthenticationEntryPointFor(
                                                                new LoginUrlAuthenticationEntryPoint("/login"),
                                                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)))
                                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
                return http.cors(Customizer.withDefaults()).build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests((authorize) -> authorize
                                                .anyRequest().authenticated())
                                .formLogin(Customizer.withDefaults());
                return http.cors(Customizer.withDefaults()).build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration config = new CorsConfiguration();
                config.addAllowedHeader("*");
                config.addAllowedMethod("*");
                config.addAllowedOrigin("http://localhost:8072");
                config.setAllowCredentials(true);
                source.registerCorsConfiguration("/**", config);
                return source;
        }
}
