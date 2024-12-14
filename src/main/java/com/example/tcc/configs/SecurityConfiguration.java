package com.example.tcc.configs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .cors().and()
//            .csrf().disable()
//            .authorizeHttpRequests()
//                .requestMatchers("/public").permitAll()
//                .requestMatchers("/logout").permitAll()
//                .requestMatchers(request -> request.getRequestURI().matches(".*\\.(jpg|pdf)$")).permitAll() // Permite arquivos .jpg e .pdf
//                .anyRequest().authenticated()
//            .and().oauth2Login(Customizer.withDefaults());
//
//        return http.build();
        return http
                .authorizeHttpRequests(
                        authorizeConfig -> {
                            authorizeConfig.requestMatchers("/public").permitAll();
                            authorizeConfig.requestMatchers("/logout").permitAll();
                            authorizeConfig.anyRequest().authenticated();
                        })
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer(config -> {
                    config.jwt(Customizer.withDefaults());
                })
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);

        return source;
    }
}