package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider provider(com.example.demo.service.UserDetailsService userService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public SecurityFilterChain filter(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(configurer -> configurer
        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("MANAGER")
        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("MANAGER")
        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/user/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/user/register").hasRole("ADMIN")
        .anyRequest().permitAll());
        //.rememberMe() özelliğine bir göz at
        // .requestMatchers(HttpMethod.GET, "/api/products/**").hasRole("USER")
        // .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
        // .requestMatchers(HttpMethod.POST, "/api/def/register").permitAll()
        // .requestMatchers(HttpMethod.POST, "/pas/request").permitAll()
        // .requestMatchers(HttpMethod.POST, "/pas/reset").permitAll());

    http.httpBasic(Customizer.withDefaults());

    // http
    // .formLogin()
    // .loginPage("/login") // Login sayfası
    // .loginProcessingUrl("/login") // Formu göndermek için kullanılan URL
    // .defaultSuccessUrl("/home", true) // Başarılı giriş sonrası yönlendirme
    // .permitAll() // Herkese açık
    // .and()
    // .logout()
    // .permitAll();

    http.csrf(csrf -> csrf.disable());

    return http.build();

  }

}

// @Bean
// public SecurityFilterChain filter(HttpSecurity http) throws Exception {
// http.authorizeHttpRequests(configurer -> configurer
// .requestMatchers(HttpMethod.POST, "/api/def/register").permitAll() // Bu
// endpoint herkes için açık
// .anyRequest().authenticated() // Diğer tüm endpoint'ler için kimlik doğrulama
// gerekiyor
// );
// http.httpBasic(Customizer.withDefaults()); // HTTP Basic Authentication
// http.csrf(csrf -> csrf.disable()); // CSRF korumasını devre dışı bırak
// return http.build();
// }
