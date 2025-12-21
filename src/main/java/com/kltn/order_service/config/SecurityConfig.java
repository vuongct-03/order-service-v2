package com.kltn.order_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
// import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

@EnableMethodSecurity
@EnableAsync
public class SecurityConfig {

    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    private final String[] PUBLIC_GET_ENDPOINT = { "/api/province/**", "/api/provinceGHN/**", "/getprovince",
            "/getdistrict" };
    private final String[] PUBLIC_POST_ENDPOINT = { "/api/provinceGHN/**", "/api/shipping-order/**" };

    private final String[] PUBLIC_PUT_ENDPOINT = { "/order/update-order-payment/**" };
    
    // ** */
    // private final String[] PUBLIC_POST_ENDPOINT_SCOPE_ADMIN = {
    // "/accounts/createadmin" };

    // @Value("${jwt.signerKey}")
    // private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINT).permitAll()
                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINT).permitAll()
                .requestMatchers(HttpMethod.PUT, PUBLIC_PUT_ENDPOINT).permitAll()
                .anyRequest()
                .authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
                .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        // disable csfs
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        // return httpSecurity.oauth2Login(Customizer.withDefaults()).build();
        return httpSecurity.build();
    }

    // Chuyeenr SCOPE_ADMIN to ROLE_ADMIN
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10) {
        };
    }

}