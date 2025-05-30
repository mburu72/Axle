package com.tumi.haul.security;

import com.tumi.haul.model.admin.config.IpWhiteListFilter;
import com.tumi.haul.model.enums.Roles;
import com.tumi.haul.security.jwt.JWTFilter;
import com.tumi.haul.security.jwt.JwtAuthEntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTFilter jwtFilter;

@Autowired
    public SecurityConfig(JwtAuthEntryPoint jwtAuthEntryPoint) {

}
@Autowired
private IpWhiteListFilter whiteListFilter;
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of( "http://localhost:5173","http://localhost:3000", "/ws/**"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // Include OPTIONS for preflight
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity https) throws Exception{
        return https
                .addFilterBefore(whiteListFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/admin/***").hasRole(Roles.ADMIN.getAuthority())
                        .requestMatchers(
                                "/api/pay-now/**",
                                "/api/payment/pay-now",
                                "/actuator/health",
                                "/api/v1/users/{username}/request-otp",
                                "/api/v1/users/{username}/{otp}/verify-otp",
                                "/api/v1/users/{phoneNumber}/{otp}/verify-user",
                                "/api/v1/users/{email}/{otp}/registration",
                                "/api/v1/users/{email}/{otp}/client",
                                "/api/images/**",
                                "/api/v1/auth/**",
                                "/api/v1/jobs/**",
                                "/api/v1/email",
                                "/api/v1/users/verify-otp").permitAll()

                        .anyRequest().authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public AuthenticationProvider authProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);

        return provider;

    }
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
    }

}
