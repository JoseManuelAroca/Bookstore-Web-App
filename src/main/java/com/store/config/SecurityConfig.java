package com.store.config;

import com.store.util.GetBCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@Configuration
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    private final GetBCryptPasswordEncoder encoder;

    public SecurityConfig(GetBCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    /*

    https://stackoverflow.com/questions/75080739/spring-security-6-post-requests-are-unauthorised-with-permitall
https://stackoverflow.com/questions/28907030/spring-security-authorize-request-for-certain-url-http-method-using-httpsecu
https://www.baeldung.com/spring-security-csrf
     */




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(form -> form
                .loginPage("/usuarios/login")
                .failureUrl("/login-error")
                .defaultSuccessUrl("/",true)
                .permitAll()
        );
        http.logout(logout -> logout
                        .logoutUrl("/usuarios/logout")
                        .logoutSuccessUrl("/home")
                        .invalidateHttpSession(true)
                        /*.logoutSuccessHandler(logoutSuccessHandler)
                        .addLogoutHandler(logoutHandler)
                        .deleteCookies(cookieNamesToClear)*/
        );
        http.authorizeHttpRequests()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/img/**").permitAll()
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/fonts/**").permitAll()
                .requestMatchers("/usuarios/registro","/home","/demosec").permitAll()
                .requestMatchers( HttpMethod.POST,"/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied")

                .and()
                .csrf().disable()
                .cors().disable()
                .authenticationProvider(authenticationProvider());

        return http.build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder.bCryptPasswordEncoder());
        return authenticationProvider;
    }
    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("ROLE_");
    }
}
