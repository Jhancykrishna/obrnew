package com.sayone.obr.security;

import com.sayone.obr.service.PublisherService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class PublisherWebSecurity extends WebSecurityConfigurerAdapter {

    private final PublisherService publisherService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PublisherWebSecurity(PublisherService publisherService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.publisherService = publisherService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, PublisherSecurityConstants.SIGN_UP_URL).permitAll().anyRequest().authenticated().and().addFilter(getAuthenticationFilter()).addFilter(new PublisherAuthorizationFilter(authenticationManager())).sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(publisherService).passwordEncoder(bCryptPasswordEncoder);
    }

    public PublisherAuthenticationFilter getAuthenticationFilter() throws Exception {

        final PublisherAuthenticationFilter filter = new PublisherAuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl("/publisher/login");
        return filter;

    }
}
