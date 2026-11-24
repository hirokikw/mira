package com.examplecompany.mira.common.config;

import com.examplecompany.mira.common.security.LoginSuccessHandler;
import com.examplecompany.mira.common.security.MiraUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final LoginSuccessHandler loginSuccessHandler;

  public SecurityConfig(final LoginSuccessHandler loginSuccessHandler) {
    this.loginSuccessHandler = loginSuccessHandler;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationProvider authenticationProvider(
      final MiraUserDetailsService userDetailsService,
      final PasswordEncoder passwordEncoder) {
    final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  @Bean
  SecurityFilterChain securityFilterChain(
      final HttpSecurity http, final AuthenticationProvider authenticationProvider)
      throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/login", "/css/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .authenticationProvider(authenticationProvider)
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