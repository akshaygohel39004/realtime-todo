package com.akshay.websockettask.config;

import com.akshay.websockettask.filters.JWTAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SpringSecurityConfig {


    private JWTAuthFilter jwtAuthFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,CustomOAuth2UserService customOAuth2UserService,CustomOidcUserService customOidcUserService) throws Exception {
        http
                .csrf(csrf->
                        csrf.csrfTokenRequestHandler(csrfTokenRequestHandler())
                                .ignoringRequestMatchers("/auth/**","/todos/**","/collections/**")
                )
//                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session->
                            session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,"/collections/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.POST,"/collections/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/collections/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/collections/**").hasRole("ADMIN")
                        .requestMatchers("/todos/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/ws/**","/auth/**","/signup.html","/csrf").permitAll() // WebSocket handshake,and auth
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults()) //statefull
                .httpBasic(Customizer.withDefaults())
                .oauth2Login(oauth -> oauth //stateless
                        .userInfoEndpoint(userInfo ->
                                  userInfo.userService(customOAuth2UserService) //for github or other except OAuth2
                                          .oidcUserService(customOidcUserService) //for OAuth2
                        )
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //if we want inmemory
//    @Bean
//    UserDetailsService userDetailsService(PasswordEncoder encoder){
//        UserDetails u1= User
//                .withUsername("akshay")
//                .password(encoder.encode("akshay"))
//                .roles("USER")
//                .build();
//
//        UserDetails u2=User
//                .withUsername("admin")
//                .password(encoder.encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(u1,u2);
//    }


    @Bean
    CsrfTokenRequestHandler csrfTokenRequestHandler() {
        return new CsrfTokenRequestAttributeHandler();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService,PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }
}
