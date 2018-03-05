package com.hildebrandt.polls.config;

/*
spring security with custom classes and filters
 */

import com.hildebrandt.polls.security.CustomUserDetailsService;
import com.hildebrandt.polls.security.JWTAuthenticationFilter;
import com.hildebrandt.polls.security.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity // primary spring security annotation that is used to enable web security in a project.
@EnableGlobalMethodSecurity( // method level security based on annotations
        securedEnabled = true, //protect your controller/service methods
        jsr250Enabled  = true, //enables the @RolesAllowed("ROLE_ADMIN")
        prePostEnabled = true // control syntax with @PreAuthorize and @PostAuthorize annotations
)
public class SecurityConfig extends WebSecurityConfigurerAdapter{ //implements Spring Security’s WebSecurityConfigurer interface +  allows other classes to extend it
    @Autowired
    CustomUserDetailsService customUserDetailsService; // loads a user based on username +loadUserByUsername() method returns a UserDetails object: authentication and role based validations

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler; //return a 401 unauthorized error // implements Spring Security’s AuthenticationEntryPoint interface

    //reads JWT authentication token
    //loads details for Token
    //Sets the user details in Spring Security’s SecurityContext
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(){
        return new JWTAuthenticationFilter();
    }

    //build in-memory authentication
    //AuthenticationManagerBuilder is used to create an AuthenticationManager instance which is the main Spring Security interface for authenticating a user
    @Override
    public void configure(AuthenticationManagerBuilder  authenticationMagerBuilder) throws Exception{
        authenticationMagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    //manager: authenticate a user in the login API
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //configure security functionalities like csrf, sessionManagement + rules to protect resources based on various conditions
    //In our example, we’re permitting access to static resources and few other public APIs to everyone and restricting access to other APIs to authenticated users only
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .antMatchers("/auth/**")
                .permitAll()
                .antMatchers("/user/checkUsernameAvailability", "/user/checkEmailAvailability")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/polls/**", "/users/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }


}
