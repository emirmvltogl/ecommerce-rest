package com.example.demo.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.service.UserDetailsService;
import com.example.demo.util.JwtUtil;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider provider(UserDetailsService userService) {
        DaoAuthenticationProvider prov = new DaoAuthenticationProvider();
        prov.setUserDetailsService(userService);
        prov.setPasswordEncoder(passwordEncoder());
        return prov;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtFilter jwtFilter,
            DaoAuthenticationProvider provider) throws Exception {
        http
                // 1) DaoAuthenticationProvider'ı kaydet
                .authenticationProvider(provider)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 2) CSRF ve form login kapalı, JWT filtreyi ekle
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // 3) Yetkilendirme kuralları
                .authorizeHttpRequests(auth -> auth
                        // --- PUBLIC (hani herkes görsün)
                        .requestMatchers(HttpMethod.GET, "/", "/index.html").permitAll() // Anasayfa
                        .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/**").permitAll()

                        // --- AUTH (giriş endpoint'i ve şifre sıfırlama)
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/pas/reset-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/pas/reset").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("MANAGER")

                        // --- ADMIN ROLÜ (silme ve kullanıcı yönetimi)
                        .requestMatchers(HttpMethod.GET, "/api/user/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/user/register").hasRole("ADMIN")
                        
                        .requestMatchers(HttpMethod.POST,"/api/user/default").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/cart/").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/cart/add").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/api/cart/remove/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/api/cart/clear").authenticated()

                        // --- Diğer tüm istekler için kimlik doğrulama yeterli
                        .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtFilter jwtFilter(
            AuthenticationManager authManager,
            JwtUtil jwtUtil,
            UserDetailsService userDetailsService) {
        return new JwtFilter(authManager, jwtUtil, userDetailsService);
    }
}
