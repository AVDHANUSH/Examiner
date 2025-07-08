package com.TicketXChange.TicketXChange.auth.config;


import com.TicketXChange.TicketXChange.auth.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * Configuration class for defining security settings and filters.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private  final LogoutHandler logoutHandler;

    /**
     * The success handler for handling successful authentication.
     */
    private final AuthenticationSuccessHandler successHandler;

    /**
     * The JWT authentication filter for processing JWT tokens.
     */
    private final com.TicketXChange.TicketXChange.auth.config.JwtAuthenticationFilter jwtAuthFilter;

    /**
     * The authentication provider for handling user authentication.
     */
    private final AuthenticationProvider authenticationProvider;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint; // Inject the custom entry point

//    private  final  CorsConfig corsConfig;

    /**
     * Defines a custom security filter chain for specific HTTP requests.
     *
     * @param http The HTTP security configuration object.
     * @return A custom security filter chain.
     * @throws Exception If an exception occurs while configuring security.
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http
                .cors(  cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/auth/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/gs-guide-websocket/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/auth/password/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/otp/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/login/**")).permitAll()
//                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/poll/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/user/**")).authenticated()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/ticketverify/**")).authenticated()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/submit-sell-request/**")).authenticated()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/user/profile/professional-details/**")).authenticated()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/buy-ticket/**")).authenticated()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/help/**")).authenticated()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/posts/**")).authenticated()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/deactivateanddelete/**")).authenticated()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/people/**")).authenticated()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/search-ticket/**")).authenticated()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/home/**")).authenticated()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/search/**")).authenticated()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/quizzes/**")).authenticated()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/payments/**")).authenticated()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/auth/logout")).authenticated()
                        .anyRequest()
                        .authenticated()
                )
                .csrf(it -> it.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"),
                        AntPathRequestMatcher.antMatcher("/h2-console/**"),
                        AntPathRequestMatcher.antMatcher("/api/v1/auth/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/otp/**"),
                                AntPathRequestMatcher.antMatcher("/gs-guide-websocket/**"),
                        AntPathRequestMatcher.antMatcher("/api/v1/user/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/submit-sell-request/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/user/profile/professional-details/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/connection/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/help/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/posts/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/deactivateanddelete/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/ticketverify/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/documents/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/buy-ticket/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/people/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/search-ticket/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/home/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/quizzes/**"),
                                AntPathRequestMatcher.antMatcher("/api/v1/payments/**"),

//                                AntPathRequestMatcher.antMatcher("/api/v1/poll/inactive/{pollId}"),
                        AntPathRequestMatcher.antMatcher("/login/**"),
                                AntPathRequestMatcher.antMatcher("/home"),
                                AntPathRequestMatcher.antMatcher("/api/v1/search/**")

                        )
                        )

                .exceptionHandling(exc -> exc.authenticationEntryPoint(customAuthenticationEntryPoint))

                .oauth2Login(auth -> auth.successHandler(successHandler)
                )
                .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider)

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(
                                ((request, response, authentication) ->
                                        SecurityContextHolder.clearContext())
                ));


        http.headers().frameOptions().disable();


        return http.build();
    }
}
