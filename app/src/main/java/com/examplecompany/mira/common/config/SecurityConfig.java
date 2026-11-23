package com.examplecompany.mira.common.config;

import com.examplecompany.mira.common.security.LoginSuccessHandler;
import com.examplecompany.mira.common.security.MiraUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final MiraUserDetailsService userDetailsService;
  private final LoginSuccessHandler loginSuccessHandler;

  public SecurityConfig(
      final MiraUserDetailsService userDetailsService,
      final LoginSuccessHandler loginSuccessHandler) {
    this.userDetailsService = userDetailsService;
    this.loginSuccessHandler = loginSuccessHandler;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/login", "/css/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .userDetailsService(userDetailsService)
        .formLogin(
            form ->
                form.loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("loginId")
                    .passwordParameter("password")
                    .successHandler(loginSuccessHandler)
                    .failureUrl("/login?error")
                    .permitAll())
        .logout(
            logout ->
                logout.logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll());
    return http.build();
  }
}