package com.turingSecApp.turingSec.config;


import com.turingSecApp.turingSec.filter.JwtAuthenticationFilter;
import com.turingSecApp.turingSec.filter.JwtUtil;
import com.turingSecApp.turingSec.service.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public JwtAuthenticationFilter jwtTokenFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests

                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()

                                .requestMatchers("/api/auth/activate").permitAll() // Public endpoints for registration and login
                                .requestMatchers("/api/auth/login").permitAll() // Public endpoints for registration and login
                                .requestMatchers("/api/auth/register/hacker").permitAll() // Public endpoints for registration and login
                                .requestMatchers("/api/auth/register/company").permitAll() // Public endpoints for registration and login
                                .requestMatchers("/api/auth/current-user").authenticated() // Public endpoints for registration and login
                                .requestMatchers("/api/auth/users/**").permitAll() // Public endpoints for registration and login
                                .requestMatchers("/api/companies/current-user").authenticated() // Public endpoints for registration and login

                                .requestMatchers("/api/admin/register").permitAll() // Public endpoints for registration and login
                                .requestMatchers("/api/admin/approve-company/{companyId}").hasRole("ADMIN")
                                .requestMatchers("/api/admin/login").permitAll() // Public endpoints for registration and login
                                .requestMatchers("/api/auth/update-profile").authenticated()
                                .requestMatchers("/api/bug-bounty-reports/**").hasRole("HACKER")
                                .requestMatchers("/api/bug-bounty-programs/**").hasRole("COMPANY")
                                .requestMatchers("/api/companies/**").permitAll()

                                .requestMatchers("/api/auth/test").authenticated()

                                .requestMatchers("/api/auth/change-email").authenticated()
                                .requestMatchers("/api/auth/change-password").authenticated()
                                .requestMatchers("/api/auth/delete-user").authenticated()

                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
